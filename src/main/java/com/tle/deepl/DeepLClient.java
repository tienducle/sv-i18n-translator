package com.tle.deepl;

import com.google.gson.reflect.TypeToken;
import com.tle.deepl.model.TranslateRequest;
import com.tle.deepl.model.TranslateResponse;
import com.tle.http.AbstractApiClient;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeepLClient extends AbstractApiClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger( DeepLClient.class );

    private final String scheme = "https";
    private final String host;
    private final String V2 = "v2";

    private final String apiKey;

    public DeepLClient( String apiKey, boolean proTier )
    {
        LOGGER.info( "Initializing DeepLClient with API key " + apiKey.substring( 0, 4 ) + "..." );
        this.apiKey = apiKey;
        this.host = proTier ? "api.deepl.com" : "api-free.deepl.com";
    }

    public TranslateResponse postTranslationRequest( TranslateRequest translateRequest )
    {
        final HttpUrl.Builder httpUrlBuilder = new HttpUrl.Builder().scheme( scheme )
                                                                    .host( host )
                                                                    .addPathSegments( V2 + "/translate" );


        final Request request = new Request.Builder().post( getJsonRequestBody( translateRequest ) )
                                                     .url( httpUrlBuilder.build() )
                                                     .addHeader( "Authorization", "DeepL-Auth-Key " + this.apiKey )
                                                     .addHeader( "Content-Type", "application/json" )
                                                     .build();

        return executeRequest( request, TypeToken.get( TranslateResponse.class ).getType() );
    }
}
