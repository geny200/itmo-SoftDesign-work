package ru.akirakozov.sd.refactoring.formatter;

import ru.akirakozov.sd.refactoring.domain.Product;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

public class HtmlFormatter {
    private final List<Object> lines = new LinkedList<>();

    public void toBody(Object line) {
        lines.add(line);
    }

    public void toBody(Product product) {
        lines.add(product.toHtml());
    }

    public void write(PrintWriter writer) {
        writer.println("<html><body>");
        lines.forEach(writer::println);
        writer.println("</body></html>");
    }
}
