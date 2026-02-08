package com.tle.claude.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ClaudeMessageResponse
{
    private String id;

    private String type;

    private String role;

    private List<ClaudeContentBlock> content;

    private String model;

    private ClaudeStopReason stopReason;

    private ClaudeUsage usage;

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

    public List<ClaudeContentBlock> getContent()
    {
        return content;
    }

    public String getModel()
    {
        return model;
    }

    public ClaudeStopReason getStopReason()
    {
        return stopReason;
    }

    public ClaudeUsage getUsage()
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
