package com.tle.i18n.translator.util;

import org.slf4j.Logger;

public class LoggerUtils
{
    public static void printDebugInfo( Logger logger, String originalText, String translatedText, String message )
    {
        logger.info( "*" );
        logger.info( "Original Text  : " + originalText );
        logger.info( "Translated Text: " + translatedText );
        logger.info( message );
    }
}
