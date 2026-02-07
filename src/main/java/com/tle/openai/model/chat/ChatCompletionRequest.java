package com.tle.openai.model.chat;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ChatCompletionRequest
{
    private String model = "gpt-5.1";

    private double temperature = 1.0;

    private int n = 1;

    private List<Message> messages = new ArrayList<>();

    public ChatCompletionRequest( ChatCompletionConfiguration chatCompletionConfiguration )
    {
        this( chatCompletionConfiguration.getModel(),
              chatCompletionConfiguration.getInitTemperature(),
              chatCompletionConfiguration.getN() );
    }

    public ChatCompletionRequest( String model, double temperature, int n )
    {
        this.model = model;
        this.temperature = temperature;
        this.n = n;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel( String model )
    {
        this.model = model;
    }

    public double getTemperature()
    {
        return temperature;
    }

    public void setTemperature( double temperature )
    {
        this.temperature = temperature;
    }

    public int getN()
    {
        return n;
    }

    public void setN( int n )
    {
        this.n = n;
    }

    public List<Message> getMessages()
    {
        return messages;
    }

    public void addMessage( Message message )
    {
        this.messages.add( message );
    }
}
