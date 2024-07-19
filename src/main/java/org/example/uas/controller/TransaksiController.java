package org.example.uas.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

public class TransaksiController  {

    @FXML
    private Button cancel_button;
    @FXML
    private Button new_button;
    @FXML
    private Button view_button;



    public void newButton(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addForm/add-transaksi.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) new_button.getScene().getWindow();
        stage.setTitle("Transaksi");
        stage.setScene(new Scene(root));

    }

    public void viewButton(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addForm/transaksi-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) view_button.getScene().getWindow();
        stage.setTitle("View Tansaksi");
        stage.setScene(new Scene(root));
    }

    public void cancel(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/home-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) cancel_button.getScene().getWindow();
        stage.setTitle("Home");
        stage.setScene(new Scene(root));
    }


}
