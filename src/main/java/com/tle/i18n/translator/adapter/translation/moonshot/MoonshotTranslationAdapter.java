package com.tle.i18n.translator.adapter.translation.moonshot;

import com.tle.i18n.translator.adapter.translation.TranslationAdapter;
import com.tle.i18n.translator.translation.TranslationRequest;
import com.tle.i18n.translator.translation.TranslationResult;
import com.tle.moonshot.MoonshotClient;
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
@ConditionalOnProperty( value = "translation.adapter", havingValue = "Moonshot" )
@Lazy
public class MoonshotTranslationAdapter extends TranslationAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger( MoonshotTranslationAdapter.class );

    private final MoonshotTranslationAdapterConfiguration config;

    private final MoonshotClient moonshotClient;

    private final Message systemMessage;

    private final double maxTemperature = 1.0;
    private final int maxN = 5;
    private final int adapterMaxAttempts = 3;

    public MoonshotTranslationAdapter( MoonshotTranslationAdapterConfiguration config )
    {
        LOGGER.info( "Initializing MoonshotTranslationAdapter" );
        this.config = config;
        this.moonshotClient = new MoonshotClient( config.getApiKey() );

        this.systemMessage = new Message();
        systemMessage.setRole( "system" );
        systemMessage.setContent( config.getSystemMessageText() );
        LOGGER.info( "Model: {}", config.getModel() );
        LOGGER.info( "System message:" );
        LOGGER.info( config.getSystemMessageText() );
        LOGGER.info( "Initialized MoonshotTranslationAdapter" );
    }

    @Override
    public TranslationResult translate( TranslationRequest translationRequest )
    {
        final int totalAttempts = translationRequest.getTotalAttempts();
        final int currentAttempt = translationRequest.getCurrentAttempt();

        if ( currentAttempt > adapterMaxAttempts )
        {
            return new TranslationResult( translationRequest, "Moonshot adapter max attempts to translate text reached", false );
        }

        final String originalText = translationRequest.getOriginalText();

        final double temperature = Math.min(
                Math.ceil( ( config.getInitTemperature() + config.getTemperatureIncrement() * totalAttempts ) * 10 ) / 10,
                maxTemperature );

        final int requestedTranslations = Math.min( config.getN() + ( 2 * totalAttempts ),
                                                    maxN );

        if ( currentAttempt > 1 )
        {
            LOGGER.warn( "------" );
            LOGGER.warn( String.format( "Retrying with temperature %s and n %s", temperature, requestedTranslations ) );
        }

        ChatCompletionRequest chatCompletionRequest = createTranslationRequest( temperature, requestedTranslations, originalText );

        LOGGER.info( String.format( "Input text length: %s characters", originalText.length() ) );

        final ChatCompletionResult completionResult = sendTranslationRequest( currentAttempt,
                                                                              chatCompletionRequest );

        if ( completionResult == null )
        {
            return new TranslationResult( translationRequest,
                                          "ChatCompletion request failed.",
                                          currentAttempt <= getMaxAttempts() );
        }

        final TranslationResult translationResult = new TranslationResult( translationRequest );
        Arrays.stream( completionResult.getChoices() )
              .forEach( choice -> translationResult.addTranslatedText( choice.getMessage().getContent() ) );
        return translationResult;
    }

    public ChatCompletionResult sendTranslationRequest( int currentAttempt, ChatCompletionRequest chatCompletionRequest )
    {
        ChatCompletionResult completionResult = null;
        try
        {
            completionResult = moonshotClient.postChatCompletion( chatCompletionRequest );
        }
        catch ( Exception e )
        {
            LOGGER.error( String.format( "Error while sending ChatCompletion request (attempt %s): ", currentAttempt ), e );
        }
        return completionResult;
    }

    private ChatCompletionRequest createTranslationRequest( double temperature, int count, String originalText )
    {
        final double effectiveTemperature = isFixedTemperatureModel() ? 1.0 : temperature;

        final ChatCompletionRequest request = new ChatCompletionRequest( this.config.getModel(),
                                                                         effectiveTemperature,
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

    // Checks if the current model has a fixed temperature (kimi-k2.5). These models don't allow custom temperature values
    private boolean isFixedTemperatureModel()
    {
        return "kimi-k2.5".equals( config.getModel() );
    }
}
