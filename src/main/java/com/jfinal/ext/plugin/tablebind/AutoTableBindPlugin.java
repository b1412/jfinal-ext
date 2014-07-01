/**
 * Copyright (c) 2011-2013, kidzhou 周磊 (zhouleib1412@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jfinal.ext.plugin.tablebind;

import java.util.List;

import javax.sql.DataSource;

import com.google.common.collect.Lists;
import com.jfinal.ext.kit.ClassSearcher;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.activerecord.Model;

public class AutoTableBindPlugin extends ActiveRecordPlugin {

    protected final Logger log = Logger.getLogger(getClass());

    @SuppressWarnings("rawtypes")
    private List<Class<? extends Model>> excludeClasses = Lists.newArrayList();
    private List<String> includeJars = Lists.newArrayList();
    private boolean autoScan = true;
    private boolean includeAllJarsInLib;
    private INameStyle nameStyle;

    public AutoTableBindPlugin(DataSource dataSource) {
        this(dataSource, SimpleNameStyles.DEFAULT);
    }

    public AutoTableBindPlugin(DataSource dataSource, INameStyle nameStyle) {
        super(dataSource);
        this.nameStyle = nameStyle;
    }

    public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider) {
        this(dataSourceProvider, SimpleNameStyles.DEFAULT);
    }

    public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider, INameStyle nameStyle) {
        super(dataSourceProvider);
        this.nameStyle = nameStyle;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public AutoTableBindPlugin addExcludeClasses(Class<? extends Model>... clazzes) {
        for (Class<? extends Model> clazz : clazzes) {
            excludeClasses.add(clazz);
        }
        return this;
    }

    @SuppressWarnings("rawtypes")
    public AutoTableBindPlugin addExcludeClasses(List<Class<? extends Model>> clazzes) {
        if (clazzes != null) {
            excludeClasses.addAll(clazzes);
        }
        return this;
    }

    public AutoTableBindPlugin addJars(List<String> jars) {
        if (jars != null) {
            includeJars.addAll(jars);
        }
        return this;
    }

    public AutoTableBindPlugin addJars(String... jars) {
        if (jars != null) {
            for (String jar : jars) {
                includeJars.add(jar);
            }
        }
        return this;
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public boolean start() {
        List<Class<? extends Model>> modelClasses = ClassSearcher.of(Model.class).injars(includeJars).includeAllJarsInLib(includeAllJarsInLib).search();
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
                if (StrKit.notBlank(tb.pkName())) {
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

    public AutoTableBindPlugin autoScan(boolean autoScan) {
        this.autoScan = autoScan;
        return this;
    }

    public AutoTableBindPlugin includeAllJarsInLib(boolean includeAllJarsInLib) {
        this.includeAllJarsInLib = includeAllJarsInLib;
        return this;
    }
}
