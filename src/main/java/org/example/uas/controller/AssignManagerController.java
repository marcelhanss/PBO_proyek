package org.example.uas.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.uas.Database.JdbcConnection;
import org.example.uas.HelloApplication;
import org.example.uas.beans.Employees;
import org.example.uas.dao.EmployeeDao;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AssignManagerController implements Initializable {
    public Button back_button;
    @FXML
    private TableView<Employees> available_employee_table;

    @FXML
    private TableView<Employees> manager_table;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setEmployeeTable();
        setManagerTable();
    }
    public void backButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/home-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) back_button.getScene().getWindow();
        stage.setTitle("Home");
        stage.setScene(new Scene(root));
    }
    private void setEmployeeTable() {
        available_employee_table.getColumns().clear();

        EmployeeDao employeeDao = new EmployeeDao();

        TableColumn nrp = new TableColumn<Employees, String>("NIP");
        nrp.setMinWidth(75);
        nrp.setCellValueFactory(new PropertyValueFactory<Employees, String>("nip"));

        TableColumn nameCol = new TableColumn<Employees, String>("Name");
        nameCol.setMinWidth(125);
        nameCol.setCellValueFactory(new PropertyValueFactory<Employees, String>("nama"));

        TableColumn emailCol = new TableColumn<Employees, String>("Email");
        emailCol.setMinWidth(145);
        emailCol.setCellValueFactory(new PropertyValueFactory<Employees, String>("email"));

        available_employee_table.getColumns().addAll(nrp, nameCol, emailCol);

        try {
            ArrayList<Employees> listEmployee = employeeDao.getAllAvailableEmployee();
            ObservableList<Employees> obListTable = FXCollections.observableArrayList(listEmployee);
            available_employee_table.setItems(obListTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private void setManagerTable() {
        manager_table.getColumns().clear();

        EmployeeDao employeeDao = new EmployeeDao();

        TableColumn nrp = new TableColumn<Employees, String>("NIP");
        nrp.setMinWidth(65);
        nrp.setCellValueFactory(new PropertyValueFactory<Employees, String>("nip"));

        TableColumn nameCol = new TableColumn<Employees, String>("Name");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(new PropertyValueFactory<Employees, String>("nama"));

        TableColumn emailCol = new TableColumn<Employees, String>("Email");
        emailCol.setMinWidth(110);
        emailCol.setCellValueFactory(new PropertyValueFactory<Employees, String>("email"));

        TableColumn departmentName = new TableColumn<Employees, String>("Department");
        departmentName.setMinWidth(110);
        departmentName.setCellValueFactory(new PropertyValueFactory<Employees,String>("departmentName"));

        manager_table.getColumns().addAll(nrp, nameCol, emailCol, departmentName);

        try {
            ArrayList<Employees> listEmployee = employeeDao.getAllManager();
            ObservableList<Employees> obListTable = FXCollections.observableArrayList(listEmployee);
            manager_table.setItems(obListTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void addButton(ActionEvent actionEvent) throws IOException{
        Employees selectedEmployee = available_employee_table.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            EmployeeDao employeeDao = new EmployeeDao();
            try {
                if (employeeDao.canAssignManager(selectedEmployee.getDepartmentName())) {
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("assignForm/assign-view.fxml"));

                    fxmlLoader.setControllerFactory(type -> {
                        if (type == AssignManagerDepartmentController.class) {
                            AssignManagerDepartmentController controller = new AssignManagerDepartmentController();
                            controller.setEmployees(selectedEmployee);
                            return controller;
                        } else {
                            try {
                                return type.getDeclaredConstructor().newInstance();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                    Parent root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setTitle("Assign Department");
                    stage.setScene(new Scene(root));
                    stage.showAndWait();

                } else {
                    showWarningDialog("Error", "No data selected");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    public void removeButton(ActionEvent actionEvent) {
        Employees selectedManager = manager_table.getSelectionModel().getSelectedItem();
        if (selectedManager != null) {
            EmployeeDao employeeDao = new EmployeeDao();
            try {
                employeeDao.removeManager(selectedManager);
                setEmployeeTable();
                setManagerTable();
            } catch (SQLException e) {
                throw new RuntimeException(e);
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
