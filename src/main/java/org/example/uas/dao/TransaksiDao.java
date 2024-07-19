package org.example.uas.dao;

import javafx.fxml.Initializable;
import org.example.uas.Database.JdbcConnection;
import org.example.uas.beans.Date;
import org.example.uas.beans.Transaksi;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static org.example.uas.dao.CRUD_DAO.db;

public class TransaksiDao implements CRUD_DAO<Transaksi> {

    @Override
    public ArrayList<Transaksi> getAll() throws SQLException {
        String query = "select \n" +
                "\tt.transaction_date, \n" +
                "\tconcat(coalesce(e.nama, ''), coalesce(s.nama, '')) as seller, \n" +
                "\tst.description as nama_barang, \n" +
                "\tt.quantity,\n" +
                "\tt.total_jual, \n" +
                "\tt.profit\n" +
                "from transactions t\n" +
                "left join students s on t.student_id = s.student_id\n" +
                "left join employees e on t.employee_id = e.employee_id\n" +
                "join stock st on t.stock_id = st.stock_id ";
        try (Statement statement = db.createStatement();
             ResultSet result = statement.executeQuery(query)) {
            ArrayList<Transaksi> listTransactions = new ArrayList<>();
            while (result.next()) {
                String date = String.valueOf(result.getDate("transaction_date"));
                String namaSeller = result.getString("seller");
                String namaBarang = result.getString("nama_barang");
                int quantity = result.getInt("quantity");
                int totalJual = result.getInt("total_jual");
                int profit = result.getInt("profit");

                listTransactions.add(new Transaksi(date, namaSeller, namaBarang, quantity, totalJual, profit));
            }
            return listTransactions;
        }
    }

    public ArrayList<String> getAllBarang() throws SQLException {
        String query = "select description as nama_barang from stock where status_aktif = 1";
        Statement statement = db.createStatement();
        ResultSet result = statement.executeQuery(query);
        ArrayList<String> listBarang = new ArrayList<>();
        while (result.next()) {
            String department = result.getString("nama_barang");
            listBarang.add(department);
        }
        return listBarang;
    }
//    public ArrayList<String> getAllSeller() throws SQLException {
//        String query = "SELECT nama FROM employees WHERE status_aktif = 1 AND isManager = false AND department_id = 20";
//        try (Statement statement = db.createStatement();
//             ResultSet result = statement.executeQuery(query)) {
//            ArrayList<String> listSeller = new ArrayList<>();
//            while (result.next()) {
//                String namaSeller = result.getString("nama");
//                listSeller.add(namaSeller);
//            }
//            return listSeller;
//        }
//    }

    public void save(Transaksi transaksi) {
        String query;
        if ("Employee".equals(transaksi.getSellerType())) {
            query = "insert into transactions(employee_id, stock_id, total_jual, profit, quantity) values (?, ?, ?, ?, ?)";
        } else if ("Student".equals(transaksi.getSellerType())) {
            query = "insert into transactions(student_id, stock_id, total_jual, profit, quantity) values (?, ?, ?, ?, ?)";
        } else {
            throw new IllegalArgumentException("Invalid seller type: " + transaksi.getSellerType());
        }

        try (PreparedStatement statement = db.prepareStatement(query)) {
            if ("Employee".equals(transaksi.getSellerType())) {
                statement.setInt(1, transaksi.getEmployeeId());
            } else {
                statement.setInt(1, transaksi.getStudentId());
            }
            statement.setInt(2, transaksi.getStockId());
            statement.setInt(3, transaksi.getTotalJual());
            statement.setInt(4, transaksi.getProfit());
            statement.setInt(5, transaksi.getQuantity());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Transaksi transaksi) throws SQLException {
        return false;
    }

    @Override
    public void delete(String id) throws SQLException {

    }

    public int getSellingPrice(int stock_id) throws SQLException {
        String query = "select selling_price from stock \n" +
                "where stock_id = " + stock_id;
        Statement statement = db.createStatement();
        ResultSet result = statement.executeQuery(query);
        int sellingPrice = 0;
        while (result.next()) {
            sellingPrice = result.getInt("selling_price");

        }
        return sellingPrice;
    }

    public int getBuyingPrice(int stock_id) throws SQLException {
        String query = "select buying_price from stock \n" +
                "where stock_id = " + stock_id;
        Statement statement = db.createStatement();
        ResultSet result = statement.executeQuery(query);
        int buyingPrice = 0;
        while (result.next()) {
            buyingPrice = result.getInt("buying_price");
        }

        return buyingPrice;
    }

    public int getCorrespondingBarang(String namaBarang) throws SQLException {
        String query = "select stock_id from stock\n" +
                "where description = '" + namaBarang + "'";
        Statement statement = db.createStatement();
        ResultSet result = statement.executeQuery(query);
        int stockId = 0;
        while (result.next()) {
            stockId = result.getInt("stock_id");

        }

        return stockId;
    }

    public int getCorrespondingSeller(String seller) throws SQLException {
        String query = "select employee_id from employees\n" +
                "where nama = '" + seller + "'";
        Statement statement = db.createStatement();
        ResultSet result = statement.executeQuery(query);
        int employeeId = 0;
        while (result.next()) {
            employeeId = result.getInt("employee_id");
        }

        return employeeId;
    }
    public int getCorrespondingStudent(String student) throws SQLException {
        String query = "select student_id from students where nama = '" + student + "'";
        try (Statement statement = db.createStatement();
             ResultSet result = statement.executeQuery(query)) {
            if (result.next()) {
                return result.getInt("student_id");
            }
            return 0;
        }
    }

    public void decreaseQuantity(int stockId, int quantity) throws SQLException {
        String query = "update stock\n" +
                "set quantity = quantity - " + quantity +
                " where stock_id = " + stockId;
        Statement statement = db.createStatement();
        statement.executeUpdate(query);


    }

    public int getTotalProfit() {
        String query = "select sum(profit) as total_profit from transactions";
        int totalProfit = 0;
        try {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery(query);

            while(result.next()) {
                totalProfit = result.getInt("total_profit");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return totalProfit;
    }

    public ArrayList<String> getBestSellers() {
        String query = "select e.nama as seller from transactions t\n" +
                "join employees e on t.employee_id = e.employee_id\n" +
                "group by e.nama\n" +
                "having sum(t.profit) + 1 > all (\n" +
                "\tselect sum(t.profit) from transactions t\n" +
                "\tjoin employees e on t.employee_id = e.employee_id\n" +
                "\tgroup by e.nama\n" +
                ")";
        ArrayList<String> bestSeller = new ArrayList<>();
        try {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery(query);

            while(result.next()) {
                String seller = result.getString("seller");
                bestSeller.add(seller);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return bestSeller;
    }

    public ArrayList<String> getBestSellingItem() {
        String query = "select s.description\n" +
                "from transactions t \n" +
                "join stock s on t.stock_id = s.stock_id\n" +
                "group by s.description\n" +
                "having sum(t.quantity) + 1 > all(\n" +
                "\tselect sum(t.quantity)\n" +
                "\tfrom transactions t \n" +
                "\tjoin stock s on t.stock_id = s.stock_id\n" +
                "\tgroup by s.description\n" +
                ")";
        ArrayList<String> bestSellings = new ArrayList<>();
        try {
            Statement statement = db.createStatement();
            ResultSet result = statement.executeQuery(query);

            while(result.next()) {
                 bestSellings.add(result.getString("description"));
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return bestSellings;
    }
    public ArrayList<String> getAllSellersAndStudents() throws SQLException {
        ArrayList<String> combinedList = new ArrayList<>();

        String employeeQuery = "select nama from employees where status_aktif = 1 and ismanager = false and department_id = 30";
        try (Statement employeeStatement = db.createStatement();
             ResultSet employeeResult = employeeStatement.executeQuery(employeeQuery)) {
            while (employeeResult.next()) {
                String employeeName = employeeResult.getString("nama");
                combinedList.add("Employee: " + employeeName);
            }
        }

        String studentQuery = "select nama from students where status_aktif = 1 and department_id = 30";
        try (Statement studentStatement = db.createStatement();
             ResultSet studentResult = studentStatement.executeQuery(studentQuery)) {
            while (studentResult.next()) {
                String studentName = studentResult.getString("nama");
                combinedList.add("Student: " + studentName);
            }
        }
        return combinedList;
    }
}
