package com.tle.i18n.translator.adapter.translation.ollama;

import com.tle.i18n.translator.adapter.translation.TranslationAdapter;
import com.tle.i18n.translator.translation.TranslationRequest;
import com.tle.i18n.translator.translation.TranslationResult;
import com.tle.ollama.OllamaClient;
import com.tle.ollama.model.chat.ChatCompletionRequest;
import com.tle.ollama.model.chat.ChatCompletionResponse;
import com.tle.ollama.model.chat.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty( value = "translation.adapter", havingValue = "Ollama" )
@Lazy
public class OllamaTranslationAdapter extends TranslationAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger( OllamaTranslationAdapter.class );

    private final OllamaClient ollamaClient;

    private final String model;

    private final Message systemMessage;

    public OllamaTranslationAdapter( @Value( "${translation.adapter.ollama.scheme:http}" ) String scheme,
                                     @Value( "${translation.adapter.ollama.host:localhost}" ) String host,
                                     @Value( "${translation.adapter.ollama.port:11434}" ) int port,
                                     @Value( "${translation.adapter.ollama.model:llama3:instruct}" ) String model,
                                     @Value( "${translation.adapter.ollama.chat.systemMessage:}" ) String systemMessage )
    {
        LOGGER.info( String.format( "Initializing OllamaTranslationAdapter for model '%s'", model ) );
        this.ollamaClient = new OllamaClient( scheme, host, port );
        this.model = model;
        this.systemMessage = new Message();
        this.systemMessage.setRole( "system" );
        this.systemMessage.setContent( systemMessage );
        LOGGER.info( "System message:" );
        LOGGER.info( systemMessage );
        LOGGER.info( "Initialized OllamaTranslationAdapter" );
    }

    @Override
    public TranslationResult translate( TranslationRequest translationRequest )
    {
        // how many attempts were made to translate the text
        final int totalAttempts = translationRequest.getTotalAttempts();
        final int currentAttempt = translationRequest.getCurrentAttempt();

        final String originalText = translationRequest.getOriginalText();

        if ( currentAttempt > 1 )
        {
            LOGGER.warn( "------" );
            LOGGER.warn( "Retrying" );
        }

        final ChatCompletionResponse chatCompletionResponse = ollamaClient.postChatCompletion( createTranslationRequest( originalText ) );

        // OpenAI request failed
        if ( chatCompletionResponse == null )
        {
            return new TranslationResult( translationRequest, "ChatCompletion request failed.", currentAttempt <= getMaxAttempts() );
        }

        // OpenAI request succeeded
        final TranslationResult translationResult = new TranslationResult( translationRequest );
        translationResult.addTranslatedText( chatCompletionResponse.getMessage().getContent().trim() );
        return translationResult;
    }

    /**
     * Creates a {@link ChatCompletionRequest} to translate the given text.
     *
     * @return {@link ChatCompletionRequest}
     */
    private ChatCompletionRequest createTranslationRequest( String originalText )
    {
        final ChatCompletionRequest request = new ChatCompletionRequest( this.model );

        request.addMessage( this.systemMessage );

        getContextMessages().forEach( message -> request.addMessage( toOllamaMessage( message ) ) );
        getHistoryMessages().forEachRemaining( message -> request.addMessage( toOllamaMessage( message ) ) );

        final Message translationMessage = new Message();
        translationMessage.setRole( "user" );
        translationMessage.setContent( originalText );
        request.addMessage( translationMessage );

        return request;
    }

    private Message toOllamaMessage( com.tle.i18n.translator.common.Message message )
    {
        final Message ollamaMessage = new Message();
        ollamaMessage.setRole( message.getRole() );
        ollamaMessage.setContent( message.getContent() );
        return ollamaMessage;
    }
}
