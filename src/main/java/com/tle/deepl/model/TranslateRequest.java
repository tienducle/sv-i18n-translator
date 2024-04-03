package com.tle.deepl.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TranslateRequest
{
    private final List<String> text = new ArrayList<>();

    @SerializedName( value = "source_lang" )
    private final String sourceLang;

    @SerializedName( value = "target_lang" )
    private final String targetLang;

    @SerializedName( value = "formality" )
    private final String formality;

    @SerializedName( value = "glossary_id" )
    private final String glossaryId;

    public TranslateRequest( String sourceLang, String targetLang, String text, String formality, String glossaryId )
    {
        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
        this.text.add( text );
        this.formality = formality;
        this.glossaryId = glossaryId;
    }
}
