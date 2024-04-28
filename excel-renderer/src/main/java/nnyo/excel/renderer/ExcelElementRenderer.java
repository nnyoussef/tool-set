package nnyo.excel.renderer;

import nnyo.excel.renderer.cursor.CursorPosition;
import org.apache.poi.xssf.streaming.SXSSFSheet;

public interface ExcelElementRenderer {

    void handle(CursorPosition cursorPosition,
                Object elementToHandle,
                SXSSFSheet worksheet,
                CellStyleProcessor cellStyleProcessor);
}
