package com.tle.i18n.translator.translation;

public class TranslationRequest
{
    private final String key;

    /**
     * The original text to be translated.
     */
    private final String originalText;

    /**
     * Indicates how many attempts were made to translate the text.
     */
    private int totalAttempts = 0;

    public TranslationRequest( String key, String originalText )
    {
        this.key = key;
        this.originalText = originalText;
    }

    /**
     * Initializes a new translation request with the last failed TranslationResult.
     */
    public TranslationRequest( TranslationResult failedResult )
    {
        this.key = failedResult.getKey();
        this.originalText = failedResult.getOriginalText();
        this.totalAttempts = failedResult.getCurrentAttempt() + 1;
    }

    public String getKey()
    {
        return key;
    }

    public String getOriginalText()
    {
        return originalText;
    }

    public int getTotalAttempts()
    {
        return totalAttempts;
    }

    public int getCurrentAttempt()
    {
        return totalAttempts + 1;
    }
}
