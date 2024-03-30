package com.tle.i18n.translator.translation;

import java.util.ArrayList;
import java.util.List;

public class TranslationResult
{
    private final String key;

    /**
     * The original text to be translated.
     */
    private final String originalText;

    /**
     * The translated texts.
     */
    private List<String> translatedTexts = new ArrayList<>();

    /**
     * Indicates the current count of attempts to translate the text.
     */
    private int currentAttempt = 1;

    private boolean retryable = true;
    private String errorMessage = null;

    public TranslationResult( TranslationRequest translationRequest )
    {
        this.key = translationRequest.getKey();
        this.originalText = translationRequest.getOriginalText();
        this.currentAttempt = translationRequest.getTotalAttempts();
    }

    public TranslationResult( TranslationRequest translationRequest,
                              String errorMessage,
                              boolean retryable )
    {
        this.key = translationRequest.getKey();
        this.originalText = translationRequest.getOriginalText();
        this.currentAttempt = translationRequest.getTotalAttempts();
        this.errorMessage = errorMessage;
        this.retryable = retryable;
    }

    public String getKey()
    {
        return key;
    }

    public String getOriginalText()
    {
        return originalText;
    }

    public int getCurrentAttempt()
    {
        return currentAttempt;
    }

    public void addTranslatedText( final String translatedText )
    {
        translatedTexts.add( translatedText );
    }

    public List<String> getTranslatedTexts()
    {
        return translatedTexts;
    }

    public boolean isRetryable()
    {
        return retryable;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public boolean hasError()
    {
        return errorMessage != null;
    }
}
