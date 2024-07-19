package org.example.uas.controller;

import javafx.application.Application;
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
import org.example.uas.beans.Employees;
import org.example.uas.dao.EmployeeDao;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AddPegawaiController  implements Initializable {
    @FXML
    private ComboBox<String> departments_combo_box;

    private HashMap<String, String> parameters = new HashMap<>();
    @FXML
    private TextField first_name_field;
    @FXML
    private TextField last_name_field;
    @FXML
    private TextField email_field;
    @FXML
    private TextField nip_field;

    public Button back_Button;
    public Button submit_Button;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setComboBoxEmployee();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void backButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/home-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) back_Button.getScene().getWindow();
        stage.setTitle("Home");
        stage.setScene(new Scene(root));
    }
    private void setComboBoxEmployee() throws SQLException {
        EmployeeDao employeeDao = new EmployeeDao();

        ArrayList<String> listDepartments = employeeDao.getAllDepartments();

        ObservableList<String> obListDepartments = FXCollections.observableArrayList();
        obListDepartments.add("none");
        obListDepartments.addAll(listDepartments);

        departments_combo_box.setItems(obListDepartments);

        departments_combo_box.setButtonCell(new ListCell<String>() {
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
    protected void processParameters() {
        if(containsParameter("id")) {
            Employees employee = EmployeeDao.get(getParameter("id"));
            String [] names = employee.getNama().split(" ");

            first_name_field.setText(names[0]);
            if(names.length > 1) {
                last_name_field.setText(names[1]);
            }

            email_field.setText(employee.getEmail());
            nip_field.setText(employee.getNip());

            if(employee.getDepartmentName() == null) {
                departments_combo_box.setValue("none");
            } else {
                departments_combo_box.setValue(employee.getDepartmentName());
            }
        }
    }

    public void addParameter(String key, String value) {parameters.put(key, value);}
    public void setParameters(Scene scene) {
        parameters = (HashMap<String, String>) scene.getUserData();
        processParameters();
    }

    protected boolean containsParameter(String key) {
        return parameters.containsKey(key);
    }

    protected String getParameter(String key) {return parameters.get(key);}


    public Employees getDataInternal(EmployeeDao employeeDao) throws SQLException {
        Employees employees = new Employees();
        employees.setNip(nip_field.getText());
        employees.setNama(first_name_field.getText() + " "+ last_name_field.getText());
        employees.setEmail(email_field.getText());

        int department_id = employeeDao.findRightDepartment(departments_combo_box.getValue());
        employees.setDeparmentID(department_id);
        int manager_id = employeeDao.findCorrespondingManager(department_id);
        employees.setManagerID(manager_id);
        return employees;
    }

    public void submitButton(ActionEvent actionEvent) throws SQLException, IOException {
        EmployeeDao employeeDao = new EmployeeDao();
        Employees employee = getDataInternal(employeeDao);
        if(employee.getNip().isEmpty() || employee.getNama().isEmpty() || employee.getEmail().isEmpty()){
            showWarningDialog("Error", "Please fill the column correctly");
        }else{
            if(containsParameter("mode") && getParameter("mode").equalsIgnoreCase("edit")) {
                String employee_id = getParameter("id");
                employeeDao.update(employee, employee_id);
            } else {
                employeeDao.save(employee);

            }
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/home-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) back_Button.getScene().getWindow();
            stage.setTitle("Home");
            stage.setScene(new Scene(root));
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
