package com.tle.i18n.translator.adapter.validation;

import org.springframework.beans.factory.annotation.Value;

public class DefaultValidationAdapter extends ValidationAdapter
{
    public DefaultValidationAdapter( @Value( "${validation.adapter.configurationFile}" ) String validationConfigurationFileName )
    {
        super( validationConfigurationFileName );
    }
}
