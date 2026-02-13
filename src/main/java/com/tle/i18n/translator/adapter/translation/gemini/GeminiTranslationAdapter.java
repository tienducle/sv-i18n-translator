package com.tle.i18n.translator.adapter.translation.gemini;

import com.tle.gemini.GeminiClient;
import com.tle.gemini.model.Content;
import com.tle.gemini.model.GenerateContentRequest;
import com.tle.gemini.model.GenerateContentResponse;
import com.tle.i18n.translator.adapter.translation.TranslationAdapter;
import com.tle.i18n.translator.translation.TranslationRequest;
import com.tle.i18n.translator.translation.TranslationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty( value = "translation.adapter", havingValue = "Gemini" )
@Lazy
public class GeminiTranslationAdapter extends TranslationAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger( GeminiTranslationAdapter.class );

    private final GeminiTranslationAdapterConfiguration config;

    private final GeminiClient geminiClient;

    private final String systemMessage;

    private final double maxTemperature = 2.0;
    private final int adapterMaxAttempts = 3;

    public GeminiTranslationAdapter( GeminiTranslationAdapterConfiguration config )
    {
        LOGGER.info( "Initializing GeminiTranslationAdapter" );
        this.config = config;
        this.geminiClient = new GeminiClient( config.getApiKey(), config.getModel() );
        this.systemMessage = config.getSystemMessageText();
        LOGGER.info( "Model: {}", config.getModel() );
        LOGGER.info( "Max output tokens: {}", config.getMaxOutputTokens() );
        LOGGER.info( "System message:" );
        LOGGER.info( config.getSystemMessageText() );
        LOGGER.info( "Initialized GeminiTranslationAdapter" );
    }

    @Override
    public TranslationResult translate( TranslationRequest translationRequest )
    {
        final int totalAttempts = translationRequest.getTotalAttempts();
        final int currentAttempt = translationRequest.getCurrentAttempt();

        if ( currentAttempt > adapterMaxAttempts )
        {
            return new TranslationResult( translationRequest, "Gemini adapter max attempts to translate text reached", false );
        }

        final String originalText = translationRequest.getOriginalText();

        final double temperature = Math.min(
                Math.ceil( ( config.getInitTemperature() + config.getTemperatureIncrement() * totalAttempts ) * 10 ) / 10,
                maxTemperature );

        final int count = Math.min( config.getN(), 1 );

        if ( currentAttempt > 1 )
        {
            LOGGER.warn( "------" );
            LOGGER.warn( String.format( "Retrying with temperature %s", temperature ) );
        }

        GenerateContentRequest generateContentRequest = createTranslationRequest( temperature, count, originalText );

        final List<GenerateContentResponse> responses = sendTranslationRequest( currentAttempt, generateContentRequest, count );

        if ( responses == null || responses.isEmpty() || responses.get( 0 ) == null )
        {
            return new TranslationResult( translationRequest,
                                          "Gemini request failed.",
                                          currentAttempt <= getMaxAttempts() );
        }

        final TranslationResult translationResult = new TranslationResult( translationRequest );
        for ( GenerateContentResponse response : responses )
        {
            if ( response != null )
            {
                translationResult.addTranslatedText( response.getTextContent() );
            }
        }
        return translationResult;
    }

    private List<GenerateContentResponse> sendTranslationRequest( int currentAttempt, GenerateContentRequest generateContentRequest, int count )
    {
        List<GenerateContentResponse> responses = new ArrayList<>();
        for ( int i = 0; i < count; i++ )
        {
            try
            {
                GenerateContentResponse response = geminiClient.generateContent( generateContentRequest );
                responses.add( response );
            }
            catch ( Exception e )
            {
                LOGGER.error( String.format( "Error while sending Gemini request (attempt %s, request %s): ", currentAttempt, i + 1 ), e );
            }
        }
        return responses;
    }

    private GenerateContentRequest createTranslationRequest( double temperature, int count, String originalText )
    {
        GenerateContentRequest request = new GenerateContentRequest(
                config.getModel(),
                temperature,
                config.getMaxOutputTokens(),
                systemMessage
        );

        getContextMessages().forEach( message -> request.addContent( toGeminiContent( message ) ) );
        getHistoryMessages().forEachRemaining( message -> request.addContent( toGeminiContent( message ) ) );

        Content translationContent = new Content( "user", originalText );
        request.addContent( translationContent );

        return request;
    }

    private Content toGeminiContent( com.tle.i18n.translator.common.Message message )
    {
        return new Content( message.getRole(), message.getContent() );
    }
}
