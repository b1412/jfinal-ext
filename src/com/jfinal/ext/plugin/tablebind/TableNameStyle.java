package com.jfinal.ext.plugin.tablebind;

import com.jfinal.kit.StringKit;

/**
 * 1.2之后使用NameStyles代替
 * @see com.jfinal.ext.plugin.tablebind.SimpleNameStyles
 * @author kid
 * 
 */
@Deprecated
public enum TableNameStyle {
	DEFAULT {
		@Override
		public String tableName(String className) {
			return className;
		}
	},
	FIRST_LOWER {
		@Override
		public String tableName(String className) {
			return StringKit.firstCharToLowerCase(className);
		}
	},
	UP {
		@Override
		public String tableName(String className) {
			return className.toUpperCase();
		}
	},
	LOWER {
		@Override
		public String tableName(String className) {
			return className.toLowerCase();
		}
	},
	UP_UNDERLINE {
		@Override
		public String tableName(String className) {
			String tableName="";
			for (int i = 0; i < className.length(); i++) {
				char ch = className.charAt(i);
				if(Character.isUpperCase(ch)){
					tableName+=("_"+ch);
				}else{
					tableName+=Character.toUpperCase(ch);
				}
			}
			return tableName;
		}
	},
	LOWER_UNDERLINE {
		@Override
		public String tableName(String className) {
			String tableName="";
			for (int i = 0; i < className.length(); i++) {
				char ch = className.charAt(i);
				if(Character.isUpperCase(ch)){
					tableName+=("_"+Character.toLowerCase(ch));
				}else{
					tableName+=ch;
				}
			}
			return tableName;
		}
	};

	public abstract String tableName(String className);
	
}
