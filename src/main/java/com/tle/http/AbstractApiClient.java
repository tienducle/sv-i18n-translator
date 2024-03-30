package com.tle.http;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;

public abstract class AbstractApiClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger( AbstractApiClient.class );

    private final Gson gson = new Gson();

    /**
     * Executes a {@link Request} and parses the response into an object of the given {@code Type}
     */
    public <T> T executeRequest( Request request, Type type )
    {
        final OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout( Duration.ofSeconds( 300 ) )
                .build();

        // Execute request
        try ( Response response = client.newCall( request ).execute() )
        {
            // Request was executed, parse the response message
            String responseString = response.body().string();

            // Check if the response is OK
            if ( response.code() >= 200 && response.code() < 300 )
            {
                // Try to parse the result if a class was specified
                if ( type != null )
                {
                    return gson.fromJson( responseString, type );
                }
                return null;
            }
            if ( request.body() != null )
            {
                final Buffer buffer = new Buffer();
                request.body().writeTo( buffer );
                //LOGGER.error( String.format( "Request body was: %s", buffer.readUtf8() ) );
                responseString += "Request body was: " + buffer.readUtf8();
            }
            throw new RuntimeException( responseString );
        }
        catch ( JsonParseException e )
        {
            LOGGER.error( String.format( "Failed to parse response: %s", e ), e );
        }
        catch ( IOException e )
        {
            LOGGER.error( String.format( "Failed to execute request: %s", e ), e );
        }

        return null;
    }

    /**
     * Converts the given object into a {@link RequestBody} in JSON format
     */
    public RequestBody getJsonRequestBody( Object object )
    {
        return RequestBody.create( gson.toJson( object ), MediaType.get( "application/json" ) );
    }
}
