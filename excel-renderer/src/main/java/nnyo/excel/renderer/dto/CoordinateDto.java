package nnyo.excel.renderer.dto;

public class CoordinateDto {
    private int rowPosition = 1;

    private int cellPosition = 1;

    public int getRowPosition() {
        return rowPosition;
    }

    public int getCellPosition() {
        return cellPosition;
    }

    public void setCellPosition(int cellPosition) {
        this.cellPosition = cellPosition;
    }

    public void incrementPosition(int rowIncrement,
                                  int colIncrement) {
        this.rowPosition += rowIncrement;
        this.cellPosition += colIncrement;
    }
}
