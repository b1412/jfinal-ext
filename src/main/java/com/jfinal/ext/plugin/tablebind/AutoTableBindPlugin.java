package com.jfinal.ext.plugin.tablebind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import com.google.common.collect.Lists;
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
    private List<Class<? extends Model>> excludeClasses = Lists.newArrayList();

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
    public AutoTableBindPlugin addExcludeClass(Class<? extends Model> clazz) {
        if (clazz != null) {
            excludeClasses.add(clazz);
        }
        return this;
    }

    @SuppressWarnings("rawtypes")
    public AutoTableBindPlugin addExcludeClasses(Class<? extends Model>[] clazzes) {
        excludeClasses.addAll(Arrays.asList(clazzes));
        return this;
    }

    @SuppressWarnings("rawtypes")
    public AutoTableBindPlugin addExcludeClasses(List<Class<? extends Model>> clazzes) {
        excludeClasses.addAll(clazzes);
        return this;
    }

    public AutoTableBindPlugin addJar(String jarName) {
        if (StringKit.notBlank(jarName)) {
            includeJars.add(jarName);
        }
        return this;
    }

    public AutoTableBindPlugin addJars(String jarNames) {
        if (StringKit.notBlank(jarNames)) {
            addJars(jarNames.split(","));
        }
        return this;
    }

    public AutoTableBindPlugin addJars(String[] jarsName) {
        includeJars.addAll(Arrays.asList(jarsName));
        return this;
    }

    public AutoTableBindPlugin addJars(List<String> jarsName) {
        includeJars.addAll(jarsName);
        return this;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public boolean start() {
        List<Class<? extends Model>> modelClasses = ClassSearcher.me.addJars(includeJars).search(Model.class);
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
        return super.start();
    }

    @Override
    public boolean stop() {
        return super.stop();
    }

    public boolean isAutoScan() {
        return autoScan;
    }

    public AutoTableBindPlugin setAutoScan(boolean autoScan) {
        this.autoScan = autoScan;
        return this;
    }

    public AutoTableBindPlugin setIncludeAllJarsInLib(boolean includeAllJarsInLib) {
        this.includeAllJarsInLib = includeAllJarsInLib;
        return this;
    }

    public boolean getIncludeAllJarsInLib() {
        return includeAllJarsInLib;
    }

    @SuppressWarnings("rawtypes")
    public AutoTableBindPlugin setExcludeClasses(List<Class<? extends Model>> excludeClasses) {
        this.excludeClasses = excludeClasses;
        return this;
    }

}
