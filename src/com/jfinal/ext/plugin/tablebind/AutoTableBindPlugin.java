package com.jfinal.ext.plugin.tablebind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import com.jfinal.ext.kit.ClassSearcher;
import com.jfinal.kit.StringKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.activerecord.Model;

public class AutoTableBindPlugin extends ActiveRecordPlugin {

    protected final Logger log = Logger.getLogger(getClass());

    private INameStyle nameStyle;

    private List<String> includeJars = new ArrayList<String>();

    private boolean includeAllJarsInLib;

    private boolean autoScan = true;

    @SuppressWarnings("rawtypes")
    private List<Class<? extends Model>> excludeClasses = new ArrayList<Class<? extends Model>>();

    public AutoTableBindPlugin(DataSource dataSource) {
        this(dataSource, SimpleNameStyles.DEFAULT);
    }

    public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider) {
        this(dataSourceProvider, SimpleNameStyles.DEFAULT);
    }

    public AutoTableBindPlugin(DataSource dataSource, INameStyle nameStyle) {
        super(dataSource);
        this.nameStyle = nameStyle;
    }

    public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider, INameStyle nameStyle) {
        super(dataSourceProvider);
        this.nameStyle = nameStyle;
    }

    @SuppressWarnings("rawtypes")
    public void addExcludeClass(Class<? extends Model> clazz) {
        if (clazz == null) {
            return;
        }
        excludeClasses.add(clazz);
    }

    @SuppressWarnings("rawtypes")
    public void addExcludeClasses(Class<? extends Model>[] clazzes) {
        excludeClasses.addAll(Arrays.asList(clazzes));
    }

    @SuppressWarnings("rawtypes")
    public void addExcludeClasses(List<Class<? extends Model>> clazzes) {
        excludeClasses.addAll(clazzes);
    }

    public boolean setIncludeAllJarsInLib() {
        return includeAllJarsInLib;
    }

    public void addJar(String jarName) {
        if (StringKit.isBlank(jarName)) {
            return;
        }
        includeJars.add(jarName);
    }

    public void addJars(String jarNames) {
        if (StringKit.isBlank(jarNames)) {
            return;
        }
        addJars(jarNames.split(","));
    }

    public void addJars(String[] jarsName) {
        includeJars.addAll(Arrays.asList(jarsName));
    }

    public void addJars(List<String> jarsName) {
        includeJars.addAll(jarsName);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public boolean start() {
        try {
            List<Class> modelClasses = ClassSearcher.findInClasspathAndJars(Model.class, includeJars);
            TableBind tb = null;
            for (Class modelClass : modelClasses) {
                if (excludeClasses.contains(modelClass)) {
                    continue;
                }
                tb = (TableBind) modelClass.getAnnotation(TableBind.class);
                String tableName;
                if (tb == null) {
                    if (!autoScan) {
                        continue;
                    }
                    tableName = nameStyle.name(modelClass.getSimpleName());
                    this.addMapping(tableName, modelClass);
                    log.debug("addMapping(" + tableName + ", " + modelClass.getName() + ")");
                } else {
                    tableName = tb.tableName();
                    if (StringKit.notBlank(tb.pkName())) {
                        this.addMapping(tableName, tb.pkName(), modelClass);
                        log.debug("addMapping(" + tableName + ", " + tb.pkName() + "," + modelClass.getName() + ")");
                    } else {
                        this.addMapping(tableName, modelClass);
                        log.debug("addMapping(" + tableName + ", " + modelClass.getName() + ")");
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

    public boolean isAutoScan() {
        return autoScan;
    }

    public void setAutoScan(boolean autoScan) {
        this.autoScan = autoScan;
    }

    public void setIncludeAllJarsInLib(boolean includeAllJarsInLib) {
        this.includeAllJarsInLib = includeAllJarsInLib;
    }

    @SuppressWarnings("rawtypes")
    public void setExcludeClasses(List<Class<? extends Model>> excludeClasses) {
        this.excludeClasses = excludeClasses;
    }

}
