package com.jfinal.ext.render.csv;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * 该类是把数据转化成csv字符串做了简要的封装 List headers是显示数据每列的属性，建议使用字符 List data数据，单个元素格式可以为Array，list，map，model，record List columns
 * 表示需要显示的数据，如果data是Array与list，输入希望显示列的下标即可 如果data是map，model，record，输入希望显示列的key值即可。
 * 
 * @author
 * 
 */
public class CsvUtil {

    private static UserSettings userSettings = new UserSettings();

    private CsvUtil() {
    }

    /**
     * 将文本头与数据共同转成csv字符串
     * 
     * @param headers
     *            列属性
     * @param data
     *            数据
     * @param columns
     *            需要显示列的key值
     * @return csv字符串
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String createCSV(List headers, List data, List columns) {
        StringBuffer strOut = new StringBuffer("");
        if (null != headers && !headers.isEmpty()) { // 如果文本不为空则添加到csv字符串中
            listToCSV(strOut, headers);
        }
        if (null == data || data.isEmpty()) {
            return strOut.toString();
        }

        Iterator itr = data.iterator();
        while (itr.hasNext()) {
            Object obj = itr.next(); // 将数据添加到csv字符串
            Class cls = obj.getClass();
            if (cls != null && cls.isArray()) {
                if (obj != null) {
                    Object[] objs = (Object[]) obj;
                    if (objs != null) {
                        for (short i = 0; i < objs.length; i++) {
                            createCol(strOut, objs[i]);
                            strOut.append(",");
                        }
                        strOut = strOut.deleteCharAt(strOut.length() - 1); // 去点多余逗号
                        strOut.append("\n");
                    }
                }
            } else if (obj instanceof List) {
                List objlist = (List) obj;
                if (null == columns || columns.isEmpty()) { // 如果没有限制，默认全部显示
                    listToCSV(strOut, objlist);
                } else {
                    for (int i = 0; i < columns.size(); i++) {
                        createCol(strOut, objlist.get((Integer) columns.get(i)));
                        strOut.append(",");
                    }
                    strOut = strOut.deleteCharAt(strOut.length() - 1);
                    strOut.append("\n");
                }
            } else if (obj instanceof Map) {
                Map objmap = (Map) obj;
                if (null == columns || columns.isEmpty()) { // 如果没有限制，默认全部显示
                    Set keyset = objmap.keySet();
                    for (Object key : keyset) {
                        createCol(strOut, objmap.get(key));
                        strOut.append(",");
                    }
                    strOut = strOut.deleteCharAt(strOut.length() - 1);
                    strOut.append("\n");
                } else {
                    for (int i = 0; i < columns.size(); i++) {
                        createCol(strOut, objmap.get(columns.get(i)));
                        strOut.append(",");
                    }
                    strOut = strOut.deleteCharAt(strOut.length() - 1);
                    strOut.append("\n");
                }
            } else if (obj instanceof Model) {
                Model objmodel = (Model) obj;
                if (null == columns || columns.isEmpty()) { // 如果没有限制，默认全部显示
                    Set<Entry<String, Object>> entries = objmodel.getAttrsEntrySet();
                    for (Entry entry : entries) {
                        createCol(strOut, entry.getValue());
                        strOut.append(",");
                    }
                    strOut = strOut.deleteCharAt(strOut.length() - 1);
                    strOut.append("\n");
                } else {
                    for (int i = 0; i < columns.size(); i++) {
                        createCol(strOut, objmodel.get(columns.get(i) + ""));
                        strOut.append(",");
                    }
                    strOut = strOut.deleteCharAt(strOut.length() - 1);
                    strOut.append("\n");
                }
            } else if (obj instanceof Record) {
                Record objrecord = (Record) obj;
                Map<String, Object> map = objrecord.getColumns();
                if (null == columns || columns.isEmpty()) { // 如果没有限制，默认全部显示
                    Set<String> keys = map.keySet();
                    for (String key : keys) {
                        createCol(strOut, objrecord.get(key));
                        strOut.append(",");
                    }
                    strOut = strOut.deleteCharAt(strOut.length() - 1);
                    strOut.append("\n");
                } else {
                    for (int i = 0; i < columns.size(); i++) {
                        createCol(strOut, objrecord.get(columns.get(i) + ""));
                        strOut.append(",");
                    }
                    strOut = strOut.deleteCharAt(strOut.length() - 1);
                    strOut.append("\n");
                }
            } else {
                while (itr.hasNext()) {
                    Object objs = itr.next();
                    if (objs != null) {
                        createCol(strOut, objs);
                        strOut.append("\n");
                    }
                }
            }
            obj = null;
        }
        return strOut.toString();
    }

    /**
     * 把单纯的集合转化成csv字符串
     * 
     * @param strOut
     *            StringBuffer
     * @param list
     *            数据
     */
    public static void listToCSV(StringBuffer strOut, List<?> list) {
        if (null != list && !list.isEmpty()) { // 如果文本不为空则添加到csv字符串中
            for (short i = 0; i < list.size(); i++) {
                createCol(strOut, list.get(i));
                strOut.append(",");
            }
            strOut = strOut.deleteCharAt(strOut.length() - 1);
            strOut.append("\n");
        }
    }

    // 把单个元素转化
    public static void createCol(StringBuffer strOut, Object obj) {
        if (obj != null) {
            strOut.append("\"");
            String content = null;

            if (obj instanceof Boolean) {
                content = ((Boolean) obj).toString();
            } else if (obj instanceof Calendar) {
                content = ((Calendar) obj).toString();
            } else if (obj instanceof Timestamp) {
                content = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(((Timestamp) obj).getTime()));
            } else if (obj instanceof Date) {
                content = new SimpleDateFormat("yyyy-MM-dd HH:mm").format((Date) obj);
            } else {
                content = write(String.valueOf(obj));
            }
            strOut.append(content);
            strOut.append("\"");
        } else {
            strOut.append("\" \" ");
        }
    }

    public static String write(String content) {

        boolean textQualify = userSettings.forceQualifier;

        if (content.length() > 0) {
            content = content.trim();
        }

        if (!textQualify
                && userSettings.useTextQualifier
                && (content.indexOf(userSettings.textQualifier) > -1 || content.indexOf(userSettings.delimiter) > -1
                        || (content.indexOf(Letters.LF) > -1 || content.indexOf(Letters.CR) > -1)
                        || (content.indexOf(userSettings.recordDelimiter) > -1)
                        || (content.length() > 0 && content.charAt(0) == userSettings.comment) || (content.length() == 0))) {
            textQualify = true;
        }

        if (userSettings.useTextQualifier && !textQualify && content.length() > 0) {
            char firstLetter = content.charAt(0);

            if (firstLetter == Letters.SPACE || firstLetter == Letters.TAB) {
                textQualify = true;
            }

            if (!textQualify && content.length() > 1) {
                char lastLetter = content.charAt(content.length() - 1);

                if (lastLetter == Letters.SPACE || lastLetter == Letters.TAB) {
                    textQualify = true;
                }
            }
        }

        if (textQualify) {

            if (userSettings.escapeMode == UserSettings.ESCAPE_MODE_BACKSLASH) {
                content = replace(content, "" + Letters.BACKSLASH, "" + Letters.BACKSLASH + Letters.BACKSLASH);
                content = replace(content, "" + userSettings.textQualifier, "" + Letters.BACKSLASH
                        + userSettings.textQualifier);
            } else {
                content = replace(content, "" + userSettings.textQualifier, "" + userSettings.textQualifier
                        + userSettings.textQualifier);
            }
        } else if (userSettings.escapeMode == UserSettings.ESCAPE_MODE_BACKSLASH) {
            content = replace(content, "" + Letters.BACKSLASH, "" + Letters.BACKSLASH + Letters.BACKSLASH);
            content = replace(content, "" + userSettings.delimiter, "" + Letters.BACKSLASH + userSettings.delimiter);

            content = replace(content, "" + Letters.CR, "" + Letters.BACKSLASH + Letters.CR);
            content = replace(content, "" + Letters.LF, "" + Letters.BACKSLASH + Letters.LF);
        }
        return content;
    }

    // 特殊字符的转换 "\t" -> "\\t"
    public static String replace(String original, String pattern, String replace) {
        final int len = pattern.length();
        int found = original.indexOf(pattern);
        if (found > -1) {
            StringBuffer sb = new StringBuffer();
            int start = 0;
            while (found != -1) {
                sb.append(original.substring(start, found));
                sb.append(replace);
                start = found + len;
                found = original.indexOf(pattern, start);
            }
            sb.append(original.substring(start));
            return sb.toString();
        } else {
            return original;
        }
    }

}
