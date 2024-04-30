package lu.nyo.excel.renderer;

import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;
import lu.nyo.excel.renderer.constantes.CssConstantes;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleSheet;

import java.awt.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.poi.ss.usermodel.BorderStyle.NONE;
import static org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND;
import static org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT;
import static org.apache.poi.ss.usermodel.HorizontalAlignment.RIGHT;
import static org.apache.poi.ss.usermodel.VerticalAlignment.CENTER;

public class CellStyleProcessor {

    private final Map<String, XSSFCellStyle> cacheCellStyle = new HashMap<>(30);

    private final SXSSFWorkbook xssfWorkbook;

    private final Map<String, Map<String, String>> cssRuleDeclaration;

    private CellStyleProcessor(SXSSFWorkbook xssfWorkbook,
                               Map<String, Map<String, String>> cssRuleDeclaration) {
        this.xssfWorkbook = xssfWorkbook;
        this.cssRuleDeclaration = cssRuleDeclaration;
    }

    //-------------------------- Init One Time - Public Usage -------------------------------------------------
    public static CellStyleProcessor init(String css,
                                          SXSSFWorkbook xssfWorkbook) throws IOException {
        final Map<String, Map<String, String>> cssRuleDeclaration = new HashMap<>(30);

        final InputSource inputSource = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS3());

        final CSSStyleSheet styleSheet1 = parser.parseStyleSheet(inputSource, null, null);

        final CSSRuleList rules = styleSheet1.getCssRules();
        for (int i = 0; i < rules.getLength(); i++) {
            final CSSRule rule = rules.item(i);

            final String ruleName = rule.getCssText().split("[{]")[0].strip().trim();
            cssRuleDeclaration.put(ruleName, new HashMap<>(12));

            String ruleDeclaration = rule.getCssText().replace("}", "");
            ruleDeclaration = ruleDeclaration.split("[{]")[1].trim().strip();

            final InputSource source = new InputSource(new StringReader(ruleDeclaration));
            final CSSStyleDeclaration declaration = parser.parseStyleDeclaration(source);

            for (int j = 0; j < declaration.getLength(); j++) {
                final String propName = declaration.item(j).trim().strip();
                final String propertyValue = declaration.getPropertyValue(propName);
                cssRuleDeclaration.get(ruleName).put(propName, propertyValue.trim().strip());
            }
        }
        return new CellStyleProcessor(xssfWorkbook, cssRuleDeclaration);
    }

    //-------------------------- Public Usage -------------------------------------------------

    public XSSFCellStyle createStyle(String css) {
        css = css.trim().strip();

        if (cacheCellStyle.containsKey(css)) {
            return cacheCellStyle.get(css);
        }

        final XSSFCellStyle xssfCellStyle = (XSSFCellStyle) this.xssfWorkbook.createCellStyle();

        final Map<String, String> cssProperties = this.cssRuleDeclaration.getOrDefault(css.trim().strip(), CssConstantes.DEFAULT_CSS_PROPERTIES);

        createBorderFromCssInstructions(cssProperties, xssfCellStyle);
        createAlignementFromCssInstruction(cssProperties, xssfCellStyle);
        createCellBackgroundFromCssInstruction(cssProperties, xssfCellStyle);
        createFontFromCssInstruction(cssProperties, xssfCellStyle);

        cacheCellStyle.put(css, xssfCellStyle);
        return xssfCellStyle;
    }

    //-------------------------- Private Usage -------------------------------------------------
    private void createBorderFromCssInstructions(final Map<String, String> instructions,
                                                 final XSSFCellStyle xssfCellStyle) {
        instructions.forEach((prop, value) -> {
            if (prop.equals(CssConstantes.BORDER)) {
                final XSSFColor xssfColor = getBorderColor(value);
                xssfCellStyle.setBorderBottom(getBorderStyle(value));
                xssfCellStyle.setBottomBorderColor(xssfColor);

                xssfCellStyle.setBorderTop(getBorderStyle(value));
                xssfCellStyle.setTopBorderColor(xssfColor);

                xssfCellStyle.setBorderLeft(getBorderStyle(value));
                xssfCellStyle.setLeftBorderColor(xssfColor);

                xssfCellStyle.setBorderRight(getBorderStyle(value));
                xssfCellStyle.setRightBorderColor(xssfColor);

            } else if (prop.contains(CssConstantes.BORDER_DASH)) {
                final String side = prop.split(CssConstantes.DASH)[1];
                final XSSFColor xssfColor = getBorderColor(value);
                switch (side) {
                    case CssConstantes.LEFT -> {
                        xssfCellStyle.setBorderBottom(getBorderStyle(value));
                        xssfCellStyle.setBottomBorderColor(xssfColor);
                    }
                    case CssConstantes.TOP -> {
                        xssfCellStyle.setBorderTop(getBorderStyle(value));
                        xssfCellStyle.setTopBorderColor(xssfColor);
                    }
                    case CssConstantes.BOTTOM -> {
                        xssfCellStyle.setBorderLeft(getBorderStyle(value));
                        xssfCellStyle.setLeftBorderColor(xssfColor);
                    }
                    default -> {
                        xssfCellStyle.setBorderRight(getBorderStyle(value));
                        xssfCellStyle.setRightBorderColor(xssfColor);
                    }
                }
            }
        });
    }

    private BorderStyle getBorderStyle(String css) {
        final String borderPropertiesWithoutColor = Arrays.stream(css.split("[ ]"))
                .filter(e -> e.contains("1p") || e.contains("2p") || e.contains("sol") || e.contains("das"))
                .collect(joining(" "));
        return CssConstantes.CSS_BORDER_VALUE_TO_BORDER_STYLE_MAP.getOrDefault(borderPropertiesWithoutColor, NONE);

    }

    private XSSFColor getBorderColor(String css) {
        final XSSFColor xssfColor = new XSSFColor();
        final String[] borderStyleComposition = css.split(SPACE);
        final String borderColor = Arrays.stream(borderStyleComposition)
                .filter(e -> e.contains(CssConstantes.HASHBANG))
                .findFirst()
                .orElse(CssConstantes.NORMAL_GRAY_BORDER_COLOR_HEX);
        xssfColor.setARGBHex(borderColor.replace(CssConstantes.HASHBANG, EMPTY));
        return xssfColor;
    }

    private HorizontalAlignment getHorizontalAlignmentFromString(String textAlignment) {
        return switch (ofNullable(textAlignment).orElse(EMPTY)) {
            case CssConstantes.TEXT_ALIGN_CENTER -> HorizontalAlignment.CENTER;
            case CssConstantes.TEXT_ALIGN_RIGHT -> RIGHT;
            default -> LEFT;
        };
    }

    private XSSFColor getXSSFColorFromRgb(String rgb) {
        final XSSFColor xssfColor = new XSSFColor();
        xssfColor.setARGBHex(CssConstantes.HEX_WHITE);

        if (StringUtils.isEmpty(rgb)) {
            xssfColor.setARGBHex(CssConstantes.HEX_WHITE);
        } else if (rgb.contains("rgb")) {
            rgb = rgb.replace("rgb(", "").replace(")", "");
            String[] rgbArr = rgb.split(",");

            final int r = parseInt(rgbArr[0].trim().strip());
            final int g = parseInt(rgbArr[1].trim().strip());
            final int b = parseInt(rgbArr[2].trim().strip());

            final Color color = new Color(r, g, b);
            final String buf = Integer.toHexString(color.getRGB());
            final String hex = buf.substring(buf.length() - 6);
            xssfColor.setARGBHex(hex);
        }
        return xssfColor;
    }

    private boolean isBold(Map<String, String> cssProperties) {
        return ofNullable(cssProperties.get(CssConstantes.FONT_WEIGHT))
                .map(fw -> fw.contains(CssConstantes.BOLD))
                .orElse(false);
    }

    private void createAlignementFromCssInstruction(Map<String, String> instructions,
                                                    XSSFCellStyle xssfCellStyle) {
        final String textAlignment = instructions.get(CssConstantes.TEXT_ALIGN);
        xssfCellStyle.setVerticalAlignment(CENTER);
        xssfCellStyle.setAlignment(getHorizontalAlignmentFromString(textAlignment));
    }

    private void createCellBackgroundFromCssInstruction(Map<String, String> instructions,
                                                        XSSFCellStyle xssfCellStyle) {
        final String cellBackground = instructions.get(CssConstantes.BACKGROUND);
        xssfCellStyle.setFillForegroundColor(getXSSFColorFromRgb(cellBackground));
        xssfCellStyle.setFillPattern(SOLID_FOREGROUND);
    }

    private void createFontFromCssInstruction(Map<String, String> instructions,
                                              XSSFCellStyle xssfCellStyle) {
        final XSSFColor textColor = getXSSFColorFromRgb(instructions.get(CssConstantes.COLOR));
        boolean isBold = isBold(instructions);

        final XSSFFont xssfFont = ((XSSFFont) xssfWorkbook.createFont());
        xssfFont.setColor(textColor);
        xssfFont.setBold(isBold);
        xssfCellStyle.setFont(xssfFont);
    }
}