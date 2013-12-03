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

public class PageInfo {

    public static final int DEFAULT_PAGE_SIZE = 10;

    List<Filter> filters = Lists.newArrayList();

    int pageNumber;

    int pageSize;

    String sorterField;

    String sorterDirection;

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNum) {
        this.pageNumber = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSorterField() {
        return sorterField;
    }

    public void setSorterField(String sorterField) {
        this.sorterField = sorterField;
    }

    public String getSorterDirection() {
        return sorterDirection;
    }

    public void setSorterDirection(String sorterDirection) {
        this.sorterDirection = sorterDirection;
    }

}
