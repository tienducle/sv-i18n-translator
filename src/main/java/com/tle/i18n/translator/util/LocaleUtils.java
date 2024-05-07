package com.tle.i18n.translator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This class provides utility methods for locale operations.
 * It provides a map of language names to ISO language codes.
 */
@Component
@Lazy
public class LocaleUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger( LocaleUtils.class );

    private final Map<String, String> reverseLocaleLookupMap = new HashMap<>();

    public LocaleUtils()
    {
        LOGGER.info( "Initializing LocaleUtils" );

        for ( String isoLanguage : Locale.getISOLanguages() )
        {
            final Locale locale = Locale.forLanguageTag( isoLanguage + "-xx" );
            final String language = locale.getDisplayLanguage( Locale.ENGLISH );
            reverseLocaleLookupMap.put( locale.getDisplayLanguage( Locale.ENGLISH ), isoLanguage );
            LOGGER.debug( "Added {} -> '{}' to lookup map", language, isoLanguage);
        }

        LOGGER.info( "Initialized LocaleUtils" );
    }

    public String getCountryCodeForLanguage( String language )
    {
        final String countryCode = reverseLocaleLookupMap.get( language );
        LOGGER.info( "Country code for '{}' is '{}'", language, countryCode );
        return countryCode;
    }
}
