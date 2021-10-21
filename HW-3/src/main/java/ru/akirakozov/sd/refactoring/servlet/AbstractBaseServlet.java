package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.DataBase;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

/**
 * @author akirakozov
 */
public abstract class AbstractBaseServlet extends HttpServlet {
    protected final DataBase dataBase;

    public AbstractBaseServlet(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    protected void setResponse(HttpServletResponse response) {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
