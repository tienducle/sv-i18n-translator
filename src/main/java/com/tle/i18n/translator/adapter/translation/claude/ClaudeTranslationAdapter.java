package com.tle.i18n.translator.adapter.translation.claude;

import com.tle.claude.ClaudeClient;
import com.tle.claude.model.ClaudeMessage;
import com.tle.claude.model.ClaudeMessageRequest;
import com.tle.claude.model.ClaudeMessageResponse;
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
@ConditionalOnProperty( value = "translation.adapter", havingValue = "Claude" )
@Lazy
public class ClaudeTranslationAdapter extends TranslationAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ClaudeTranslationAdapter.class );

    private final ClaudeTranslationAdapterConfiguration config;

    private final ClaudeClient claudeClient;

    private final String systemMessage;

    private final double maxTemperature = 1.0;
    private final int adapterMaxAttempts = 3;

    public ClaudeTranslationAdapter( ClaudeTranslationAdapterConfiguration config )
    {
        LOGGER.info( "Initializing ClaudeTranslationAdapter" );
        this.config = config;
        this.claudeClient = new ClaudeClient( config.getApiKey() );
        this.systemMessage = config.getSystemMessageText();
        LOGGER.info( "Model: {}", config.getModel() );
        LOGGER.info( "Max tokens: {}", config.getMaxTokens() );
        LOGGER.info( "System message:" );
        LOGGER.info( config.getSystemMessageText() );
        LOGGER.info( "Initialized ClaudeTranslationAdapter" );
    }

    @Override
    public TranslationResult translate( TranslationRequest translationRequest )
    {
        final int totalAttempts = translationRequest.getTotalAttempts();
        final int currentAttempt = translationRequest.getCurrentAttempt();

        if ( currentAttempt > adapterMaxAttempts )
        {
            return new TranslationResult( translationRequest, "Claude adapter max attempts to translate text reached", false );
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

        ClaudeMessageRequest messageRequest = createTranslationRequest( temperature, count, originalText );

        final List<ClaudeMessageResponse> responses = sendTranslationRequest( currentAttempt, messageRequest, count );

        if ( responses == null || responses.isEmpty() || responses.get( 0 ) == null )
        {
            return new TranslationResult( translationRequest,
                                          "Claude request failed.",
                                          currentAttempt <= getMaxAttempts() );
        }

        final TranslationResult translationResult = new TranslationResult( translationRequest );
        for ( ClaudeMessageResponse response : responses )
        {
            if ( response != null )
            {
                translationResult.addTranslatedText( response.getTextContent() );
            }
        }
        return translationResult;
    }

    private List<ClaudeMessageResponse> sendTranslationRequest( int currentAttempt, ClaudeMessageRequest messageRequest, int count )
    {
        List<ClaudeMessageResponse> responses = new ArrayList<>();
        for ( int i = 0; i < count; i++ )
        {
            try
            {
                ClaudeMessageResponse response = claudeClient.postMessage( messageRequest );
                responses.add( response );
            }
            catch ( Exception e )
            {
                LOGGER.error( String.format( "Error while sending Claude request (attempt %s, request %s): ", currentAttempt, i + 1 ), e );
            }
        }
        return responses;
    }

    private ClaudeMessageRequest createTranslationRequest( double temperature, int count, String originalText )
    {
        ClaudeMessageRequest request = new ClaudeMessageRequest(
                config.getModel(),
                temperature,
                config.getMaxTokens(),
                systemMessage
        );

        getContextMessages().forEach( message -> request.addMessage( toClaudeMessage( message ) ) );
        getHistoryMessages().forEachRemaining( message -> request.addMessage( toClaudeMessage( message ) ) );

        ClaudeMessage translationMessage = new ClaudeMessage( "user", originalText );
        request.addMessage( translationMessage );

        return request;
    }

    private ClaudeMessage toClaudeMessage( com.tle.i18n.translator.common.Message message )
    {
        return new ClaudeMessage( message.getRole(), message.getContent() );
    }
}
