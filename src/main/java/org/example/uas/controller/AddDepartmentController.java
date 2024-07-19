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
import org.example.uas.beans.Departments;
import org.example.uas.beans.Employees;
import org.example.uas.dao.DepartmentDao;
import org.example.uas.dao.EmployeeDao;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.SplittableRandom;

public class AddDepartmentController implements Initializable {
    public Button back_Button;
    public Button submit_Button;
    public Label label_combo_box;
    private HashMap<String, String> parameters= new HashMap<>();
    @FXML
    public ComboBox<String> manager_combo_box;
    @FXML
    public TextField department_name;
    @FXML
    public TextField departmentId_field;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            setComboBoxManager();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        manager_combo_box.setVisible(false);
        label_combo_box.setVisible(false);
    }

    public void backButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/home-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) back_Button.getScene().getWindow();
        stage.setTitle("Home");
        stage.setScene(new Scene(root));
    }

    private void setComboBoxManager() throws SQLException {
        DepartmentDao departmentDao = new DepartmentDao();
        ArrayList<String> listDepartments = departmentDao.getAllManager();

        ObservableList<String> obListManger = FXCollections.observableArrayList();
        obListManger.addAll(listDepartments);

        manager_combo_box.setItems(obListManger);

        manager_combo_box.setButtonCell(new ListCell<String>() {
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
            Departments departments = DepartmentDao.get(getParameter("id"));
            departmentId_field.setText(String.valueOf(departments.getDepartmentId()));


            department_name.setText(departments.getName());
            manager_combo_box.setValue(departments.getNameEmployee());
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


    public Departments getDataInternal(DepartmentDao departmentDao) throws SQLException {
        Departments departments = new Departments();
        departments.setDepartmentId(Integer.parseInt(departmentId_field.getText()));
        departments.setName(department_name.getText());

        int manager = departmentDao.findRightManagerId(manager_combo_box.getValue());
        departments.setManagerId(manager);
        System.out.println(manager);
        return departments;
    }

    public void submitButton(ActionEvent actionEvent) throws SQLException, IOException {
        DepartmentDao departmentDao = new DepartmentDao();
        Departments departments = getDataInternal(departmentDao);
        if(departments.getDepartmentId() == 0 || departments.getName().isEmpty()){
            showWarningDialog("Error", "Please fill the column correctly");
        }
        else{
            if(containsParameter("mode") && getParameter("mode").equalsIgnoreCase("edit")) {
                String department_id = getParameter("id");
                departmentDao.update(departments, department_id);
            } else {
                departmentDao.save(departments);

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
