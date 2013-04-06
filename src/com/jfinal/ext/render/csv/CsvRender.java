package com.jfinal.ext.render.csv;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;

import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

@SuppressWarnings("serial")
public class CsvRender extends Render {
    private static final String DEFAULT_FILE_NAME = "default.csv";
    private static final String DEFAULT_ENCODE_TYPE = "gbk";

    private List<String> headers;
    private List<?> data;
    private String fileName;
    private String encodeType;
    private List<String> clomuns; // 隐藏列

    public CsvRender() {
    }

    public CsvRender(List<String> headers, List<?> data, String fileName, String encodeType) {
        this.headers = headers;
        this.data = data;
        this.fileName = fileName;
        this.encodeType = encodeType;
    }

    public CsvRender(List<String> headers, List<?> data, String fileName, String encodeType, List<String> clomuns) {
        this.headers = headers;
        this.data = data;
        this.fileName = fileName;
        this.encodeType = encodeType;
        this.clomuns = clomuns;
    }

    // 未包含隐藏列
    public static CsvRender csv(List<String> headers, List<?> data) {
        return new CsvRender(headers, data, DEFAULT_FILE_NAME, DEFAULT_ENCODE_TYPE);
    }

    public static CsvRender csv(List<String> headers, List<?> data, String fileName) {
        return new CsvRender(headers, data, fileName, DEFAULT_ENCODE_TYPE);
    }

    public static CsvRender csv(List<String> headers, List<?> data, String fileName, String encodeType) {
        return new CsvRender(headers, data, fileName, encodeType);
    }

    // 包含隐藏列
    public static CsvRender csv(List<String> headers, List<?> data, List<String> clomuns) {
        return new CsvRender(headers, data, DEFAULT_FILE_NAME, DEFAULT_ENCODE_TYPE, clomuns);
    }

    public static CsvRender csv(List<String> headers, List<?> data, String fileName, List<String> clomuns) {
        return new CsvRender(headers, data, fileName, DEFAULT_ENCODE_TYPE, clomuns);
    }

    public static CsvRender csv(List<String> headers, List<?> data, String fileName, String encodeType, List<String> clomuns) {
        return new CsvRender(headers, data, fileName, encodeType, clomuns);
    }

    @Override
    public void render() {
        response.reset();
        PrintWriter out = null;
        try {
            response.setContentType("application/csv;charset=gbk");
            response.setHeader("Content-Disposition", "attachment;  filename=" + URLEncoder.encode(fileName, encodeType));
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

    public List<?> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getEncodeType() {
        return encodeType;
    }

    public void setEncodeType(String encodeType) {
        this.encodeType = encodeType;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<String> getClomuns() {
        return clomuns;
    }

    public void setClomuns(List<String> clomuns) {
        this.clomuns = clomuns;
    }

}
