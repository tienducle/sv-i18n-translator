package com.tle.i18n.translator.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils
{
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static void flushToFile( File file, Object object )
    {
        try ( FileWriter fileWriter = new FileWriter( file );
              BufferedWriter bufferedWriter = new BufferedWriter( fileWriter ))
        {
            String json = gson.toJson( object );
            bufferedWriter.write( json );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }
}
