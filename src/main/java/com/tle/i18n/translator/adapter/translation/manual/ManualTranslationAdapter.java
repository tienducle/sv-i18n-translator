package com.tle.i18n.translator.adapter.translation.manual;

import com.tle.i18n.translator.adapter.translation.TranslationAdapter;
import com.tle.i18n.translator.translation.TranslationRequest;
import com.tle.i18n.translator.translation.TranslationResult;

public class ManualTranslationAdapter extends TranslationAdapter
{
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
