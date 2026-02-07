package com.tle.i18n.translator.adapter.translation.openai;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.tle.i18n.translator.adapter.translation.TranslationAdapter;
import com.tle.i18n.translator.translation.TranslationRequest;
import com.tle.i18n.translator.translation.TranslationResult;
import com.tle.openai.OpenAIClient;
import com.tle.openai.model.chat.ChatCompletionRequest;
import com.tle.openai.model.chat.ChatCompletionResult;
import com.tle.openai.model.chat.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@ConditionalOnProperty( value = "translation.adapter", havingValue = "OpenAI" )
@Lazy
public class OpenAITranslationAdapter extends TranslationAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger( OpenAITranslationAdapter.class );

    private final EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();

    private final OpenAITranslationAdapterConfiguration config;

    private final OpenAIClient openAIClient;

    private final Message systemMessage;

    private final double maxTemperature = 2.0;
    private final int maxN = 7;
    private final int adapterMaxAttempts = 3;

    public OpenAITranslationAdapter( OpenAITranslationAdapterConfiguration config )
    {
        LOGGER.info( "Initializing OpenAITranslationAdapter" );
        this.config = config;
        this.openAIClient = new OpenAIClient( config.getApiKey() );

        this.systemMessage = new Message();
        systemMessage.setRole( "system" );
        systemMessage.setContent( config.getSystemMessageText() );
        LOGGER.info( "Model: {}", config.getModel() );
        LOGGER.info( "System message:" );
        LOGGER.info( config.getSystemMessageText() );
        LOGGER.info( "Initialized OpenAITranslationAdapter" );
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
                Math.ceil( ( config.getInitTemperature() + config.getTemperatureIncrement() * totalAttempts ) * 10 ) / 10,
                maxTemperature );

        // 1, 3, 5, 7
        final int requestedTranslations = Math.min( config.getN() + ( 3 * totalAttempts ),
                                                    maxN );

        if ( currentAttempt > 1 )
        {
            LOGGER.warn( "------" );
            LOGGER.warn( String.format( "Retrying with temperature %s and n %s", temperature, requestedTranslations ) );
        }

        ChatCompletionRequest chatCompletionRequest = createTranslationRequest( temperature, requestedTranslations, originalText );

        final int inputTokensEstimation = countMessageTokens( chatCompletionRequest.getModel(), chatCompletionRequest.getMessages() );
        LOGGER.info( String.format( "Input tokens estimation: %s", inputTokensEstimation ) );

        final ChatCompletionResult completionResult = sendTranslationRequest( currentAttempt,
                                                                              chatCompletionRequest );

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
    public ChatCompletionResult sendTranslationRequest( int currentAttempt, ChatCompletionRequest chatCompletionRequest )
    {
        ChatCompletionResult completionResult = null;
        try
        {
            completionResult = openAIClient.postChatCompletion( chatCompletionRequest );
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

        getContextMessages().forEach( message -> request.addMessage( toOpenAiMessage( message ) ) );
        getHistoryMessages().forEachRemaining( message -> request.addMessage( toOpenAiMessage( message ) ) );

        Message translationMessage = new Message();
        translationMessage.setRole( "user" );
        translationMessage.setContent( originalText );
        request.addMessage( translationMessage );

        return request;
    }

    private Message toOpenAiMessage( com.tle.i18n.translator.common.Message message )
    {
        final Message openAiMessage = new Message();
        openAiMessage.setRole( message.getRole() );
        openAiMessage.setContent( message.getContent() );
        return openAiMessage;
    }

    // from https://jtokkit.knuddels.de/docs/getting-started/recipes/chatml
    private int countMessageTokens( String model, List<Message> messages )
    {
        Encoding encoding = model.startsWith( "gpt-5" )
                            ? registry.getEncodingForModel( "gpt-4o" ).get()
                            : registry.getEncodingForModel( model ).orElseThrow();

        int tokensPerMessage;
        if ( model.startsWith( "gpt-3.5-turbo" ) )
        {
            tokensPerMessage = 4; // every message follows <|start|>{role/name}\n{content}<|end|>\n
        }
        else
        {
            tokensPerMessage = 3;
        }

        int sum = 0;
        for ( final var message : messages )
        {
            sum += tokensPerMessage;
            sum += encoding.countTokens( message.getContent() );
            sum += encoding.countTokens( message.getRole() );
        }

        sum += 3; // every reply is primed with <|start|>assistant<|message|>

        return sum;
    }
}
