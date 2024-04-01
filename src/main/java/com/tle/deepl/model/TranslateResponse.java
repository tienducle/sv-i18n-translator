package com.tle.deepl.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TranslateResponse
{
    @SerializedName( value = "translations" )
    private List<Translation> translations;

    public List<Translation> getTranslations()
    {
        return translations;
    }
}
