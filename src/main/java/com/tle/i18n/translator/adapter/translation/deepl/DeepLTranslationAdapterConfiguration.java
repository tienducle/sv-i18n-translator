package com.tle.i18n.translator.adapter.translation.deepl;

public class DeepLTranslationAdapterConfiguration
{
    private final String apiKey;
    private boolean proTier;
    private final String sourceLanguage;
    private final String targetLanguage;
    private final String formality;
    private final String glossaryId;

    public DeepLTranslationAdapterConfiguration( String apiKey,
                                                 boolean proTier,
                                                 String sourceLanguage,
                                                 String targetLanguage,
                                                 String formality,
                                                 String glossaryId )
    {
        this.apiKey = apiKey;
        this.proTier = proTier;
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
        this.formality = formality.isEmpty() ? null : formality;
        this.glossaryId = glossaryId.isEmpty() ? null : glossaryId;
    }

    public String getApiKey()
    {
        return apiKey;
    }

    public boolean isProTier()
    {
        return proTier;
    }

    public String getSourceLanguage()
    {
        return sourceLanguage;
    }

    public String getTargetLanguage()
    {
        return targetLanguage;
    }

    public String getFormality()
    {
        return formality;
    }

    public String getGlossaryId()
    {
        return glossaryId;
    }
}
