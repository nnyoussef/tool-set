package lu.nyo.excel.renderer.excel_element_handlers;


import lu.nyo.excel.renderer.ExcelElementRenderer;
import lu.nyo.excel.renderer.cursor.CursorPosition;
import lu.nyo.excel.renderer.excel_element.Table;
import lu.nyo.excel.renderer.utils.CellDataUtils;
import lu.nyo.excel.renderer.CellStyleProcessor;
import lu.nyo.excel.renderer.cursor.CursorPositionManager;
import lu.nyo.excel.renderer.excel_element.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static lu.nyo.excel.renderer.utils.SpanUtils.createSpan;
import static org.apache.poi.ss.util.RegionUtil.setBorderBottom;
import static org.apache.poi.ss.util.RegionUtil.setBorderRight;

public class TableExcelElementHandler implements ExcelElementRenderer {

    private static final Map<String, String> CELL_CSS_FULL_CLASS_NAMES_CACHE_TBODY = new HashMap<>(50);

    static {
        CELL_CSS_FULL_CLASS_NAMES_CACHE_TBODY.put("", "tbody .default");
    }

    @Override
    public void handle(CursorPosition cursorPosition,
                       Object elementToHandle,
                       SXSSFSheet worksheet,
                       CellStyleProcessor cellStyleProcessor) {

        final Table table = ((Table) elementToHandle);

        AtomicReference<Stream<Row>> header = new AtomicReference<>(Stream.empty());
        AtomicReference<Stream<Row>> body = new AtomicReference<>(Stream.empty());
        AtomicReference<Stream<Row>> footer = new AtomicReference<>(Stream.empty());

        table.tableContentInitializer(header, body, footer);

        render(worksheet, header.get(), cursorPosition, "thead", cellStyleProcessor);
        render(worksheet, body.get(), cursorPosition, "tbody", cellStyleProcessor);
        render(worksheet, footer.get(), cursorPosition, "tfoot", cellStyleProcessor);

        CursorPositionManager.putCursorOnNextEmptyLine(cursorPosition);
    }

    private void render(final SXSSFSheet worksheet,
                        final Stream<Row> rows,
                        final CursorPosition cursorPosition,
                        final String tableSection,
                        final CellStyleProcessor cellStyleProcessor) {

        final CursorPositionManager cursorPositionManager = new CursorPositionManager(cursorPosition);

        rows.forEach(row -> {

            row.getCells().forEach(cell -> {
                final int rowSpan = cell.getRowSpan();
                final int colSpan = cell.getColSpan();
                final String css = getCssClassFullName(tableSection, cell.getCssClass());

                cursorPositionManager.setCursorToNextAvailableUnmergedColOnCurrentRow();

                final SXSSFRow xssfRow = getRow(worksheet, cursorPosition);

                final SXSSFCell xssfCell = xssfRow.createCell(cursorPosition.getCellPosition());

                final XSSFCellStyle xssfCellStyle = cellStyleProcessor.createStyle(css);

                final CellRangeAddress cellAddressesAfterMerging = createSpan(worksheet, rowSpan, colSpan, cursorPosition);

                cursorPositionManager.add(cellAddressesAfterMerging);

                CellDataUtils.setData(cell.getData(), xssfCell);
                resolveBorderForMergedCells(cellAddressesAfterMerging, xssfCellStyle, worksheet);
                xssfCell.setCellStyle(xssfCellStyle);
            });
            cursorPositionManager.setCursorToNextAvailablePosition();
        });
    }

    private String getCssClassFullName(String tableSection,
                                       String cellCss) {
        String css;
        if ("tbody" == tableSection) {
            if (CELL_CSS_FULL_CLASS_NAMES_CACHE_TBODY.containsKey(cellCss)) {
                css = CELL_CSS_FULL_CLASS_NAMES_CACHE_TBODY.get(cellCss);
            } else {
                css = tableSection + " ." + cellCss;
                CELL_CSS_FULL_CLASS_NAMES_CACHE_TBODY.put(cellCss, css);
            }
        } else {
            if ("default" == cellCss)
                css = tableSection + " .default";
            else {
                css = tableSection + " ." + cellCss;
            }
        }
        return css;
    }

    private SXSSFRow getRow(SXSSFSheet sheet,
                            CursorPosition cursorPosition) {
        final SXSSFRow xssfRow = sheet.getRow(cursorPosition.getRowPosition());
        if (xssfRow == null)
            return sheet.createRow(cursorPosition.getRowPosition());
        else return sheet.getRow(cursorPosition.getRowPosition());
    }

    private void resolveBorderForMergedCells(CellRangeAddress cellRangeAddress,
                                             XSSFCellStyle xssfCellStyle,
                                             SXSSFSheet sheet) {
        if (cellRangeAddress.getNumberOfCells() == 1)
            return;

        setBorderBottom(xssfCellStyle.getBorderBottom(), cellRangeAddress, sheet);
        setBorderRight(xssfCellStyle.getBorderRight(), cellRangeAddress, sheet);

        SXSSFRow lastRow = sheet.getRow(cellRangeAddress.getLastRow());
        for (int i = cellRangeAddress.getFirstColumn(); i < cellRangeAddress.getLastColumn() + 1; i++) {
            final SXSSFCell xssfCell = lastRow.getCell(i);
            xssfCell.setCellStyle(xssfCellStyle);
        }

        for (int i = cellRangeAddress.getFirstRow(); i < cellRangeAddress.getLastRow() + 1; i++) {
            final SXSSFCell xssfCell = sheet.getRow(i).getCell(cellRangeAddress.getLastColumn());
            xssfCell.setCellStyle(xssfCellStyle);
        }

    }
}
