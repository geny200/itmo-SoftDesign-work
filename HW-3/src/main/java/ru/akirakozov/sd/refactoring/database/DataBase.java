package ru.akirakozov.sd.refactoring.database;

import ru.akirakozov.sd.refactoring.domain.Product;

import java.util.List;

public interface DataBase {
    public void insertIntoProduct(Product product);

    public List<Product> selectAllFromProduct();

    public int countProducts();

    public long sumProducts();

    public Product maxInProducts();

    public Product minInProducts();

    public void dropProducts();
}
