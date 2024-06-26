package com.tle.i18n.translator.step;

import com.tle.i18n.translator.adapter.translation.TranslationAdapter;
import com.tle.i18n.translator.adapter.validation.ValidationAdapter;
import com.tle.i18n.translator.translation.TranslationRequest;
import com.tle.i18n.translator.translation.TranslationResult;
import com.tle.i18n.translator.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component
@Lazy
public class TranslateStep extends Step
{
    private static final Logger LOGGER = LoggerFactory.getLogger( TranslateStep.class );

    // Input file containing the original text
    private final File originalFile;

    // Output file containing the translated text
    private final File translatedFile;

    private final TranslationAdapter translationAdapter;
    private final ValidationAdapter validationAdapter;
    private final FileUtils fileUtils;

    public TranslateStep( TranslationAdapter translationAdapter,
                          ValidationAdapter validationAdapter,
                          FileUtils fileUtils,
                          @Value( "${originalFilePath}" ) String originalFilePath,
                          @Value( "${targetLanguage}" ) String targetLanguage )
    {
        LOGGER.info( "Initializing TranslateStep" );
        this.translationAdapter = translationAdapter;
        this.validationAdapter = validationAdapter;
        this.fileUtils = fileUtils;
        this.originalFile = new File( originalFilePath );
        this.translatedFile = new File( fileUtils.getTranslatedFilePath( originalFilePath, targetLanguage ) );
        LOGGER.info( "Initialized TranslateStep" );
    }

    @Override
    public void execute() throws IOException
    {
        LOGGER.info( "Executing TranslateStep" );
        LOGGER.info( "Input file: {}", originalFile );
        LOGGER.info( "Output file: {}", translatedFile );

        final Map<String, String> originalFileLines = fileUtils.readI18nFile( originalFile );
        if ( originalFileLines == null )
        {
            LOGGER.error( "Error reading {}", originalFile );
            return;
        }

        final Map<String, String> translatedFileLines = fileUtils.readI18nFile( translatedFile );
        if ( translatedFileLines == null )
        {
            LOGGER.error( "Error reading {}", translatedFile );
            return;
        }

        for ( final String key : originalFileLines.keySet() )
        {
            final String originalText = originalFileLines.get( key );

            // check if key exists in translated file
            if ( translatedFileLines.containsKey( key ) )
            {
                final String translatedText = translatedFileLines.get( key );

                // if the existing translation doesn't contain errors and passes current validations, skip
                if ( !validationAdapter.containsError( translatedText ) )
                {
                    LOGGER.debug( String.format( "Skipping existing translation of '%s'", key ) );
                    continue;
                }
            }

            LOGGER.info( "------------" );
            LOGGER.info( String.format( "Translating '%s'", key ) );

            final String translatedText = getTranslatedTextWithRetry( new TranslationRequest( key, originalText ) );
            if ( translatedText == null )
            {
                LOGGER.error( String.format( "Fatal error while translating '%s'", key ) );
                translatedFileLines.put( key, ValidationAdapter.TRANSLATION_ERROR_VALUE + " " + originalText );
            }
            else if ( validationAdapter.containsError( translatedText ) )
            {
                LOGGER.error( String.format( "Gave up on '%s': '%s'", key, originalText ) );
            }
            else
            {
                translationAdapter.addRequestToHistory( originalText );
                translationAdapter.addResponseToHistory( translatedText );
            }

            translatedFileLines.put( key, translatedText );
            fileUtils.flushToFile( translatedFile, translatedFileLines );
        }

        fileUtils.flushToFile( translatedFile, translatedFileLines );
        LOGGER.info( "Finished TranslateStep" );
    }

    /**
     * Translates a given text.
     */
    public String getTranslatedTextWithRetry( TranslationRequest translationRequest )
    {
        if ( translationRequest.getTotalAttempts() >= translationAdapter.getMaxAttempts() )
        {
            LOGGER.error( "Max attempts to translate text reached" );
            return ValidationAdapter.TRANSLATION_ERROR_VALUE + " " + translationRequest.getOriginalText();
        }

        final TranslationResult translationResult = this.translationAdapter.translate( translationRequest );

        // request failed
        if ( translationResult.hasError() )
        {
            // can be retried?
            if ( translationResult.isRetryable() )
            {
                return getTranslatedTextWithRetry( new TranslationRequest( translationResult ) );
            }

            LOGGER.error( String.format( "Final error: %s", translationResult.getErrorMessage() ) );
            return ValidationAdapter.TRANSLATION_ERROR_VALUE + " " + translationRequest.getOriginalText();
        }

        final String translatedText = selectTranslatedText( translationResult );

        // if no choice was good, retry with higher temperature and n
        // if maxTemperature is reached, return error value
        return ( translatedText != null )
               // translated text found
               ? translatedText
               // shortcut if text is too short
               : translationResult.getOriginalText().length() > 3 && translationResult.isRetryable()
                 ? getTranslatedTextWithRetry( new TranslationRequest( translationResult ) )
                 // if text is to short, give up early
                 : ValidationAdapter.TRANSLATION_ERROR_VALUE + " " + translationResult.getOriginalText();
    }

    /**
     * Given the original text and a {@link TranslationResult}, select a match from the choices.
     *
     * @return the accepted translated text if a match was found, otherwise null
     */
    private String selectTranslatedText( TranslationResult translationResult )
    {
        // find a match and return; otherwise return null
        return translationResult.getTranslatedTexts()
                                .stream()
                                .filter( translatedText -> validationAdapter.accept( translationResult.getOriginalText(), translatedText, false ) )
                                .findFirst()
                                .orElse( null );
    }
}
