package com.tle.i18n.translator.adapter.translation.openai;

import com.tle.i18n.translator.adapter.translation.TranslationAdapter;
import com.tle.i18n.translator.translation.TranslationRequest;
import com.tle.i18n.translator.translation.TranslationResult;
import com.tle.openai.OpenAIClient;
import com.tle.openai.model.chat.ChatCompletionRequest;
import com.tle.openai.model.chat.ChatCompletionResult;
import com.tle.openai.model.chat.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class OpenAITranslationAdapter extends TranslationAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger( OpenAITranslationAdapter.class );

    private final OpenAITranslationAdapterConfiguration config;

    private final OpenAIClient openAIClient;

    private final Message systemMessage;

    private final double maxTemperature = 2.0;
    private final int maxN = 16;
    private final int adapterMaxAttempts = 4;

    public OpenAITranslationAdapter( OpenAITranslationAdapterConfiguration config )
    {
        this.config = config;
        this.openAIClient = new OpenAIClient( config.getApiKey() );

        this.systemMessage = new Message();
        systemMessage.setRole( "system" );
        systemMessage.setContent( config.getSystemMessageText() );
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
            return new TranslationResult( translationRequest, "OpenAI adapter max attempts to translate text reached", false );
        }

        final String originalText = translationRequest.getOriginalText();

        // 0.2, 0.8, 1.4, max 2.0
        final double temperature = Math.min(
                // round up to 1 decimal digit
                Math.ceil( ( config.getInitTemperature() + ( config.getTemperatureIncrement() * totalAttempts ) * 10 ) ) / 10,
                maxTemperature );

        // 1, 6, 11, max. 16
        final int requestedTranslations = Math.min( config.getN() + ( 5 * totalAttempts ),
                                                    maxN );

        if ( currentAttempt > 1 )
        {
            LOGGER.warn( "------" );
            LOGGER.warn( String.format( "Retrying with temperature %s and n %s", temperature, requestedTranslations ) );
        }

        final ChatCompletionResult completionResult = sendTranslationRequest( currentAttempt,
                                                                              originalText,
                                                                              temperature,
                                                                              requestedTranslations );

        // OpenAI request failed
        if ( completionResult == null )
        {
            return new TranslationResult( translationRequest,
                                          "ChatCompletion request failed.",
                                          currentAttempt <= getMaxAttempts() );
        }

        // OpenAI request succeeded
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
    public ChatCompletionResult sendTranslationRequest( int currentAttempt, String text, double temperature, int n )
    {
        ChatCompletionResult completionResult = null;
        try
        {
            completionResult = openAIClient.postChatCompletion( createTranslationRequest( temperature, n, text ) );
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
                                                                         this.config.getMaxTokens(),
                                                                         temperature,
                                                                         count );

        request.addMessage( this.systemMessage );

        Message translationMessage = new Message();
        translationMessage.setRole( "user" );
        translationMessage.setContent( originalText );
        request.addMessage( translationMessage );

        return request;
    }
}
