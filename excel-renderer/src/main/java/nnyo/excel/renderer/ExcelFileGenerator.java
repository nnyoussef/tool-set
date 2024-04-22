package nnyo.excel.renderer;


import nnyo.excel.renderer.dto.CursorPosition;
import nnyo.excel.renderer.excel_element.Table;
import nnyo.excel.renderer.excel_element_handlers.TableExcelElementHandler;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

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

        try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
            final CellStyleProcessor cellStyleProcessor = CellStyleProcessor.init(css, workbook);

            rendereableObjectsPerSheet.forEach((key, value) -> {
                SXSSFSheet xssfSheet = workbook.createSheet(key);
                CursorPosition cursorPosition = new CursorPosition();

                Iterator<Object> excelElementPerSheet = value.iterator();

                while (excelElementPerSheet.hasNext()) {
                    Object excelElementToHandle = excelElementPerSheet.next();
                    STRATEGIES_MAP.get(Table.class).handle(cursorPosition, excelElementToHandle, xssfSheet, cellStyleProcessor);
                    excelElementPerSheet.remove();
                }
            });
            workbook.write(outputStream);
        } catch (Exception error) {
            //not treated
        }
    }
}
