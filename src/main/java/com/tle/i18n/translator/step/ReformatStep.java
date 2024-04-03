package com.tle.i18n.translator.step;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

/**
 * Re-formats the translated file to match the original file in terms of comments and line breaks.
 */
@Component
@Lazy
public class ReformatStep extends Step
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ReformatStep.class );

    // File containing the original text
    private final File originalFile;

    // File containing the translated text
    private final File translatedFile;

    public ReformatStep( @Value( "${step.reformat.originalFilePath}" ) String originalFilePath,
                         @Value( "${step.reformat.translatedFilePath}" ) String translatedFilePath )
    {
        LOGGER.info( "Initializing ReformatStep" );
        this.originalFile = new File( originalFilePath );
        this.translatedFile = new File( translatedFilePath );
        LOGGER.info( "Initialized ReformatStep" );
    }

    @Override
    public void execute()
    {
        LOGGER.info( "Executing ReformatStep" );
        final String lineSeparator = System.getProperty( "line.separator" );

        StringBuilder sb = new StringBuilder();

        int lineCount = 0;

        try (
                FileReader originalFileReader = new FileReader( originalFile );
                BufferedReader originalBufferedReader = new BufferedReader( originalFileReader );
                FileReader translatedFileReader = new FileReader( translatedFile );
                BufferedReader translatedBufferedReader = new BufferedReader( translatedFileReader )
        )
        {
            String originalLine = originalBufferedReader.readLine();
            String translatedLine = translatedBufferedReader.readLine();

            boolean insideBlockComment = false;

            while ( originalLine != null )
            {
                lineCount++;
                if ( translatedLine == null )
                {
                    LOGGER.debug( "Translated line is null, while original line is:" );
                    LOGGER.debug( translatedLine );
                    LOGGER.debug( "------------" );
                    sb.append( originalLine );
                    sb.append( lineSeparator );
                    originalLine = originalBufferedReader.readLine();
                    continue;
                }

                if ( insideBlockComment )
                {
                    if ( originalLine.trim().endsWith( "*/" ) )
                    {
                        LOGGER.debug( "End of block comment:" );
                        LOGGER.debug( originalLine );
                        insideBlockComment = false;
                    }
                    sb.append( originalLine );
                    sb.append( lineSeparator );
                    originalLine = originalBufferedReader.readLine();
                    continue;
                }

                if ( originalLine.trim().equals( translatedLine.trim() ) )
                {
                    LOGGER.debug( "Translated line matches original line:" );
                    LOGGER.debug( "Original line: " + originalLine );
                    LOGGER.debug( "Translated line: " + translatedLine );
                    LOGGER.debug( "Moving both cursors" );
                    LOGGER.debug( "------------" );

                    sb.append( translatedLine );
                    sb.append( lineSeparator );
                    originalLine = originalBufferedReader.readLine();
                    translatedLine = translatedBufferedReader.readLine();
                    continue;
                }

                if ( originalLine.trim().startsWith( "//" ) )
                {
                    LOGGER.debug( "Original line is a comment:" );
                    LOGGER.debug( originalLine );
                    LOGGER.debug( "Keep translated line cursor" );
                    LOGGER.debug( "------------" );
                    sb.append( originalLine );
                    sb.append( lineSeparator );
                    originalLine = originalBufferedReader.readLine();
                    continue;
                }

                if ( originalLine.trim().isEmpty() )
                {
                    LOGGER.debug( "Original line is empty" );
                    LOGGER.debug( "Keep translated line cursor" );
                    LOGGER.debug( "------------" );
                    sb.append( lineSeparator );
                    originalLine = originalBufferedReader.readLine();
                    continue;
                }

                if ( originalLine.trim().startsWith( "/*" ) )
                {
                    LOGGER.debug( "Starting block comment" );
                    LOGGER.debug( "Keep translated line cursor" );
                    LOGGER.debug( "------------" );
                    insideBlockComment = true;
                    sb.append( originalLine );
                    sb.append( lineSeparator );
                    originalLine = originalBufferedReader.readLine();
                    continue;
                }

                LOGGER.debug( "Appending translated line:" );
                LOGGER.debug( "Original line: " + originalLine );
                LOGGER.debug( "Translated line: " + translatedLine );
                LOGGER.debug( "Moving both cursors" );
                LOGGER.debug( "------------" );
                sb.append( translatedLine );
                sb.append( lineSeparator );
                originalLine = originalBufferedReader.readLine();
                translatedLine = translatedBufferedReader.readLine();
            }
        }
        catch ( Exception e )
        {
            LOGGER.error( "An error occurred during ReformatStep: ", e );
        }

        try ( BufferedWriter bufferedWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( translatedFile ) ) ) )
        {
            bufferedWriter.write( String.valueOf( sb ) );
            bufferedWriter.flush();
        }
        catch ( Exception e )
        {
            LOGGER.error( "An error occurred during ReformatStep: ", e );
        }

        LOGGER.info( "Finished ReformatStep" );
    }
}
