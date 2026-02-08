package com.tle.i18n.translator.adapter.translation.claude;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty( value = "translation.adapter", havingValue = "Claude" )
public class ClaudeTranslationAdapterConfiguration
{
    private final String apiKey;
    private final String model;
    private final int maxTokens;
    private final double initTemperature;
    private final double temperatureIncrement;
    private final int n;
    private final String systemMessageText;

    public ClaudeTranslationAdapterConfiguration( @Value( "${translation.adapter.claude.apiKey:}" ) String apiKey,
                                                  @Value( "${translation.adapter.claude.chat.model:}" ) String model,
                                                  @Value( "${translation.adapter.claude.chat.maxTokens:2000}" ) int maxTokens,
                                                  @Value( "${translation.adapter.claude.chat.initTemperature:0.2}" ) double initTemperature,
                                                  @Value( "${translation.adapter.claude.chat.temperatureIncrement:0.6}" ) double temperatureIncrement,
                                                  @Value( "${translation.adapter.claude.chat.n:1}" ) int n,
                                                  @Value( "${translation.adapter.claude.chat.systemMessage:}" ) String systemMessage )
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
