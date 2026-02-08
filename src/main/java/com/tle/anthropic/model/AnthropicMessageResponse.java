package com.tle.anthropic.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AnthropicMessageResponse
{
    private String id;

    private String type;

    private String role;

    private List<AnthropicContentBlock> content;

    private String model;

    private AnthropicStopReason stopReason;

    private AnthropicUsage usage;

    public String getId()
    {
        return id;
    }

    public String getType()
    {
        return type;
    }

    public String getRole()
    {
        return role;
    }

    public List<AnthropicContentBlock> getContent()
    {
        return content;
    }

    public String getModel()
    {
        return model;
    }

    public AnthropicStopReason getStopReason()
    {
        return stopReason;
    }

    public AnthropicUsage getUsage()
    {
        return usage;
    }

    public String getTextContent()
    {
        if ( content == null || content.isEmpty() )
        {
            return "";
        }
        return content.get( 0 ).getText();
    }
}
