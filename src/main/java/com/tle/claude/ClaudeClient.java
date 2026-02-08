package com.tle.claude;

import com.google.gson.reflect.TypeToken;
import com.tle.claude.model.ClaudeMessageRequest;
import com.tle.claude.model.ClaudeMessageResponse;
import com.tle.http.AbstractApiClient;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClaudeClient extends AbstractApiClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ClaudeClient.class );

    private final String scheme = "https";
    private final String host = "api.anthropic.com";
    private final String VERSION = "v1";

    private final String apiKey;

    public ClaudeClient( String apiKey )
    {
        LOGGER.info( "Initializing ClaudeClient with key " + apiKey.substring( 0, Math.min( 4, apiKey.length() ) ) + "..." );
        this.apiKey = apiKey;
    }

    public ClaudeMessageResponse postMessage( ClaudeMessageRequest messageRequest )
    {
        final HttpUrl.Builder httpUrlBuilder = new HttpUrl.Builder()
                .scheme( scheme )
                .host( host )
                .addPathSegments( VERSION + "/messages" );

        final Request request = new Request.Builder().post( getJsonRequestBody( messageRequest ) )
                                                     .url( httpUrlBuilder.build() )
                                                     .addHeader( "x-api-key", this.apiKey )
                                                     .addHeader( "anthropic-version", "2023-06-01" )
                                                     .addHeader( "Content-Type", "application/json" )
                                                     .build();

        return executeRequest( request, TypeToken.get( ClaudeMessageResponse.class ).getType() );
    }
}
