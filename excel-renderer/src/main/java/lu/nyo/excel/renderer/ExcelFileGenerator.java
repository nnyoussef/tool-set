package lu.nyo.excel.renderer;


import lu.nyo.excel.renderer.cursor.CursorPosition;
import lu.nyo.excel.renderer.excel_element.Table;
import lu.nyo.excel.renderer.excel_element_handlers.TableExcelElementHandler;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;


public class ExcelFileGenerator {

    private ExcelFileGenerator() {
    }

    private static final Map<Class<?>, ExcelElementRenderer> STRATEGIES_MAP = new HashMap<>();

    static {
        STRATEGIES_MAP.put(Table.class, new TableExcelElementHandler());
    }

    public static void generate(Map<String, LinkedList<Object>> rendereableObjectsPerSheet,
                                String css,
                                OutputStream outputStream) {

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(25)) {

            final CellStyleProcessor cellStyleProcessor = CellStyleProcessor.init(css, workbook);
            workbook.setCompressTempFiles(true);
            rendereableObjectsPerSheet.forEach((key, value) -> {
                SXSSFSheet xssfSheet = workbook.createSheet(key);
                CursorPosition cursorPosition = new CursorPosition();
                Iterator<Object> excelElementPerSheet = value.iterator();

                while (excelElementPerSheet.hasNext()) {
                    Object excelElementToHandle = excelElementPerSheet.next();
                    STRATEGIES_MAP.get(Table.class).handle(cursorPosition, excelElementToHandle, xssfSheet, cellStyleProcessor);
                    excelElementPerSheet.remove();
                    flushRows(xssfSheet);
                }
                flushBufferedData(xssfSheet);
            });
            workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void flushRows(SXSSFSheet sheet) {
        try {
            sheet.flushRows();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void flushBufferedData(SXSSFSheet sxssfSheet) {
        try {
            sxssfSheet.flushBufferedData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
