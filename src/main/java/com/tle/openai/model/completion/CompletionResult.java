package com.tle.openai.model.completion;

import com.google.gson.annotations.SerializedName;
import com.tle.openai.model.common.UsageInformation;

public class CompletionResult
{
    @SerializedName( value = "id" )
    private String id;

    @SerializedName( value = "object" )
    private String object;

    @SerializedName( value = "created" )
    private int created;

    @SerializedName( value = "model" )
    private String model;

    @SerializedName( value = "usage" )
    private UsageInformation usageInformation;

    @SerializedName( value = "choices" )
    private Choice[] choices;

    public String getId()
    {
        return id;
    }

    public String getObject()
    {
        return object;
    }

    public int getCreated()
    {
        return created;
    }

    public String getModel()
    {
        return model;
    }

    public UsageInformation getUsageInformation()
    {
        return usageInformation;
    }

    public Choice[] getChoices()
    {
        return choices;
    }
}
