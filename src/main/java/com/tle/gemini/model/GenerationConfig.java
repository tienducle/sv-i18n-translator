package com.tle.gemini.model;

import com.google.gson.annotations.SerializedName;

public class GenerationConfig
{
    @SerializedName( "temperature" )
    private double temperature;

    @SerializedName( "maxOutputTokens" )
    private int maxOutputTokens;

    public GenerationConfig()
    {
    }

    public GenerationConfig( double temperature, int maxOutputTokens )
    {
        this.temperature = temperature;
        this.maxOutputTokens = maxOutputTokens;
    }

    public double getTemperature()
    {
        return temperature;
    }

    public void setTemperature( double temperature )
    {
        this.temperature = temperature;
    }

    public int getMaxOutputTokens()
    {
        return maxOutputTokens;
    }

    public void setMaxOutputTokens( int maxOutputTokens )
    {
        this.maxOutputTokens = maxOutputTokens;
    }
}
