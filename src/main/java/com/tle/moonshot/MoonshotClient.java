package com.tle.moonshot;

import com.google.gson.reflect.TypeToken;
import com.tle.http.AbstractApiClient;
import com.tle.openai.model.chat.ChatCompletionRequest;
import com.tle.openai.model.chat.ChatCompletionResult;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoonshotClient extends AbstractApiClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger( MoonshotClient.class );

    private final String scheme = "https";
    private final String host;
    private final String V1 = "v1";

    private final String bearerToken;

    public MoonshotClient( String bearerToken )
    {
        this( bearerToken, "api.moonshot.ai" );
    }

    public MoonshotClient( String bearerToken, String host )
    {
        LOGGER.info( "Initializing MoonshotClient with Bearer " + bearerToken.substring( 0, 4 ) + "... host: " + host );
        this.bearerToken = bearerToken;
        this.host = host;
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
