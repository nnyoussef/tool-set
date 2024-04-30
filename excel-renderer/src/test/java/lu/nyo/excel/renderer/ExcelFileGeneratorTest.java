package lu.nyo.excel.renderer;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class ExcelFileGeneratorTest {

    @Test
    public void test01() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", DataGenerator.rowSpanResolutionSimpleTableFormat1());

        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, Utils.getTestCss(), Utils.getOutputStream("testRowSpanResolutionSimpleTableFormat1"));
        final long stop = System.nanoTime();
        Utils.printTime("Test Case 1", stop - start);
    }

    @Test
    public void test02() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", DataGenerator.rowSpanResolutionSimpleTableFormat2());

        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, Utils.getTestCss(), Utils.getOutputStream("testRowSpanResolutionSimpleTableFormat2"));
        final long stop = System.nanoTime();
        Utils.printTime("Test Case 2", stop - start);
    }

    @Test
    public void test03() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", DataGenerator.rowSpanResolutionSimpleTableFormat3());

        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, Utils.getTestCss(), Utils.getOutputStream("testRowSpanResolutionSimpleTableFormat3"));
        final long stop = System.nanoTime();
        Utils.printTime("Test Case 3", stop - start);
    }

    @Test
    public void test04() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", DataGenerator.rowSpanResolutionSimpleTableFormat4());

        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, Utils.getTestCss(), Utils.getOutputStream("testRowSpanResolutionSimpleTableFormat4"));
        final long stop = System.nanoTime();
        Utils.printTime("Test Case 4", stop - start);
    }

    @Test
    public void test05() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", DataGenerator.rowSpanResolutionSimpleTableFormat5());

        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, Utils.getTestCss(), Utils.getOutputStream("testRowSpanResolutionSimpleTableFormat5"));
        final long stop = System.nanoTime();
        Utils.printTime("Test Case 5", stop - start);
    }

    @Test
    public void test06() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", DataGenerator.rowSpanResolutionSimpleTableFormat6());

        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, Utils.getTestCss(), Utils.getOutputStream("testRowSpanResolutionSimpleTableFormat6"));
        final long stop = System.nanoTime();
        Utils.printTime("Test Case 6", stop - start);
    }

    @Test
    public void test07() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", DataGenerator.rowSpanResolutionSimpleTableFormat7());
        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, Utils.getTestCss(), Utils.getOutputStream("testRowSpanResolutionSimpleTableFormat7"));
        final long stop = System.nanoTime();
        Utils.printTime("Test Case 7", stop - start);
    }

    @Test
    public void test08() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", DataGenerator.rowSpanResolutionSimpleTableFormat8());
        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, Utils.getTestCss(), Utils.getOutputStream("testRowSpanResolutionSimpleTableFormat8"));
        final long stop = System.nanoTime();
        Utils.printTime("Test Case 8", stop - start);
    }

    @Test
    public void test09() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", DataGenerator.rowSpanResolutionSimpleTableFormat9());
        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, Utils.getTestCss(), Utils.getOutputStream("testRowSpanResolutionSimpleTableFormat9"));
        final long stop = System.nanoTime();
        Utils.printTime("Test Case 9", stop - start);
    }

    @Test
    public void test10() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", DataGenerator.rowSpanResolutionSimpleTableFormat10());
        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, Utils.getTestCss(), Utils.getOutputStream("testRowSpanResolutionSimpleTableFormat10"));
        final long stop = System.nanoTime();
        Utils.printTime("Test Case 10", stop - start);
    }

    @Test
    public void test11() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", DataGenerator.rowSpanResolutionWithColSpanResolutionSimpleTableFormat());
        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, Utils.getTestCss(), Utils.getOutputStream("testRowSpanResolutionSimpleTableFormat11"));
        final long stop = System.nanoTime();
        Utils.printTime("Test Case 11", stop - start);
    }

    @Test
    public void test12() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", DataGenerator.rowSpanResolutionSimpleTableFormat());

        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, Utils.getTestCss(), Utils.getOutputStream("testRowSpanResolutionSimpleTableFormat12"));
        final long stop = System.nanoTime();
        Utils.printTime("Test Case 12", stop - start);
    }

    @Test
    public void test13() throws IOException {

        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();

        data.put("Sheet 1 - Test", DataGenerator.simpleTableFormatData());

        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, Utils.getTestCss(), Utils.getOutputStream("testRowSpanResolutionSimpleTableFormat13"));
        final long stop = System.nanoTime();
        Utils.printTime("Test Case 13", stop - start);
    }

}
