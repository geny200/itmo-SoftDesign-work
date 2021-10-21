package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Test;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GetProductServletTest extends BaseAddServletTest {

    public GetProductServletTest() {
        super(new GetProductsServlet(), "get-products");
    }

    void getProducts(List<String> products) throws ServletException, IOException {
        ArrayList<String> list = new ArrayList<>();
        list.add("<html><body>");
        list.addAll(products.stream().map(a -> a + "</br>").collect(Collectors.toList()));
        list.add("</body></html>" + System.lineSeparator());
        servletAssertCall(
                Collections.emptyMap(),
                String.join(System.lineSeparator(), list)
        );
    }

    @Test
    public void getEmpty() throws ServletException, IOException {
        getProducts(Collections.emptyList());
    }

    @Test
    public void getOneProduct() throws ServletException, IOException {
        addProduct("iphone6", 300);
        getProducts(List.of("iphone6\t" + 300));
    }

    @Test
    public void getTwoProduct() throws ServletException, IOException {
        addProduct("iphone7", 700);
        addProduct("iphone6", 300);
        getProducts(List.of("iphone7\t" + 700, "iphone6\t" + 300));
    }

    @Test
    public void getTreeProductWhichTwoWithSameName() throws ServletException, IOException {
        addProduct("iphone6", 300);
        addProduct("iphone7", 700);
        addProduct("iphone6", 400);

        getProducts(List.of("iphone6\t" + 300, "iphone7\t" + 700, "iphone6\t" + 400));
    }

    @Test
    public void getProductAndAdd() throws ServletException, IOException {
        addProduct("iphone6", 300);
        getProducts(List.of("iphone6\t" + 300));

        addProduct("iphone0", 0);
        getProducts(List.of("iphone6\t" + 300, "iphone0\t" + 0));
    }
}
