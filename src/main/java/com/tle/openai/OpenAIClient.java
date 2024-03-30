package com.tle.openai;

import com.google.gson.reflect.TypeToken;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.tle.http.AbstractApiClient;
import com.tle.openai.model.chat.ChatCompletionRequest;
import com.tle.openai.model.chat.ChatCompletionResult;
import com.tle.openai.model.chat.Message;
import com.tle.openai.model.completion.CompletionRequest;
import com.tle.openai.model.completion.CompletionResult;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OpenAIClient extends AbstractApiClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger( OpenAIClient.class );

    private final String scheme = "https";
    private final String host = "api.openai.com";
    private final String V1 = "v1";

    private final String bearerToken;

    private final EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();

    public OpenAIClient(String bearerToken )
    {
        LOGGER.info( "Initializing ChatGptClient with Bearer " + bearerToken.substring( 0, 4 ) + "..." );
        this.bearerToken = bearerToken;
    }

    public CompletionResult postCompletion( CompletionRequest completionRequest )
    {
        final HttpUrl.Builder httpUrlBuilder = new HttpUrl.Builder()
                .scheme( scheme )
                .host( host )
                .addPathSegments( V1 + "/completions" );

        final Request request = new Request.Builder().post( getJsonRequestBody( completionRequest ) )
                                                     .url( httpUrlBuilder.build() )
                                                     .addHeader( "Authorization", "Bearer " + this.bearerToken )
                                                     .addHeader( "Content-Type", "application/json" )
                                                     .build();

        return executeRequest( request, TypeToken.get( CompletionResult.class ).getType() );
    }

    public ChatCompletionResult postChatCompletion( ChatCompletionRequest chatCompletionRequest )
    {
        final HttpUrl.Builder httpUrlBuilder = new HttpUrl.Builder()
                .scheme( scheme )
                .host( host )
                .addPathSegments( V1 + "/chat/completions" );

        // request 20% more tokens than the sum of all message tokens
        chatCompletionRequest.setMaxTokens( (int) ( countMessageTokens( chatCompletionRequest.getModel(), chatCompletionRequest.getMessages() ) * 1.2 )
                                            + (int) ( chatCompletionRequest.getTemperature() * 50 ) );

        final Request request = new Request.Builder().post( getJsonRequestBody( chatCompletionRequest ) )
                                                     .url( httpUrlBuilder.build() )
                                                     .addHeader( "Authorization", "Bearer " + this.bearerToken )
                                                     .addHeader( "Content-Type", "application/json" )
                                                     .build();

        return executeRequest( request, TypeToken.get( ChatCompletionResult.class ).getType() );
    }

    // from https://jtokkit.knuddels.de/docs/getting-started/recipes/chatml
    private int countMessageTokens( String model, List<Message> messages )
    {
        Encoding encoding = registry.getEncodingForModel( model ).orElseThrow();
        int tokensPerMessage;
        if ( model.startsWith( "gpt-4" ) )
        {
            tokensPerMessage = 3;
        }
        else if ( model.startsWith( "gpt-3.5-turbo" ) )
        {
            tokensPerMessage = 4; // every message follows <|start|>{role/name}\n{content}<|end|>\n
        }
        else
        {
            throw new IllegalArgumentException( "Unsupported model: " + model );
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
