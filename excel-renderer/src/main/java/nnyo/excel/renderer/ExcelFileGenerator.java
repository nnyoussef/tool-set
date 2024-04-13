package nnyo.excel.renderer;


import nnyo.excel.renderer.dto.CoordinateDto;
import nnyo.excel.renderer.excel_element.Table;
import nnyo.excel.renderer.excel_element_handlers.TableExcelElementHandler;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
        final Map<String, XSSFSheet> xssfSheetConcurrentHashMap = new HashMap<>(20);

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            final CellStyleProcessor cellStyleProcessor = CellStyleProcessor.init(css, workbook);

            rendereableObjectsPerSheet.forEach((key, value) -> {
                xssfSheetConcurrentHashMap.put(key, workbook.createSheet(key));
                XSSFSheet xssfSheet = xssfSheetConcurrentHashMap.get(key);
                CoordinateDto coordinateDto = new CoordinateDto();

                Iterator<Object> excelElementPerSheet = value.iterator();

                while (excelElementPerSheet.hasNext()) {
                    Object excelElementToHandle = excelElementPerSheet.next();
                    STRATEGIES_MAP.get(Table.class).handle(coordinateDto, excelElementToHandle, xssfSheet, cellStyleProcessor);
                    excelElementPerSheet.remove();
                }
            });
            workbook.write(outputStream);
        } catch (Exception ignored) {
        }
    }
}
