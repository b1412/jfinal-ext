package com.jfinal.render.csv;

public class UserSettings {
	public static final int ESCAPE_MODE_DOUBLED = 1;
	
	public static final int ESCAPE_MODE_BACKSLASH = 2;
	
	public char TextQualifier;

	public boolean UseTextQualifier;

	public char Delimiter;

	public char RecordDelimiter;

	public char Comment;

	public int EscapeMode;

	public boolean ForceQualifier;

	public UserSettings() {
		TextQualifier = Letters.QUOTE;
		UseTextQualifier = true;
		Delimiter = Letters.COMMA;
		RecordDelimiter = Letters.NULL;
		Comment = Letters.POUND;
		EscapeMode = ESCAPE_MODE_DOUBLED;
		ForceQualifier = false;
	}
}