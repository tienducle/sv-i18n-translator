package com.tle.gemini.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class SystemInstruction
{
    @SerializedName( "parts" )
    private List<Part> parts = new ArrayList<>();

    public SystemInstruction()
    {
    }

    public SystemInstruction( String text )
    {
        this.parts.add( new Part( text ) );
    }

    public List<Part> getParts()
    {
        return parts;
    }

    public void setParts( List<Part> parts )
    {
        this.parts = parts;
    }

    public void addPart( Part part )
    {
        this.parts.add( part );
    }

    public String getText()
    {
        if ( parts == null || parts.isEmpty() )
        {
            return "";
        }
        return parts.get( 0 ).getText();
    }
}
