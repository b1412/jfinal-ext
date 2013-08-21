package com.jfinal.ext.render.excel;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Map;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.ss.usermodel.Workbook;

import com.google.common.collect.Maps;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

@SuppressWarnings("serial")
public class JxlsRender extends Render {
    private static final String CONTENT_TYPE = "application/vnd.ms-excel;charset=" + getEncoding();
    private String filename = "file1.xls";
    private String templetFile;
    private Map<String, Object> beans = Maps.<String, Object> newHashMap();

    public JxlsRender(String filename, String templetFile, Map<String, Object> beans) {
        this.filename = filename;
        this.templetFile = templetFile;
        this.beans = beans;
    }

    private JxlsRender() {
    }

    public static JxlsRender me(String templetFile) {
        return new JxlsRender().templetFile(templetFile);
    }

    @Override
    public void render() {
        if (beans.isEmpty()) {
            buildBean();
        }
        response.setContentType(CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment;Filename=" + filename);
        try {
            OutputStream out = response.getOutputStream();
            InputStream is = new BufferedInputStream(new FileInputStream(templetFile));
            XLSTransformer transformer = new XLSTransformer();
            Workbook workBook = transformer.transformXLS(is, beans);
            workBook.write(out);
        } catch (Exception e) {
            throw new RenderException(e);
        }

    }

    private void buildBean() {
        Enumeration<String> attrs = request.getAttributeNames();
        while (attrs.hasMoreElements()) {
            String key = attrs.nextElement();
            Object value = request.getAttribute(key);
            beans.put(key, value);
        }

    }

    public JxlsRender filename(String filename) {
        this.filename = filename;
        return this;
    }

    public JxlsRender templetFile(String templetFile) {
        this.templetFile = templetFile;
        return this;
    }

    public JxlsRender beans(Map<String, Object> beans) {
        this.beans = beans;
        return this;
    }

}
