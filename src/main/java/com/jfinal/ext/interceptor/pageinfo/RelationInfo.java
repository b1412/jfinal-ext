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

import com.jfinal.plugin.activerecord.Model;

/**
 * @author kid create 2013-10-20 
 */
public class RelationInfo {
    private RelationType type;
    
    private String condition;
    
    private Class<? extends Model<?>> model;

    
    public RelationInfo(RelationType type, String condition, Class<? extends Model<?>> model) {
        this.type = type;
        this.condition = condition;
        this.model = model;
    }

    public RelationType getType() {
        return type;
    }

    public void setType(RelationType type) {
        this.type = type;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Class<? extends Model<?>> getModel() {
        return model;
    }

    public void setModel(Class<? extends Model<?>> model) {
        this.model = model;
    }
    
    
}

