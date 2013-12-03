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
package com.jfinal.ext.interceptor.pageinfo;

import java.util.List;

import com.google.common.collect.Lists;
import com.jfinal.aop.PrototypeInterceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public abstract class PageInfoInterceptor extends PrototypeInterceptor {

    private boolean useColumnLabel = false;

    private String columns;

    private Class<? extends Model<?>> model;

    private List<RelationInfo> relations = Lists.newArrayList();

    public abstract void config();

    public PageInfoInterceptor setModel(Class<? extends Model<?>> model) {
        this.model = model;
        return this;
    }

    public PageInfoInterceptor addRelation(Class<? extends Model<?>> model, String condition, RelationType relationType) {
        relations.add(new RelationInfo(relationType, condition, model));
        return this;
    }

    @SuppressWarnings({ "rawtypes" })
    public void doIntercept(ActionInvocation ai) {
        config();
        Controller controller;
        controller = ai.getController();
        PageInfo pageInfo = PageInfoKit.injectPageInfo(model, controller,relations);
        extendsPageInfo(pageInfo);
        Page page = PageInfoKit.populate(pageInfo, this);
        controller.setAttr("pageInfo", pageInfo);
        controller.setAttr("page", page);
        ai.invoke();
    }


    /**pageInfo方法增强，如权限管理*/
    public void extendsPageInfo(PageInfo pageInfo) {
    }

    public PageInfoInterceptor useColumnLabel(boolean useColumnLabel) {
        this.useColumnLabel = useColumnLabel;
        return this;
    }

    public boolean useColumnLabel() {
        return useColumnLabel;
    }

    public String columns() {
        return columns;
    }
    public PageInfoInterceptor columns(String columns) {
        this.columns = columns;
        return this;
    }

    public List<RelationInfo> relations() {
        return relations;
    }

    public PageInfoInterceptor relations(List<RelationInfo> relations) {
        this.relations = relations;
        return this;
    }

    public Class<? extends Model<?>> model() {
        return model;
    }

}
