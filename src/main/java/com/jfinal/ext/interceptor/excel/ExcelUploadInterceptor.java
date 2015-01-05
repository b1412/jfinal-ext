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
package com.jfinal.ext.interceptor.excel;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Lists;
import com.jfinal.aop.PrototypeInterceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.excel.filter.RowFilter;
import com.jfinal.ext.kit.ModelExt;
import com.jfinal.ext.kit.Reflect;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Model;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class ExcelUploadInterceptor<M extends Model<?>> extends PrototypeInterceptor {

    protected final Logger LOG = Logger.getLogger(getClass());

    private Class clazz;

    private Rule rule;

    public abstract Rule configRule();

    public abstract void callback(M model);

    @SuppressWarnings("unchecked")
    public ExcelUploadInterceptor() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        clazz = (Class<? extends ExcelUploadInterceptor>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
    }

    @SuppressWarnings({"rawtypes"})
    public void doIntercept(ActionInvocation ai) {
        rule = configRule();
        Controller controller = ai.getController();
        List<Model<?>> list = ExcelTools.processSheet(controller.getFile().getFile(), rule, clazz);
        execPreListProcessor(list);
        for (Model<?> model : list) {
            execPreExcelProcessor(model);
            callback((M) model);
            execPostExcelProcessor(model);
        }
        execPostListProcessor(list);
        ai.invoke();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void execPreListProcessor(List<?> list) {
        String preListProcessorClassName = rule.getPreListProcessor();
        if (StrKit.notBlank(preListProcessorClassName)) {
            PreListProcessor preListProcessor = Reflect.on(preListProcessorClassName).create().get();
            preListProcessor.process(list);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void execPostListProcessor(List<?> list) {
        String postListProcessorClassName = rule.getPostListProcessor();
        if (StrKit.notBlank(postListProcessorClassName)) {
            PostListProcessor postListProcessor = Reflect.on(postListProcessorClassName).create().get();
            postListProcessor.process(list);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void execPreExcelProcessor(Object obj) {
        String preExcelProcessorClassName = rule.getPreExcelProcessor();
        if (StrKit.notBlank(preExcelProcessorClassName)) {
            PreExcelProcessor preExcelProcessor = Reflect.on(preExcelProcessorClassName).create().get();
            preExcelProcessor.process(obj);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void execPostExcelProcessor(Object obj) {
        String postExcelProcessorClassName = rule.getPostExcelProcessor();
        if (StrKit.notBlank(postExcelProcessorClassName)) {
            PostExcelProcessor postExcelProcessor = Reflect.on(postExcelProcessorClassName).create().get();
            postExcelProcessor.process(obj);
        }
    }

    private List<RowFilter> getRowFilterList(String rowFilter) {
        List<RowFilter> rowFilterList = Lists.newArrayList();
        String[] rowFilters = rowFilter.split(",");
        if (rowFilters == null)
            return rowFilterList;
        for (String filter : rowFilters) {
            rowFilterList.add((RowFilter) Reflect.on(filter).create().get());
        }
        return rowFilterList;
    }

}
