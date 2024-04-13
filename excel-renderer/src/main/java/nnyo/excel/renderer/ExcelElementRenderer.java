package nnyo.excel.renderer;

import nnyo.excel.renderer.dto.CoordinateDto;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public interface ExcelElementRenderer {

    void handle(CoordinateDto coordinateDto,
                Object elementToHandle,
                XSSFSheet worksheet,
                CellStyleProcessor cellStyleProcessor);
}
