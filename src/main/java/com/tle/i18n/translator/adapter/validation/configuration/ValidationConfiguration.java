package com.tle.i18n.translator.adapter.validation.configuration;

import java.util.List;

public class ValidationConfiguration
{
    private List<String> rejectionList;
    private String countSpecialCharacters;
    private List<RegexValidation> regexValidationList;

    public List<String> getRejectionList()
    {
        return rejectionList;
    }

    public String getCountSpecialCharacters()
    {
        return countSpecialCharacters;
    }

    public List<RegexValidation> getRegexValidationList()
    {
        return regexValidationList;
    }
}
