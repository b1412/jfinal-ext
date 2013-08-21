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

    private List<? extends Object> data;
    private String[] columns;
    private String[] headers;
    private String fileName = "file1.xls";
    private String sheetName = "sheet1";
    private int cellWidth;
    private int headerRow;
    
    private PoiRender() {
    }
    
    public static PoiRender excel(List<? extends Object> data, String[] headers) {
        return new PoiRender().headers(headers).data(data);
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
                if(os != null){
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }

        }
    }

    public PoiRender columns(String[] columns) {
        this.columns = columns;
        return this;
    }

    public PoiRender headers(String[] headers) {
        this.headers = headers;
        return this;
    }

    public PoiRender fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public PoiRender sheetName(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }
    
    public PoiRender data(List<? extends Object> data) {
        this.data = data;
        return this;
    }

    public PoiRender cellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
        return this;
    }

    
}
