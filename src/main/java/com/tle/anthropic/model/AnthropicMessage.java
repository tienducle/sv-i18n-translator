package com.tle.anthropic.model;

import com.google.gson.annotations.SerializedName;

public class AnthropicMessage
{
    private String role;

    private String content;

    public AnthropicMessage()
    {
    }

    public AnthropicMessage( String role, String content )
    {
        this.role = role;
        this.content = content;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole( String role )
    {
        this.role = role;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent( String content )
    {
        this.content = content;
    }
}
