package com.jfinal.ext.interceptor.excel;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;

/**
 * 
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * excel文件上传并解析持久化
 * 
 * @author zhoulei create 2012-4-24
 * @version 1.0
 * 
 */
public class ExcelUploadController extends Controller {

    protected final Logger logger = Logger.getLogger(getClass());
    /** 客户端上传的文件 */
    private File excelFile;
    /** excel文件解析开始行 */
    private int start;
    /** excel文件解析结束行 */
    private int end;
    /** excel解析规则文件名 */
    private String rule;
    /** hi服务名 */
    private String model;
    /** 上传成功后更新的navTab的id */
    private Digester digester = DigesterLoader.createDigester(ExcelUploadController.class.getClassLoader().getResource(
            "excel.xml"));
    private ExcelBean excelBean;

    public void index() throws Exception {
        String realPath = ExcelUploadController.class.getResource("/").getFile() + "excelrule/" + rule + ".xml";
        excelBean = (ExcelBean) digester.parse(new File(realPath));
        String modeName = excelBean.getName() + "." + rule;
        Object mgr = null;
        Method saveMethod = mgr.getClass().getMethod("save" + rule, Class.forName(modeName));
        List<?> list = null;
        try {
            list = ExcelTools.digesterSheetWithOneDimensionalRule(excelFile, excelBean, Class.forName(modeName));
        } catch (ExcelException e) {
            renderText(e.getMessage());
        }

        execPreListProcessor(list);
        for (Object obj : list) {
            execPreExcelProcessor(obj);
            saveMethod.invoke(mgr, obj);
            execPostExcelProcessor(obj);
        }
        excePostListProcessor(list);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void excePostListProcessor(List list) {
        String postListProcessorClassName = excelBean.getPostListProcessor();
        if (!StringUtils.isBlank(postListProcessorClassName)) {
            PostListProcessor postListProcessor = null;
            try {
                postListProcessor = (PostListProcessor) Class.forName(postListProcessorClassName).newInstance();
            } catch (Exception e) {
            }
            postListProcessor.process(list);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void execPostExcelProcessor(Object obj) {
        String postExcelProcessorClassName = excelBean.getPostExcelProcessor();
        if (!StringUtils.isBlank(postExcelProcessorClassName)) {
            PostExcelProcessor postExcelProcessor = null;
            try {
                postExcelProcessor = (PostExcelProcessor) Class.forName(postExcelProcessorClassName).newInstance();
            } catch (Exception e) {
                logger.error("create PostExcelProcessor error", e);
            }
            postExcelProcessor.process(obj);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void execPreExcelProcessor(Object obj) {
        String preExcelProcessorClassName = excelBean.getPreExcelProcessor();
        if (!StringUtils.isBlank(preExcelProcessorClassName)) {
            PreExcelProcessor preExcelProcessor = null;
            try {
                preExcelProcessor = (PreExcelProcessor) Class.forName(preExcelProcessorClassName).newInstance();
            } catch (Exception e) {
                logger.error("create PreExcelProcessor error", e);
            }
            preExcelProcessor.process(obj);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void execPreListProcessor(List list) {
        String preListProcessorClassName = excelBean.getPreListProcessor();
        if (!StringUtils.isBlank(preListProcessorClassName)) {
            PreListProcessor preListProcessor = null;
            try {
                preListProcessor = (PreListProcessor) Class.forName(preListProcessorClassName).newInstance();
            } catch (Exception e) {
                logger.error("create PreExcelProcessor error", e);
            }
            preListProcessor.process(list);
        }
    }

    @SuppressWarnings("unused")
    private List<RowFilter> getRowFilterList(String rowFilter) throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        List<RowFilter> rowFilterList = Lists.newArrayList();
        String[] rowFilters = StringUtils.split(StringUtils.deleteWhitespace(rowFilter), ",");
        if (ArrayUtils.getLength(rowFilters) > 0) {
            for (String string : rowFilters) {
                rowFilterList.add((RowFilter) Class.forName(string).newInstance());
            }
        }
        return rowFilterList;
    }

    public File getExcelFile() {
        return excelFile;
    }

    public void setExcelFile(File excelFile) {
        this.excelFile = excelFile;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
