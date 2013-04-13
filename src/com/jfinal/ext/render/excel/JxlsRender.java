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
    private static final String DEFAULT_FILE_NAME = "file1.xls";
    private String filename;
    private String templetFile;
    private Map<String, Object> beans;

    public JxlsRender(String filename, String templetFile, Map<String, Object> beans) {
        this.filename = filename;
        this.templetFile = templetFile;
        this.beans = beans;
    }

    public static JxlsRender me(String templetFile) {
        JxlsRender render = new JxlsRender(DEFAULT_FILE_NAME, templetFile, Maps.<String, Object> newHashMap());
        return render;
    }

    public static JxlsRender me(String templetFile, Map<String, Object> beans) {
        JxlsRender render = new JxlsRender(DEFAULT_FILE_NAME, templetFile, beans);
        return render;
    }

    public static JxlsRender me(String filename, String templetFile) {
        JxlsRender render = new JxlsRender(filename, templetFile, Maps.<String, Object> newHashMap());
        return render;
    }

    public static JxlsRender me(String filename, String templetFile, Map<String, Object> beans) {
        JxlsRender render = new JxlsRender(filename, templetFile, beans);
        return render;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTempletFile() {
        return templetFile;
    }

    public void setTempletFile(String templetFile) {
        this.templetFile = templetFile;
    }

    public Map<String, Object> getBeans() {
        return beans;
    }

    public void setBeans(Map<String, Object> beans) {
        this.beans = beans;
    }

}
