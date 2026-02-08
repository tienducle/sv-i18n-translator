package com.tle.gemini.model;

import com.google.gson.annotations.SerializedName;

public class Candidate
{
    @SerializedName( "content" )
    private Content content;

    @SerializedName( "finishReason" )
    private String finishReason;

    @SerializedName( "index" )
    private int index;

    public Content getContent()
    {
        return content;
    }

    public void setContent( Content content )
    {
        this.content = content;
    }

    public String getFinishReason()
    {
        return finishReason;
    }

    public void setFinishReason( String finishReason )
    {
        this.finishReason = finishReason;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex( int index )
    {
        this.index = index;
    }

    public String getText()
    {
        if ( content == null )
        {
            return "";
        }
        return content.getText();
    }
}
