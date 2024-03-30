package com.tle.openai.model.completion;

import com.google.gson.annotations.SerializedName;
import com.tle.openai.model.chat.Message;

import java.util.ArrayList;
import java.util.List;

public class CompletionRequest
{
    private String model = "text-davinci-003";

    @SerializedName( value = "max_tokens")
    private int maxTokens = 2000;

    private double temperature = 1.0;

    private double top_p = 1.0;

    private int n = 1;

    private String prompt;

    public CompletionRequest()
    {
    }

    public CompletionRequest( String model, int maxTokens, int n, String prompt )
    {
        this.model = model;
        this.maxTokens = maxTokens;
        this.n = n;
        this.prompt = prompt;
    }

    public CompletionRequest( double temperature, String prompt )
    {
        this.temperature = temperature;
        this.prompt = prompt;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel( String model )
    {
        this.model = model;
    }

    public int getMaxTokens()
    {
        return maxTokens;
    }

    public void setMaxTokens( int maxTokens )
    {
        this.maxTokens = maxTokens;
    }

    public int getN()
    {
        return n;
    }

    public void setN( int n )
    {
        this.n = n;
    }

    public double getTemperature()
    {
        return temperature;
    }

    public void setTemperature( double temperature )
    {
        this.temperature = temperature;
    }

    public double getTop_p()
    {
        return top_p;
    }

    public void setTop_p( double top_p )
    {
        this.top_p = top_p;
    }

    public String getPrompt()
    {
        return prompt;
    }

    public void setPrompt( String prompt )
    {
        this.prompt = prompt;
    }
}
