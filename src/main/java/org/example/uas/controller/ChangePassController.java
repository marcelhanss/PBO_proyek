package org.example.uas.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.uas.Database.JdbcConnection;
import org.example.uas.HelloApplication;
import org.example.uas.dao.CRUD_DAO;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.example.uas.dao.CRUD_DAO.db;

public class ChangePassController {
    public Button back_Button;
    public Button submit_Button;
    public TextField nip_field;
    public TextField current_pass_field;
    public TextField new_pass_field;

    public void backButton() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/login-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) back_Button.getScene().getWindow();
        stage.setTitle("Login");
        stage.setScene(new Scene(root));
    }

    public void submitButton(ActionEvent actionEvent) throws IOException {
        String nip = nip_field.getText();
        String current_pass = current_pass_field.getText();
        String new_pass = new_pass_field.getText();
        PreparedStatement statement = null;
        String query = "update employees set password = ? where nip = ? and password = ? ";
        if(current_pass.isEmpty() || new_pass.isEmpty() || nip.isEmpty()){
            showWarningDialog("Error", "Password dan nip harus di isi");
        }else {
            try {
                statement = db.prepareStatement(query);
                statement.setString(1, new_pass);
                statement.setString(2, nip);
                statement.setString(3, current_pass);

                statement.executeUpdate();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/login-view.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = (Stage) submit_Button.getScene().getWindow();
                stage.setTitle("Change Password");
                stage.setScene(new Scene(root));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                JdbcConnection.closeConnection(statement);
            }
        }

    }

    private void showWarningDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
