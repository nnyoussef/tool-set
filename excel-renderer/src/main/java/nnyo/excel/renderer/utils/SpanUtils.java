package nnyo.excel.renderer.utils;

import nnyo.excel.renderer.dto.CoordinateDto;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public final class SpanUtils {

    private SpanUtils() {
    }

    public static int findClosestUnmergedCellForRowAt(final CoordinateDto coordinateDto,
                                                      final LinkedList<CellRangeAddress> mergedCell) {
        final AtomicInteger closestUnmergedCellForRowAt = new AtomicInteger(coordinateDto.getCellPosition());

        final Iterator<CellRangeAddress> cellRangeAddressIterator = mergedCell.iterator();

        boolean isAnUnmergedRegionFoundYet = false;

        while (cellRangeAddressIterator.hasNext()) {
            CellRangeAddress cellAddresses = cellRangeAddressIterator.next();
            final int firstRow = cellAddresses.getFirstRow();
            final int lastRow = cellAddresses.getLastRow();

            final int firstColumn = cellAddresses.getFirstColumn();
            final int lastColumn = cellAddresses.getLastColumn();

            final int currentRow = coordinateDto.getRowPosition();
            final int currentColumnIndex = closestUnmergedCellForRowAt.get();

            if ((currentRow >= firstRow & currentRow <= lastRow) & (currentColumnIndex >= firstColumn & currentColumnIndex <= lastColumn)) {
                closestUnmergedCellForRowAt.set(lastColumn + coordinateDto.getCellPosition());
                isAnUnmergedRegionFoundYet = true;
                if (currentRow + 1 > lastRow)
                    cellRangeAddressIterator.remove();
                continue;
            }
            if (currentRow > lastRow) {
                // Remove the current Merged Cell in Check as it is no longer necessary
                cellRangeAddressIterator.remove();
            }
            if (isAnUnmergedRegionFoundYet)
                break;

        }
        return closestUnmergedCellForRowAt.get();
    }

    public static CellRangeAddress createSpan(XSSFSheet sheet,
                                              int rowSpan,
                                              int colSpan,
                                              CoordinateDto coordinateDto) {
        final int rowIndex = coordinateDto.getRowPosition();
        final int colIndex = coordinateDto.getCellPosition();
        final CellRangeAddress cellAddresses = new CellRangeAddress(rowIndex, rowIndex + rowSpan - 1, colIndex, colIndex + colSpan - 1);
        if (rowSpan * colSpan > 1)
            sheet.addMergedRegionUnsafe(cellAddresses);
        return cellAddresses;
    }
}
