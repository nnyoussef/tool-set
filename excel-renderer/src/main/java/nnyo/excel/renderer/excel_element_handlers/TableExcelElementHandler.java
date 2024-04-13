package nnyo.excel.renderer.excel_element_handlers;


import nnyo.excel.renderer.CellStyleProcessor;
import nnyo.excel.renderer.ExcelElementRenderer;
import nnyo.excel.renderer.dto.CoordinateDto;
import nnyo.excel.renderer.excel_element.Row;
import nnyo.excel.renderer.excel_element.Table;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static nnyo.excel.renderer.utils.CellDataUtils.setData;
import static nnyo.excel.renderer.utils.SpanUtils.createSpan;
import static nnyo.excel.renderer.utils.SpanUtils.findClosestUnmergedCellForRowAt;
import static org.apache.poi.ss.util.RegionUtil.setBorderBottom;
import static org.apache.poi.ss.util.RegionUtil.setBorderRight;

public class TableExcelElementHandler implements ExcelElementRenderer {

    private static final Map<String, String> CELL_CSS_FULL_CLASS_NAMES_CACHE_TBODY = new HashMap<>(50);

    static {
        CELL_CSS_FULL_CLASS_NAMES_CACHE_TBODY.put("", "tbody .default");
    }

    @Override
    public void handle(CoordinateDto coordinateDto,
                       Object elementToHandle,
                       XSSFSheet worksheet,
                       CellStyleProcessor cellStyleProcessor) {

        final Table table = ((Table) elementToHandle);

        AtomicReference<Stream<Row>> header = new AtomicReference<>(Stream.empty());
        AtomicReference<Stream<Row>> body = new AtomicReference<>(Stream.empty());
        AtomicReference<Stream<Row>> footer = new AtomicReference<>(Stream.empty());

        table.tableContentInitializer(header, body, footer);

        render(worksheet, header.get(), coordinateDto, "thead", cellStyleProcessor);
        render(worksheet, body.get(), coordinateDto, "tbody", cellStyleProcessor);
        render(worksheet, footer.get(), coordinateDto, "tfoot", cellStyleProcessor);

        coordinateDto.setCellPosition(1);
        coordinateDto.incrementPosition(1, 0);
    }

    private void render(final XSSFSheet worksheet,
                        final Stream<Row> rows,
                        final CoordinateDto coordinateDto,
                        final String tableSection,
                        final CellStyleProcessor cellStyleProcessor) {

        final LinkedList<CellRangeAddress> trackedMergedRegions = new LinkedList<>();

        rows.forEach(row -> {
            row.getCells().forEach(cell -> {
                final int rowSpan = cell.getRowSpan();
                final int colSpan = cell.getColSpan();
                final String css = getCssClassFullName(tableSection, cell.getCssClass());

                coordinateDto.setCellPosition(findClosestUnmergedCellForRowAt(coordinateDto, trackedMergedRegions));

                final XSSFRow xssfRow = getRow(worksheet, coordinateDto);

                final XSSFCell xssfCell = xssfRow.createCell(coordinateDto.getCellPosition());

                final XSSFCellStyle xssfCellStyle = cellStyleProcessor.createStyle(css);

                final CellRangeAddress cellAddressesAfterMerging = createSpan(worksheet, rowSpan, colSpan, coordinateDto);

                if (cellAddressesAfterMerging.getNumberOfCells() > 1)
                    trackedMergedRegions.add(cellAddressesAfterMerging);

                coordinateDto.incrementPosition(0, colSpan);

                setData(cell.getData(), xssfCell);
                resolveBorderForMergedCells(cellAddressesAfterMerging, xssfCellStyle, worksheet);
                xssfCell.setCellStyle(xssfCellStyle);

            });
            coordinateDto.setCellPosition(1); // each row iteration the cell cursor should be back to 1
            coordinateDto.incrementPosition(1, 0); // tracing the position of the current row
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

    private XSSFRow getRow(XSSFSheet sheet,
                           CoordinateDto coordinateDto) {
        final XSSFRow xssfRow = sheet.getRow(coordinateDto.getRowPosition());
        if (xssfRow == null)
            return sheet.createRow(coordinateDto.getRowPosition());
        else return sheet.getRow(coordinateDto.getRowPosition());
    }

    private void resolveBorderForMergedCells(CellRangeAddress cellRangeAddress,
                                             XSSFCellStyle xssfCellStyle,
                                             XSSFSheet sheet) {
        if (cellRangeAddress.getNumberOfCells() == 1)
            return;

        setBorderBottom(xssfCellStyle.getBorderBottom(), cellRangeAddress, sheet);
        setBorderRight(xssfCellStyle.getBorderRight(), cellRangeAddress, sheet);

        XSSFRow lastRow = sheet.getRow(cellRangeAddress.getLastRow());
        for (int i = cellRangeAddress.getFirstColumn(); i < cellRangeAddress.getLastColumn() + 1; i++) {
            final XSSFCell xssfCell = lastRow.getCell(i);
            xssfCell.setCellStyle(xssfCellStyle);
        }

        for (int i = cellRangeAddress.getFirstRow(); i < cellRangeAddress.getLastRow() + 1; i++) {
            final XSSFCell xssfCell = sheet.getRow(i).getCell(cellRangeAddress.getLastColumn());
            xssfCell.setCellStyle(xssfCellStyle);
        }

    }
}
