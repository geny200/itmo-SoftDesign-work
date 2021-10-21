package ru.akirakozov.sd.refactoring.database;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import ru.akirakozov.sd.refactoring.domain.Product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SqlLiteDataBase implements DataBase {
    private final Connection connectionToDataBase;
    private final Multiset<Product> dataBase = HashMultiset.create();

    public SqlLiteDataBase(Connection connectionToDataBase) {
        this.connectionToDataBase = connectionToDataBase;
        String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)";
        update(sql);
    }

    private void sqlCall(SQLFunction<Statement> sqlExecutor) {
        try (Statement dataBaseStatement = connectionToDataBase.createStatement()) {
            sqlExecutor.apply(dataBaseStatement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void readLineByLineSqlCall(String sql, SQLFunction<ResultSet> writer) {
        sqlCall(stmt -> {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next())
                writer.apply(rs);
        });
    }

    private void update(String sql) {
        sqlCall(stmt -> stmt.executeUpdate(sql));
    }

    private int queryInt(String sql) {
        AtomicInteger result = new AtomicInteger();
        readLineByLineSqlCall(sql,
                reader -> result.set(reader.getInt(1))
        );

        return result.get();
    }


    private List<Product> queryProducts(String sql) {
        LinkedList<Product> products = new LinkedList<>();

        readLineByLineSqlCall(sql, reader -> products.add(new Product(
                reader.getString("name"),
                reader.getInt("price"))
        ));

        return products;
    }

    @Override
    public void insertIntoProduct(Product product) {
        update(String.format(
                "INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"%s\", %d)",
                product.getName(),
                product.getPrice())
        );
    }

    @Override
    public List<Product> selectAllFromProduct() {
        String sql = "SELECT * FROM PRODUCT";
        return queryProducts(sql);
    }

    @Override
    public int countProducts() {
        String sql = "SELECT COUNT(*) FROM PRODUCT";
        return queryInt(sql);
    }

    @Override
    public long sumProducts() {
        String sql = "SELECT SUM(price) FROM PRODUCT";
        return queryInt(sql);
    }

    @Override
    public Product maxInProducts() {
        String sql = "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
        List<Product> maxProsucts = queryProducts(sql);
        assert maxProsucts.size() <= 1;

        if (maxProsucts.isEmpty())
            return null;
        return maxProsucts.get(0);
    }

    @Override
    public Product minInProducts() {
        String sql = "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
        List<Product> minProsucts = queryProducts(sql);
        assert minProsucts.size() <= 1;

        if (minProsucts.isEmpty())
            return null;
        return minProsucts.get(0);
    }

    @Override
    public void dropProducts() {
        update("DROP TABLE PRODUCT");
    }

    @FunctionalInterface
    public interface SQLFunction<T> {
        void apply(T t) throws SQLException;
    }
}
