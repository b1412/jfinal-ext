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

    protected final Logger logger = Logger.getLogger(getClass());
    private final static String CONTENT_TYPE = "application/msexcel;charset=utf-8";
    private static final String DEFAULT_FILE_NAME = "file1.xls";
    private static final String DEFAULT_SHEET_NAME = "sheet1";

    private List<Object> data;
    private String[] columns;
    private String[] headers;
    private String fileName;
    private String sheetName;

    public PoiRender() {
    }

    public PoiRender(String fileName, String sheetName, String[] headers, List<Object> data) {
        this.fileName = fileName;
        this.headers = headers;
        this.data = data;
    }

    public PoiRender(String fileName, String sheetName, String[] headers, String[] columns, List<Object> data) {
        this.fileName = fileName;
        this.headers = headers;
        this.columns = columns;
        this.data = data;
    }

    public static PoiRender excel(List<Object> data, String[] headers) {
        return new PoiRender(DEFAULT_FILE_NAME, DEFAULT_SHEET_NAME, headers, data);
    }

    public static PoiRender excel(List<Object> data, String[] headers, String[] columns) {
        PoiRender render = new PoiRender(DEFAULT_FILE_NAME, DEFAULT_SHEET_NAME, headers, data);
        render.setColumns(columns);
        return render;
    }

    public static PoiRender excel(List<Object> data, String fileName, String[] headers) {
        return new PoiRender(fileName, DEFAULT_SHEET_NAME, headers, data);
    }

    public static PoiRender excel(List<Object> data, String fileName, String sheetName, String[] headers) {
        PoiRender render = new PoiRender(fileName, sheetName, headers, data);
        render.setSheetName(sheetName);
        return render;
    }

    public static PoiRender excel(List<Object> data, String fileName, String sheetName, String[] headers,
            String[] columns) {
        PoiRender render = new PoiRender(fileName, sheetName, headers, data);
        render.setSheetName(sheetName);
        render.setColumns(columns);
        return render;
    }

    @Override
    public void render() {
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        response.setContentType(CONTENT_TYPE);
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            HSSFWorkbook wb = PoiKit.export(sheetName, 0, headers, columns, data, 0);
            wb.write(os);
        } catch (Exception e) {
            throw new RenderException(e);
        } finally {
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }

        }
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

}
