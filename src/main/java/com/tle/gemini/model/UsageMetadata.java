package com.tle.gemini.model;

import com.google.gson.annotations.SerializedName;

public class UsageMetadata
{
    @SerializedName( "promptTokenCount" )
    private int promptTokenCount;

    @SerializedName( "candidatesTokenCount" )
    private int candidatesTokenCount;

    @SerializedName( "totalTokenCount" )
    private int totalTokenCount;

    public int getPromptTokenCount()
    {
        return promptTokenCount;
    }

    public void setPromptTokenCount( int promptTokenCount )
    {
        this.promptTokenCount = promptTokenCount;
    }

    public int getCandidatesTokenCount()
    {
        return candidatesTokenCount;
    }

    public void setCandidatesTokenCount( int candidatesTokenCount )
    {
        this.candidatesTokenCount = candidatesTokenCount;
    }

    public int getTotalTokenCount()
    {
        return totalTokenCount;
    }

    public void setTotalTokenCount( int totalTokenCount )
    {
        this.totalTokenCount = totalTokenCount;
    }
}
