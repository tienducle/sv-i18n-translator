package com.tle.openai.model.common;

import com.google.gson.annotations.SerializedName;

public class UsageInformation
{
    @SerializedName( value = "prompt_tokens" )
    private int promptTokens;

    @SerializedName( value = "completion_tokens" )
    private int completionTokens;

    @SerializedName( value = "total_tokens" )
    private int totalTokens;

    public int getPromptTokens()
    {
        return promptTokens;
    }

    public int getCompletionTokens()
    {
        return completionTokens;
    }

    public int getTotalTokens()
    {
        return totalTokens;
    }
}
