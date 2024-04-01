package com.tle.i18n.translator.adapter.translation;

import com.tle.i18n.translator.translation.TranslationRequest;
import com.tle.i18n.translator.translation.TranslationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component( "TranslationAdapter" )
public abstract class TranslationAdapter
{
    @Value( "${translation.adapter.maxAttempts:4}" )
    private int maxAttempts;

    public TranslationAdapter()
    {
    }

    /**
     * Returns the maximum number of attempts for translating a text.
     * Configured in the application.properties file under the key "translation.adapter.maxAttempts".
     */
    public int getMaxAttempts()
    {
        return maxAttempts;
    }

    /**
     * Translates the text in the given {@link TranslationRequest} object and returns a {@link TranslationResult}
     * containing one or more translated texts.
     * <p></p>
     * Depending on {@link TranslationResult#hasError()} and {@link TranslationResult#isRetryable()},
     * the translation will be retried if no satisfiable result was found.
     */
    public abstract TranslationResult translate( TranslationRequest translation );
}
