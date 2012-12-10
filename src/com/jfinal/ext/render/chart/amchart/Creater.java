package com.jfinal.ext.render.chart.amchart;

import java.util.List;

import com.jfinal.ext.kit.KeyLabel;
import com.jfinal.kit.StringKit;
import com.jfinal.log.Logger;


public class Creater {
	
	protected final static Logger logger = Logger.getLogger(Creater.class);
	
	private Creater(){}
	
	public static boolean isFormat = true;
	
	/**
	 *
	 * Description: <br> 创建报表,曲线图,区域图
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <chart>
	 *     <series>
	 *         <value xid="0">USA</value>
	 *         <value xid="1">UK</value>
	 *     </series>
	 *     <graphs>
	 *         <graph gid="1">
	 *             <value xid="0">3.5</value>
	 *             <value xid="1">1.7</value>
	 *         </graph>
	 *         <graph gid="0">
	 *             <value xid="0">4.2</value>
	 *             <value xid="1">3.1</value>
	 *         </graph>
	 *     </graphs>
	 * </chart>
	 * 
	 * @param chart
	 *            报表实体
	 * 
     * @return 创建的字符串
	 */
	@SuppressWarnings("rawtypes")
	public static String createMultipleChart(GraphChart chart) {
		StringBuffer strXML = new StringBuffer("<?xml version='1.0' encoding='UTF-8'?>").append(newLine()).append("<chart>").append(newLine());
		strXML.append(newLine());
		strXML = appendSeries(strXML,chart.getSeriesNames());
		List value = chart.getValues();
		if(value!=null&&value.size()>0){
			if(value.get(0) instanceof String){
				strXML = appendSimpleGraphs(strXML, value);
			}else if(value.get(0) instanceof List){
				strXML = appendMultipleGraphs(strXML,value);
			}
		}
		strXML.append(newLine()).append("</chart>");
		return strXML.toString();
	}
	/**
	 * 
	 * Description: <br>创建饼状图
	 * 
	 *  数据格式
	 *	<pie>
  	 *		<slice title="月租费">0.0</slice>
  	 *		<slice title="语音费">24.58</slice>
  	 *		<slice title="数据费">1.5</slice>
  	 *		<slice title="增值费">12.6</slice>
  	 *		<slice title="一次性费">0.0</slice>
  	 *		<slice title="其他费用">60.0</slice>
	 *	</pie>
	 * @param chart
	 * @return
	 */
	public static String createPieChart(PieChart chart){
		StringBuffer strXML = new StringBuffer("<?xml version='1.0' encoding='UTF-8'?>").append(newLine());
		strXML.append("<pie>").append(newLine());
		List<KeyLabel> pies = chart.getPies();
		for (KeyLabel pie : pies) {
			String label = pie.getLabel();
			if(StringKit.isBlank(label)){
				label="0";
			}
			strXML.append(space(1)).append("<slice title='").append(pie.getKey()).append("'>").append(pie.getLabel()).append("</slice>").append(newLine());
		}
		strXML.append("</pie>");
		return strXML.toString();		
	}
	
	private static StringBuffer appendMultipleGraphs(StringBuffer strXML,
			List<List<String>> values) {
			if(values ==null){
				return strXML;
			}
			strXML.append(space(1)).append("<graphs>");
			for (int i = 0,size = values.size(); i < size; i++) {
				strXML.append(newLine()).append(space(2)).append("<graph gid ='").append(i+1).append("'>");
				List<String> value = values.get(i);
				for (int j = 0; j < value.size(); j++) {
					String val = value.get(j);
					if(StringKit.isBlank(val)){
						val="0";
					}
					strXML.append(newLine()).append(space(3)).append("<value xid='").append(j).append("'>").append(val).append("</value>");
				}
				strXML.append(newLine()).append(space(2)).append("</graph>");
			}
			strXML.append(newLine()).append(space(1)).append("</graphs>");
			return strXML;
	}
	/**
	 *  生成x轴描述
	 * @param strXML
	 * @param seriesNames
	 *         描述的list
	 * @return
	 */
	private static StringBuffer appendSeries(StringBuffer strXML, List<String> seriesNames) {
		if(seriesNames == null){
			return strXML;
		}
		strXML.append(space(1)).append("<series>").append(newLine());
		for (int i = 0,size = seriesNames.size();i < size; i++) {
			String str = seriesNames.get(i);
			strXML.append(space(2)).append("<value xid='").append(i).append("'>").append(str).append("</value>").append(newLine());
		}
		strXML.append(space(1)).append("</series>").append(newLine());
		return strXML;
	}
	/**
	 * 生成报表图形元素
	 * @param strXML
	 * @param list
	 * @return
	 */
	private static StringBuffer appendSimpleGraphs(StringBuffer strXML, List<String> list) {
		if(list ==null){
			return strXML;
		}
		strXML.append(space(1)).append("<graphs>");
		strXML.append(newLine()).append(space(2)).append("<graph gid = '1' ").append(">");
		for (int i = 0,size = list.size(); i < size; i++) {
			String value = list.get(i);
			if(StringKit.isBlank(value)){
				value="0";
			}
			strXML.append(newLine()).append(space(3)).append("<value xid='").append(i).append("'>").append(value).append("</value>");
		}
		strXML.append(newLine()).append(space(2)).append("</graph>");
		strXML.append(newLine()).append(space(1)).append("</graphs>");
		return strXML;
	}
	/**
	 * 生成缩进
	 * @param level
	 * 			缩进的级别（1级4个空格）
	 * @return
	 *        
	 */
	private static StringBuffer space(int level){
		StringBuffer sb = new StringBuffer();
		if(!isFormat){
			return sb;
		}
		while (level-->0) {
			sb.append("    ");
		}
		return sb;
	}
	/**
	 *  生成换行符
	 * Description: <br>
	 * @return
	 */
	private static StringBuffer newLine() {
		StringBuffer sb = new StringBuffer();
		if(!isFormat){
			return sb;
		}
		return sb;
	}
	
}
