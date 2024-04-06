package com.tle.i18n.translator.step;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tle.i18n.translator.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Synchronizes the original and translated files by adding missing translation keys to the translated file.
 * If a translation key is missing in the translated file, it will be added with <ERROR><MISSING> tags.
 */
@Component( "SyncStep" )
@Lazy
public class SyncStep extends Step
{
    private static final Logger LOGGER = LoggerFactory.getLogger( SyncStep.class );
    private static final String TRANSLATION_MISSING_VALUE = "<ERROR><MISSING>";

    // File containing the original text
    private final File originalFile;

    // File containing the translated text
    private final File translatedFile;

    public SyncStep( @Value( "${step.sync.originalFilePath}" ) String originalFilePath,
                     @Value( "${step.sync.translatedFilePath}" ) String translatedFilePath )
    {
        LOGGER.info( "Initializing SyncStep" );
        this.originalFile = new File( originalFilePath );
        this.translatedFile = new File( translatedFilePath );
        LOGGER.info( "Initialized SyncStep" );
    }

    @Override
    public void execute() throws IOException
    {
        LOGGER.info( "Executing SyncStep" );
        LOGGER.info( "Original file  : " + originalFile );
        LOGGER.info( "Translated file: " + translatedFile );

        int missingTranslations = 0;

        final ObjectMapper objectMapper = FileUtils.getObjectMapper();
        final JsonNode originalFileJson = objectMapper.readTree( originalFile );
        final JsonNode translatedFileJson = translatedFile.exists()
                                            ? objectMapper.readTree( translatedFile )
                                            : objectMapper.createObjectNode();

        final Map<String, String> mergedContent = new LinkedHashMap<>();

        for ( Iterator<String> iterator = originalFileJson.fieldNames(); iterator.hasNext(); )
        {
            final String fieldName = iterator.next();

            if ( !translatedFileJson.has( fieldName ) )
            {
                LOGGER.info( String.format( "Missing translation for '%s'", fieldName ) );
                mergedContent.put( fieldName, TRANSLATION_MISSING_VALUE + originalFileJson.get( fieldName ).asText() );
                missingTranslations++;
            }
            else
            {
                mergedContent.put( fieldName, translatedFileJson.get( fieldName ).asText() );
            }
        }

        LOGGER.info( String.format( "Number of new keys: %d", missingTranslations ) );
        FileUtils.flushToFile( this.translatedFile, mergedContent );
        LOGGER.info( "Finished SyncStep" );
    }
}
