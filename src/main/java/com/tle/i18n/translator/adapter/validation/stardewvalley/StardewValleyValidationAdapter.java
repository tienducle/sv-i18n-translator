package com.tle.i18n.translator.adapter.validation.stardewvalley;

import com.tle.i18n.translator.adapter.validation.ValidationAdapter;
import com.tle.i18n.translator.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StardewValleyValidationAdapter extends ValidationAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger( StardewValleyValidationAdapter.class );

    public StardewValleyValidationAdapter( String validationConfigurationFileName )
    {
        super( validationConfigurationFileName );
    }

    @Override
    public boolean accept( String originalText, String translatedText, boolean omitAcceptMessage )
    {
        if ( !super.accept( originalText, translatedText, true ) )
        {
            return false;
        }

        // Custom validations for Stardew Valley
        if ( originalText.split( "\n", -1 ).length != translatedText.split( "\n", -1 ).length )
        {
            LoggerUtils.printDebugInfo( LOGGER, originalText, translatedText, "Rejected due to newline count mismatch" );
            return false;
        }

        LoggerUtils.printDebugInfo( LOGGER, originalText, translatedText, "Accepted" );
        return true;
    }
}
