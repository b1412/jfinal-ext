/**
 * 
 * 
 */
package com.jfinal.ext.render.chart.amchart;

import java.util.List;

import com.jfinal.ext.kit.KeyLabel;

public class PieChart {

    private List<KeyLabel> pies;

    public PieChart() {

    }

    public PieChart(List<KeyLabel> pies) {
        this.pies = pies;
    }

    public List<KeyLabel> getPies() {
        return pies;
    }

    public void setPies(List<KeyLabel> pies) {
        this.pies = pies;
    }

}
