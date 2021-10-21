package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.DataBase;
import ru.akirakozov.sd.refactoring.formatter.HtmlFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends AbstractBaseServlet {

    public QueryServlet(DataBase dataBase) {
        super(dataBase);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        HtmlFormatter formatter = new HtmlFormatter();

        if ("max".equals(command)) {
            try {
                formatter.body("<h1>Product with max price: </h1>");
                dataBase.maxInProducts()
                        .ifPresent(product -> formatter.body(product.toHtml()));

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("min".equals(command)) {
            try {
                formatter.body("<h1>Product with min price: </h1>");
                dataBase.minInProducts()
                        .ifPresent(product -> formatter.body(product.toHtml()));

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("sum".equals(command)) {
            try {
                long sum = dataBase.sumProducts();
                formatter.body("Summary price: ");
                formatter.body(Long.toString(sum));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("count".equals(command)) {
            try {
                long countProducts = dataBase.countProducts();
                formatter.body("Number of products: ");
                formatter.body(Long.toString(countProducts));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            response.getWriter().println("Unknown command: " + command);
            setResponse(response);
            return;
        }
        formatter.write(response.getWriter());
        setResponse(response);
    }

}
