package nnyo.excel.renderer;

import nnyo.excel.renderer.excel_element.Cell;
import nnyo.excel.renderer.excel_element.Row;
import nnyo.excel.renderer.excel_element.Table;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static java.nio.file.Files.newOutputStream;
import static java.nio.file.Path.of;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toCollection;

public class ExcelFileGeneratorTest {

    private static final String EXPORT_PATH = System.getProperty("user.dir");

    private static final int ROWS_COUNT_IN_BODY = 1;

    @Test
    public void testSimpleTableFormat() throws IOException {

        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();

        data.put("Sheet 1 - Test", simpleTableFormatData());
        data.put("Sheet 2 - Test", simpleTableFormatData());
        data.put("Sheet 3 - Test", simpleTableFormatData());
        data.put("Sheet 4 - Test", simpleTableFormatData());

        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, getTestCss(), getOutputStream("simpleTableFormatTest"));
        final long stop = System.nanoTime();
        System.out.println("simpleTableFormatTest: " + ((stop - start) / 1000f / 1000 / 1000) + " Seconds");
    }

    @Test
    public void testSpanResolutionSimpleTableFormat() throws IOException {
        LinkedHashMap<String, LinkedList<Object>> data = new LinkedHashMap<>();
        data.put("RowSpanSimpleTable", rowSpanResolutionSimpleTableFormat());
        data.put("RowSpanWithColSpanSimpleTable", rowSpanResolutionWithColSpanResolutionSimpleTableFormat());
        data.put("RowSpanSimpleTableAlt1", rowSpanResolutionSimpleTableFormatAlt1());
        data.put("RowSpanSimpleTableAlt2", rowSpanResolutionSimpleTableFormatAlt2());
        data.put("RowSpanSimpleTableAlt3", rowSpanResolutionSimpleTableFormatAlt3());

        final long start = System.nanoTime();
        ExcelFileGenerator.generate(data, getTestCss(), getOutputStream("spanResolutionSimpleTableFormatTest"));
        final long stop = System.nanoTime();
        System.out.println("spanResolutionSimpleTableFormatTest: " + ((stop - start) / 1000f / 1000 / 1000) + " Seconds");
    }

    // data generator
    private static LinkedList<Object> simpleTableFormatData() {


        final Cell year = new Cell().setData("Year").setColSpan(8).setCssClass("bleuFonce");
        final Cell empty = new Cell().setData("I am an Empty Cell").setRowSpan(2).setColSpan(2).setCssClass("bleuFonce");

        final Cell y1 = new Cell().setData("2020").setCssClass("bleuClaire");
        final Cell y2 = new Cell().setData("2021").setCssClass("bleuClaire");
        final Cell y3 = new Cell().setData("2022").setCssClass("bleuClaire");
        final Cell y4 = new Cell().setData("2023").setCssClass("bleuClaire");

        final List<Row> headerRow = new LinkedList<>();
        headerRow.add(new Row().setCells(asList(year, empty)));
        headerRow.add(new Row().setCells(asList(y1, y2, y3, y4, y1, y2, y3, y4)));

        final List<Row> body = IntStream.range(0, ROWS_COUNT_IN_BODY)
                .mapToObj(i -> {
                    Cell data1 = new Cell().setData(100000);
                    Cell data2 = new Cell().setData(100000).setColSpan(2);
                    return new Row().setCells(asList(data1, data1, data1, data1, data2, data1, data1, data1, data1, data2));
                }).collect(toCollection(LinkedList::new));

        final List<Row> footerRow = new LinkedList<>();
        final Cell t1 = new Cell().setData("2020");
        final Cell t2 = new Cell().setData("2021");
        final Cell t3 = new Cell().setData("2022");
        final Cell t4 = new Cell().setData("2023");
        final Cell t5 = new Cell().setData("Total").setColSpan(2);
        footerRow.add(new Row().setCells(asList(t1, t2, t3, t4, t5)));

        LinkedList<Object> tables = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            final Table table = (header, body1, footer) -> {
                header.set(new LinkedList<>(headerRow).stream());
                body1.set(new LinkedList<>(body).stream());
                footer.set(new LinkedList<>(footerRow).stream());
            };
            tables.add(table);
        }


        return tables;
    }

    private static LinkedList<Object> rowSpanResolutionWithColSpanResolutionSimpleTableFormat() {
        Cell empty1 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(3);
        Cell empty2 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(2).setColSpan(2);
        Cell empty3 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(4);
        Cell empty4 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(3).setColSpan(2);
        Cell empty5 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(5).setColSpan(2);

        Row row1 = new Row();
        List<Cell> cellsOfRow1 = new LinkedList<>();
        cellsOfRow1.add(empty1);
        cellsOfRow1.add(empty2);
        cellsOfRow1.add(empty3);
        row1.setCells(cellsOfRow1);

        Row row2 = new Row();
        List<Cell> cellsOfRow2 = new LinkedList<>();
        row2.setCells(cellsOfRow2);

        Row row3 = new Row();
        List<Cell> cellsOfRow3 = new LinkedList<>();
        cellsOfRow3.add(new Cell().setData("γ").setCssClass("bleuClaire").setColSpan(2));
        row3.setCells(cellsOfRow3);

        Row row4 = new Row();
        List<Cell> cellsOfRow4 = new LinkedList<>();
        cellsOfRow4.add(empty4);
        cellsOfRow4.add(new Cell().setData("ζ").setCssClass("bleuClaire"));
        row4.setCells(cellsOfRow4);

        Row row5 = new Row();
        List<Cell> cellsOfRow5 = new LinkedList<>();
        cellsOfRow5.add(new Cell().setData("θ").setCssClass("bleuClaire"));
        cellsOfRow5.add(new Cell().setData("ι").setCssClass("bleuClaire"));
        row5.setCells(cellsOfRow5);

        Row row6 = new Row();
        List<Cell> cellsOfRow6 = new LinkedList<>();
        cellsOfRow6.add(empty5);
        row6.setCells(cellsOfRow6);

        Row row7 = new Row();
        List<Cell> cellsOfRow7 = new LinkedList<>();
        cellsOfRow7.add(new Cell().setData("μ").setCssClass("bleuClaire"));
        cellsOfRow7.add(new Cell().setData("ν").setCssClass("bleuClaire"));
        row7.setCells(cellsOfRow7);

        Row row8 = new Row();
        List<Cell> cellsOfRow8 = new LinkedList<>();
        cellsOfRow8.add(new Cell().setData("ο").setCssClass("bleuClaire"));
        cellsOfRow8.add(new Cell().setData("π").setCssClass("bleuClaire"));
        row8.setCells(cellsOfRow8);

        Row row9 = new Row();
        List<Cell> cellsOfRow9 = new LinkedList<>();
        cellsOfRow9.add(new Cell().setData("Σ").setCssClass("bleuClaire"));
        cellsOfRow9.add(new Cell().setData("τ").setCssClass("bleuClaire"));
        row9.setCells(cellsOfRow9);

        Row row10 = new Row();
        List<Cell> cellsOfRow10 = new LinkedList<>();
        cellsOfRow10.add(new Cell().setData("φ").setCssClass("bleuClaire").setColSpan(2));
        row10.setCells(cellsOfRow10);

        LinkedList<Row> header = new LinkedList<>();
        header.add(row1);
        header.add(row2);
        header.add(row3);
        header.add(row4);
        header.add(row5);
        header.add(row6);
        header.add(row7);
        header.add(row8);
        header.add(row9);
        header.add(row10);

        LinkedList<Object> tables = new LinkedList<>();
        final Table table = (header1, body, footer) -> header1.set(header.stream());
        tables.add(table);

        return tables;
    }

    private static LinkedList<Object> rowSpanResolutionSimpleTableFormatAlt1() {
        Cell empty1 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(3);
        Cell empty2 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(2);
        Cell empty3 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(4);

        Row row1 = new Row();
        List<Cell> cellsOfRow1 = new LinkedList<>();
        cellsOfRow1.add(empty1);
        cellsOfRow1.add(empty2);
        cellsOfRow1.add(new Cell().setData("α").setCssClass("bleuClaire"));
        cellsOfRow1.add(empty1);
        row1.setCells(cellsOfRow1);

        Row row2 = new Row();
        List<Cell> cellsOfRow2 = new LinkedList<>();
        cellsOfRow2.add(new Cell().setData("β").setCssClass("bleuClaire"));
        row2.setCells(cellsOfRow2);

        Row row3 = new Row();
        List<Cell> cellsOfRow3 = new LinkedList<>();
        cellsOfRow3.add(new Cell().setData("γ").setCssClass("bleuClaire"));
        cellsOfRow3.add(new Cell().setData("δ").setCssClass("bleuClaire"));
        row3.setCells(cellsOfRow3);

        Row row4 = new Row();
        List<Cell> cellsOfRow4 = new LinkedList<>();
        cellsOfRow4.add(empty1);
        cellsOfRow4.add(new Cell().setData("ε").setCssClass("bleuClaire"));
        cellsOfRow4.add(new Cell().setData("ζ").setCssClass("bleuClaire"));
        cellsOfRow4.add(new Cell().setData("ζ").setCssClass("bleuClaire"));
        row4.setCells(cellsOfRow4);

        Row row5 = new Row();
        List<Cell> cellsOfRow5 = new LinkedList<>();
        cellsOfRow5.add(new Cell().setData("η").setCssClass("bleuClaire"));
        cellsOfRow5.add(new Cell().setData("θ").setCssClass("bleuClaire"));
        cellsOfRow5.add(new Cell().setData("ι").setCssClass("bleuClaire"));
        row5.setCells(cellsOfRow5);

        Row row6 = new Row();
        List<Cell> cellsOfRow6 = new LinkedList<>();
        cellsOfRow6.add(new Cell().setData("κ").setCssClass("bleuClaire"));
        cellsOfRow6.add(new Cell().setData("λ").setCssClass("bleuClaire"));
        cellsOfRow6.add(empty3);
        row6.setCells(cellsOfRow6);

        Row row7 = new Row();
        List<Cell> cellsOfRow7 = new LinkedList<>();
        cellsOfRow7.add(new Cell().setData("μ").setCssClass("bleuClaire"));
        cellsOfRow7.add(new Cell().setData("ν").setCssClass("bleuClaire"));
        cellsOfRow7.add(new Cell().setData("ξ").setCssClass("bleuClaire"));
        row7.setCells(cellsOfRow7);

        Row row8 = new Row();
        List<Cell> cellsOfRow8 = new LinkedList<>();
        cellsOfRow8.add(new Cell().setData("ο").setCssClass("bleuClaire"));
        cellsOfRow8.add(new Cell().setData("π").setCssClass("bleuClaire"));
        cellsOfRow8.add(new Cell().setData("ρ").setCssClass("bleuClaire"));
        row8.setCells(cellsOfRow8);

        Row row9 = new Row();
        List<Cell> cellsOfRow9 = new LinkedList<>();
        cellsOfRow9.add(new Cell().setData("Σ").setCssClass("bleuClaire"));
        cellsOfRow9.add(new Cell().setData("τ").setCssClass("bleuClaire"));
        cellsOfRow9.add(new Cell().setData("υ").setCssClass("bleuClaire"));
        row9.setCells(cellsOfRow9);

        Row row10 = new Row();
        List<Cell> cellsOfRow10 = new LinkedList<>();
        cellsOfRow10.add(new Cell().setData("φ").setCssClass("bleuClaire"));
        cellsOfRow10.add(new Cell().setData("χ").setCssClass("bleuClaire"));
        cellsOfRow10.add(new Cell().setData("ψ").setCssClass("bleuClaire"));
        cellsOfRow10.add(new Cell().setData("Ω").setCssClass("bleuClaire"));
        row10.setCells(cellsOfRow10);

        LinkedList<Row> header = new LinkedList<>();
        header.add(row1);
        header.add(row2);
        header.add(row3);
        header.add(row4);
        header.add(row5);
        header.add(row6);
        header.add(row7);
        header.add(row8);
        header.add(row9);
        header.add(row10);

        LinkedList<Object> tables = new LinkedList<>();
        final Table table = (header1, body, footer) -> header1.set(header.stream());
        tables.add(table);

        return tables;
    }

    private static LinkedList<Object> rowSpanResolutionSimpleTableFormatAlt2() {
        Cell empty1 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(3);
        Cell empty2 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(2);
        Cell empty3 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(4);
        Cell empty4 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(1).setColSpan(3);

        Row row1 = new Row();
        List<Cell> cellsOfRow1 = new LinkedList<>();
        cellsOfRow1.add(empty1);
        cellsOfRow1.add(empty2);
        cellsOfRow1.add(new Cell().setData("α").setCssClass("bleuClaire"));
        cellsOfRow1.add(empty1);
        row1.setCells(cellsOfRow1);

        Row row2 = new Row();
        List<Cell> cellsOfRow2 = new LinkedList<>();
        cellsOfRow2.add(new Cell().setData("β").setCssClass("bleuClaire"));
        row2.setCells(cellsOfRow2);

        Row row3 = new Row();
        List<Cell> cellsOfRow3 = new LinkedList<>();
        cellsOfRow3.add(new Cell().setData("γ").setCssClass("bleuClaire"));
        cellsOfRow3.add(new Cell().setData("δ").setCssClass("bleuClaire"));
        row3.setCells(cellsOfRow3);

        Row row4 = new Row();
        List<Cell> cellsOfRow4 = new LinkedList<>();
        cellsOfRow4.add(empty1);
        cellsOfRow4.add(new Cell().setData("ε").setCssClass("bleuClaire"));
        cellsOfRow4.add(new Cell().setData("ζ").setCssClass("bleuClaire"));
        cellsOfRow4.add(new Cell().setData("ζ").setCssClass("bleuClaire"));
        row4.setCells(cellsOfRow4);

        Row row5 = new Row();
        List<Cell> cellsOfRow5 = new LinkedList<>();
        cellsOfRow5.add(new Cell().setData("η").setCssClass("bleuClaire"));
        cellsOfRow5.add(new Cell().setData("θ").setCssClass("bleuClaire"));
        cellsOfRow5.add(new Cell().setData("ι").setCssClass("bleuClaire"));
        row5.setCells(cellsOfRow5);

        Row row6 = new Row();
        List<Cell> cellsOfRow6 = new LinkedList<>();
        cellsOfRow6.add(new Cell().setData("κ").setCssClass("bleuClaire"));
        cellsOfRow6.add(new Cell().setData("λ").setCssClass("bleuClaire"));
        cellsOfRow6.add(empty3);
        row6.setCells(cellsOfRow6);

        Row row7 = new Row();
        List<Cell> cellsOfRow7 = new LinkedList<>();
        cellsOfRow7.add(new Cell().setData("μ").setCssClass("bleuClaire"));
        cellsOfRow7.add(new Cell().setData("ν").setCssClass("bleuClaire"));
        cellsOfRow7.add(new Cell().setData("ξ").setCssClass("bleuClaire"));
        row7.setCells(cellsOfRow7);

        Row row8 = new Row();
        List<Cell> cellsOfRow8 = new LinkedList<>();
        cellsOfRow8.add(new Cell().setData("ο").setCssClass("bleuClaire"));
        cellsOfRow8.add(new Cell().setData("π").setCssClass("bleuClaire"));
        cellsOfRow8.add(new Cell().setData("ρ").setCssClass("bleuClaire"));
        row8.setCells(cellsOfRow8);

        Row row9 = new Row();
        List<Cell> cellsOfRow9 = new LinkedList<>();
        cellsOfRow9.add(new Cell().setData("Σ").setCssClass("bleuClaire"));
        cellsOfRow9.add(new Cell().setData("τ").setCssClass("bleuClaire"));
        cellsOfRow9.add(new Cell().setData("υ").setCssClass("bleuClaire"));
        row9.setCells(cellsOfRow9);

        Row row10 = new Row();
        List<Cell> cellsOfRow10 = new LinkedList<>();
        cellsOfRow10.add(empty4);
        cellsOfRow10.add(new Cell().setData("Ω").setCssClass("bleuClaire"));
        row10.setCells(cellsOfRow10);

        LinkedList<Row> header = new LinkedList<>();
        header.add(row1);
        header.add(row2);
        header.add(row3);
        header.add(row4);
        header.add(row5);
        header.add(row6);
        header.add(row7);
        header.add(row8);
        header.add(row9);
        header.add(row10);

        LinkedList<Object> tables = new LinkedList<>();
        final Table table = (header1, body, footer) -> header1.set(header.stream());
        tables.add(table);

        return tables;
    }

    private static LinkedList<Object> rowSpanResolutionSimpleTableFormat() {
        Cell empty1 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(3);
        Cell empty2 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(2);
        Cell empty3 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(4);

        Row row1 = new Row();
        List<Cell> cellsOfRow1 = new LinkedList<>();
        cellsOfRow1.add(empty1);
        cellsOfRow1.add(empty2);
        cellsOfRow1.add(new Cell().setData("α").setCssClass("bleuClaire"));
        cellsOfRow1.add(empty3);
        row1.setCells(cellsOfRow1);

        Row row2 = new Row();
        List<Cell> cellsOfRow2 = new LinkedList<>();
        cellsOfRow2.add(new Cell().setData("β").setCssClass("bleuClaire"));
        row2.setCells(cellsOfRow2);

        Row row3 = new Row();
        List<Cell> cellsOfRow3 = new LinkedList<>();
        cellsOfRow3.add(new Cell().setData("γ").setCssClass("bleuClaire"));
        cellsOfRow3.add(new Cell().setData("δ").setCssClass("bleuClaire"));
        row3.setCells(cellsOfRow3);

        Row row4 = new Row();
        List<Cell> cellsOfRow4 = new LinkedList<>();
        cellsOfRow4.add(empty1);
        cellsOfRow4.add(new Cell().setData("ε").setCssClass("bleuClaire"));
        cellsOfRow4.add(new Cell().setData("ζ").setCssClass("bleuClaire"));
        row4.setCells(cellsOfRow4);

        Row row5 = new Row();
        List<Cell> cellsOfRow5 = new LinkedList<>();
        cellsOfRow5.add(new Cell().setData("η").setCssClass("bleuClaire"));
        cellsOfRow5.add(new Cell().setData("θ").setCssClass("bleuClaire"));
        cellsOfRow5.add(new Cell().setData("ι").setCssClass("bleuClaire"));
        row5.setCells(cellsOfRow5);

        Row row6 = new Row();
        List<Cell> cellsOfRow6 = new LinkedList<>();
        cellsOfRow6.add(new Cell().setData("κ").setCssClass("bleuClaire"));
        cellsOfRow6.add(new Cell().setData("λ").setCssClass("bleuClaire"));
        cellsOfRow6.add(empty3);
        row6.setCells(cellsOfRow6);

        Row row7 = new Row();
        List<Cell> cellsOfRow7 = new LinkedList<>();
        cellsOfRow7.add(new Cell().setData("μ").setCssClass("bleuClaire"));
        cellsOfRow7.add(new Cell().setData("ν").setCssClass("bleuClaire"));
        cellsOfRow7.add(new Cell().setData("ξ").setCssClass("bleuClaire"));
        row7.setCells(cellsOfRow7);

        Row row8 = new Row();
        List<Cell> cellsOfRow8 = new LinkedList<>();
        cellsOfRow8.add(new Cell().setData("ο").setCssClass("bleuClaire"));
        cellsOfRow8.add(new Cell().setData("π").setCssClass("bleuClaire"));
        cellsOfRow8.add(new Cell().setData("ρ").setCssClass("bleuClaire"));
        row8.setCells(cellsOfRow8);

        Row row9 = new Row();
        List<Cell> cellsOfRow9 = new LinkedList<>();
        cellsOfRow9.add(new Cell().setData("Σ").setCssClass("bleuClaire"));
        cellsOfRow9.add(new Cell().setData("τ").setCssClass("bleuClaire"));
        cellsOfRow9.add(new Cell().setData("υ").setCssClass("bleuClaire"));
        row9.setCells(cellsOfRow9);

        Row row10 = new Row();
        List<Cell> cellsOfRow10 = new LinkedList<>();
        cellsOfRow10.add(new Cell().setData("φ").setCssClass("bleuClaire"));
        cellsOfRow10.add(new Cell().setData("χ").setCssClass("bleuClaire"));
        cellsOfRow10.add(new Cell().setData("ψ").setCssClass("bleuClaire"));
        cellsOfRow10.add(new Cell().setData("Ω").setCssClass("bleuClaire"));
        row10.setCells(cellsOfRow10);

        LinkedList<Row> header = new LinkedList<>();
        header.add(row1);
        header.add(row2);
        header.add(row3);
        header.add(row4);
        header.add(row5);
        header.add(row6);
        header.add(row7);
        header.add(row8);
        header.add(row9);
        header.add(row10);

        LinkedList<Object> tables = new LinkedList<>();
        final Table table = (header1, body, footer) -> header1.set(header.stream());
        tables.add(table);

        return tables;
    }

    private static LinkedList<Object> rowSpanResolutionSimpleTableFormatAlt3() {
        Cell empty1 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(3);
        Cell empty2 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(2);
        Cell empty3 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(4);
        Cell empty4 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setColSpan(2);

        Row row1 = new Row();
        List<Cell> cellsOfRow1 = new LinkedList<>();
        cellsOfRow1.add(empty1);
        cellsOfRow1.add(empty2);
        cellsOfRow1.add(new Cell().setData("α").setCssClass("bleuClaire"));
        cellsOfRow1.add(empty1);
        row1.setCells(cellsOfRow1);

        Row row2 = new Row();
        List<Cell> cellsOfRow2 = new LinkedList<>();
        cellsOfRow2.add(new Cell().setData("β").setCssClass("bleuClaire"));
        row2.setCells(cellsOfRow2);

        Row row3 = new Row();
        List<Cell> cellsOfRow3 = new LinkedList<>();
        cellsOfRow3.add(new Cell().setData("γ").setCssClass("bleuClaire"));
        cellsOfRow3.add(new Cell().setData("δ").setCssClass("bleuClaire"));
        row3.setCells(cellsOfRow3);

        Row row4 = new Row();
        List<Cell> cellsOfRow4 = new LinkedList<>();
        cellsOfRow4.add(empty1);
        cellsOfRow4.add(new Cell().setData("ε").setCssClass("bleuClaire"));
        cellsOfRow4.add(new Cell().setData("ζ").setCssClass("bleuClaire"));
        cellsOfRow4.add(new Cell().setData("ζ").setCssClass("bleuClaire"));
        row4.setCells(cellsOfRow4);

        Row row5 = new Row();
        List<Cell> cellsOfRow5 = new LinkedList<>();
        cellsOfRow5.add(new Cell().setData("η").setCssClass("bleuClaire"));
        cellsOfRow5.add(new Cell().setData("θ").setCssClass("bleuClaire"));
        cellsOfRow5.add(new Cell().setData("ι").setCssClass("bleuClaire"));
        row5.setCells(cellsOfRow5);

        Row row6 = new Row();
        List<Cell> cellsOfRow6 = new LinkedList<>();
        cellsOfRow6.add(new Cell().setData("κ").setCssClass("bleuClaire"));
        cellsOfRow6.add(new Cell().setData("λ").setCssClass("bleuClaire"));
        cellsOfRow6.add(empty3);
        row6.setCells(cellsOfRow6);

        Row row7 = new Row();
        List<Cell> cellsOfRow7 = new LinkedList<>();
        cellsOfRow7.add(new Cell().setData("μ").setCssClass("bleuClaire"));
        cellsOfRow7.add(new Cell().setData("ν").setCssClass("bleuClaire"));
        cellsOfRow7.add(new Cell().setData("ξ").setCssClass("bleuClaire"));
        row7.setCells(cellsOfRow7);

        Row row8 = new Row();
        List<Cell> cellsOfRow8 = new LinkedList<>();
        cellsOfRow8.add(new Cell().setData("ο").setCssClass("bleuClaire"));
        cellsOfRow8.add(new Cell().setData("π").setCssClass("bleuClaire"));
        cellsOfRow8.add(new Cell().setData("ρ").setCssClass("bleuClaire"));
        row8.setCells(cellsOfRow8);

        Row row9 = new Row();
        List<Cell> cellsOfRow9 = new LinkedList<>();
        cellsOfRow9.add(new Cell().setData("Σ").setCssClass("bleuClaire"));
        cellsOfRow9.add(new Cell().setData("τ").setCssClass("bleuClaire"));
        cellsOfRow9.add(new Cell().setData("υ").setCssClass("bleuClaire"));
        row9.setCells(cellsOfRow9);

        Row row10 = new Row();
        List<Cell> cellsOfRow10 = new LinkedList<>();
        cellsOfRow10.add(new Cell().setData("φ").setCssClass("bleuClaire"));
        cellsOfRow10.add(empty4);
        cellsOfRow10.add(new Cell().setData("Ω").setCssClass("bleuClaire"));
        row10.setCells(cellsOfRow10);

        LinkedList<Row> header = new LinkedList<>();
        header.add(row1);
        header.add(row2);
        header.add(row3);
        header.add(row4);
        header.add(row5);
        header.add(row6);
        header.add(row7);
        header.add(row8);
        header.add(row9);
        header.add(row10);

        LinkedList<Object> tables = new LinkedList<>();
        final Table table = (header1, body, footer) -> header1.set(header.stream());
        tables.add(table);

        return tables;
    }

    // utils
    private static String getTestCss() throws IOException {
        return IOUtils.toString(Objects.requireNonNull(ExcelFileGenerator.class.getClassLoader().getResourceAsStream("test.css")), StandardCharsets.UTF_8);
    }

    private static OutputStream getOutputStream(String fileName) throws IOException {
        return newOutputStream(of(EXPORT_PATH, "generated", fileName + ".xlsx"));
    }
}
