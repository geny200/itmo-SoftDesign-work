package ru.akirakozov.sd.refactoring.formatter;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

public class HtmlFormatter {
    private final List<String> lines = new LinkedList<>();

    public void body(String line) {
        lines.add(line);
    }

    public void write(PrintWriter writer) {
        writer.println("<html><body>");
        lines.forEach(writer::println);
        writer.println("</body></html>");
    }
}
