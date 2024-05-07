package com.tle.i18n.translator.adapter.translation.deepl;

import com.tle.i18n.translator.util.LocaleUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty( value = "translation.adapter", havingValue = "DeepL" )
public class DeepLTranslationAdapterConfiguration
{
    private final String apiKey;
    private boolean proTier;
    private final String sourceLanguage;
    private final String targetLanguage;
    private final String formality;
    private final String glossaryId;

    public DeepLTranslationAdapterConfiguration( @Value( "${translation.adapter.deepl.apiKey:}" ) String apiKey,
                                                 @Value( "${translation.adapter.deepl.proTier:false}" ) boolean proTier,
                                                 @Value( "${translation.adapter.deepl.formality:}" ) String formality,
                                                 @Value( "${translation.adapter.deepl.glossaryId:}" ) String glossaryId,
                                                 @Value( "${translation.adapter.deepl.sourceLanguage:English}" ) String sourceLanguage,
                                                 @Value( "${targetLanguage:German}" ) String targetLanguage,
                                                 LocaleUtils localeUtils )
    {
        final String sourceLanguageCode = localeUtils.getCountryCodeForLanguage( sourceLanguage );
        final String targetLanguageCode = localeUtils.getCountryCodeForLanguage( targetLanguage );
        this.apiKey = apiKey;
        this.proTier = proTier;
        this.formality = formality.isEmpty() ? null : formality;
        this.glossaryId = glossaryId.isEmpty() ? null : glossaryId;
        this.sourceLanguage = sourceLanguageCode;
        this.targetLanguage = targetLanguageCode;
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
