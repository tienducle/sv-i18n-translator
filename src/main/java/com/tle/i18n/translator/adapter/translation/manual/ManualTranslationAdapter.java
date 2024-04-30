package com.tle.i18n.translator.adapter.translation.manual;

import com.tle.i18n.translator.adapter.translation.TranslationAdapter;
import com.tle.i18n.translator.translation.TranslationRequest;
import com.tle.i18n.translator.translation.TranslationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty( value = "translation.adapter", havingValue = "Manual" )
@Lazy
public class ManualTranslationAdapter extends TranslationAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ManualTranslationAdapter.class );

    public ManualTranslationAdapter()
    {
        LOGGER.info( "Initialized ManualTranslationAdapter" );
    }

    @Override
    public TranslationResult translate( TranslationRequest translation )
    {
        System.out.println( "Please provide translation for the following text:" );
        System.out.println( translation.getOriginalText() );

        final TranslationResult translationResult = new TranslationResult( translation );
        translationResult.addTranslatedText( System.console().readLine() );

        return translationResult;
    }
}
