package ru.akirakozov.sd.refactoring.servlet;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mockito;
import ru.akirakozov.sd.refactoring.database.DataBase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import static org.mockito.Mockito.*;

public class BaseServletTest extends Assert {
    private final HttpServlet servlet;
    private final String endPoint;
    private final DataBase dataBase;

    public BaseServletTest(ServletProducer<HttpServlet> servlet, String endPoint, DataBase dataBase) {
        this.servlet = servlet.apply(dataBase);
        this.endPoint = endPoint;
        this.dataBase = dataBase;
    }

    @After
    public void dropTable() {
        dataBase.dropProducts();
    }

    public void servletAssertCall(Map<String, String> req, String res)
            throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);

        when(request.getMethod()).thenReturn("GET");
        when(response.getWriter()).thenReturn(printWriter);

        for (Map.Entry<String, String> entry : req.entrySet()) {
            when(request.getParameter(entry.getKey())).thenReturn(entry.getValue());
        }

        servlet.service(request, response);

        Mockito.verify(request, times(req.size()))
                .getParameter(Mockito.any());

        verify(response, times(1))
                .setStatus(HttpServletResponse.SC_OK);

        verify(response, times(1))
                .setContentType("text/html");

        verify(response, atLeastOnce()).getWriter();

        assertEquals(writer.toString(), res);
    }

    @FunctionalInterface
    public interface ServletProducer<R> {
        R apply(DataBase t);
    }
}
