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
                                                 String formality,
                                                 String glossaryId,
                                                 String sourceLanguage,
                                                 String targetLanguage )
    {
        this.apiKey = apiKey;
        this.proTier = proTier;
        this.formality = formality.isEmpty() ? null : formality;
        this.glossaryId = glossaryId.isEmpty() ? null : glossaryId;
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
    }

    public String getApiKey()
    {
        return apiKey;
    }

    public boolean isProTier()
    {
        return proTier;
    }

    public String getFormality()
    {
        return formality;
    }

    public String getGlossaryId()
    {
        return glossaryId;
    }

    public String getSourceLanguage()
    {
        return sourceLanguage;
    }

    public String getTargetLanguage()
    {
        return targetLanguage;
    }
}
