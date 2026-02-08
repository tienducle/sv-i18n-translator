package com.tle.i18n.translator.adapter.translation.zai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty( value = "translation.adapter", havingValue = "Zai" )
public class ZaiTranslationAdapterConfiguration
{
    private final String apiKey;
    private final String model;
    private final int maxTokens;
    private final double initTemperature;
    private final double temperatureIncrement;
    private final int n;
    private final String systemMessageText;
    private final boolean useCodingPlan;

    public ZaiTranslationAdapterConfiguration( @Value( "${translation.adapter.zai.apiKey:}" ) String apiKey,
                                                @Value( "${translation.adapter.zai.chat.model:}" ) String model,
                                                @Value( "${translation.adapter.zai.chat.maxTokens:2000}" ) int maxTokens,
                                                @Value( "${translation.adapter.zai.chat.initTemperature:0.2}" ) double initTemperature,
                                                @Value( "${translation.adapter.zai.chat.temperatureIncrement:0.6}" ) double temperatureIncrement,
                                                @Value( "${translation.adapter.zai.chat.n:1}" ) int n,
                                                @Value( "${translation.adapter.zai.chat.systemMessage:}" ) String systemMessage,
                                                @Value( "${translation.adapter.zai.useCodingPlan:true}" ) boolean useCodingPlan )
    {
        this.apiKey = apiKey;
        this.model = model;
        this.maxTokens = maxTokens;
        this.initTemperature = initTemperature;
        this.temperatureIncrement = temperatureIncrement;
        this.n = n;
        this.systemMessageText = systemMessage;
        this.useCodingPlan = useCodingPlan;
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

    public boolean isUseCodingPlan()
    {
        return useCodingPlan;
    }
}
