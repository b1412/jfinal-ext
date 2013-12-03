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

public class Filter {

    public static final String OPERATOR_LIKE = "LIKE";

    public static final String OPERATOR_EQ = "=";

    public static final String OPERATOR_NOT_EQ = "<>";
    

    public static final String OPERATOR_GREATER_THAN = ">";

    public static final String OPERATOR_LESS_THEN = "<";

    public static final String OPERATOR_GREATER_EQ = ">=";

    public static final String OPERATOR_LESS_EQ = "<=";

    public static final String OPERATOR_NULL = "NULL";
    
    public static final String OPERATOR_NOT_NULL = "NOTNULL";
    
    public static final String RELATION_AND = "AND";

    public static final String RELATION_OR = "OR";

    public static final String RELATION_NOT = "NOT";

    List<Condition> conditions = Lists.newArrayList();

    String relation;

    public Filter() {
        this.relation = RELATION_AND;
    }

    public Filter(String relation) {
        this.relation = relation;
    }
    public void addConditions(String fieldName, Object value, String operater, String relation) {
        conditions.add(new Condition(fieldName, value, operater, relation));
    }

    public void addConditions(String fieldName, Object value, String operater) {
        conditions.add(new Condition(fieldName, value, operater, Filter.RELATION_AND));
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

}
