package com.tle.openai.model.completion;

import com.google.gson.annotations.SerializedName;
import com.tle.openai.model.chat.Message;

public class Choice
{
    @SerializedName( value = "text" )
    private String text;

    @SerializedName( value = "finish_reason" )
    private String finishReason;

    @SerializedName( value = "index" )
    private int index;

    public String getText()
    {
        return text;
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
