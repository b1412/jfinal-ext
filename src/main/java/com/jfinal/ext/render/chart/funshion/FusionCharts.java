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
package com.jfinal.ext.render.chart.funshion;

import javax.servlet.http.HttpServletResponse;

public class FusionCharts {
    /**
     * Encodes the dataURL before it's served to FusionCharts. If you have parameters in your dataURL, you necessarily need to encode it.
     * 
     * @param strDataURL
     *            - dataURL to be fed to chart
     * @param addNoCacheStr
     *            - Whether to add aditional string to URL to disable caching of data
     * @return
     */

    public String encodeDataURL(String strDataURL, String addNoCacheStr, HttpServletResponse response) {
        String encodedURL = strDataURL;
        // Add the no-cache string if required
        if (addNoCacheStr.equals("true")) {
            /*
             * We add ?FCCurrTime=xxyyzz If the dataURL already contains a ?, we add &FCCurrTime=xxyyzz We send the date separated with '_',
             * instead of the usual ':' as FusionCharts cannot handle : in URLs
             */
            java.util.Calendar nowCal = java.util.Calendar.getInstance();
            java.util.Date now = nowCal.getTime();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy HH_mm_ss a");
            String strNow = sdf.format(now);
            if (strDataURL.indexOf("?") > 0) {
                encodedURL = strDataURL + "&FCCurrTime=" + strNow;
            } else {
                strDataURL = strDataURL + "?FCCurrTime=" + strNow;
            }
            encodedURL = response.encodeURL(strDataURL);

        }
        return encodedURL;
    }

    /**
     * Creates the Chart HTML+Javascript to create the FusionCharts object with the given parameters. This method uses JavaScript to
     * overcome the IE browser problem with SWF wherein you have to 'Click to activate' the control
     * 
     * @param chartSWF
     *            - SWF File Name (and Path) of the chart which you intend to plot
     * @param strURL
     *            - If you intend to use dataURL method for this chart, pass the URL as this parameter. Else, set it to "" (in case of
     *            dataXML method)
     * @param strXML
     *            - If you intend to use dataXML method for this chart, pass the XML data as this parameter. Else, set it to "" (in case of
     *            dataURL method)
     * @param chartId
     *            - Id for the chart, using which it will be recognized in the HTML page. Each chart on the page needs to have a unique Id.
     * @param chartWidth
     *            - Intended width for the chart (in pixels)
     * @param chartHeight
     *            - Intended height for the chart (in pixels)
     * @param debugMode
     *            - Whether to start the chart in debug mode
     * @param registerWithJS
     *            - Whether to ask chart to register itself with JavaScript
     */
    public String createChart(String chartSWF, String strURL, String strXML, String chartId, int chartWidth, int chartHeight,
            boolean debugMode, boolean registerWithJS) {
        StringBuffer strBuf = new StringBuffer();
        /*
         * First we create a new DIV for each chart. We specify the name of DIV as "chartId"Div. DIV names are case-sensitive.
         */
        strBuf.append("<!--START Script Block for Chart -->\n");
        strBuf.append("\t\t<div id='" + chartId + "Div' align='center'>\n");
        strBuf.append("\t\t\t\tChart.\n");

        /*
         * The above text "Chart" is shown to users before the chart has started loading (if there is a lag in relaying SWF from server).
         * This text is also shown to users who do not have Flash Player installed. You can configure it as per your needs.
         */

        strBuf.append("\t\t</div>\n");

        /*
         * Now, we render the chart using FusionCharts Class. Each chart's instance (JavaScript) Id is named as chart_"chartId".
         */

        strBuf.append("\t\t<script type='text/javascript'>\n");
        // Instantiate the Chart
        Boolean registerWithJSBool = new Boolean(registerWithJS);
        Boolean debugModeBool = new Boolean(debugMode);
        int regWithJSInt = boolToNum(registerWithJSBool);
        int debugModeInt = boolToNum(debugModeBool);

        strBuf.append("\t\t\t\tvar chart_" + chartId + " = new FusionCharts('" + chartSWF + "', '" + chartId + "', '" + chartWidth + "', '"
                + chartHeight + "', '" + debugModeInt + "', '" + regWithJSInt + "');\n");
        // Check whether we've to provide data using dataXML method or dataURL method
        if (strXML.equals("")) {
            strBuf.append("\t\t\t\t//Set the dataURL of the chart\n");
            strBuf.append("\t\t\t\tchart_" + chartId + ".setDataURL(\"" + strURL + "\");\n");
        } else {
            strBuf.append("\t\t\t\t//Provide entire XML data using dataXML method\n");
            strBuf.append("\t\t\t\tchart_" + chartId + ".setDataXML(\"" + strXML + "\");\n");
        }
        strBuf.append("\t\t\t\t//Finally, render the chart.\n");
        strBuf.append("\t\t\t\tchart_" + chartId + ".render(\"" + chartId + "Div\");\n");
        strBuf.append("\t\t</script>\n");
        strBuf.append("\t\t<!--END Script Block for Chart-->\n");
        return strBuf.substring(0);
    }

    /**
     * Creates the Chart HTML to embed the swf object with the given parameters
     * 
     * @param chartSWF
     *            - SWF File Name (and Path) of the chart which you intend to plot
     * @param strURL
     *            - If you intend to use dataURL method for this chart, pass the URL as this parameter. Else, set it to "" (in case of
     *            dataXML method)
     * @param strXML
     *            - If you intend to use dataXML method for this chart, pass the XML data as this parameter. Else, set it to "" (in case of
     *            dataURL method)
     * @param chartId
     *            - Id for the chart, using which it will be recognized in the HTML page. Each chart on the page needs to have a unique Id.
     * @param chartWidth
     *            - Intended width for the chart (in pixels)
     * @param chartHeight
     *            - Intended height for the chart (in pixels)
     * @param debugMode
     *            - Whether to start the chart in debug mode
     */

    public String createChartHTML(String chartSWF, String strURL, String strXML, String chartId, String chartWidth, String chartHeight,
            boolean debugMode) {
        /*
         * Generate the FlashVars string based on whether dataURL has been provided or dataXML.
         */
        String strFlashVars = "";
        Boolean debugModeBool = new Boolean(debugMode);

        if (strXML.equals("")) {
            // DataURL Mode
            strFlashVars = "chartWidth=" + chartWidth + "&chartHeight=" + chartHeight + "&debugMode=" + boolToNum(debugModeBool)
                    + "&dataURL=" + strURL + "";
        } else {
            // DataXML Mode
            strFlashVars = "chartWidth=" + chartWidth + "&chartHeight=" + chartHeight + "&debugMode=" + boolToNum(debugModeBool)
                    + "&dataXML=" + strXML + "";
        }
        StringBuffer strBuf = new StringBuffer();

        strBuf.append("\t\t<!--START Code Block for Chart-->\n");
        strBuf.append("\t\t\t\t<object classid='clsid:d27cdb6e-ae6d-11cf-96b8-444553540000' "
                + "codebase='http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0' width='" + chartWidth
                + "' height='" + chartHeight + "' id='" + chartId + "'>\n");
        strBuf.append("\t\t\t\t <param name='allowScriptAccess' value='always' />\n");
        strBuf.append("\t\t\t\t <param name='movie' value='" + chartSWF + "'/>\n");
        strBuf.append("\t\t\t\t <param name='FlashVars' value=\"" + strFlashVars + "\" />\n");
        strBuf.append("\t\t\t\t <param name='quality' value='high' />\n");
        strBuf.append("\t\t\t\t <embed src='" + chartSWF + "' FlashVars=\"" + strFlashVars + "\" quality='high' width='" + chartWidth
                + "' height='" + chartHeight + "' name='" + chartId + "' allowScriptAccess='always' type='application/x-shockwave-flash' "
                + "pluginspage='http://www.macromedia.com/go/getflashplayer' />\n");
        strBuf.append("\t\t</object>\n");
        // END Code Block for Chart
        strBuf.append("\t\t<!--END Code Block for Chart-->\n");
        return strBuf.substring(0);
    }

    private int boolToNum(Boolean bool) {
        int num = 0;
        if (bool.booleanValue()) {
            num = 1;
        }
        return num;
    }
}
