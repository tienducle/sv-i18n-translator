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
import java.util.ArrayList;
import java.util.List;
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

    private final ValidationAdapter validationAdapter;

    private final FileUtils fileUtils;

    // Input file containing the original text
    private final File originalFile;

    // Output file containing the translated text
    private final File translatedFile;

    // Ignore file containing keys, that should be ignored during validation
    private final File ignoreFile;

    private List<String> ignoreKeys = new ArrayList<>();

    public RevalidateStep(
            ValidationAdapter validationAdapter,
            FileUtils fileUtils,
            @Value( "${originalFilePath}" ) String originalFilePath,
            @Value( "${targetLanguage}" ) String targetLanguage )
    {
        LOGGER.info( "Initializing RevalidateStep" );
        this.validationAdapter = validationAdapter;
        this.fileUtils = fileUtils;
        this.originalFile = new File( originalFilePath );
        this.translatedFile = new File( fileUtils.getTranslatedFilePath( originalFilePath, targetLanguage ) );
        this.ignoreFile = new File( fileUtils.getIgnoreFilePath( originalFilePath, targetLanguage ) );
        if ( ignoreFile.exists() )
        {
            // read line by line
            ignoreKeys.addAll( fileUtils.getIgnoreKeysFromFile( ignoreFile ) );
            LOGGER.info( "Ignoring keys: {}", String.join( ", ", ignoreKeys ) );
        }

        LOGGER.info( "Initialized RevalidateStep" );
    }

    @Override
    public void execute() throws IOException
    {
        LOGGER.info( "Executing RevalidateStep" );
        LOGGER.info( "Original file  : " + originalFile );
        LOGGER.info( "Translated file: " + translatedFile );

        final Map<String, String> originalFileLines = fileUtils.readI18nFile( originalFile );
        if ( originalFileLines == null )
        {
            LOGGER.error( "Error reading " + originalFile );
            return;
        }

        final Map<String, String> translatedFileLines = fileUtils.readI18nFile( translatedFile );
        if ( translatedFileLines == null )
        {
            LOGGER.error( "Error reading " + translatedFile );
            return;
        }

        int outdatedTranslations = 0;

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
            if ( validationAdapter.accept( originalText, translatedText, true ) )
            {
                continue;
            }

            // check if the key should be ignored
            if ( ignoreKeys.contains( key ) )
            {
                LOGGER.info( "Existing translation does not pass current validation, but '{}' is in ignore list.", key );
                continue;
            }

            LOGGER.info( String.format( "Existing translation does not pass current validation. Adding <ERROR> tag to '%s'", key ) );
            translatedFileLines.put( key, ValidationAdapter.TRANSLATION_ERROR_VALUE + " " + originalText + "<EXISTING_TRANSLATION>: " + translatedText );
            fileUtils.flushToFile( translatedFile, translatedFileLines );
            outdatedTranslations++;
        }

        LOGGER.info( String.format( "Number of outdated translations: %d", outdatedTranslations ) );
        fileUtils.flushToFile( translatedFile, translatedFileLines );
        LOGGER.info( "Finished RevalidateStep" );
    }
}
