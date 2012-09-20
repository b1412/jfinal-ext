package com.jfinal.plugin.tablebind;

import com.jfinal.util.StringKit;

public enum TableNameStyle {
	DEFAULT {
		public String tableName(String className) {
			return className;
		}
	},
	FIRST_LOWER {
		public String tableName(String className) {
			return StringKit.firstCharToLowerCase(className);
		}
	},
	UP {
		public String tableName(String className) {
			return className.toUpperCase();
		}
	},
	LOWER {
		public String tableName(String className) {
			return className.toLowerCase();
		}
	},
	UP_UNDERLINE {
		public String tableName(String className) {
			// TODO
			throw new RuntimeException("not finished");
		}
	},
	LOWER_UNDERLINE {
		public String tableName(String className) {
			// TODO
			throw new RuntimeException("not finished");
		}
	};

	public abstract String tableName(String className);

}
