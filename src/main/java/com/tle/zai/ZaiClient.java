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

    private static final String CODING_PLAN_PATH = "api/coding/paas/v4";
    private static final String STANDARD_API_PATH = "api/paas/v4";

    private final String scheme = "https";
    private final String host = "api.z.ai";
    private final String apiPath;

    private final String bearerToken;

    public ZaiClient( String bearerToken, boolean useCodingPlan )
    {
        LOGGER.info( "Initializing ZaiClient with Bearer " + bearerToken.substring( 0, Math.min( 4, bearerToken.length() ) ) + "..." );
        this.bearerToken = bearerToken;
        this.apiPath = useCodingPlan ? CODING_PLAN_PATH : STANDARD_API_PATH;
        LOGGER.info( "Using Zai {} endpoint", useCodingPlan ? "Coding Plan" : "Standard API" );
    }

    public ChatCompletionResult postChatCompletion( ChatCompletionRequest chatCompletionRequest )
    {
        final HttpUrl.Builder httpUrlBuilder = new HttpUrl.Builder()
                .scheme( scheme )
                .host( host )
                .addPathSegments( apiPath + "/chat/completions" );

        final Request request = new Request.Builder().post( getJsonRequestBody( chatCompletionRequest ) )
                                                     .url( httpUrlBuilder.build() )
                                                     .addHeader( "Authorization", "Bearer " + this.bearerToken )
                                                     .addHeader( "Content-Type", "application/json" )
                                                     .build();

        return executeRequest( request, TypeToken.get( ChatCompletionResult.class ).getType() );
    }
}
