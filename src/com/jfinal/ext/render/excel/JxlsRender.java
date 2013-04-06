/**
 * Date:2013-4-3下午9:57:24
 * Copyright (c) 2010-2013, www.trafree.com  All Rights Reserved.
 */

package com.jfinal.ext.render.excel;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.ss.usermodel.Workbook;

import com.jfinal.render.Render;

/**
 * JxlsRender. <br/>
 * Date: 2013-4-3 下午9:57:24 <br/>
 * 
 * @author kid
 */
public class JxlsRender extends Render {
    private static final long serialVersionUID = -8167145464557988406L;
    private static final String DEFAULT_FILE_NAME = "file1.xls";
    private String filename;
    private String templetFile;
    private Map<String, Object> beans;

    public JxlsRender(String filename, String templetFile, Map<String, Object> beans) {
        this.filename = filename;
        this.templetFile = templetFile;
        this.beans = beans;
    }

    public static JxlsRender me(String templetFile, Map<String, Object> beans) {
        JxlsRender render = new JxlsRender(DEFAULT_FILE_NAME, templetFile, beans);
        return render;
    }
    public static JxlsRender me(String filename, String templetFile, Map<String, Object> beans) {
        JxlsRender render = new JxlsRender(filename, templetFile, beans);
        return render;
    }

    @Override
    public void render() {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;Filename=" + filename);
        try {
            OutputStream out = response.getOutputStream();
            InputStream is = new BufferedInputStream(new FileInputStream(templetFile));
            XLSTransformer transformer = new XLSTransformer();
            Workbook workBook = transformer.transformXLS(is, beans);
            workBook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
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
