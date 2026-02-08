package com.tle.i18n.translator.adapter.translation.anthropic;

import com.tle.anthropic.AnthropicClient;
import com.tle.anthropic.model.AnthropicMessage;
import com.tle.anthropic.model.AnthropicMessageRequest;
import com.tle.anthropic.model.AnthropicMessageResponse;
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
@ConditionalOnProperty( value = "translation.adapter", havingValue = "Anthropic" )
@Lazy
public class AnthropicTranslationAdapter extends TranslationAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger( AnthropicTranslationAdapter.class );

    private final AnthropicTranslationAdapterConfiguration config;

    private final AnthropicClient anthropicClient;

    private final String systemMessage;

    private final double maxTemperature = 1.0;
    private final int adapterMaxAttempts = 3;

    public AnthropicTranslationAdapter( AnthropicTranslationAdapterConfiguration config )
    {
        LOGGER.info( "Initializing AnthropicTranslationAdapter" );
        this.config = config;
        this.anthropicClient = new AnthropicClient( config.getApiKey() );
        this.systemMessage = config.getSystemMessageText();
        LOGGER.info( "Model: {}", config.getModel() );
        LOGGER.info( "Max tokens: {}", config.getMaxTokens() );
        LOGGER.info( "System message:" );
        LOGGER.info( config.getSystemMessageText() );
        LOGGER.info( "Initialized AnthropicTranslationAdapter" );
    }

    @Override
    public TranslationResult translate( TranslationRequest translationRequest )
    {
        final int totalAttempts = translationRequest.getTotalAttempts();
        final int currentAttempt = translationRequest.getCurrentAttempt();

        if ( currentAttempt > adapterMaxAttempts )
        {
            return new TranslationResult( translationRequest, "Anthropic adapter max attempts to translate text reached", false );
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

        AnthropicMessageRequest messageRequest = createTranslationRequest( temperature, count, originalText );

        final List<AnthropicMessageResponse> responses = sendTranslationRequest( currentAttempt, messageRequest, count );

        if ( responses == null || responses.isEmpty() || responses.get( 0 ) == null )
        {
            return new TranslationResult( translationRequest,
                                          "Anthropic request failed.",
                                          currentAttempt <= getMaxAttempts() );
        }

        final TranslationResult translationResult = new TranslationResult( translationRequest );
        for ( AnthropicMessageResponse response : responses )
        {
            if ( response != null )
            {
                translationResult.addTranslatedText( response.getTextContent() );
            }
        }
        return translationResult;
    }

    private List<AnthropicMessageResponse> sendTranslationRequest( int currentAttempt, AnthropicMessageRequest messageRequest, int count )
    {
        List<AnthropicMessageResponse> responses = new ArrayList<>();
        for ( int i = 0; i < count; i++ )
        {
            try
            {
                AnthropicMessageResponse response = anthropicClient.postMessage( messageRequest );
                responses.add( response );
            }
            catch ( Exception e )
            {
                LOGGER.error( String.format( "Error while sending Anthropic request (attempt %s, request %s): ", currentAttempt, i + 1 ), e );
            }
        }
        return responses;
    }

    private AnthropicMessageRequest createTranslationRequest( double temperature, int count, String originalText )
    {
        AnthropicMessageRequest request = new AnthropicMessageRequest(
                config.getModel(),
                temperature,
                config.getMaxTokens(),
                systemMessage
        );

        getContextMessages().forEach( message -> request.addMessage( toAnthropicMessage( message ) ) );
        getHistoryMessages().forEachRemaining( message -> request.addMessage( toAnthropicMessage( message ) ) );

        AnthropicMessage translationMessage = new AnthropicMessage( "user", originalText );
        request.addMessage( translationMessage );

        return request;
    }

    private AnthropicMessage toAnthropicMessage( com.tle.i18n.translator.common.Message message )
    {
        return new AnthropicMessage( message.getRole(), message.getContent() );
    }
}
