package com.tle.claude.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class ClaudeMessageRequest
{
    private String model;

    @SerializedName( "max_tokens" )
    private int maxTokens = 4096;
    private double temperature = 1.0;

    @SerializedName( "messages" )
    private List<ClaudeMessage> messages = new ArrayList<>();

    private String system;

    public ClaudeMessageRequest( String model, double temperature, int maxTokens, String system )
    {
        this.model = model;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.system = system;
    }

    public void addMessage( ClaudeMessage message )
    {
        this.messages.add( message );
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

    public double getTemperature()
    {
        return temperature;
    }

    public void setTemperature( double temperature )
    {
        this.temperature = temperature;
    }

    public List<ClaudeMessage> getMessages()
    {
        return messages;
    }

    public String getSystem()
    {
        return system;
    }

    public void setSystem( String system )
    {
        this.system = system;
    }
}
