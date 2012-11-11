package com.jfinal.render.csv;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CsvUtil {

	private static UserSettings userSettings = new UserSettings();

	private CsvUtil() {
	}

	public static String createCSV(List<Object[]> list) {
		if (list.isEmpty())
			return null;

		StringBuffer strOut = new StringBuffer("");
		Iterator itr = list.iterator();

		Object obj = list.get(0);
		Class cls = obj.getClass();
		if (cls.isArray()) {
			while (itr.hasNext()) {
				Object[] objs = (Object[]) itr.next();
				if (objs != null) {
					int col_num = objs.length;
					for (short i = 0; i < objs.length; i++) {
						createCol(strOut, objs[i]);
						if (i < col_num - 1) {
							strOut.append(",");
						}
					}
					strOut.append("\n");
				}
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

		return strOut.toString();
	}

	/**
	 * 创建CSV文件列值
	 * 
	 * @param tmpRow
	 *            行
	 * @param obj
	 *            需要输入的单元格值
	 * @param i
	 *            单元格的顺序值
	 */
	public static void createCol(StringBuffer strOut, Object obj) {
		if (obj != null) {
			strOut.append("\"");
			String content = null;

			if (obj instanceof Boolean) {
				content = ((Boolean) obj).toString();
			} else if (obj instanceof Calendar) {
				content = ((Calendar) obj).toString();
			} else if (obj instanceof Timestamp) {
				content = new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.format(new Date(((Timestamp) obj).getTime()));
			} else if (obj instanceof Date) {
				content = new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.format((Date) obj);
			} else {
				try {
					content = write(String.valueOf(obj));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			strOut.append(content);

			strOut.append("\"");
		} else {
			strOut.append("\" \" ");
		}
	}

	public static String write(String content) throws IOException {

		boolean textQualify = userSettings.ForceQualifier;

		if (content.length() > 0) {
			content = content.trim();
		}

		if (!textQualify
				&& userSettings.UseTextQualifier
				&& (content.indexOf(userSettings.TextQualifier) > -1
						|| content.indexOf(userSettings.Delimiter) > -1
						|| (content.indexOf(Letters.LF) > -1 || content
								.indexOf(Letters.CR) > -1)
						|| (content.indexOf(userSettings.RecordDelimiter) > -1)
						|| (content.length() > 0 && content.charAt(0) == userSettings.Comment) || (content
						.length() == 0))) {
			textQualify = true;
		}

		if (userSettings.UseTextQualifier && !textQualify
				&& content.length() > 0) {
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

			if (userSettings.EscapeMode == UserSettings.ESCAPE_MODE_BACKSLASH) {
				content = replace(content, "" + Letters.BACKSLASH, ""
						+ Letters.BACKSLASH + Letters.BACKSLASH);
				content = replace(content, "" + userSettings.TextQualifier, ""
						+ Letters.BACKSLASH + userSettings.TextQualifier);
			} else {
				content = replace(content, "" + userSettings.TextQualifier, ""
						+ userSettings.TextQualifier
						+ userSettings.TextQualifier);
			}
		} else if (userSettings.EscapeMode == UserSettings.ESCAPE_MODE_BACKSLASH) {
			content = replace(content, "" + Letters.BACKSLASH, ""
					+ Letters.BACKSLASH + Letters.BACKSLASH);
			content = replace(content, "" + userSettings.Delimiter, ""
					+ Letters.BACKSLASH + userSettings.Delimiter);

			content = replace(content, "" + Letters.CR, "" + Letters.BACKSLASH
					+ Letters.CR);
			content = replace(content, "" + Letters.LF, "" + Letters.BACKSLASH
					+ Letters.LF);

		}

		return content;

	}

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
