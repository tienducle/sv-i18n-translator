package com.tle.i18n.translator.adapter.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tle.i18n.translator.adapter.validation.configuration.RegexValidation;
import com.tle.i18n.translator.adapter.validation.configuration.ValidationConfiguration;
import com.tle.i18n.translator.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class ValidationAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ValidationAdapter.class );

    public static final String TRANSLATION_ERROR_VALUE = "<ERROR>";

    protected ValidationConfiguration validationConfiguration;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public ValidationAdapter( String validationConfigurationFileName )
    {
        LOGGER.info( "Reading validation configuration file: " + validationConfigurationFileName );
        try
        {
            validationConfiguration = objectMapper.readValue( getClass().getClassLoader()
                                                                        .getResourceAsStream( "validation/" + validationConfigurationFileName ), ValidationConfiguration.class );
        }
        catch ( Exception e )
        {
            LOGGER.error( "Error reading validation configuration file: " + validationConfigurationFileName, e );
        }
    }

    public boolean accept( String originalText, String translatedText, boolean omitAcceptMessage )
    {
        if ( containsError( translatedText ) )
        {
            LoggerUtils.printDebugInfo( LOGGER, originalText, translatedText, "Rejected due to " + TRANSLATION_ERROR_VALUE );
            return false;
        }

        if ( !validateSpecialCharacterCounts( validationConfiguration.getCountSpecialCharacters(),
                                              originalText, translatedText ) )
        {
            return false;
        }

        if ( !validateRegexPatterns( validationConfiguration.getRegexValidationList(),
                                     originalText, translatedText ) )
        {
            return false;
        }

        if ( !validateRejections( validationConfiguration.getRejectionList(),
                                  originalText, translatedText ) )
        {
            return false;
        }

        if ( !omitAcceptMessage )
        {
            LoggerUtils.printDebugInfo( LOGGER, originalText, translatedText, "Accepted" );
        }

        return true;
    }

    /**
     * Checks if the translated text contains the <ERROR> tag
     */
    public boolean containsError( String text )
    {
        return text.toUpperCase().contains( TRANSLATION_ERROR_VALUE );
    }

    /**
     * Given the original text and the translated text, check if the translated text
     * @param originalText
     * @param translatedText
     * @return
     */
    protected boolean validateRejections( List<String> rejectionList, String originalText, String translatedText )
    {
        if ( rejectionList == null || rejectionList.isEmpty() )
        {
            return true;
        }

        for ( String rejectText : rejectionList )
        {
            if ( translatedText.toLowerCase().contains( rejectText.toLowerCase() ) )
            {
                LoggerUtils.printDebugInfo( LOGGER, originalText, translatedText, String.format( "Rejected because '%s' was found.", rejectText ) );
                return false;
            }
        }

        return true;
    }

    /**
     * Given the original text and the translated text, check if defined special characters
     * are occurring the same number of times in both texts.
     *
     * @return true if the special characters occur the same number of times in both texts, false otherwise
     */
    protected boolean validateSpecialCharacterCounts( String specialCharacters, String originalText, String translatedText )
    {
        if ( specialCharacters == null )
        {
            return true;
        }

        final Map<String, Integer> originalTextSpecialCharacters = countSpecialCharacters( specialCharacters, originalText );
        final Map<String, Integer> translatedTextSpecialCharacters = countSpecialCharacters( specialCharacters, translatedText );

        if ( originalTextSpecialCharacters.size() != translatedTextSpecialCharacters.size()
             || !originalTextSpecialCharacters.equals( translatedTextSpecialCharacters ) )
        {
            LoggerUtils.printDebugInfo( LOGGER,
                                        originalText,
                                        translatedText,
                                        String.format( "Rejected due to special character count mismatch: '%s' vs. '%s'",
                                                       originalTextSpecialCharacters,
                                                       translatedTextSpecialCharacters ) );
            return false;
        }

        return true;
    }

    protected boolean validateRegexPatterns( List<RegexValidation> regexValidationList,
                                             String originalText, String translatedText )
    {
        if ( regexValidationList == null || regexValidationList.isEmpty() )
        {
            // pass if no patterns defined for validation
            return true;
        }

        for ( RegexValidation regexValidation : validationConfiguration.getRegexValidationList() )
        {
            // all patterns must pass validation
            if ( !validateRegex( regexValidation, originalText, translatedText ) )
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Given a string containing special characters and a text,
     * count the occurrences of each special character in the text.
     *
     * @return a map containing the special characters and their counts
     */
    protected Map<String, Integer> countSpecialCharacters( String specialCharacters, String text )
    {
        final Map<String, Integer> result = new HashMap<>();

        for ( char c : text.toCharArray() )
        {
            String character = String.valueOf( c );
            if ( specialCharacters.contains( character ) )
            {
                if ( !result.containsKey( character ) )
                {
                    result.put( character, 0 );
                }
                result.put( character, result.get( character ) + 1 );
            }
        }

        return result;
    }

    /**
     * Validate the given text against the given regex validation pattern.
     *
     * @return true if the text passes the validation, false otherwise
     */
    private boolean validateRegex( RegexValidation regexValidation, String originalText, String translatedText )
    {
        final Pattern pattern = regexValidation.getPattern();
        final String patternName = regexValidation.getId();
        final List<MatchResult> originalTextResult = pattern.matcher( originalText ).results().collect( Collectors.toList() );
        final List<MatchResult> translatedTextResult = pattern.matcher( translatedText ).results().collect( Collectors.toList() );

        if ( originalTextResult.size() != translatedTextResult.size() )
        {
            LoggerUtils.printDebugInfo( LOGGER, originalText, translatedText, String.format( "Rejected due to '%s' match count mismatch", patternName ) );
            return false;
        }

        // compare if results are the same in both lists
        for ( int i = 0; i < originalTextResult.size(); i++ )
        {
            if ( !originalTextResult.get( i ).group().equals( translatedTextResult.get( i ).group() ) )
            {
                LoggerUtils.printDebugInfo( LOGGER, originalText, translatedText, String.format( "Rejected due to '%s' match group mismatch", patternName ) );
                return false;
            }
        }

        return true;
    }

}
