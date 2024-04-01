package com.tle.deepl.model;

import com.google.gson.annotations.SerializedName;

public class Translation
{
    @SerializedName( value = "detected_source_language" )
    private String detectedSourceLanguage;

    @SerializedName( value = "text" )
    private String text;

    public String getDetectedSourceLanguage()
    {
        return detectedSourceLanguage;
    }

    public String getText()
    {
        return text;
    }
}
