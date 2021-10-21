package ru.akirakozov.sd.refactoring.database;

import ru.akirakozov.sd.refactoring.domain.Product;

import java.util.*;

public class InMemoryDataBase implements DataBase {
    private final List<Product> dataBase = new LinkedList<>();

    @Override
    public void insertIntoProduct(Product product) {
        dataBase.add(product);
    }

    @Override
    public List<Product> selectAllFromProduct() {
        return new ArrayList<>(dataBase);
    }

    @Override
    public int countProducts() {
        return dataBase.size();
    }

    @Override
    public long sumProducts() {
        return dataBase
                .stream()
                .map(Product::getPrice)
                .reduce(0L, (Long::sum));
    }

    @Override
    public Optional<Product> maxInProducts() {
        return dataBase
                .stream()
                .max(Comparator.comparingLong(Product::getPrice));
    }

    @Override
    public Optional<Product> minInProducts() {
        return dataBase
                .stream()
                .min(Comparator.comparingLong(Product::getPrice));
    }

    @Override
    public void dropProducts() {
        dataBase.removeAll(selectAllFromProduct());
    }
}
