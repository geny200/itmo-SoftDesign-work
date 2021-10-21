package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.DataBase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.Map;

public class BaseAddServletTest extends BaseServletTest {
    private final AddServletCall addProductServlet;

    public BaseAddServletTest(ServletProducer<HttpServlet> servlet, String endPoint, DataBase dataBase) {
        super(servlet, endPoint, dataBase);
        this.addProductServlet = new AddServletCall(dataBase);
    }

    public void addProduct(String name, Integer price) throws ServletException, IOException {
        addProductServlet.servletAssertCall(
                Map.of("name", name, "price", price.toString()),
                "OK" + System.lineSeparator()
        );
    }

    static class AddServletCall extends BaseServletTest {
        public AddServletCall(DataBase dataBase) {
            super(AddProductServlet::new, "add-product", dataBase);
        }
    }
}
