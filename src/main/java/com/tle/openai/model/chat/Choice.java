package com.tle.openai.model.chat;

import com.google.gson.annotations.SerializedName;

public class Choice
{
    @SerializedName( value = "message" )
    private Message message;

    @SerializedName( value = "finish_reason" )
    private String finishReason;

    @SerializedName( value = "index" )
    private int index;

    public Message getMessage()
    {
        return message;
    }

    public String getFinishReason()
    {
        return finishReason;
    }

    public int getIndex()
    {
        return index;
    }
}
