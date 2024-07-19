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
import org.example.uas.beans.Employees;
import org.example.uas.beans.Students;
import org.example.uas.dao.EmployeeDao;
import org.example.uas.dao.StudentDao;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AddMahasiswaController implements Initializable {
    public Button back_Button;
    public Button submit_Button;
    private HashMap<String, String> parameters = new HashMap<>();
    @FXML
    public ComboBox<String> department_combo_box;

    @FXML
    private TextField first_name_field;
    @FXML
    private TextField last_name_field;
    @FXML
    private TextField email_field;
    @FXML
    private TextField nrp_field;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setComboBoxStudent();
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

    private void setComboBoxStudent() throws SQLException {
        StudentDao studentDao = new StudentDao();

        ArrayList<String> listDepartments = studentDao.getAllDepartments();

        ObservableList<String> obListDepartments = FXCollections.observableArrayList();
        obListDepartments.add("none");
        obListDepartments.addAll(listDepartments);

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

    protected void processParameters() {
        if(containsParameter("id")) {
            Students students = StudentDao.get(getParameter("id"));
            String [] names = students.getStudent_name().split(" ");

            first_name_field.setText(names[0]);

            if(names.length > 1) {
                last_name_field.setText(names[1]);
            }


            email_field.setText(students.getEmail());
            nrp_field.setText(students.getNrp());
            department_combo_box.setValue(students.getDepartmentName());

            if(students.getDepartmentName() == null) {
                department_combo_box.setValue("none");
            } else {
                department_combo_box.setValue(students.getDepartmentName());
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


    public Students getDataInternal(StudentDao studentDao) throws SQLException {
        Students students = new Students();
        students.setNrp(nrp_field.getText());
        students.setStudent_name(first_name_field.getText() + " "+ last_name_field.getText());
        students.setEmail(email_field.getText());

        int department_id = studentDao.findRightDepartmentId(department_combo_box.getValue());
        students.setDepartmentId(department_id);
        int manager_id = studentDao.findCorrespondingManager(department_id);
        students.setManagerId(manager_id);
        return students;
    }

    public void submitButton(ActionEvent actionEvent) throws SQLException, IOException {
        StudentDao studentDao = new StudentDao();
        Students students = getDataInternal(studentDao);
        if(students.getNrp().isEmpty() || students.getStudent_name().isEmpty() || students.getEmail().isEmpty() ){
            showWarningDialog("Error", "Please fill the column corectly ");
        }else{
            if(containsParameter("mode") && getParameter("mode").equalsIgnoreCase("edit")) {
                String employee_id = getParameter("id");
                studentDao.update(students, employee_id);
            } else {
                studentDao.save(students);

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
