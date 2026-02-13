package com.tle.gemini.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Content
{
    @SerializedName( "role" )
    private String role;

    @SerializedName( "parts" )
    private List<Part> parts = new ArrayList<>();

    public Content()
    {
    }

    public Content( String role, String text )
    {
        this.role = role;
        this.parts.add( new Part( text ) );
    }

    public String getRole()
    {
        return role;
    }

    public void setRole( String role )
    {
        this.role = role;
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
