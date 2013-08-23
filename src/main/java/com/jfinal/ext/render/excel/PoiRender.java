package com.jfinal.ext.render.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.jfinal.log.Logger;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

@SuppressWarnings("serial")
public class PoiRender extends Render {

    protected final Logger LOG = Logger.getLogger(getClass());
    private final static String CONTENT_TYPE = "application/msexcel;charset=" + getEncoding();
    private int cellWidth;
    private String[] columns;
    private List<?> data;
    private String fileName = "file1.xls";
    private int headerRow;
    private String[] headers;
    private String sheetName = "sheet1";

    public PoiRender(List<?> data, String[] headers) {
        this.data = data;
        this.headers = headers;
    }

    public static PoiRender me(List<?> data, String[] headers) {
        return new PoiRender(data, headers);
    }

    @Override
    public void render() {
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        response.setContentType(CONTENT_TYPE);
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            HSSFWorkbook wb = PoiKit.export(sheetName, headerRow, headers, columns, data, cellWidth);
            wb.write(os);
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

    public PoiRender sheetName(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }

    public PoiRender cellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
        return this;
    }

    public PoiRender columns(String[] columns) {
        this.columns = columns;
        return this;
    }

    public PoiRender data(List<?> data) {
        this.data = data;
        return this;
    }

    public PoiRender fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public PoiRender headers(String[] headers) {
        this.headers = headers;
        return this;
    }
}
