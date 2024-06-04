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

    public OpenAIClient( String bearerToken )
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

        final Request request = new Request.Builder().post( getJsonRequestBody( chatCompletionRequest ) )
                                                     .url( httpUrlBuilder.build() )
                                                     .addHeader( "Authorization", "Bearer " + this.bearerToken )
                                                     .addHeader( "Content-Type", "application/json" )
                                                     .build();

        return executeRequest( request, TypeToken.get( ChatCompletionResult.class ).getType() );
    }

}
