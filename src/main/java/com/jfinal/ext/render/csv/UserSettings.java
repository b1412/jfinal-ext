package com.jfinal.ext.render.csv;

public class UserSettings {
    public static final int ESCAPE_MODE_DOUBLED = 1;

    public static final int ESCAPE_MODE_BACKSLASH = 2;

    public char textQualifier;

    public boolean useTextQualifier;

    public char delimiter;

    public char recordDelimiter;

    public char comment;

    public int escapeMode;

    public boolean forceQualifier;

    public UserSettings() {
        textQualifier = Letters.QUOTE;
        useTextQualifier = true;
        delimiter = Letters.COMMA;
        recordDelimiter = Letters.NULL;
        comment = Letters.POUND;
        escapeMode = ESCAPE_MODE_DOUBLED;
        forceQualifier = false;
    }
}
