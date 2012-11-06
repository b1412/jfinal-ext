package com.jfinal.ext.plugin.tablebind;

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
	
	private  TableNameStyle tableNameStyle;
	
	private  INameStyle nameStyle;
	
	private  List<String> includeJars = new ArrayList<String>();
	
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
	
	public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider) {
		this(dataSourceProvider, TableNameStyle.DEFAULT);
	}
	
	@Deprecated
	public AutoTableBindPlugin(DataSource dataSource,TableNameStyle tableNameStyle) {
		super(dataSource);
		this.tableNameStyle = tableNameStyle;
	}
	
	public AutoTableBindPlugin(DataSource dataSource,INameStyle nameStyle) {
		super(dataSource);
		this.nameStyle = nameStyle;
	}
   
	@Deprecated
	public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider,TableNameStyle tableNameStyle) {
		super(dataSourceProvider);
		this.tableNameStyle = tableNameStyle;
	}
	
	public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider,INameStyle nameStyle) {
		super(dataSourceProvider);
		this.nameStyle = nameStyle;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean start() {
		try {
			List<Class> modelClasses = ClassSearcher.findInClasspathAndInJars(Model.class,includeJars);
			TableBind tb = null;
			for (Class modelClass : modelClasses) {
				tb = (TableBind) modelClass.getAnnotation(TableBind.class);
				String tableName;
				if (tb == null) {
					if(tableNameStyle!=null){
						tableName = tableNameStyle.tableName(modelClass.getSimpleName());
					}else{
						tableName = nameStyle.name(modelClass.getSimpleName());
					}
					this.addMapping(tableName, modelClass);
					logger.debug("addMapping("+ tableName + ", "+ modelClass.getName() + ")");
				} else {
					tableName = tb.tableName();
					if (StringKit.notBlank(tb.pkName())) {
						this.addMapping(tableName, tb.pkName(), modelClass);
						logger.debug("addMapping("+ tableName + ", " + tb.pkName() + ","+ modelClass.getName() + ")");
					} else {
						this.addMapping(tableName, modelClass);
						logger.debug("addMapping("+ tableName + ", " + modelClass.getName()+ ")");
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
