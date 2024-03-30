package com.tle.i18n.translator.adapter.translation.openai;

public class OpenAITranslationAdapterConfiguration
{
    private final String apiKey;

    private final String model;
    private final int maxTokens;
    private final double initTemperature;
    private final double temperatureIncrement;
    private final int n;

    private final String systemMessageText;

    public OpenAITranslationAdapterConfiguration( String apiKey,
                                                  String model,
                                                  int maxTokens,
                                                  double initTemperature,
                                                  double temperatureIncrement,
                                                  int n,
                                                  String systemMessageText )
    {
        this.apiKey = apiKey;
        this.model = model;
        this.maxTokens = maxTokens;
        this.initTemperature = initTemperature;
        this.temperatureIncrement = temperatureIncrement;
        this.n = n;
        this.systemMessageText = systemMessageText;
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
