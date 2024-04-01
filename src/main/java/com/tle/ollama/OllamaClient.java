package com.tle.ollama;

import com.google.gson.reflect.TypeToken;
import com.tle.http.AbstractApiClient;
import com.tle.ollama.model.chat.ChatCompletionRequest;
import com.tle.ollama.model.chat.ChatCompletionResponse;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OllamaClient extends AbstractApiClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger( OllamaClient.class );

    private final String scheme;
    private final String host;
    private int port;

    public OllamaClient( String scheme, String host, int port )
    {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
    }

    public ChatCompletionResponse postChatCompletion( ChatCompletionRequest chatCompletionRequest )
    {
        final HttpUrl.Builder httpUrlBuilder = new HttpUrl.Builder()
                .scheme( scheme )
                .host( host )
                .port( port )
                .addPathSegments( "api/chat" );


        final Request request = new Request.Builder().post( getJsonRequestBody( chatCompletionRequest ) )
                                                     .url( httpUrlBuilder.build() )
                                                     .build();

        return executeRequest( request, TypeToken.get( ChatCompletionResponse.class ).getType() );
    }
}
