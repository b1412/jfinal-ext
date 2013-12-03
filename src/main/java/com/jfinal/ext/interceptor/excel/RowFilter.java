package com.jfinal.ext.interceptor.excel;

import java.util.List;

public interface RowFilter {
    boolean doFilter(int rowNum, List<?> list);
}
