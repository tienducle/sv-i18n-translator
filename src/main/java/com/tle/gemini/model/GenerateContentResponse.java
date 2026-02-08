package com.tle.gemini.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GenerateContentResponse
{
    @SerializedName( "candidates" )
    private List<Candidate> candidates;

    @SerializedName( "usageMetadata" )
    private UsageMetadata usageMetadata;

    public List<Candidate> getCandidates()
    {
        return candidates;
    }

    public void setCandidates( List<Candidate> candidates )
    {
        this.candidates = candidates;
    }

    public UsageMetadata getUsageMetadata()
    {
        return usageMetadata;
    }

    public void setUsageMetadata( UsageMetadata usageMetadata )
    {
        this.usageMetadata = usageMetadata;
    }

    public String getTextContent()
    {
        if ( candidates == null || candidates.isEmpty() )
        {
            return "";
        }
        return candidates.get( 0 ).getText();
    }
}
