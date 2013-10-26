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
package com.jfinal.ext.render.chart.amchart;

import java.util.List;

public class GraphChart {
    /**
     * 每条数据的说明（x轴）
     */
    private List<String> seriesNames;

    /**
     * 数据描述
     */
    private List<?> values;

    public List<String> getSeriesNames() {
        return seriesNames;
    }

    public void setSeriesNames(List<String> seriesNames) {
        this.seriesNames = seriesNames;
    }

    public List<?> getValues() {
        return values;
    }

    public void setValues(List<?> values) {
        this.values = values;
    }

}
