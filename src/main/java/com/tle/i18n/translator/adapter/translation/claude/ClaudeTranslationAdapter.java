package com.tle.i18n.translator.adapter.translation.claude;

import com.tle.claude.ClaudeClient;
import com.tle.i18n.translator.adapter.translation.TranslationAdapter;
import com.tle.i18n.translator.translation.TranslationRequest;
import com.tle.i18n.translator.translation.TranslationResult;
import com.tle.openai.model.chat.ChatCompletionRequest;
import com.tle.openai.model.chat.ChatCompletionResult;
import com.tle.openai.model.chat.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@ConditionalOnProperty( value = "translation.adapter", havingValue = "Claude" )
@Lazy
public class ClaudeTranslationAdapter extends TranslationAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ClaudeTranslationAdapter.class );

    private final ClaudeTranslationAdapterConfiguration config;

    private final ClaudeClient claudeClient;

    private final Message systemMessage;

    private final double maxTemperature = 1.0;
    private final int maxN = 5;
    private final int adapterMaxAttempts = 3;

    public ClaudeTranslationAdapter( ClaudeTranslationAdapterConfiguration config )
    {
        LOGGER.info( "Initializing ClaudeTranslationAdapter" );
        this.config = config;
        this.claudeClient = new ClaudeClient( config.getApiKey() );

        this.systemMessage = new Message();
        systemMessage.setRole( "system" );
        systemMessage.setContent( config.getSystemMessageText() );
        LOGGER.info( "Model: {}", config.getModel() );
        LOGGER.info( "System message:" );
        LOGGER.info( config.getSystemMessageText() );
        LOGGER.info( "Initialized ClaudeTranslationAdapter" );
    }

    @Override
    public TranslationResult translate( TranslationRequest translationRequest )
    {
        // how many attempts were made to translate the text
        final int totalAttempts = translationRequest.getTotalAttempts();
        final int currentAttempt = translationRequest.getCurrentAttempt();

        if ( currentAttempt > adapterMaxAttempts )
        {
            // hard limit of adapter
            return new TranslationResult( translationRequest, "Claude adapter max attempts to translate text reached", false );
        }

        final String originalText = translationRequest.getOriginalText();

        // 0.2, 0.6, 1.0, max 1.0
        final double temperature = Math.min(
                Math.ceil( ( config.getInitTemperature() + config.getTemperatureIncrement() * totalAttempts ) * 10 ) / 10,
                maxTemperature );

        // 1, 2, 3, 5
        final int requestedTranslations = Math.min( config.getN() + ( 1 * totalAttempts ),
                                                    maxN );

        if ( currentAttempt > 1 )
        {
            LOGGER.warn( "------" );
            LOGGER.warn( String.format( "Retrying with temperature %s and n %s", temperature, requestedTranslations ) );
        }

        ChatCompletionRequest chatCompletionRequest = createTranslationRequest( temperature, requestedTranslations, originalText );

        final ChatCompletionResult completionResult = sendTranslationRequest( currentAttempt,
                                                                              chatCompletionRequest );

        // Claude request failed
        if ( completionResult == null )
        {
            return new TranslationResult( translationRequest,
                                          "ChatCompletion request failed.",
                                          currentAttempt <= getMaxAttempts() );
        }

        // Claude request succeeded
        final TranslationResult translationResult = new TranslationResult( translationRequest );
        Arrays.stream( completionResult.getChoices() )
              .forEach( choice -> translationResult.addTranslatedText( choice.getMessage().getContent() ) );
        return translationResult;
    }

    /**
     * Sends a ChatCompletion request to translate the given text.
     *
     * @return the {@link ChatCompletionResult} if a valid response was received, null otherwise.
     */
    public ChatCompletionResult sendTranslationRequest( int currentAttempt, ChatCompletionRequest chatCompletionRequest )
    {
        ChatCompletionResult completionResult = null;
        try
        {
            completionResult = claudeClient.postChatCompletion( chatCompletionRequest );
        }
        catch ( Exception e )
        {
            LOGGER.error( String.format( "Error while sending ChatCompletion request (attempt %s): ", currentAttempt ), e );
        }
        return completionResult;
    }

    /**
     * Creates a {@link ChatCompletionRequest} to translate the given text.
     *
     * @return {@link ChatCompletionRequest}
     */
    private ChatCompletionRequest createTranslationRequest( double temperature, int count, String originalText )
    {
        final ChatCompletionRequest request = new ChatCompletionRequest( this.config.getModel(),
                                                                         temperature,
                                                                         count );

        request.addMessage( this.systemMessage );

        getContextMessages().forEach( message -> request.addMessage( toClaudeMessage( message ) ) );
        getHistoryMessages().forEachRemaining( message -> request.addMessage( toClaudeMessage( message ) ) );

        Message translationMessage = new Message();
        translationMessage.setRole( "user" );
        translationMessage.setContent( originalText );
        request.addMessage( translationMessage );

        return request;
    }

    private Message toClaudeMessage( com.tle.i18n.translator.common.Message message )
    {
        final Message claudeMessage = new Message();
        claudeMessage.setRole( message.getRole() );
        claudeMessage.setContent( message.getContent() );
        return claudeMessage;
    }
}
