/**
 * Copyright (c) 2011-2013, kidzhou 周磊 (zhouleib1412@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
