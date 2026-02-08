package com.tle.anthropic.model;

public class AnthropicStopReason
{
    private String type;

    private String stopReason;

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public String getStopReason()
    {
        return stopReason;
    }

    public void setStopReason( String stopReason )
    {
        this.stopReason = stopReason;
    }
}
