package lu.nyo.excel.renderer;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static java.nio.file.Files.newOutputStream;
import static java.nio.file.Path.of;
import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Utils {
    private static final String EXPORT_PATH = System.getProperty("user.dir");

    public static String getTestCss() throws IOException {
        return IOUtils.toString(Objects.requireNonNull(ExcelFileGenerator.class.getClassLoader().getResourceAsStream("test.css")), StandardCharsets.UTF_8);
    }

    public static OutputStream getOutputStream(String fileName) throws IOException {
        return newOutputStream(of(EXPORT_PATH, "generated", fileName + ".xlsx"));
    }

    public static void printTime(String testName,
                                  long nanoseconds) {
        long milliSeconds = MILLISECONDS.convert(nanoseconds, TimeUnit.NANOSECONDS);
        System.out.printf("%s: %s%n", testName, ofInstant(ofEpochMilli(milliSeconds), systemDefault()).format(ofPattern("mm:ss.SSS")));
    }
}
