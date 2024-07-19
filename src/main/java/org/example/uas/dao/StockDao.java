package org.example.uas.dao;

import org.example.uas.Database.JdbcConnection;
import org.example.uas.beans.Employees;
import org.example.uas.beans.Stock;

import java.sql.*;
import java.util.ArrayList;

public class StockDao implements CRUD_DAO<Stock> {
    @Override
    public ArrayList<Stock> getAll() throws SQLException {
        String query = "select s.stock_id, s.description, c.category_name, s.buying_price, s.selling_price, s.quantity from stock s join category c on s.category = c.category_id where status_aktif = 1";
        Statement statement = db.createStatement();
        ResultSet result = statement.executeQuery(query);
        ArrayList<Stock> listStock = new ArrayList<>();
        while (result.next()) {
            String description = result.getString("description");
            String categoryName = result.getString("category_name");
            int buyingPrice = result.getInt("buying_price");
            int selling_price = result.getInt("selling_price");
            int quantity = result.getInt("quantity");
            listStock.add(new Stock(description, categoryName, buyingPrice, selling_price, quantity));
        }

        return listStock;
    }

    public static Stock get(String description) {
        PreparedStatement statement = null;
        ResultSet result = null;
        String query = "select e.description, d.category_name, e.buying_price, e.selling_price, e.quantity from stock e \n" +
                "join category d on e.category = d.category_id where e.status_aktif = 1 and e.description = ?\n" +
                "order by e.stock_id asc";
        Stock stock = null;
        try {
            statement = db.prepareStatement(query);
            statement.setString(1,description);
            result = statement.executeQuery();

            while(result.next()) {
                stock = new Stock();
                stock.setDescription(result.getString("description"));
                stock.setCategoryName(result.getString("category_name"));
                stock.setBuyingPrice(result.getInt("buying_price"));
                stock.setSellingPrice(result.getInt("selling_price"));
                stock.setQuantity(result.getInt("quantity"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcConnection.closeConnection(result, statement);
        }
        return stock;

    }


    public ArrayList<String> getAllCategory() throws SQLException {
        String query = "select distinct category_name from category e join stock s on e.category_id = s.category";
        Statement statement = db.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        ArrayList<String> listCategory = new ArrayList<>();
        while (resultSet.next()){
            String category = resultSet.getString("category_name");
            listCategory.add(category);
        }
        return listCategory;
    }


        @Override
    public void save(Stock stock) throws SQLException {
        PreparedStatement statement = null;
        String query ="insert into stock(description, category, buying_price, selling_price,quantity) values (?, ?, ? , ?, ?)";
        try{
            statement = db.prepareStatement(query);
            statement.setString(1, stock.getDescription());
            statement.setInt(2,stock.getCategoryId());
            statement.setInt(3,stock.getBuyingPrice());
            statement.setInt(4,stock.getSellingPrice());
            statement.setInt(5,stock.getQuantity());
            statement.execute();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcConnection.closeConnection(statement);
        }
    }

    public int findRightCategoryId(String category_name) throws SQLException {
        String query="select distinct category_id from category e join stock s on s.category = s.category where category_name = '"+category_name+"'";
        Statement statement = db.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int category = 0;
        while(resultSet.next()){
            category = resultSet.getInt("category_id");
        }
        return category;
    }

    @Override
    public boolean update(Stock stock) throws SQLException {
        return false;
    }


    public void update(Stock stock, String description) throws SQLException {
        PreparedStatement statement = null;
        String query = "update stock \n" +
                "set description = ?, buying_price = ?, selling_price = ?, quantity = ?,\n" +
                "category = ?\n" +
                "where description = ?";
        try {
            statement = db.prepareStatement(query);
            statement.setString(1, stock.getDescription());
            statement.setInt(2, stock.getBuyingPrice());
            statement.setInt(3, stock.getSellingPrice());
            statement.setInt(4, stock.getQuantity());
            statement.setInt(5, stock.getCategoryId());
            statement.setString(6, description);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcConnection.closeConnection(statement);
        }
    }
    @Override
    public void delete(String description) throws SQLException {
            String query = "update stock set status_aktif = 0 where description = ?";
            try(PreparedStatement preparedStatement = db.prepareStatement(query)) {
                preparedStatement.setString(1, description);
                preparedStatement.executeUpdate();
            }
    }
}