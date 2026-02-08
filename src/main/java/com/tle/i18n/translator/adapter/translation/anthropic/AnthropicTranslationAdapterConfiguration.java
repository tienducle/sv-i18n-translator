package com.tle.i18n.translator.adapter.translation.anthropic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty( value = "translation.adapter", havingValue = "Anthropic" )
public class AnthropicTranslationAdapterConfiguration
{
    private final String apiKey;
    private final String model;
    private final int maxTokens;
    private final double initTemperature;
    private final double temperatureIncrement;
    private final int n;
    private final String systemMessageText;

    public AnthropicTranslationAdapterConfiguration( @Value( "${translation.adapter.anthropic.apiKey:}" ) String apiKey,
                                                     @Value( "${translation.adapter.anthropic.model:}" ) String model,
                                                     @Value( "${translation.adapter.anthropic.maxTokens:${translation.adapter.maxTokens:2000}}" ) int maxTokens,
                                                     @Value( "${translation.adapter.anthropic.initTemperature:${translation.adapter.initTemperature:0.2}}" ) double initTemperature,
                                                     @Value( "${translation.adapter.anthropic.temperatureIncrement:${translation.adapter.temperatureIncrement:0.6}}" ) double temperatureIncrement,
                                                     @Value( "${translation.adapter.anthropic.n:1}" ) int n,
                                                     @Value( "${translation.adapter.anthropic.systemMessage:${translation.adapter.systemMessage:}}" ) String systemMessage )
    {
        this.apiKey = apiKey;
        this.model = model;
        this.maxTokens = maxTokens;
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

    public int getMaxTokens()
    {
        return maxTokens;
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
