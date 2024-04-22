package nnyo.excel.renderer;

import nnyo.excel.renderer.dto.CursorPosition;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public interface ExcelElementRenderer {

    void handle(CursorPosition cursorPosition,
                Object elementToHandle,
                XSSFSheet worksheet,
                CellStyleProcessor cellStyleProcessor);
}
