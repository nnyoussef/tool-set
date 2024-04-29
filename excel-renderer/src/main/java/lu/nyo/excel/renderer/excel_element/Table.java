package lu.nyo.excel.renderer.excel_element;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public interface Table {

    void tableContentInitializer(AtomicReference<Stream<Row>> header,
                                 AtomicReference<Stream<Row>> body,
                                 AtomicReference<Stream<Row>> footer);
}
