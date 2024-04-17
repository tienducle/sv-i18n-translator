package com.tle.i18n.translator;

import com.tle.i18n.translator.adapter.translation.TranslationAdapter;
import com.tle.i18n.translator.adapter.translation.deepl.DeepLTranslationAdapter;
import com.tle.i18n.translator.adapter.translation.deepl.DeepLTranslationAdapterConfiguration;
import com.tle.i18n.translator.adapter.translation.manual.ManualTranslationAdapter;
import com.tle.i18n.translator.adapter.translation.ollama.OllamaTranslationAdapter;
import com.tle.i18n.translator.adapter.translation.openai.OpenAITranslationAdapter;
import com.tle.i18n.translator.adapter.translation.openai.OpenAITranslationAdapterConfiguration;
import com.tle.i18n.translator.adapter.validation.DefaultValidationAdapter;
import com.tle.i18n.translator.adapter.validation.ValidationAdapter;
import com.tle.i18n.translator.adapter.validation.stardewvalley.StardewValleyValidationAdapter;
import com.tle.i18n.translator.util.LocaleUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.util.List;

@Configuration
@Lazy
@PropertySources( value = {
        @PropertySource( value = "classpath:local.properties", ignoreResourceNotFound = true ),
        @PropertySource( value = "classpath:application.properties" ),
} )
public class Config
{
    @Value( "${runModes}" )
    private List<RunMode> runModes;

    @Value( "${sourceLanguage:English}" )
    private String sourceLanguage;

    @Value( "${targetLanguage:German}" )
    private String targetLanguage;

    public List<RunMode> getRunModes()
    {
        return runModes;
    }

    /*
     * Translation Adapter
     */
    @Bean
    @ConditionalOnProperty( value = "translation.adapter", havingValue = "Manual" )
    public TranslationAdapter manualTranslationAdapter()
    {
        return new ManualTranslationAdapter();
    }

    @Bean
    @ConditionalOnProperty( value = "translation.adapter", havingValue = "DeepL" )
    @Lazy
    public TranslationAdapter deepLTranslationAdapter( LocaleUtils localeUtils,
                                                       @Value( "${translation.adapter.deepl.apiKey:}" ) String apiKey,
                                                       @Value( "${translation.adapter.deepl.proTier:false}" ) boolean proTier,
                                                       @Value( "${translation.adapter.deepl.formality:}" ) String formality,
                                                       @Value( "${translation.adapter.deepl.glossaryId:}" ) String glossaryId )
    {
        final String sourceLanguageCode = localeUtils.getCountryCodeForLanguage( this.sourceLanguage );
        final String targetLanguageCode = localeUtils.getCountryCodeForLanguage( this.targetLanguage );
        return new DeepLTranslationAdapter( new DeepLTranslationAdapterConfiguration( apiKey,
                                                                                      proTier,
                                                                                      formality,
                                                                                      glossaryId,
                                                                                      sourceLanguageCode,
                                                                                      targetLanguageCode ) );
    }

    @Bean
    @ConditionalOnProperty( value = "translation.adapter", havingValue = "Ollama" )
    public TranslationAdapter ollamaTranslationAdapter( @Value( "${translation.adapter.ollama.scheme:http}" ) String scheme,
                                                        @Value( "${translation.adapter.ollama.host:localhost}" ) String host,
                                                        @Value( "${translation.adapter.ollama.port:11434}" ) int port,
                                                        @Value( "${translation.adapter.ollama.model:llama2}" ) String model,
                                                        @Value( "${translation.adapter.ollama.chat.systemMessage:}" ) String systemMessage
                                                      )
    {
        return new OllamaTranslationAdapter( scheme, host, port, model, systemMessage );
    }

    @Bean
    @ConditionalOnProperty( value = "translation.adapter", havingValue = "OpenAI" )
    public TranslationAdapter openAITranslationAdapter( @Value( "${translation.adapter.openai.apiKey:}" ) String apiKey,
                                                        @Value( "${translation.adapter.translation.openai.chat.model:}" ) String model,
                                                        @Value( "${translation.adapter.openai.chat.maxTokens:2000}" ) int maxTokens,
                                                        @Value( "${translation.adapter.openai.chat.initTemperature:0.2}" ) double initTemperature,
                                                        @Value( "${translation.adapter.openai.chat.temperatureIncrement:0.6}" ) double temperatureIncrement,
                                                        @Value( "${adapter.translation.openai.chat.n:1}" ) int n,
                                                        @Value( "${translation.adapter.openai.chat.systemMessage:}" ) String systemMessage )
    {
        return new OpenAITranslationAdapter( new OpenAITranslationAdapterConfiguration( apiKey,
                                                                                        model,
                                                                                        maxTokens,
                                                                                        initTemperature,
                                                                                        temperatureIncrement,
                                                                                        n,
                                                                                        systemMessage )
        );
    }

    /*
     * Validation Adapter
     */
    @Bean
    @ConditionalOnProperty( value = "validation.adapter", havingValue = "Default" )
    public ValidationAdapter defaultValidationAdapter( @Value( "${validation.adapter.configurationFile:}" ) String validationConfigurationFileName )
    {
        return new DefaultValidationAdapter( validationConfigurationFileName );
    }

    @Bean
    @ConditionalOnProperty( value = "validation.adapter", havingValue = "StardewValley" )
    public ValidationAdapter stardewValleyValidationAdapter( @Value( "${validation.adapter.configurationFile:}" ) String validationConfigurationFileName )
    {
        return new StardewValleyValidationAdapter( validationConfigurationFileName );
    }
}
