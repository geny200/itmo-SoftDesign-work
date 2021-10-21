package ru.akirakozov.sd.refactoring.database;

import ru.akirakozov.sd.refactoring.domain.Product;

import java.util.List;
import java.util.Optional;

public interface DataBase {
    public void insertIntoProduct(Product product);

    public List<Product> selectAllFromProduct();

    public int countProducts();

    public long sumProducts();

    public Optional<Product> maxInProducts();

    public Optional<Product> minInProducts();

    public void dropProducts();
}
