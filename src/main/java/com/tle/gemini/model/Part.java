package com.tle.gemini.model;

import com.google.gson.annotations.SerializedName;

public class Part
{
    @SerializedName( "text" )
    private String text;

    public Part()
    {
    }

    public Part( String text )
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }

    public void setText( String text )
    {
        this.text = text;
    }
}
