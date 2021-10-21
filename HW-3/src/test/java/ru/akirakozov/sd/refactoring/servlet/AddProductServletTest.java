package ru.akirakozov.sd.refactoring.servlet;

import org.junit.Test;

import javax.servlet.ServletException;
import java.io.IOException;

public class AddProductServletTest extends BaseAddServletTest {
    public AddProductServletTest() {
        super(new AddProductServlet(), "add-product");
    }

    @Test
    public void addOneProduct() throws ServletException, IOException {
        addProduct("iphone6", 300);
    }

    @Test
    public void addProductWithNegativePrice() throws ServletException, IOException {
        addProduct("iphone6", -100);
    }

    @Test
    public void addTwoProducts() throws ServletException, IOException {
        addProduct("iphone6", 300);
        addProduct("iphone7", 400);
    }

    @Test
    public void addTwoProductsWithSameName() throws ServletException, IOException {
        addProduct("iphone6", 300);
        addProduct("iphone6", 300);
    }
}
