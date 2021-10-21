package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.Map;

public class BaseAddServletTest extends BaseServletTest {
    private final AddServletCall addProductServlet = new AddServletCall();

    public BaseAddServletTest(HttpServlet servlet, String endPoint) {
        super(servlet, endPoint);
    }

    public void addProduct(String name, Integer price) throws ServletException, IOException {
        addProductServlet.servletAssertCall(
                Map.of("name", name, "price", price.toString()),
                "OK" + System.lineSeparator()
        );
    }

    static class AddServletCall extends BaseServletTest {
        public AddServletCall() {
            super(new AddProductServlet(), "add-product");
        }
    }
}
