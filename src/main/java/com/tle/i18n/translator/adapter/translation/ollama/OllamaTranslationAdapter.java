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

public class OllamaTranslationAdapter extends TranslationAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger( OllamaTranslationAdapter.class );

    private final OllamaClient ollamaClient;

    private final String model;

    private final Message systemMessage;


    public OllamaTranslationAdapter( String scheme, String host, int port, String model, String systemMessage )
    {
        LOGGER.info( "Initializing OllamaTranslationAdapter" );
        this.ollamaClient = new OllamaClient( scheme, host, port );
        this.model = model;
        this.systemMessage = new Message();
        this.systemMessage.setRole( "system" );
        this.systemMessage.setContent( systemMessage );
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

        StringBuilder sb = new StringBuilder();
        sb.append( "[INST]" );
        sb.append( " " );
        sb.append( systemMessage.getContent() );
        sb.append( " " );
        sb.append( "[/INST]" );
        sb.append( "\n" );
        sb.append( "Okay please provide your text for translation." );
        sb.append( "\n" );
        sb.append( "[INST]" );
        sb.append( " " );
        sb.append( originalText );
        sb.append( " " );
        sb.append( "[/INST]" );

        final ChatCompletionResponse chatCompletionResponse = ollamaClient.postChatCompletion( createTranslationRequest( originalText ) );

        // OpenAI request failed
        if ( chatCompletionResponse == null )
        {
            return new TranslationResult( translationRequest,
                                          "ChatCompletion request failed.",
                                          currentAttempt <= getMaxAttempts() );
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

        final Message translationMessage = new Message();
        translationMessage.setRole( "user" );
        translationMessage.setContent( originalText );
        request.addMessage( translationMessage );

        return request;
    }
}
