package com.jfinal.ext.render.chart.funshion;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.jfinal.ext.kit.KeyLabel;
import com.jfinal.log.Logger;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

@SuppressWarnings("serial")
public class FunshionChartsRender extends Render implements FunshionChartsConstans {

    private static final String CONTENT_TYPE = "text/html;charset=" + getEncoding();

    protected final Logger logger = Logger.getLogger(getClass());

    private List<KeyLabel> pies;

    private String type = "superChart";

    private String flashPath;

    /**
     * 报表数据源
     */
    private List<List<String>> chartList;

    private List<List<String>> rightChartList;

    private List<String> seriesNames;

    private List<String> rightSeriesNames;

    private List<String> labels;
    /**
     * 报表div的高度
     */
    private String height;
    /**
     * 报表div的宽度
     */
    private String width;
    /**
     * 报表flash存放路径
     */
    private String flashFile;
    /**
     * 报表页面的标题
     */
    private String title;

    /**
     * 饼图 ，单线图
     */
    private PieChart superChart = new PieChart();

    /**
     * 多线图
     */
    private GraphChart superMultiCharts = new GraphChart();

    private List<List<String>> values;

    private String caption;

    private String subcaption;

    private String numberPrefix;

    private String numberSuffix;

    private String genChartXml() {
        String dataString = "";
        if (type.equals("superChart")) {
            superChart.setCharUrl(request.getContextPath() + FLASH_PATH + flashFile);
            superChart.setCharHigh(height);
            superChart.setCharWidth(width);
            superChart.setList(pies);
            superChart.setCaption(caption);
            dataString = CreateCharts.createPieChart(superChart);
        } else if (type.equals("superMultiCharts")) {
            superMultiCharts.setCharUrl(request.getContextPath() + FLASH_PATH + flashFile);
            superMultiCharts.setCaption(caption);
            superMultiCharts.setSubcaption(subcaption);
            superMultiCharts.setLeftSeriesNames(seriesNames);
            superMultiCharts.setRightSeriesNames(rightSeriesNames);
            superMultiCharts.setRightValues(rightChartList);
            superMultiCharts.setLabels(labels);
            superMultiCharts.setLeftValues(chartList);
            superMultiCharts.setCharHigh(height);
            superMultiCharts.setCharWidth(width);
            superMultiCharts.setNumberPrefix(numberPrefix);
            superMultiCharts.setNumberSuffix(numberSuffix);
            dataString = CreateCharts.createMultiCharts(superMultiCharts);
        }
        return dataString;
    }

    @Override
    public void render() {
        PrintWriter out = null;
        try {
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType(CONTENT_TYPE);
            out = response.getWriter();
            out.write(genChartXml());
            out.flush();
        } catch (IOException e) {
            throw new RenderException(e);
        } finally {
            if (out != null) {
                out.close();
            }
        }

    }

    public List<KeyLabel> getPies() {
        return pies;
    }

    public void setPies(List<KeyLabel> pies) {
        this.pies = pies;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<List<String>> getChartList() {
        return chartList;
    }

    public void setChartList(List<List<String>> chartList) {
        this.chartList = chartList;
    }

    public List<List<String>> getRightChartList() {
        return rightChartList;
    }

    public void setRightChartList(List<List<String>> rightChartList) {
        this.rightChartList = rightChartList;
    }

    public List<String> getSeriesNames() {
        return seriesNames;
    }

    public void setSeriesNames(List<String> seriesNames) {
        this.seriesNames = seriesNames;
    }

    public List<String> getRightSeriesNames() {
        return rightSeriesNames;
    }

    public void setRightSeriesNames(List<String> rightSeriesNames) {
        this.rightSeriesNames = rightSeriesNames;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getFlashFile() {
        return flashFile;
    }

    public void setFlashFile(String flashFile) {
        this.flashFile = flashFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PieChart getSuperChart() {
        return superChart;
    }

    public void setSuperChart(PieChart superChart) {
        this.superChart = superChart;
    }

    public GraphChart getSuperMultiCharts() {
        return superMultiCharts;
    }

    public void setSuperMultiCharts(GraphChart superMultiCharts) {
        this.superMultiCharts = superMultiCharts;
    }

    public List<List<String>> getValues() {
        return values;
    }

    public void setValues(List<List<String>> values) {
        this.values = values;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getSubcaption() {
        return subcaption;
    }

    public void setSubcaption(String subcaption) {
        this.subcaption = subcaption;
    }

    public String getNumberPrefix() {
        return numberPrefix;
    }

    public void setNumberPrefix(String numberPrefix) {
        this.numberPrefix = numberPrefix;
    }

    public String getNumberSuffix() {
        return numberSuffix;
    }

    public void setNumberSuffix(String numberSuffix) {
        this.numberSuffix = numberSuffix;
    }

    public String getFlashPath() {
        return flashPath;
    }

    public void setFlashPath(String flashPath) {
        this.flashPath = flashPath;
    }

}
