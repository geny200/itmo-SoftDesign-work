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

        switch (command) {
            case "max":
                formatter.toBody("<h1>Product with max price: </h1>");
                dataBase.maxInProducts().ifPresent(formatter::toBody);
                break;

            case "min":
                formatter.toBody("<h1>Product with min price: </h1>");
                dataBase.minInProducts().ifPresent(formatter::toBody);
                break;

            case "sum":
                long sum = dataBase.sumProducts();
                formatter.toBody("Summary price: ");
                formatter.toBody(sum);
                break;

            case "count":
                long countProducts = dataBase.countProducts();
                formatter.toBody("Number of products: ");
                formatter.toBody(countProducts);
                break;

            default:
                response.getWriter().println("Unknown command: " + command);
                setResponse(response);
                return;
        }

        formatter.write(response.getWriter());
        setResponse(response);
    }

}
