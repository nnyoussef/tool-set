package nnyo.excel.renderer;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import static nnyo.excel.renderer.DataGenerator.*;
import static nnyo.excel.renderer.Utils.*;

public class ExcelFileGeneratorTest {

    @Test
    public void test01() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", rowSpanResolutionSimpleTableFormat1());

        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, getTestCss(), getOutputStream("testRowSpanResolutionSimpleTableFormat1"));
        final long stop = System.nanoTime();
        printTime("Test Case 1", stop - start);
    }

    @Test
    public void test02() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", rowSpanResolutionSimpleTableFormat2());

        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, getTestCss(), getOutputStream("testRowSpanResolutionSimpleTableFormat2"));
        final long stop = System.nanoTime();
        printTime("Test Case 2", stop - start);
    }

    @Test
    public void test03() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", rowSpanResolutionSimpleTableFormat3());

        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, getTestCss(), getOutputStream("testRowSpanResolutionSimpleTableFormat3"));
        final long stop = System.nanoTime();
        printTime("Test Case 3", stop - start);
    }

    @Test
    public void test04() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", rowSpanResolutionSimpleTableFormat4());

        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, getTestCss(), getOutputStream("testRowSpanResolutionSimpleTableFormat4"));
        final long stop = System.nanoTime();
        printTime("Test Case 4", stop - start);
    }

    @Test
    public void test05() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", rowSpanResolutionSimpleTableFormat5());

        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, getTestCss(), getOutputStream("testRowSpanResolutionSimpleTableFormat5"));
        final long stop = System.nanoTime();
        printTime("Test Case 5", stop - start);
    }

    @Test
    public void test06() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", rowSpanResolutionSimpleTableFormat6());

        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, getTestCss(), getOutputStream("testRowSpanResolutionSimpleTableFormat6"));
        final long stop = System.nanoTime();
        printTime("Test Case 6", stop - start);
    }

    @Test
    public void test07() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", rowSpanResolutionSimpleTableFormat7());
        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, getTestCss(), getOutputStream("testRowSpanResolutionSimpleTableFormat7"));
        final long stop = System.nanoTime();
        printTime("Test Case 7", stop - start);
    }

    @Test
    public void test08() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", rowSpanResolutionSimpleTableFormat8());
        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, getTestCss(), getOutputStream("testRowSpanResolutionSimpleTableFormat8"));
        final long stop = System.nanoTime();
        printTime("Test Case 8", stop - start);
    }

    @Test
    public void test09() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", rowSpanResolutionSimpleTableFormat9());
        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, getTestCss(), getOutputStream("testRowSpanResolutionSimpleTableFormat9"));
        final long stop = System.nanoTime();
        printTime("Test Case 9", stop - start);
    }

    @Test
    public void test10() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", rowSpanResolutionSimpleTableFormat10());
        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, getTestCss(), getOutputStream("testRowSpanResolutionSimpleTableFormat10"));
        final long stop = System.nanoTime();
        printTime("Test Case 10", stop - start);
    }

    @Test
    public void test11() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", rowSpanResolutionWithColSpanResolutionSimpleTableFormat());
        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, getTestCss(), getOutputStream("testRowSpanResolutionSimpleTableFormat11"));
        final long stop = System.nanoTime();
        printTime("Test Case 11", stop - start);
    }

    @Test
    public void test12() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("Sheet 1", rowSpanResolutionSimpleTableFormat());

        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, getTestCss(), getOutputStream("testRowSpanResolutionSimpleTableFormat12"));
        final long stop = System.nanoTime();
        printTime("Test Case 12", stop - start);
    }

    @Test
    public void test13() throws IOException {

        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();

        data.put("Sheet 1 - Test", simpleTableFormatData());
        data.put("Sheet 2 - Test", simpleTableFormatData());
        data.put("Sheet 3 - Test", simpleTableFormatData());
        data.put("Sheet 4 - Test", simpleTableFormatData());

        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, getTestCss(), getOutputStream("testRowSpanResolutionSimpleTableFormat13"));
        final long stop = System.nanoTime();
        printTime("Test Case 13", stop - start);
    }

}
