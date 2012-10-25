package com.jfinal.plugin.tablebind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.util.StringKit;

public class AutoTableBindPlugin extends ActiveRecordPlugin {
	
	protected final Logger logger = Logger.getLogger(getClass());
	
	private final TableNameStyle tableNameStyle;
	
	private final List<String> includeJars = new ArrayList<String>();
	
	boolean includeAllJarsInLib ;
	
	public boolean setIncludeAllJarsInLib() {
		return includeAllJarsInLib;
	}

	public void addJar(String jarName){
		if(StringKit.isBlank(jarName)){
			return;
		}
		includeJars.add(jarName);
	}
	
	public void addJars(String jarNames){
		if(StringKit.isBlank(jarNames)){
			return;
		}
		addJars(jarNames.split(","));
	}
	
	public void addJars(String [] jarsName){
		includeJars.addAll(Arrays.asList(jarsName));
	}
	
	public void addJars(List<String> jarsName){
		includeJars.addAll(jarsName);
	}
	
	public AutoTableBindPlugin(DataSource dataSource) {
		this(dataSource, TableNameStyle.DEFAULT);
	}
	
	public AutoTableBindPlugin(DataSource dataSource,TableNameStyle tableNameStyle) {
		super(dataSource);
		this.tableNameStyle = tableNameStyle;
	}
	
	

	public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider) {
		this(dataSourceProvider, TableNameStyle.DEFAULT);
	}

	public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider,TableNameStyle tableNameStyle) {
		super(dataSourceProvider);
		this.tableNameStyle = tableNameStyle;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean start() {
		try {
			List<Class> modelClasses = ClassSearcher.findInClasspathAndInJars(Model.class,includeJars);
			TableBind tb = null;
			for (Class modelClass : modelClasses) {
				tb = (TableBind) modelClass.getAnnotation(TableBind.class);
				if (tb == null) {
					this.addMapping(tableNameStyle.tableName(modelClass.getSimpleName()), modelClass);
					logger.debug("addMapping("+ tableNameStyle.tableName(modelClass.getSimpleName()) + ", "+ modelClass.getName() + ")");
				} else if (StringKit.notBlank(tb.pkName())) {
					this.addMapping(tb.tableName(), tb.pkName(), modelClass);
						logger.debug("addMapping("+ tb.tableName() + ", " + tb.pkName() + ","+ modelClass.getName() + ")");
				} else {
					this.addMapping(tb.tableName(), modelClass);
					logger.debug("addMapping("+ tb.tableName() + ", " + modelClass.getName()+ ")");
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
