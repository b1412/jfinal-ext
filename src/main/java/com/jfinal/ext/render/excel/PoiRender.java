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
package com.jfinal.ext.render.excel;

import com.jfinal.ext.kit.excel.PoiExporter;
import com.jfinal.log.Logger;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@SuppressWarnings("serial")
public class PoiRender extends Render {

    protected final Logger LOG = Logger.getLogger(getClass());
    private final static String CONTENT_TYPE = "application/msexcel;charset=" + getEncoding();
    private List<?>[] data;
    private String[][] headers;
    private String[] sheetNames = new String[]{};
    private int cellWidth;
    private String[][] columns = new String[]{};
    private String fileName = "file1.xls";
    private int headerRow;
    private String version;

    public PoiRender(List<?>[] data) {
        this.data = data;
    }

    public static PoiRender me(List<?>... data) {
        return new PoiRender(data);
    }

    @Override
    public void render() {
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        response.setContentType(CONTENT_TYPE);
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            PoiExporter.data(data).version(version).sheetNames(sheetNames).headerRow(headerRow).headers(headers).columns(columns)
                    .cellWidth(cellWidth).export().write(os);
        } catch (Exception e) {
            throw new RenderException(e);
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }

        }
    }

    public PoiRender headers(String[]... headers) {
        this.headers = headers;
        return this;
    }

    public PoiRender headerRow(int headerRow) {
        this.headerRow = headerRow;
        return this;
    }

    public PoiRender columns(String... columns) {
        this.columns = columns;
        return this;
    }

    public PoiRender sheetName(String... sheetName) {
        this.sheetNames = sheetName;
        return this;
    }

    public PoiRender cellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
        return this;
    }

    public PoiRender fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public PoiRender version(String version) {
        this.version = version;
        return this;
    }
}
