package com.tle.i18n.translator;

public enum RunMode
{
    // Merges original file with translated file
    // If new keys have been added in original, the translated file will be updated with the new keys
    SYNC( 0, "Sync" ),

    // Revalidate translated file with original file
    // Used to check if the translated file is still valid with the current set of validations
    REVALIDATE( 1, "Revalidate" ),

    // Do translation
    TRANSLATE( 2, "Translate" ),

    // Reformat translated file and add all comments from original file
    REFORMAT( 3, "Reformat" );

    private final int id;
    private final String name;

    RunMode( int id, String name )
    {
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}
