package com.jfinal.ext.render.chart.funshion;

import java.util.List;

import com.jfinal.ext.kit.KeyLabel;
import com.jfinal.kit.StringKit;
import com.jfinal.log.Logger;

public class CreateCharts {

    protected static final Logger LOG = Logger.getLogger(CreateCharts.class);

    // private static Configuration cfg;

    // static {
    // cfg = new Configuration();
    // try {
    // String path=PathKit.getWebRootPath()+"tpo/funshionCharts/flt";
    // File file =new File(path);
    // if(!file.exists()){
    // file.mkdirs();
    // }
    // cfg.setDirectoryForTemplateLoading(file);
    // } catch (IOException e) {
    // logger.error("init freemaker error", e);
    // }
    // //设置FreeMarker模版文件的位置
    // }

    /**
     * 创建报表柱状图,曲线图,区域图
     * 
     * @param chart
     *            报表实体
     * @return 创建的字符串
     */
    public static String createChart(PieChart chart) {
        String strXML = "";
        // if (StringKit.isBlank(chart.getFltPath())) { //freemaker模板为空调用list数据源生成xml
        strXML += "<chart caption='" + chart.getCaption() + "' xAxisName='" + chart.getXAxisName() + "' yAxisName='" + chart.getYAxisName()
                + "' showValues='1' formatNumberScale='1' baseFontSize ='12' bgColor='#CCCCCC'  rotateYAxisName='0' >";

        List<KeyLabel> list = chart.getList();

        for (KeyLabel key : list) {
            strXML += "<set label='" + key.getLabel() + "' value='" + key.getKey() + "' />";
        }

        strXML += "</chart>";
        // } else {
        // Template t = null;
        // try {
        // t = cfg.getTemplate(chart.getFltPath());
        // } catch (IOException e1) {
        // logger.error("CreateCharts error", e1);
        // }
        //
        // ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        // Writer out = new OutputStreamWriter(byteArray);
        // try {
        // t.process(chart.getMapRoot(), out);
        // strXML = new String(byteArray.toByteArray());
        // } catch (TemplateException e) {
        // logger.error("CreateCharts TemplateException", e);
        // } catch (IOException e) {
        // logger.error("CreateCharts IOException", e);
        // }
        //
        // }
        FusionCharts charts = new FusionCharts();

        String chartCode = charts.createChartHTML(chart.getCharUrl(), "", strXML, "myNext", chart.getCharWidth(), chart.getCharHigh(),
                false);
        return chartCode;
    }

    public static String createPieChart(PieChart chart) {
        String strXML = "";
        strXML += "<chart caption='" + chart.getCaption() + "' palette='4' baseFontSize ='12' showNames='1' >";

        List<KeyLabel> list = chart.getList();

        for (KeyLabel key : list) {
            String tkey = key.getKey();
            if (StringKit.isBlank(tkey)) {
                tkey = "0";
            }

            strXML += "<set label='" + key.getLabel() + "' value='" + tkey + "' />";
        }

        strXML += "</chart>";
        FusionCharts charts = new FusionCharts();

        String chartCode = charts.createChartHTML(chart.getCharUrl(), "", strXML, "chartId", chart.getCharWidth(), chart.getCharHigh(),
                false);
        return chartCode;
    }

    /**
     * 
     * @param multiCharts
     * @return
     */
    public static String createMultiCharts(GraphChart multiCharts) {
        StringBuffer strXML = new StringBuffer("");
        strXML.append("<chart caption='"
                + multiCharts.getCaption()
                + "' subcaption='"
                + multiCharts.getSubcaption()// bgColor='FFFFFF,CC3300' alternateHGridColor='CF3300'
                + "' lineThickness='1'  numDivLines='4' numberSuffix='" + multiCharts.getNumberSuffix() + "' numberPrefix='"
                + multiCharts.getNumberPrefix() + "' showValues='1' formatNumberScale='0' anchorRadius='5' divLineAlpha='20' "
                + "divLineColor='#666666 ' bgColor='#6633FF' alternateHGridColor='#6633FF' "
                + "divLineIsDashed='1' showAlternateHGridColor='1' alternateHGridAlpha='5'  shadowAlpha='40' labelStep='1' "
                + "numvdivlines='5' chartRightMargin='35' bgAngle='270' bgAlpha='10,10' rotateYAxisName='0' baseFontSize ='12' >");
        strXML.append("<categories>");
        for (String lable : multiCharts.getLabels()) {
            strXML.append("<category label='" + lable + "'/>");
        }
        strXML.append("</categories>");
        List<String> seriesName = multiCharts.getLeftSeriesNames();

        List<List<String>> list = multiCharts.getLeftValues();
        List<String> colors = multiCharts.getColors();
        for (int i = 0; i < seriesName.size(); i++) {
            strXML.append("<dataset seriesName='" + seriesName.get(i) + "' anchorBgColor='" + colors.get(i) + "' color='" + colors.get(i)
                    + "'>");
            List<String> values = list.get(i);
            for (int j = 0; j < values.size(); j++) {
                strXML.append("<set value='" + values.get(j) + "'/>");
            }
            strXML.append("</dataset>");
        }
        List<String> sSeriesNames = multiCharts.getRightSeriesNames();

        List<List<String>> slist = multiCharts.getRightValues();

        if (sSeriesNames != null) {
            for (int i = 0; i < sSeriesNames.size(); i++) {
                strXML.append("<dataset  parentYAxis='S' seriesName='" + sSeriesNames.get(i) + "' anchorBgColor='" + colors.get(i)
                        + "' color='" + colors.get(i) + "'>");
                List<String> values = slist.get(i);
                for (int j = 0; j < values.size(); j++) {
                    strXML.append("<set value='" + values.get(j) + "'/>");
                }
                strXML.append("</dataset>");
            }
        }
        strXML.append("</chart>");

        FusionCharts charts = new FusionCharts();

        // Create the chart - Column 3D Chart with data from strXML variable
        // using dataXML method
        String chartCode = charts.createChartHTML(multiCharts.getCharUrl(), "", strXML.toString(), "myNext", multiCharts.getCharWidth(),
                multiCharts.getCharHigh(), false);
        return chartCode;
    }
}
