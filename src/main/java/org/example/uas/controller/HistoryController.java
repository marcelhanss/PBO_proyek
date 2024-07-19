package org.example.uas.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.uas.HelloApplication;
import org.example.uas.beans.Employees;
import org.example.uas.beans.Students;
import org.example.uas.dao.EmployeeDao;
import org.example.uas.dao.StudentDao;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {
    public Button back_button;
    public TableView tabel_history_employee;
    public TableView tabel_history_student;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setEmployeeTable();
        setStudentTable();
    }
    public void backButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/home-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) back_button.getScene().getWindow();
        stage.setTitle("Home");
        stage.setScene(new Scene(root));
    }
    private void setEmployeeTable() {
        tabel_history_employee.getColumns().clear();

        EmployeeDao employeeDao = new EmployeeDao();

        TableColumn nrp = new TableColumn<Employees, String>("NIP");
        nrp.setMinWidth(75);
        nrp.setCellValueFactory(new PropertyValueFactory<Employees, String>("nip"));

        TableColumn nameCol = new TableColumn<Employees, String>("Name");
        nameCol.setMinWidth(125);
        nameCol.setCellValueFactory(new PropertyValueFactory<Employees, String>("nama"));

        TableColumn emailCol = new TableColumn<Employees, String>("Department Name");
        emailCol.setMinWidth(145);
        emailCol.setCellValueFactory(new PropertyValueFactory<Employees, String>("departmentName"));

        tabel_history_employee.getColumns().addAll(nrp, nameCol, emailCol);

        try {
            ArrayList<Employees> listEmployee = employeeDao.historyEmployee();
            ObservableList<Employees> obListTable = FXCollections.observableArrayList(listEmployee);
            tabel_history_employee.setItems(obListTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setStudentTable() {
        tabel_history_student.getColumns().clear();

        StudentDao studentDao = new StudentDao();

        TableColumn nrp = new TableColumn<Employees, String>("NRP");
        nrp.setMinWidth(75);
        nrp.setCellValueFactory(new PropertyValueFactory<Employees, String>("nrp"));

        TableColumn nameCol = new TableColumn<Employees, String>("Name");
        nameCol.setMinWidth(125);
        nameCol.setCellValueFactory(new PropertyValueFactory<Employees, String>("student_name"));

        TableColumn emailCol = new TableColumn<Employees, String>("Department Name");
        emailCol.setMinWidth(145);
        emailCol.setCellValueFactory(new PropertyValueFactory<Employees, String>("departmentName"));

        tabel_history_student.getColumns().addAll(nrp, nameCol, emailCol);

        try {
            ArrayList<Students> listStudent = studentDao.historyStudent();
            ObservableList<Students> obListTable = FXCollections.observableArrayList(listStudent);
            tabel_history_student.setItems(obListTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
