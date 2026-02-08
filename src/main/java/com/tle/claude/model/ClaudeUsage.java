package com.tle.claude.model;

public class ClaudeUsage
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
