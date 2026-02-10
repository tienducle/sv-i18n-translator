package com.tle.i18n.translator.adapter.translation.moonshot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty( value = "translation.adapter", havingValue = "Moonshot" )
public class MoonshotTranslationAdapterConfiguration
{
    private final String apiKey;

    private final String model;
    private final double initTemperature;
    private final double temperatureIncrement;
    private final int n;

    private final String systemMessageText;

    public MoonshotTranslationAdapterConfiguration(
            @Value( "${translation.adapter.moonshot.apiKey:}" ) String apiKey,
            @Value( "${translation.adapter.moonshot.chat.model:kimi-k2-turbo-preview}" ) String model,
            @Value( "${translation.adapter.moonshot.chat.initTemperature:0.3}" ) double initTemperature,
            @Value( "${translation.adapter.moonshot.chat.temperatureIncrement:0.2}" ) double temperatureIncrement,
            @Value( "${translation.adapter.moonshot.chat.n:1}" ) int n,
            @Value( "${translation.adapter.moonshot.chat.systemMessage:}" ) String systemMessage )
    {
        this.apiKey = apiKey;
        this.model = model;
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
