package com.jfinal.ext.kit;

import java.util.List;

import com.google.common.collect.Lists;

public class PageInfo {

    public static final int DEFAULT_PAGE_SIZE = 20;

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
