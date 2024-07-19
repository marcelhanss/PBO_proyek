package org.example.uas.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.uas.Database.JdbcConnection;
import org.example.uas.HelloApplication;
import org.example.uas.beans.Employees;
import org.example.uas.dao.EmployeeDao;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static org.example.uas.dao.CRUD_DAO.db;

public class AssignManagerDepartmentController implements Initializable {

    public Button back_Button;
    public Button submit_Button;
    public AnchorPane mainPane;
    private Stage stage;

    @FXML
    private ComboBox<String> department_combo_box;

    private Employees employees;

    private JdbcConnection connection;

    public void setEmployees(Employees employees) {
        this.employees = employees;
    }
    
    public void backButton(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) back_Button.getScene().getWindow();
        stage.close();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            setComboBox();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setComboBox() throws SQLException {
        EmployeeDao employeeDao = new EmployeeDao();
        ArrayList<String> listAvailableDepartments = employeeDao.getAllAvailableDepartments();

        ObservableList<String> obListDepartments = FXCollections.observableArrayList();
        obListDepartments.addAll(listAvailableDepartments);

        department_combo_box.setItems(obListDepartments);
        department_combo_box.setButtonCell(new ListCell<String>() {
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
    public void submitNewManager(ActionEvent actionEvent) throws SQLException {
        EmployeeDao employeeDao = new EmployeeDao();
        employeeDao.assignManager(employees, department_combo_box.getValue());
        Stage stage = (Stage) back_Button.getScene().getWindow();
        stage.close();
    }

}
