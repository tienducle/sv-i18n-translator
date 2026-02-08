package com.tle.gemini;

import com.google.gson.reflect.TypeToken;
import com.tle.gemini.model.GenerateContentRequest;
import com.tle.gemini.model.GenerateContentResponse;
import com.tle.http.AbstractApiClient;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeminiClient extends AbstractApiClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger( GeminiClient.class );

    private static final String SCHEME = "https";
    private static final String HOST = "generativelanguage.googleapis.com";
    private static final String VERSION = "v1beta";

    private final String apiKey;
    private final String model;

    public GeminiClient( String apiKey, String model )
    {
        LOGGER.info( "Initializing GeminiClient with key " + apiKey.substring( 0, Math.min( 4, apiKey.length() ) ) + "..." );
        this.apiKey = apiKey;
        this.model = model;
        LOGGER.info( "Model: {}", model );
    }

    public GenerateContentResponse generateContent( GenerateContentRequest generateContentRequest )
    {
        final HttpUrl.Builder httpUrlBuilder = new HttpUrl.Builder()
                .scheme( SCHEME )
                .host( HOST )
                .addPathSegments( VERSION + "/models/" + model + ":generateContent" )
                .addQueryParameter( "key", apiKey );

        final Request request = new Request.Builder().post( getJsonRequestBody( generateContentRequest ) )
                                                     .url( httpUrlBuilder.build() )
                                                     .addHeader( "Content-Type", "application/json" )
                                                     .build();

        return executeRequest( request, TypeToken.get( GenerateContentResponse.class ).getType() );
    }
}
