package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.database.DataBase;
import ru.akirakozov.sd.refactoring.database.SqlLiteDataBase;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            DataBase dataBase = new SqlLiteDataBase(c);

            Server server = new Server(8081);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);

            context.addServlet(new ServletHolder(new AddProductServlet(dataBase)), "/add-product");
            context.addServlet(new ServletHolder(new GetProductsServlet(dataBase)),"/get-products");
            context.addServlet(new ServletHolder(new QueryServlet(dataBase)),"/query");

            server.start();
            server.join();
        }
    }
}
