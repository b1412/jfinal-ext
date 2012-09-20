package com.jfinal.plugin.tablebind;

import java.util.List;

import javax.sql.DataSource;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.util.StringKit;

public class AutoTableBindPlugin extends ActiveRecordPlugin {
	private TableNameStyle tableNameStyle;
	public AutoTableBindPlugin(DataSource dataSource) {
		super(dataSource);
	}

	public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider, TableNameStyle tableNameStyle) {
		super(dataSourceProvider);
		this.tableNameStyle = tableNameStyle;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean start() {
		try {
			List<Class> modelClasses = ClassSearcher.findClasses(Model.class);
			System.out.println("modelClasses.size "+modelClasses.size());
			TableBind tb = null;
			for (Class modelClass : modelClasses) {
				tb = (TableBind) modelClass.getAnnotation(TableBind.class);
				if (tb == null) {
					this.addMapping(tableNameStyle.tableName(modelClass.getSimpleName()), modelClass);
					System.out.println("auto bindTable: addMapping("+tableNameStyle.tableName(modelClass.getSimpleName())+", "+modelClass.getName()+")");
				} else {
					if (StringKit.notBlank(tb.pkName())) {
						this.addMapping(tb.tableName(), tb.pkName(), modelClass);
						System.out.println("auto bindTable: addMapping("+tb.tableName()+", "+tb.pkName()+","+modelClass.getName()+")");
					} else {
						this.addMapping(tb.tableName(), modelClass);
						System.out.println("auto bindTable: addMapping("+tb.tableName()+", "+modelClass.getName()+")");
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
		return super.stop();
	}
}
