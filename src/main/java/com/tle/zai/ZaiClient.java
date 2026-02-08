package com.tle.zai;

import com.google.gson.reflect.TypeToken;
import com.tle.http.AbstractApiClient;
import com.tle.openai.model.chat.ChatCompletionRequest;
import com.tle.openai.model.chat.ChatCompletionResult;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZaiClient extends AbstractApiClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ZaiClient.class );

    private final String scheme = "https";
    private final String host = "api.z.ai";
    private final String V4 = "api/coding/paas/v4";

    private final String bearerToken;

    public ZaiClient( String bearerToken )
    {
        LOGGER.info( "Initializing ZaiClient with Bearer " + bearerToken.substring( 0, Math.min( 4, bearerToken.length() ) ) + "..." );
        this.bearerToken = bearerToken;
    }

    public ChatCompletionResult postChatCompletion( ChatCompletionRequest chatCompletionRequest )
    {
        final HttpUrl.Builder httpUrlBuilder = new HttpUrl.Builder()
                .scheme( scheme )
                .host( host )
                .addPathSegments( V4 + "/chat/completions" );

        final Request request = new Request.Builder().post( getJsonRequestBody( chatCompletionRequest ) )
                                                     .url( httpUrlBuilder.build() )
                                                     .addHeader( "Authorization", "Bearer " + this.bearerToken )
                                                     .addHeader( "Content-Type", "application/json" )
                                                     .build();

        return executeRequest( request, TypeToken.get( ChatCompletionResult.class ).getType() );
    }
}
