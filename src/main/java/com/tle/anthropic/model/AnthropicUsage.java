package com.tle.anthropic.model;

public class AnthropicUsage
{
    private int inputTokens;

    private int outputTokens;

    public int getInputTokens()
    {
        return inputTokens;
    }

    public void setInputTokens( int inputTokens )
    {
        this.inputTokens = inputTokens;
    }

    public int getOutputTokens()
    {
        return outputTokens;
    }

    public void setOutputTokens( int outputTokens )
    {
        this.outputTokens = outputTokens;
    }
}
