package com.tle.i18n.translator.step;

import com.tle.i18n.translator.adapter.validation.ValidationAdapter;
import com.tle.i18n.translator.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Revalidates the translated text by comparing it with the original text
 * and adding an <ERROR> tag to the translated text if it does not pass the validation.
 */
@Component
@Lazy
public class RevalidateStep extends Step
{
    private static final Logger LOGGER = LoggerFactory.getLogger( RevalidateStep.class );

    private ValidationAdapter validationAdapter;

    @Value( "${step.revalidate.originalFilePath}" )
    private String originalFilePath;

    @Value( "${step.revalidate.translatedFilePath}" )
    private String translatedFilePath;

    // Input file containing the original text
    private final File originalFile;

    // Output file containing the translated text
    private final File translatedFile;

    public RevalidateStep(
            ValidationAdapter validationAdapter,
            @Value( "${step.translate.originalFilePath}" ) String originalFilePath,
            @Value( "${step.translate.translatedFilePath}" ) String translatedFilePath )
    {
        LOGGER.info( "Initializing RevalidateStep" );
        this.validationAdapter = validationAdapter;
        this.originalFile = new File( originalFilePath );
        this.translatedFile = new File( translatedFilePath );
        LOGGER.info( "Initialized RevalidateStep" );
    }

    @Override
    public void execute() throws IOException
    {
        LOGGER.info( "Executing RevalidateStep" );
        LOGGER.info( "Original file  : " + originalFile );
        LOGGER.info( "Translated file: " + translatedFile );

        int outdatedTranslations = 0;

        Map<String, String> originalFileLines = objectMapper.readValue( originalFile, typeRef );
        Map<String, String> translatedFileLines = translatedFile.exists() ? objectMapper.readValue( translatedFile, typeRef ) : new LinkedHashMap<>();

        for ( String key : originalFileLines.keySet() )
        {
            final String originalText = originalFileLines.get( key );
            final String translatedText = translatedFileLines.get( key );

            // check if key exists in translated file
            if ( translatedText == null )
            {
                continue;
            }

            // if the translation already contains the <ERROR> tag, skip it
            if ( validationAdapter.containsError( translatedText ) )
            {
                continue;
            }

            // revalidate with current validation rules
            if ( !validationAdapter.accept( originalText, translatedText, true ) )
            {
                LOGGER.info( String.format( "Existing translation does not pass current validation. Adding <ERROR> tag to '%s'", key ) );
                translatedFileLines.put( key, ValidationAdapter.TRANSLATION_ERROR_VALUE + " " + originalText + "<EXISTING_TRANSLATION>: " + translatedText );
                FileUtils.flushToFile( translatedFile, translatedFileLines );
                outdatedTranslations++;
            }
        }

        LOGGER.info( String.format( "Number of outdated translations: %d", outdatedTranslations ) );
        FileUtils.flushToFile( translatedFile, translatedFileLines );
        LOGGER.info( "Finished RevalidateStep" );
    }
}
