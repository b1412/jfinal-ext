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
package com.jfinal.ext.render.csv;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;

import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

@SuppressWarnings("serial")
public class CsvRender extends Render {
    private List<String> clomuns;
    private List<?> data;
    private String encodeType = "gbk";
    private String fileName = "default.csv";
    private List<String> headers;

    public CsvRender(List<String> headers, List<?> data) {
        this.headers = headers;
        this.data = data;
    }

    public static CsvRender me(List<String> headers, List<?> data) {
        return new CsvRender(headers, data);
    }

    @Override
    public void render() {
        response.reset();
        PrintWriter out = null;
        try {
            response.setContentType("application/vnd.ms-excel;charset=" + encodeType);
            response.setHeader("Content-Disposition",
                    "attachment;  filename=" + URLEncoder.encode(fileName, encodeType));
            out = response.getWriter();
            out.write(CsvUtil.createCSV(headers, data, clomuns));
        } catch (Exception e) {
            throw new RenderException(e);
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }

    public CsvRender clomuns(List<String> clomuns) {
        this.clomuns = clomuns;
        return this;
    }

    public CsvRender data(List<? extends Object> data) {
        this.data = data;
        return this;
    }

    public CsvRender encodeType(String encodeType) {
        this.encodeType = encodeType;
        return this;
    }

    public CsvRender fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public CsvRender headers(List<String> headers) {
        this.headers = headers;
        return this;
    }

}
