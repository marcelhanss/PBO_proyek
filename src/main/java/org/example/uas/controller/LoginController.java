package org.example.uas.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.uas.HelloApplication;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.scene.media.AudioClip;


public class LoginController implements Initializable {

    public Button changePassButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    private Connection conn;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/koperasi", "postgres", "admin");
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
        loginButton.setDefaultButton(true);
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String nip = usernameField.getText();
        String password = passwordField.getText();

        if (nip.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter both username and password");
            alert.showAndWait();
            return;
        }

        try {
            PreparedStatement pstmt = conn.prepareStatement("select * from employees where nip = ? and password = ?");
            pstmt.setString(1, nip);
            pstmt.setString(2, password);

            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                AudioClip audioClip = new AudioClip(HelloApplication.class.getResource("sfx/click-sound-3.mp3").toString());
                audioClip.play();
                System.out.println("Login successful!");
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/home-view.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setTitle("Home");
                stage.setScene(new Scene(root));

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Invalid username or password");
                alert.showAndWait();
            }
        } catch (Exception e) {
            System.out.println("Error logging in: " + e.getMessage());
        }

    }
    public void handleChangePassword(ActionEvent actionEvent) throws IOException {
        AudioClip audioClip = new AudioClip(HelloApplication.class.getResource("sfx/click-sound-2.mp3").toString());
        audioClip.play();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/changePassword.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) changePassButton.getScene().getWindow();
        stage.setTitle("Change Password");
        stage.setScene(new Scene(root));
    }
}