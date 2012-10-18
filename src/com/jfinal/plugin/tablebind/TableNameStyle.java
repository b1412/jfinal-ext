package com.jfinal.plugin.tablebind;

import com.jfinal.util.StringKit;

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
			// TODO
			throw new RuntimeException("not finished");
		}
	},
	LOWER_UNDERLINE {
		@Override
		public String tableName(String className) {
			// TODO
			throw new RuntimeException("not finished");
		}
	};

	public abstract String tableName(String className);

}
