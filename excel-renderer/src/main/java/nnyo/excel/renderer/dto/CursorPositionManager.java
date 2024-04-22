package nnyo.excel.renderer.dto;

import org.apache.poi.ss.util.CellRangeAddress;

import java.util.concurrent.atomic.AtomicInteger;

public class CursorPositionManager {

    final CursorPosition cursorPosition;

    CellRangeAddressNode last = null;
    CellRangeAddressNode first = null;

    public CursorPositionManager(CursorPosition cursorPosition) {
        this.cursorPosition = cursorPosition;
    }

    static class CellRangeAddressNode {
        private CellRangeAddressNode next;
        private CellRangeAddressNode previous;
        private CellRangeAddress value;
    }

    public void add(CellRangeAddress cellAddresses) {
        CellRangeAddressNode node = new CellRangeAddressNode();
        node.previous = last;
        node.value = cellAddresses;
        if (last != null)
            last.next = node;
        last = node;

        int colSpan = cellAddresses.getLastColumn() - cellAddresses.getFirstColumn() + 1;
        cursorPosition.incrementPosition(0, colSpan);
    }

    public void setCursorToNextAvailablePosition() {

        int currentCursorRow = cursorPosition.getRowPosition();

        CellRangeAddressNode scanned = last;
        int minRowPosition = last.value.getLastRow();
        int minColPosition = last.value.getFirstColumn();
        while (scanned != null) {

            if (scanned.value.getLastRow() - currentCursorRow > -1) {
                if (isLessThan(scanned.value.getLastRow(), scanned.value.getFirstColumn(), minRowPosition, minColPosition)) {
                    minRowPosition = scanned.value.getLastRow();
                    minColPosition = scanned.value.getFirstColumn();
                }
            } else {
                removeNodeFromChain(scanned);
            }
            first = scanned;
            scanned = scanned.previous;
        }

        cursorPosition.setPosition(minRowPosition + 1, minColPosition);
    }

    public void setCursorToNextAvailableUnmergedColOnCurrentRow() {
        CellRangeAddressNode scanned = first;
        final AtomicInteger closestUnmergedCellForRowAt = new AtomicInteger(cursorPosition.getCellPosition());

        boolean isAnUnmergedRegionFoundYet = false;

        while (scanned != null) {
            CellRangeAddress cellAddresses = scanned.value;
            final int firstRow = cellAddresses.getFirstRow();
            final int lastRow = cellAddresses.getLastRow();

            final int firstColumn = cellAddresses.getFirstColumn();
            final int lastColumn = cellAddresses.getLastColumn();

            final int currentRow = cursorPosition.getRowPosition();
            final int currentColumnIndex = closestUnmergedCellForRowAt.get();

            if ((currentRow >= firstRow & currentRow <= lastRow) & (currentColumnIndex >= firstColumn & currentColumnIndex <= lastColumn)) {
                closestUnmergedCellForRowAt.set(lastColumn + 1);
                isAnUnmergedRegionFoundYet = true;
                scanned = scanned.next;
                continue;
            }
            if (isAnUnmergedRegionFoundYet)
                break;
            scanned = scanned.next;
        }
        cursorPosition.setCellPosition(closestUnmergedCellForRowAt.get());
    }

    public static void putCursorOnNextEmptyLine(CursorPosition cursorPosition) {
        cursorPosition.incrementPosition(1, 0);
        cursorPosition.setCellPosition(1);
    }

    private boolean isLessThan(int row, int col, int row1, int col1) {
        if (row <= row1)
            return col < col1;
        return false;
    }

    private void removeNodeFromChain(CellRangeAddressNode toRemove) {
        CellRangeAddressNode previous = toRemove.previous;
        CellRangeAddressNode next = toRemove.next;
        if (next != null)
            next.previous = previous;
        if (previous != null)
            previous.next = next;
    }

    private int getSize() {
        CellRangeAddressNode scanned = last;
        int s = 0;
        while (scanned != null) {
            s += 1;
            scanned = scanned.previous;
        }
        return s;
    }

}
