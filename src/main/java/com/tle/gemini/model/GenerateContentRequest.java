package com.tle.gemini.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class GenerateContentRequest
{
    @SerializedName( "contents" )
    private List<Content> contents = new ArrayList<>();

    @SerializedName( "generationConfig" )
    private GenerationConfig generationConfig;

    @SerializedName( "systemInstruction" )
    private SystemInstruction systemInstruction;

    public GenerateContentRequest()
    {
    }

    public GenerateContentRequest( String model, double temperature, int maxOutputTokens, String systemMessage )
    {
        this.generationConfig = new GenerationConfig( temperature, maxOutputTokens );
        this.systemInstruction = new SystemInstruction( systemMessage );
    }

    public List<Content> getContents()
    {
        return contents;
    }

    public void setContents( List<Content> contents )
    {
        this.contents = contents;
    }

    public void addContent( Content content )
    {
        this.contents.add( content );
    }

    public GenerationConfig getGenerationConfig()
    {
        return generationConfig;
    }

    public void setGenerationConfig( GenerationConfig generationConfig )
    {
        this.generationConfig = generationConfig;
    }

    public SystemInstruction getSystemInstruction()
    {
        return systemInstruction;
    }

    public void setSystemInstruction( SystemInstruction systemInstruction )
    {
        this.systemInstruction = systemInstruction;
    }
}
