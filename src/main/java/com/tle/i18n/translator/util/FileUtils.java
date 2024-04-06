package com.tle.i18n.translator.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

public class FileUtils
{
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static final TypeReference<LinkedHashMap<String, String>> typeRef = new TypeReference<>() {};
    private static final ObjectMapper objectMapper = new ObjectMapper().configure( JsonReadFeature.ALLOW_TRAILING_COMMA.mappedFeature(), true )
                                                                       .configure( JsonReadFeature.ALLOW_JAVA_COMMENTS.mappedFeature(), true )
                                                                       .configure( JsonReadFeature.ALLOW_YAML_COMMENTS.mappedFeature(), true )
                                                                       .configure( JsonParser.Feature.STRICT_DUPLICATE_DETECTION, true );

    public static ObjectMapper getObjectMapper()
    {
        return objectMapper;
    }

    public static void flushToFile( File file, Object object )
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

    public static LinkedHashMap<String, String> readI18nFile( File file )
    {
        if ( !file.exists() )
        {
            return new LinkedHashMap<>();
        }

        try ( InputStream fileInputStream = new FileInputStream( file );
              InputStreamReader fileInputStreamReader = new InputStreamReader( fileInputStream, StandardCharsets.UTF_8 ) )
        {
            return objectMapper.readValue( fileInputStreamReader, typeRef );
        }
        catch ( Exception e )
        {
            return null;
        }
    }
}
