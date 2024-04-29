package lu.nyo.excel.renderer.excel_element;

import java.io.Serializable;

public class Cell implements Serializable {

    private String cssClass = "default";
    private transient Object data;
    private int colSpan = 1;
    private int rowSpan = 1;

    public String getCssClass() {
        return cssClass;
    }

    public int getColSpan() {
        return colSpan;
    }

    public Cell setColSpan(int colSpan) {
        this.colSpan = colSpan;
        return this;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public Cell setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
        return this;
    }

    public Cell setCssClass(String cssClass) {
        this.cssClass = cssClass;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Cell setData(Object data) {
        this.data = data;
        return this;
    }

}
