package com.tle.i18n.translator;

import com.tle.i18n.translator.adapter.validation.DefaultValidationAdapter;
import com.tle.i18n.translator.adapter.validation.ValidationAdapter;
import com.tle.i18n.translator.adapter.validation.stardewvalley.StardewValleyValidationAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.util.List;

@Configuration
@Lazy
@PropertySources( value = {
        @PropertySource( value = "classpath:local.properties", ignoreResourceNotFound = true ),
        @PropertySource( value = "classpath:application.properties" ),
} )
public class Config
{
    @Value( "${runModes}" )
    private List<RunMode> runModes;

    @Value( "${sourceLanguage:English}" )
    private String sourceLanguage;

    @Value( "${targetLanguage:German}" )
    private String targetLanguage;

    public List<RunMode> getRunModes()
    {
        return runModes;
    }

    /*
     * Validation Adapter
     */
    @Bean
    @ConditionalOnProperty( value = "validation.adapter", havingValue = "Default" )
    public ValidationAdapter defaultValidationAdapter( @Value( "${validation.adapter.configurationFile:}" ) String validationConfigurationFileName )
    {
        return new DefaultValidationAdapter( validationConfigurationFileName );
    }

    @Bean
    @ConditionalOnProperty( value = "validation.adapter", havingValue = "StardewValley" )
    public ValidationAdapter stardewValleyValidationAdapter( @Value( "${validation.adapter.configurationFile:}" ) String validationConfigurationFileName )
    {
        return new StardewValleyValidationAdapter( validationConfigurationFileName );
    }
}
