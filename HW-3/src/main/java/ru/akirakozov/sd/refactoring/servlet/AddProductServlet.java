package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.DataBase;
import ru.akirakozov.sd.refactoring.domain.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author akirakozov
 */
public class AddProductServlet extends AbstractBaseServlet {

    public AddProductServlet(DataBase dataBase) {
        super(dataBase);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();

        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));
        dataBase.insertIntoProduct(new Product(name, price));

        writer.println("OK");
        setResponse(response);
    }
}
