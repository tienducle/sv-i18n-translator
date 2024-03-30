package com.tle.i18n.translator.adapter.validation.configuration;

import java.util.regex.Pattern;

public class RegexValidation
{
    private String id;
    private Pattern pattern;
    private String description;

    public String getId()
    {
        return id;
    }

    public Pattern getPattern()
    {
        return pattern;
    }

    public String getDescription()
    {
        return description;
    }
}
