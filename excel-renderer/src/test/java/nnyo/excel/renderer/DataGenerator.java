package nnyo.excel.renderer;

import nnyo.excel.renderer.excel_element.Cell;
import nnyo.excel.renderer.excel_element.Row;
import nnyo.excel.renderer.excel_element.Table;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toCollection;

public class DataGenerator {
    private static final int ROWS_COUNT_IN_BODY = 1_000;
    private static final int CELL_LIST_MULTIPLIER = 1;

    public static LinkedList<Object> simpleTableFormatData() {

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
                    List<Cell> cells = new LinkedList<>();

                    for (int x = 0; x < CELL_LIST_MULTIPLIER; x += 1)
                        cells.addAll(asList(data1, data1, data1, data1, data2, data1, data1, data1, data1));
                    return new Row().setCells(cells);
                }).collect(toCollection(LinkedList::new));

        final List<Row> footerRow = new LinkedList<>();
        final Cell t1 = new Cell().setData("2020");
        final Cell t2 = new Cell().setData("2021");
        final Cell t3 = new Cell().setData("2022");
        final Cell t4 = new Cell().setData("2023");
        final Cell t5 = new Cell().setData("Total").setColSpan(2);
        footerRow.add(new Row().setCells(asList(t1, t2, t3, t4, t5, t1, t2, t3, t4)));

        LinkedList<Object> tables = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            final Table table = (header, body1, footer) -> {
                header.set(new LinkedList<>(headerRow).stream());
                body1.set(new LinkedList<>(body).stream());
                footer.set(new LinkedList<>(footerRow).stream());
            };
            tables.add(table);
        }

        return tables;
    }

    public static LinkedList<Object> rowSpanResolutionWithColSpanResolutionSimpleTableFormat() {
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

    public static LinkedList<Object> rowSpanResolutionSimpleTableFormat1() {
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

    public static LinkedList<Object> rowSpanResolutionSimpleTableFormat2() {
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

    public static LinkedList<Object> rowSpanResolutionSimpleTableFormat3() {
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

    public static LinkedList<Object> rowSpanResolutionSimpleTableFormat4() {
        Cell empty1 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(3);
        Cell empty2 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(2);
        Cell empty3 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(4);
        Cell empty4 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(1).setColSpan(3);
        Cell empty5 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(9);

        Row row1 = new Row();
        List<Cell> cellsOfRow1 = new LinkedList<>();
        cellsOfRow1.add(empty1);
        cellsOfRow1.add(empty2);
        cellsOfRow1.add(new Cell().setData("α").setCssClass("bleuClaire"));
        cellsOfRow1.add(empty1);
        cellsOfRow1.add(empty5);
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

    public static LinkedList<Object> rowSpanResolutionSimpleTableFormat5() {
        Cell empty1 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(3);
        Cell empty2 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(2);
        Cell empty3 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(4);
        Cell empty4 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(1).setColSpan(3);
        Cell empty5 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(9);
        Cell empty6 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setColSpan(5);

        Row row1 = new Row();
        List<Cell> cellsOfRow1 = new LinkedList<>();
        cellsOfRow1.add(empty1);
        cellsOfRow1.add(empty2);
        cellsOfRow1.add(new Cell().setData("α").setCssClass("bleuClaire"));
        cellsOfRow1.add(empty1);
        cellsOfRow1.add(empty5);
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
        cellsOfRow10.add(new Cell().setData("Ω").setCssClass("bleuClaire"));
        row10.setCells(cellsOfRow10);

        Row row11 = new Row();
        List<Cell> cellsOfRow11 = new LinkedList<>();
        cellsOfRow11.add(empty6);
        row11.setCells(cellsOfRow11);

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
        header.add(row11);

        LinkedList<Object> tables = new LinkedList<>();
        final Table table = (header1, body, footer) -> header1.set(header.stream());
        tables.add(table);

        return tables;
    }

    public static LinkedList<Object> rowSpanResolutionSimpleTableFormat6() {
        Cell empty1 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(3);

        Row row1 = new Row();
        List<Cell> cellsOfRow1 = new LinkedList<>();
        cellsOfRow1.add(empty1);
        cellsOfRow1.add(empty1);
        row1.setCells(cellsOfRow1);

        Row row2 = new Row();
        List<Cell> cellsOfRow2 = new LinkedList<>();
        cellsOfRow2.add(new Cell().setData("α").setCssClass("bleuClaire"));
        cellsOfRow2.add(new Cell().setData("α").setCssClass("bleuClaire"));
        row2.setCells(cellsOfRow2);

        LinkedList<Row> header = new LinkedList<>();
        header.add(row1);
        header.add(row2);
        header.add(row2);
        header.add(row2);

        LinkedList<Object> tables = new LinkedList<>();
        final Table table = (header1, body, footer) -> header1.set(header.stream());
        tables.add(table);

        return tables;
    }

    public static LinkedList<Object> rowSpanResolutionSimpleTableFormat7() {
        Cell empty1 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(3);
        Cell empty2 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(2);
        Cell empty3 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(5);

        Row row1 = new Row();
        List<Cell> cellsOfRow1 = new LinkedList<>();
        cellsOfRow1.add(empty1);
        cellsOfRow1.add(empty1);
        cellsOfRow1.add(empty2);
        cellsOfRow1.add(empty3);
        cellsOfRow1.add(new Cell().setData("α").setCssClass("bleuClaire"));
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
        cellsOfRow4.add(new Cell().setData("γ").setCssClass("bleuClaire"));
        cellsOfRow4.add(new Cell().setData("δ").setCssClass("bleuClaire"));
        cellsOfRow4.add(new Cell().setData("γ").setCssClass("bleuClaire"));
        cellsOfRow4.add(new Cell().setData("δ").setCssClass("bleuClaire"));
        row4.setCells(cellsOfRow4);

        LinkedList<Row> header = new LinkedList<>();
        header.add(row1);
        header.add(row2);
        header.add(row3);
        header.add(row4);
        header.add(row4);

        LinkedList<Object> tables = new LinkedList<>();
        final Table table = (header1, body, footer) -> header1.set(header.stream());
        tables.add(table);

        return tables;
    }

    public static LinkedList<Object> rowSpanResolutionSimpleTableFormat8() {
        Cell empty1 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(2);
        Cell empty2 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(4);
        Cell empty3 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(5);
        Cell empty4 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(3);

        Row row1 = new Row();
        List<Cell> cellsOfRow1 = new LinkedList<>();
        cellsOfRow1.add(empty1);
        cellsOfRow1.add(empty1);
        cellsOfRow1.add(empty2);
        cellsOfRow1.add(empty3);
        cellsOfRow1.add(new Cell().setData("α").setCssClass("bleuClaire"));
        row1.setCells(cellsOfRow1);

        Row row2 = new Row();
        List<Cell> cellsOfRow2 = new LinkedList<>();
        cellsOfRow2.add(new Cell().setData("β").setCssClass("bleuClaire"));
        row2.setCells(cellsOfRow2);

        Row row3 = new Row();
        List<Cell> cellsOfRow3 = new LinkedList<>();
        cellsOfRow3.add(new Cell().setData("γ").setCssClass("bleuClaire"));
        cellsOfRow3.add(empty4);
        cellsOfRow3.add(new Cell().setData("δ").setCssClass("bleuClaire"));
        row3.setCells(cellsOfRow3);

        Row row4 = new Row();
        List<Cell> cellsOfRow4 = new LinkedList<>();
        cellsOfRow4.add(new Cell().setData("γ").setCssClass("bleuClaire"));
        cellsOfRow4.add(new Cell().setData("α").setCssClass("bleuClaire"));
        row4.setCells(cellsOfRow4);

        Row row5 = new Row();
        List<Cell> cellsOfRow5 = new LinkedList<>();
        cellsOfRow5.add(new Cell().setData("γ").setCssClass("bleuClaire"));
        cellsOfRow5.add(new Cell().setData("δ").setCssClass("bleuClaire"));
        cellsOfRow5.add(new Cell().setData("δ").setCssClass("bleuClaire"));
        row5.setCells(cellsOfRow5);

        LinkedList<Row> header = new LinkedList<>();
        header.add(row1);
        header.add(row2);
        header.add(row3);
        header.add(row4);
        header.add(row5);

        LinkedList<Object> tables = new LinkedList<>();
        final Table table = (header1, body, footer) -> header1.set(header.stream());
        tables.add(table);

        return tables;
    }

    public static LinkedList<Object> rowSpanResolutionSimpleTableFormat9() {
        Cell empty4 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(3);

        Row row1 = new Row();
        List<Cell> cellsOfRow1 = new LinkedList<>();

        cellsOfRow1.add(new Cell().setData("α").setCssClass("bleuClaire"));
        cellsOfRow1.add(new Cell().setData("α").setCssClass("bleuClaire"));
        cellsOfRow1.add(new Cell().setData("α").setCssClass("bleuClaire"));
        cellsOfRow1.add(empty4);
        row1.setCells(cellsOfRow1);

        Row row2 = new Row();
        List<Cell> cellsOfRow2 = new LinkedList<>();
        cellsOfRow2.add(new Cell().setData("β").setCssClass("bleuClaire"));
        cellsOfRow2.add(new Cell().setData("β").setCssClass("bleuClaire"));
        cellsOfRow2.add(new Cell().setData("β").setCssClass("bleuClaire"));
        row2.setCells(cellsOfRow2);

        Row row3 = new Row();
        List<Cell> cellsOfRow3 = new LinkedList<>();
        cellsOfRow3.add(new Cell().setData("γ").setCssClass("bleuClaire"));
        cellsOfRow3.add(new Cell().setData("γ").setCssClass("bleuClaire"));
        cellsOfRow3.add(new Cell().setData("γ").setCssClass("bleuClaire"));
        row3.setCells(cellsOfRow3);


        LinkedList<Row> header = new LinkedList<>();
        header.add(row1);
        header.add(row2);
        header.add(row3);

        LinkedList<Object> tables = new LinkedList<>();
        final Table table = (header1, body, footer) -> header1.set(header.stream());
        tables.add(table);

        return tables;
    }

    public static LinkedList<Object> rowSpanResolutionSimpleTableFormat10() {
        Cell empty4 = new Cell().setData("\uD83D\uDE35").setCssClass("bleuFonce").setRowSpan(2);

        Row row1 = new Row();
        List<Cell> cellsOfRow1 = new LinkedList<>();
        cellsOfRow1.add(empty4);
        cellsOfRow1.add(new Cell().setData("α").setCssClass("bleuClaire"));
        row1.setCells(cellsOfRow1);

        Row row2 = new Row();
        List<Cell> cellsOfRow2 = new LinkedList<>();
        cellsOfRow2.add(new Cell().setData("β").setCssClass("bleuClaire"));
        row2.setCells(cellsOfRow2);

        Row row3 = new Row();
        List<Cell> cellsOfRow3 = new LinkedList<>();
        cellsOfRow3.add(new Cell().setData("γ").setCssClass("bleuClaire"));
        cellsOfRow3.add(new Cell().setData("γ").setCssClass("bleuClaire"));
        row3.setCells(cellsOfRow3);


        LinkedList<Row> header = new LinkedList<>();
        header.add(row1);
        header.add(row2);
        header.add(row3);

        LinkedList<Object> tables = new LinkedList<>();
        final Table table = (header1, body, footer) -> header1.set(header.stream());
        tables.add(table);

        return tables;
    }

    public static LinkedList<Object> rowSpanResolutionSimpleTableFormat() {
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
}
