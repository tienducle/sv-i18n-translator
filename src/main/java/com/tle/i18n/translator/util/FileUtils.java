package com.tle.i18n.translator.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;

@Component
@Lazy
public class FileUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger( FileUtils.class );
    private final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final TypeReference<LinkedHashMap<String, String>> typeRef = new TypeReference<>() {};
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LocaleUtils localeUtils;

    public FileUtils( LocaleUtils localeUtils )
    {
        LOGGER.info( "Initializing FileUtils" );
        objectMapper.configure( JsonReadFeature.ALLOW_TRAILING_COMMA.mappedFeature(), true );
        objectMapper.configure( JsonReadFeature.ALLOW_JAVA_COMMENTS.mappedFeature(), true );
        objectMapper.configure( JsonReadFeature.ALLOW_YAML_COMMENTS.mappedFeature(), true );
        objectMapper.configure( JsonParser.Feature.STRICT_DUPLICATE_DETECTION, true );
        this.localeUtils = localeUtils;
        LOGGER.info( "Initialized FileUtils" );
    }

    public ObjectMapper getObjectMapper()
    {
        return objectMapper;
    }

    public void flushToFile( File file, Object object )
    {
        try ( FileWriter fileWriter = new FileWriter( file );
              BufferedWriter bufferedWriter = new BufferedWriter( fileWriter ) )
        {
            String json = gson.toJson( object );
            bufferedWriter.write( json );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    public LinkedHashMap<String, String> readI18nFile( File file ) throws IOException
    {
        if ( !file.exists() )
        {
            return new LinkedHashMap<>();
        }

        try ( InputStreamReader inputStreamReader = new InputStreamReader( BOMInputStream.builder()
                                                                                         .setByteOrderMarks(
                                                                                                 ByteOrderMark.UTF_8,
                                                                                                 ByteOrderMark.UTF_16BE,
                                                                                                 ByteOrderMark.UTF_16LE,
                                                                                                 ByteOrderMark.UTF_32BE,
                                                                                                 ByteOrderMark.UTF_32LE )
                                                                                         .setFile( file ).get() ) )
        {
            return objectMapper.readValue( inputStreamReader, typeRef );
        }
        catch ( Exception e )
        {
            LOGGER.error( String.format( "Failed to read file: %s", file ), e );
        }

        return null;
    }

    /**
     * Returns the path to the translated file for the given target language
     *
     * If the input is "/path/to/default.json", "German"
     * the method will return "/path/to/de.json"
     */
    public String getTranslatedFilePath( String originalFilePath, String targetLanguage )
    {
        final String targetLanguageCode = localeUtils.getCountryCodeForLanguage( targetLanguage );
        return originalFilePath.replace( "default.json", targetLanguageCode + ".json" );
    }
}
