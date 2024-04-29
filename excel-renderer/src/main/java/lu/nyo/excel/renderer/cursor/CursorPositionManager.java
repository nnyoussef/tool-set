package lu.nyo.excel.renderer.cursor;

import org.apache.poi.ss.util.CellRangeAddress;

import java.util.LinkedList;

public class CursorPositionManager {

    private static class CellRangeAddressNode {
        private CellRangeAddressNode next;
        private CellRangeAddressNode previous;
        private CellRangeAddress value;
    }

    final CursorPosition cursorPosition;

    CellRangeAddressNode first = null;

    LinkedList<CellRangeAddress> newlyAddedCellsToCurrentSelectedRow = new LinkedList<>();

    public CursorPositionManager(CursorPosition cursorPosition) {
        this.cursorPosition = cursorPosition;
    }

    public void add(CellRangeAddress cellAddresses) {
        newlyAddedCellsToCurrentSelectedRow.add(cellAddresses);
        int colSpan = cellAddresses.getLastColumn() - cellAddresses.getFirstColumn() + 1;
        cursorPosition.incrementPosition(0, colSpan);
    }

    private void integrateNewlyAddedCellsToTheCurrentLastNode() {
        CellRangeAddressNode visited = first;
        CellRangeAddressNode hold = null;
        for (CellRangeAddress cellAddresses : newlyAddedCellsToCurrentSelectedRow) {

            if (first == null) {
                first = new CellRangeAddressNode();
                first.value = cellAddresses;
                visited = first;
                continue;
            }

            while (visited != null) {
                if (visited.value.getFirstColumn() > cellAddresses.getFirstColumn()) {
                    CellRangeAddressNode newNode = new CellRangeAddressNode();
                    newNode.value = cellAddresses;
                    newNode.next = visited;

                    if (visited.previous == null) {
                        visited.previous = newNode;
                        first = newNode;
                    } else {
                        CellRangeAddressNode previous = visited.previous;
                        newNode.previous = previous;
                        visited.previous = newNode;
                        previous.next = newNode;
                    }

                    break;
                } else {
                    hold = visited;
                    visited = visited.next;
                }
            }

            if (visited == null) {
                visited = new CellRangeAddressNode();
                visited.value = cellAddresses;
                visited.previous = hold;
                hold.next = visited;
            }

        }
        newlyAddedCellsToCurrentSelectedRow.clear();
    }

    public void setCursorToNextAvailablePosition() {
        integrateNewlyAddedCellsToTheCurrentLastNode();
        CellRangeAddressNode scanned = first;
        int minRowPosition = first.value.getLastRow();
        int minColPosition = first.value.getLastColumn();
        while (scanned != null) {
            if (isLessThan(scanned.value.getLastRow(), scanned.value.getFirstColumn(), minRowPosition, minColPosition)) {
                minRowPosition = scanned.value.getLastRow();
                minColPosition = scanned.value.getFirstColumn();
            }
            scanned = scanned.next;
        }
        cursorPosition.setPosition(minRowPosition + 1, minColPosition);
        clean();
    }

    public void setCursorToNextAvailableUnmergedColOnCurrentRow() {
        CellRangeAddressNode scanned = first;
        int closestUnmergedCellForRowAt = cursorPosition.getCellPosition();
        while (scanned != null) {
            CellRangeAddress cellAddresses = scanned.value;
            final int firstColumn = cellAddresses.getFirstColumn();
            final int lastColumn = cellAddresses.getLastColumn();
            final int currentColumnIndex = closestUnmergedCellForRowAt;
            if ((currentColumnIndex >= firstColumn & currentColumnIndex <= lastColumn)) {
                closestUnmergedCellForRowAt = lastColumn + 1;
            }
            scanned = scanned.next;
        }
        cursorPosition.setCellPosition(closestUnmergedCellForRowAt);
    }

    public static void putCursorOnNextEmptyLine(CursorPosition cursorPosition) {
        cursorPosition.incrementPosition(1, 0);
        cursorPosition.setCellPosition(1);
    }

    private boolean isLessThan(int row, int col, int row1, int col1) {
        if (row < row1)
            return true;
        else if (row == row1) {
            return col < col1;
        } else {
            return false;
        }
    }

    private CellRangeAddressNode removeNodeFromChain(CellRangeAddressNode toRemove) {
        if (toRemove.next != null)
            toRemove.next.previous = toRemove.previous;
        if (toRemove.previous != null) {
            toRemove.previous.next = toRemove.next;
        } else {
            first = toRemove.next;
        }
        return toRemove.next;
    }

    private void clean() {
        CellRangeAddressNode scanned = first;
        while (scanned != null) {
            if (cursorPosition.getRowPosition() > scanned.value.getLastRow()) {
                scanned = removeNodeFromChain(scanned);
                continue;
            }
            scanned = scanned.next;
        }
    }

}
