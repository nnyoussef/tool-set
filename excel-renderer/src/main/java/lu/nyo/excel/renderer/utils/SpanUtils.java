package lu.nyo.excel.renderer.utils;

import lu.nyo.excel.renderer.cursor.CursorPosition;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;

public final class SpanUtils {

    private SpanUtils() {
    }

    public static CellRangeAddress createSpan(SXSSFSheet sheet,
                                              int rowSpan,
                                              int colSpan,
                                              CursorPosition cursorPosition) {
        final int rowIndex = cursorPosition.getRowPosition();
        final int colIndex = cursorPosition.getCellPosition();
        final CellRangeAddress cellAddresses = new CellRangeAddress(rowIndex, rowIndex + rowSpan - 1, colIndex, colIndex + colSpan - 1);
        if (rowSpan * colSpan > 1)
            sheet.addMergedRegionUnsafe(cellAddresses);
        return cellAddresses;
    }
}
