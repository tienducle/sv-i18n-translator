package com.tle.i18n.translator.adapter.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultValidationAdapter extends ValidationAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger( DefaultValidationAdapter.class );

    public DefaultValidationAdapter( String validationConfigurationFileName )
    {
        super( validationConfigurationFileName );
        LOGGER.info( "Initialized DefaultValidationAdapter" );
    }
}
