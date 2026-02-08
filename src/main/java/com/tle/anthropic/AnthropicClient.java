package com.tle.anthropic;

import com.google.gson.reflect.TypeToken;
import com.tle.anthropic.model.AnthropicMessageRequest;
import com.tle.anthropic.model.AnthropicMessageResponse;
import com.tle.http.AbstractApiClient;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnthropicClient extends AbstractApiClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger( AnthropicClient.class );

    private final String scheme = "https";
    private final String host = "api.anthropic.com";
    private final String VERSION = "v1";

    private final String apiKey;

    public AnthropicClient( String apiKey )
    {
        LOGGER.info( "Initializing AnthropicClient with key " + apiKey.substring( 0, Math.min( 4, apiKey.length() ) ) + "..." );
        this.apiKey = apiKey;
    }

    public AnthropicMessageResponse postMessage( AnthropicMessageRequest messageRequest )
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

        return executeRequest( request, TypeToken.get( AnthropicMessageResponse.class ).getType() );
    }
}
