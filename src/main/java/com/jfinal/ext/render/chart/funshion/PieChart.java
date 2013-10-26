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
package com.jfinal.ext.render.chart.funshion;

import java.util.List;

import com.jfinal.ext.kit.KeyLabel;

public class PieChart {
    /**
     * 标题
     */
    private String caption;
    /**
     * x坐标说明
     */
    private String xAxisName;
    /**
     * y坐标说明
     */
    private String yAxisName;
    /**
     * 数据源
     */
    private List<KeyLabel> list;

    /**
     * FLASH位置
     */
    private String charUrl;
    /**
     * FLASH宽
     */
    private String charWidth;

    /**
     * FLASH高
     */
    private String charHigh;

    /**
     * freemaker模板路径
     */
    private String fltPath;

    private String mapRoot;

    public String getCharWidth() {
        return charWidth;
    }

    public void setCharWidth(String charWidth) {
        this.charWidth = charWidth;
    }

    public String getCharHigh() {
        return charHigh;
    }

    public void setCharHigh(String charHigh) {
        this.charHigh = charHigh;
    }

    public String getxAxisName() {
        return xAxisName;
    }

    public void setxAxisName(String xAxisName) {
        this.xAxisName = xAxisName;
    }

    public String getyAxisName() {
        return yAxisName;
    }

    public void setyAxisName(String yAxisName) {
        this.yAxisName = yAxisName;
    }

    public String getCharUrl() {
        return charUrl;
    }

    public void setCharUrl(String charUrl) {
        this.charUrl = charUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getXAxisName() {
        return xAxisName;
    }

    public void setXAxisName(String axisName) {
        xAxisName = axisName;
    }

    public String getYAxisName() {
        return yAxisName;
    }

    public void setYAxisName(String axisName) {
        yAxisName = axisName;
    }

    public String getFltPath() {
        return fltPath;
    }

    public void setFltPath(String fltPath) {
        this.fltPath = fltPath;
    }

    public String getMapRoot() {
        return mapRoot;
    }

    public void setMapRoot(String mapRoot) {
        this.mapRoot = mapRoot;
    }

    public List<KeyLabel> getList() {
        return list;
    }

    public void setList(List<KeyLabel> list) {
        this.list = list;
    }

}
