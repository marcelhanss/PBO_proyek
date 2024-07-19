package org.example.uas.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.uas.HelloApplication;
import org.example.uas.beans.Transaksi;
import org.example.uas.dao.TransaksiDao;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddTransaksiController implements Initializable {

    @FXML
    private ComboBox<String> barang_combo_box;
    @FXML
    private TextField quantity_field;
    @FXML
    private ComboBox<String> seller_combo_box;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setComboBoxBarang();
            setComboBoxStudentsAndEmployees();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void backButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/home-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) seller_combo_box.getScene().getWindow();
        stage.setTitle("Home");
        stage.setScene(new Scene(root));
    }
//    private void setComboBoxSeller() throws SQLException {
//        TransaksiDao transaksiDao = new TransaksiDao();
//
//        ArrayList<String> listSeller = transaksiDao.getAllSeller();
//
//        ObservableList<String> obListSeller = FXCollections.observableArrayList();
//        obListSeller.addAll(listSeller);
//
//        seller_combo_box.setItems(obListSeller);
//
//        seller_combo_box.setButtonCell(new ListCell<String>() {
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setText(null);
//                } else {
//                    setText(item);
//                    setTextFill(Color.WHITE);
//                }
//            }
//        });
private void setComboBoxBarang() throws SQLException {
        TransaksiDao transaksiDao = new TransaksiDao();

        ArrayList<String> listBarang = transaksiDao.getAllBarang();

        ObservableList<String> obListBarang = FXCollections.observableArrayList();
        obListBarang.addAll(listBarang);

        barang_combo_box.setItems(obListBarang);

        barang_combo_box.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setTextFill(Color.WHITE);
                }
            }
        });

    }

    private void setComboBoxStudentsAndEmployees() throws SQLException {
        TransaksiDao transaksiDao = new TransaksiDao();

        ArrayList<String> listStudentsAndEmployees = transaksiDao.getAllSellersAndStudents();

        ObservableList<String> obListStudentsAndEmployees = FXCollections.observableArrayList();
        obListStudentsAndEmployees.addAll(listStudentsAndEmployees);

        seller_combo_box.setItems(obListStudentsAndEmployees);

        seller_combo_box.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setTextFill(Color.WHITE);
                }
            }
        });

        seller_combo_box.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
            }
        });
    }

    public void submitNewTransaction() throws SQLException, IOException {
        TransaksiDao transaksiDao = new TransaksiDao();
        Transaksi transaksi = new Transaksi();

        String seller = seller_combo_box.getValue();
        System.out.println("Selected seller: " + seller);
        int sellerId;
        String sellerType;

        if (seller != null && seller.startsWith("Employee: ")) {
            sellerId = transaksiDao.getCorrespondingSeller(seller.replace("Employee: ", ""));
            transaksi.setEmployeeId(sellerId);
            sellerType = "Employee";
        } else if (seller != null && seller.startsWith("Student: ")) {
            sellerId = transaksiDao.getCorrespondingStudent(seller.replace("Student: ", ""));
            transaksi.setStudentId(sellerId);
            sellerType = "Student";
        } else {
            throw new IllegalArgumentException("Invalid seller type: " + seller);
        }

        int item = transaksiDao.getCorrespondingBarang(barang_combo_box.getValue());
        transaksi.setStockId(item);

        int sellingPrice = transaksiDao.getSellingPrice(item);
        int totalSellingPrice = sellingPrice * Integer.parseInt(quantity_field.getText());
        transaksi.setTotalJual(totalSellingPrice);

        int profit = totalSellingPrice - (transaksiDao.getBuyingPrice(item) * Integer.parseInt(quantity_field.getText()));
        transaksi.setProfit(profit);

        transaksi.setQuantity(Integer.parseInt(quantity_field.getText()));

        transaksi.setSellerType(sellerType);
        transaksiDao.save(transaksi);

        transaksiDao.decreaseQuantity(item, Integer.parseInt(quantity_field.getText()));

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("viewForm/transaksi-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) seller_combo_box.getScene().getWindow();
        stage.setTitle("Transaction");
        stage.setScene(new Scene(root));
    }
}
