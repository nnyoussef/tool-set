package lu.nyo.excel.renderer.utils;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNumericSpace;

public final class CellDataUtils {

    private CellDataUtils() {
    }

    public static void setData(Object data,
                               SXSSFCell xssfCell) {
        final String textToInsert = ofNullable(data)
                .map(Object::toString)
                .orElse("");
        xssfCell.setCellValue(textToInsert);

        if (data != null) {
            if (isDataNumeric(data)) {
                xssfCell.setCellType(CellType.NUMERIC);
            } else if (!data.toString().isEmpty() && !data.toString().isBlank())
                xssfCell.setCellType(CellType.STRING);
            else
                xssfCell.setCellType(CellType.BLANK);
        } else {
            xssfCell.setCellType(CellType.BLANK);
        }
    }

    private static boolean isDataNumeric(Object data) {
        final String cellText = data.toString();
        final boolean isNumeric = isNumericSpace(cellText.replace(".", ""));
        final boolean containsDot = cellText.contains(".");
        final boolean isZero = cellText.equals("0");
        return (isNumeric && containsDot) || isZero;
    }
}
