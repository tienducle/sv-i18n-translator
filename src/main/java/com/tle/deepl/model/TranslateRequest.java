package com.tle.deepl.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TranslateRequest
{
    private final List<String> text = new ArrayList<>();

    @SerializedName( value = "target_lang" )
    private final String targetLang;

    public TranslateRequest( String targetLang, String text )
    {
        this.targetLang = targetLang;
        this.text.add( text );
    }
}
