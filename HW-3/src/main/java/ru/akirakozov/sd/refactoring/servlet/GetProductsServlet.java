package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.DataBase;
import ru.akirakozov.sd.refactoring.formatter.HtmlFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends AbstractBaseServlet {

    public GetProductsServlet(DataBase dataBase) {
        super(dataBase);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HtmlFormatter formatter = new HtmlFormatter();
        dataBase.selectAllFromProduct().forEach(formatter::toBody);

        formatter.write(response.getWriter());
        setResponse(response);
    }
}
