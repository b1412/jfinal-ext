
package com.jfinal.ext.kit.excel;

import com.google.common.collect.Lists;

import javax.xml.bind.annotation.*;
import java.util.List;


@XmlRootElement
public class Rule {

    protected String name;

    protected int sheetNo;

    protected int start;

    protected int end;

    protected String rowFilter;

    protected String preExcelProcessor;

    protected String postExcelProcessor;

    protected String preListProcessor;

    protected String postListProcessor;

    protected List<Cell> cells = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public int getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(int value) {
        this.sheetNo = value;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int value) {
        this.start = value;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int value) {
        this.end = value;
    }

    public String getRowFilter() {
        return rowFilter;
    }

    public void setRowFilter(String value) {
        this.rowFilter = value;
    }

    public String getPreExcelProcessor() {
        return preExcelProcessor;
    }

    public void setPreExcelProcessor(String value) {
        this.preExcelProcessor = value;
    }

    public String getPostExcelProcessor() {
        return postExcelProcessor;
    }

    public void setPostExcelProcessor(String value) {
        this.postExcelProcessor = value;
    }

    public String getPreListProcessor() {
        return preListProcessor;
    }

    public void setPreListProcessor(String value) {
        this.preListProcessor = value;
    }

    public String getPostListProcessor() {
        return postListProcessor;
    }

    public void setPostListProcessor(String value) {
        this.postListProcessor = value;
    }

    @XmlElementWrapper(name = "cells")
    @XmlElement(name = "cell")
    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public Rule addCell(Cell cell) {
        cells.add(cell);
        return this;
    }

    public Rule addCell(int index, String attribute) {
        cells.add(Cell.create(index, attribute));
        return this;
    }

    public Rule addCell(int index, String attribute, String convert, String validate) {
        cells.add(Cell.create(index, attribute, convert, validate));
        return this;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Cell {

        protected int index;

        protected String attribute;

        protected String convert;

        protected String validate;

        public static Cell create(int index, String attribute) {
            Cell cell = new Cell();
            cell.setIndex(index);
            cell.setAttribute(attribute);
            return cell;
        }

        public static Cell create(int index, String attribute, String convert, String validate) {
            Cell cell = create(index, attribute);
            cell.setConvert(convert);
            cell.setValidate(validate);
            return cell;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int value) {
            this.index = value;
        }

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String value) {
            this.attribute = value;
        }

        public String getConvert() {
            return convert;
        }

        public void setConvert(String value) {
            this.convert = value;
        }

        public String getValidate() {
            return validate;
        }

        public void setValidate(String value) {
            this.validate = value;
        }

        @Override
        public String toString() {
            return "Cell{" +
                    "index=" + index +
                    ", attribute='" + attribute + '\'' +
                    ", convert='" + convert + '\'' +
                    ", validate='" + validate + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Rule{" +
                "name='" + name + '\'' +
                ", sheetNo=" + sheetNo +
                ", start=" + start +
                ", end=" + end +
                ", rowFilter='" + rowFilter + '\'' +
                ", preExcelProcessor='" + preExcelProcessor + '\'' +
                ", postExcelProcessor='" + postExcelProcessor + '\'' +
                ", preListProcessor='" + preListProcessor + '\'' +
                ", postListProcessor='" + postListProcessor + '\'' +
                ", cells=" + cells +
                '}';
    }
}
