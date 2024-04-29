package lu.nyo.excel.renderer.excel_element;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Row implements Serializable {

    private List<Cell> cells = new LinkedList<>();

    public List<Cell> getCells() {
        return cells;
    }

    public Row setCells(List<Cell> cells) {
        this.cells = cells;
        return this;
    }

}
