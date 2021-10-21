package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Test;
import ru.akirakozov.sd.refactoring.database.InMemoryDataBase;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryServletTest extends BaseAddServletTest {

    public QueryServletTest() {
        super(QueryServlet::new, new InMemoryDataBase());
    }

    void query(String query, List<String> result) throws ServletException, IOException {
        ArrayList<String> list = new ArrayList<>();
        list.add("<html><body>");
        list.addAll(result);
        list.add("</body></html>" + System.lineSeparator());

        servletAssertCall(
                Map.of("command", query),
                String.join(System.lineSeparator(), list)
        );
    }

    void queryCount(Integer result) throws ServletException, IOException {
        query("count", List.of("Number of products: ", result.toString()));
    }

    void querySum(Integer result) throws ServletException, IOException {
        query("sum", List.of("Summary price: ", result.toString()));
    }

    void queryMax(String product) throws ServletException, IOException {
        query("max", List.of("<h1>Product with max price: </h1>", product + "</br>"));
    }

    void queryMin(String product) throws ServletException, IOException {
        query("min", List.of("<h1>Product with min price: </h1>", product + "</br>"));
    }

    @Test
    public void queryUnknownCommand() throws ServletException, IOException {
        String command = "The Ultimate Question of Life, the Universe, and Everything";
        servletAssertCall(
                Map.of("command", command),
                "Unknown command: " + command + System.lineSeparator()
        );
    }

    @Test
    public void queryEmptySum() throws ServletException, IOException {
        querySum(0);
    }

    @Test
    public void queryEmptyCount() throws ServletException, IOException {
        queryCount(0);
    }

    @Test
    public void queryEmptyMax() throws ServletException, IOException {
        query("max", List.of("<h1>Product with max price: </h1>"));
    }

    @Test
    public void queryEmptyMin() throws ServletException, IOException {
        query("min", List.of("<h1>Product with min price: </h1>"));
    }

    @Test
    public void queryOneProductSum() throws ServletException, IOException {
        addProduct("iphone6", 300);
        querySum(300);
    }

    @Test
    public void queryOneProductCount() throws ServletException, IOException {
        addProduct("iphone6", 300);
        queryCount(1);
    }

    @Test
    public void queryOneProductMax() throws ServletException, IOException {
        addProduct("iphone6", 300);
        queryMax("iphone6\t" + 300);
    }

    @Test
    public void queryOneProductMin() throws ServletException, IOException {
        addProduct("iphone6", 300);
        queryMin("iphone6\t" + 300);
    }

    @Test
    public void queryTwoProductSum() throws ServletException, IOException {
        addProduct("iphone6", 300);
        addProduct("iphone6", -100);
        querySum(200);
    }

    @Test
    public void queryTwoProductCount() throws ServletException, IOException {
        addProduct("iphone6", 300);
        addProduct("iphone6", -100);
        queryCount(2);
    }

    @Test
    public void queryTwoProductMax() throws ServletException, IOException {
        addProduct("iphone6", 300);
        addProduct("iphone6", -100);
        queryMax("iphone6\t" + 300);
    }

    @Test
    public void queryTwoProductMin() throws ServletException, IOException {
        addProduct("iphone6", 300);
        addProduct("iphone6", -100);
        queryMin("iphone6\t" + -100);
    }
}
