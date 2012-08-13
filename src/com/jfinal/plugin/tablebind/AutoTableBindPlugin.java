package com.jfinal.plugin.tablebind;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.util.StringKit;

public class AutoTableBindPlugin extends ActiveRecordPlugin {
	private TableNameStyle tableNameStyle;
	private Logger logger = LoggerFactory.getLogger(getClass());

	public AutoTableBindPlugin(DataSource dataSource) {
		super(dataSource);
	}

	public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider, TableNameStyle tableNameStyle) {
		super(dataSourceProvider);
		this.tableNameStyle = tableNameStyle;
	}

	@Override
	public boolean start() {
		try {
			List<Class> modelClasses = ClassSearcher.findClasses(Model.class);
			logger.debug("modelClasses.size  {}",modelClasses.size());
			TableBind tb = null;
			for (Class modelClass : modelClasses) {
				tb = (TableBind) modelClass.getAnnotation(TableBind.class);
				if (tb == null) {
					this.addMapping(tableName(modelClass), modelClass);
					logger.debug("auto bindTable: addMapping({}, {})", tableName(modelClass), modelClass.getName());
				} else {
					if (StringKit.notBlank(tb.pkName())) {
						this.addMapping(tb.tableName(), tb.pkName(), modelClass);
						logger.debug("auto bindTable: addMapping({}, {},{})", new Object[]{tb.tableName(),tb.pkName(), modelClass.getName()});
					} else {
						this.addMapping(tb.tableName(), modelClass);
						logger.debug("auto bindTable: addMapping({}, {})", tb.tableName(), modelClass.getName());
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return super.start();
	}

	@Override
	public boolean stop() {
		return true;
	}

	private String tableName(Class<?> clazz) {
		String tableName = clazz.getSimpleName();
		if (tableNameStyle == TableNameStyle.UP) {
			tableName = tableName.toUpperCase();
		} else if (tableNameStyle == TableNameStyle.LOWER) {
			tableName = tableName.toLowerCase();
		} else {
			tableName = StringKit.firstCharToLowerCase(tableName);
		}
		return tableName;
	}
}
