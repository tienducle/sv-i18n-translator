package com.tle.i18n.translator.adapter.translation.gemini;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty( value = "translation.adapter", havingValue = "Gemini" )
public class GeminiTranslationAdapterConfiguration
{
    private final String apiKey;
    private final String model;
    private final int maxOutputTokens;
    private final double initTemperature;
    private final double temperatureIncrement;
    private final int n;
    private final String systemMessageText;

    public GeminiTranslationAdapterConfiguration( @Value( "${translation.adapter.gemini.apiKey:}" ) String apiKey,
                                                   @Value( "${translation.adapter.gemini.model:gemini-3-pro-preview}" ) String model,
                                                   @Value( "${translation.adapter.gemini.maxOutputTokens:2000}" ) int maxOutputTokens,
                                                   @Value( "${translation.adapter.gemini.initTemperature:0.2}" ) double initTemperature,
                                                   @Value( "${translation.adapter.gemini.temperatureIncrement:0.6}" ) double temperatureIncrement,
                                                   @Value( "${translation.adapter.gemini.n:1}" ) int n,
                                                   @Value( "${translation.adapter.gemini.systemMessage:}" ) String systemMessage )
    {
        this.apiKey = apiKey;
        this.model = model;
        this.maxOutputTokens = maxOutputTokens;
        this.initTemperature = initTemperature;
        this.temperatureIncrement = temperatureIncrement;
        this.n = n;
        this.systemMessageText = systemMessage;
    }

    public String getApiKey()
    {
        return apiKey;
    }

    public String getModel()
    {
        return model;
    }

    public int getMaxOutputTokens()
    {
        return maxOutputTokens;
    }

    public double getInitTemperature()
    {
        return initTemperature;
    }

    public double getTemperatureIncrement()
    {
        return temperatureIncrement;
    }

    public int getN()
    {
        return n;
    }

    public String getSystemMessageText()
    {
        return systemMessageText;
    }
}
