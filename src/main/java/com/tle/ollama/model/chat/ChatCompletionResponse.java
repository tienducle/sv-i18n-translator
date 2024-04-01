package com.tle.ollama.model.chat;

import com.google.gson.annotations.SerializedName;

public class ChatCompletionResponse
{
    @SerializedName( value = "model" )
    private String model;

    @SerializedName( value = "created_at" )
    private String createdAt;

    @SerializedName( value = "message" )
    private Message message;

    @SerializedName( value = "done" )
    private boolean done;

    @SerializedName( value = "total_duration" )
    private long totalDuration;

    @SerializedName( value = "load_duration" )
    private long loadDuration;

    @SerializedName( value = "prompt_eval_count" )
    private int promptEvalCount;

    @SerializedName( value = "prompt_eval_duration" )
    private long promptEvalDuration;

    @SerializedName( value = "eval_count" )
    private int evalCount;

    @SerializedName( value = "eval_duration" )
    private long evalDuration;

    public String getModel()
    {
        return model;
    }

    public String getCreatedAt()
    {
        return createdAt;
    }

    public Message getMessage()
    {
        return message;
    }

    public boolean isDone()
    {
        return done;
    }

    public long getTotalDuration()
    {
        return totalDuration;
    }

    public long getLoadDuration()
    {
        return loadDuration;
    }

    public int getPromptEvalCount()
    {
        return promptEvalCount;
    }

    public long getPromptEvalDuration()
    {
        return promptEvalDuration;
    }

    public int getEvalCount()
    {
        return evalCount;
    }

    public long getEvalDuration()
    {
        return evalDuration;
    }
}
