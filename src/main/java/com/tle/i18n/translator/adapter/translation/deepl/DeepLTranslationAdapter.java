package com.tle.i18n.translator.adapter.translation.deepl;

import com.tle.deepl.DeepLClient;
import com.tle.deepl.model.TranslateRequest;
import com.tle.deepl.model.TranslateResponse;
import com.tle.i18n.translator.adapter.translation.TranslationAdapter;
import com.tle.i18n.translator.translation.TranslationRequest;
import com.tle.i18n.translator.translation.TranslationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeepLTranslationAdapter extends TranslationAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger( DeepLTranslationAdapter.class );

    private final DeepLClient deepLClient;

    private final String targetLanguage;

    public DeepLTranslationAdapter( String apiKey, boolean proTier, String targetLanguage )
    {
        this.deepLClient = new DeepLClient( apiKey, proTier );
        this.targetLanguage = targetLanguage;
    }

    @Override
    public TranslationResult translate( TranslationRequest translationRequest )
    {
        // how many attempts were made to translate the text
        final int totalAttempts = translationRequest.getTotalAttempts();
        final int currentAttempt = translationRequest.getCurrentAttempt();

        final String originalText = translationRequest.getOriginalText();

        if ( currentAttempt > 1 )
        {
            LOGGER.warn( "------" );
            LOGGER.warn( "Retrying" );
        }

        final TranslateResponse translateResponse = deepLClient.postTranslationRequest( createTranslationRequest( originalText ) );

        // OpenAI request failed
        if ( translateResponse == null )
        {
            return new TranslationResult( translationRequest,
                                          "Translate request failed.",
                                          currentAttempt <= getMaxAttempts() );
        }

        // OpenAI request succeeded
        final TranslationResult translationResult = new TranslationResult( translationRequest );
        translateResponse.getTranslations()
                         .forEach( translation -> translationResult.addTranslatedText( translation.getText() ) );
        return translationResult;
    }

    /**
     * Creates a {@link TranslateRequest} to translate the given text.
     *
     * @return {@link TranslateRequest}
     */
    private TranslateRequest createTranslationRequest( String originalText )
    {
        return new TranslateRequest( this.targetLanguage, originalText );
    }
}