package com.jfinal.ext.render.chart;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.ext.kit.KeyLabel;
import com.jfinal.ext.render.chart.amchart.AmChartsRender;
import com.jfinal.ext.render.chart.funshion.FunshionChartsRender;
import com.jfinal.log.Logger;

public class ChartController extends Controller {

    protected final Logger logger = Logger.getLogger(getClass());

    public void fpie() {
        List<KeyLabel> pies = new ArrayList<KeyLabel>();
        KeyLabel e = new KeyLabel("1111", "java");
        pies.add(e);
        KeyLabel e2 = new KeyLabel("222", "c");
        pies.add(e2);
        FunshionChartsRender fr = new FunshionChartsRender();
        fr.setPies(pies);
        fr.setHeight("500");
        fr.setWidth("500");
        fr.setCaption("pie");
        fr.setFlashFile("Pie3D.swf");
        render(fr);
    }

    public void multiple() {
        List<String> data = new ArrayList<String>();
        data.add("10");
        data.add("11");
        data.add("12");
        data.add("13");
        data.add("14");
        List<String> data1 = new ArrayList<String>();
        data1.add("20");
        data1.add("21");
        data1.add("22");
        data1.add("23");
        data1.add("24");
        List<List<String>> list = new ArrayList<List<String>>();
        list.add(data);
        list.add(data1);
        List<String> series = new ArrayList<String>();
        series.add("1月");
        series.add("2月");
        series.add("3月");
        series.add("4月");
        series.add("5月");
        render(AmChartsRender.graph(list, series, "amline.swf", "line_settings.xml"));
    }

    public void pie() {
        List<KeyLabel> pies = new ArrayList<KeyLabel>();
        KeyLabel e = new KeyLabel("java", "111");
        pies.add(e);
        KeyLabel e2 = new KeyLabel("c", "11");
        pies.add(e2);
        render(AmChartsRender.pie(pies, "ampie.swf", "pie_settings.xml", 500, 500));
    }

    public void simple() {
        List<String> data = new ArrayList<String>();
        data.add("10");
        data.add("11");
        data.add("12");
        data.add("13");
        data.add("14");
        List<String> series = new ArrayList<String>();
        series.add("1月");
        series.add("2月");
        series.add("3月");
        series.add("4月");
        series.add("5月");
        render(AmChartsRender.graph(data, series, "amline.swf", "line_settings.xml"));
    }
}
