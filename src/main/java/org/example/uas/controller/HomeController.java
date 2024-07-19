package org.example.uas.controller;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.uas.HelloApplication;
import org.example.uas.beans.*;
import org.example.uas.dao.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;


public class HomeController implements Initializable {
    public Button logout_fxId;
    private ParallelTransition welcomeAnimation;

    public Button stokBtn;
    public Button history;
    public Button transaksiBtn;
    @FXML
    private Label judul;

    @FXML
    private Button export_button;

    @FXML
    public Button addBtn;

    @FXML
    public Button editBtn;

    @FXML
    public Button deleteBtn;
    private int controller = 0;
    @FXML
    private Button employeeBtn;
    @FXML
    private Button mahasiswaBtn;
    @FXML
    private Button departemenBtn;
    @FXML
    private Button stockBtn;
    @FXML
    private HBox buttonContainer;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private AnchorPane secondPane;

    @FXML
    private Button export_button_department;
    private Button currentActiveButton;

    @FXML
    private TableView tabel_data = new TableView<>();

    private Label welcomeLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupWelcomeText();
        mainPane.setOnMouseClicked(event -> resetAnimations());
        secondPane.setOnMouseClicked(event -> resetAnimations());
        tabel_data.setVisible(false);
        history.setVisible(false);
        export_button.setVisible(false);
        export_button_department.setVisible(false);
    }

    public void logout_button(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main/login-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) logout_fxId.getScene().getWindow();
        stage.setTitle("Login");
        stage.setScene(new Scene(root));
    }
    private void setupWelcomeText() {
        welcomeLabel = new Label("Welcome!");
        welcomeLabel.setFont(new Font("Arial Bold", 50));
        welcomeLabel.setTextFill(Color.WHITE);
        welcomeLabel.setTextAlignment(TextAlignment.CENTER);

        welcomeLabel.translateXProperty().bind(secondPane.widthProperty().subtract(welcomeLabel.widthProperty()).divide(2));
        welcomeLabel.translateYProperty().bind(secondPane.heightProperty().subtract(welcomeLabel.heightProperty()).divide(2));

        secondPane.getChildren().add(welcomeLabel);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), welcomeLabel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(2), welcomeLabel);
        scaleTransition.setFromX(0.5);
        scaleTransition.setFromY(0.5);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);

        ParallelTransition parallelTransition = new ParallelTransition(fadeIn, scaleTransition);
        parallelTransition.setOnFinished(event -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> secondPane.getChildren().remove(welcomeLabel)); // Remove the label after the animation
            pause.play();
        });

        parallelTransition.play();
    }
    @FXML
    private void handleButtonAction(Button button) {
        if (button == currentActiveButton) {
            return;
        }

        if (welcomeAnimation != null && welcomeAnimation.getStatus() == Animation.Status.RUNNING) {
            welcomeAnimation.stop();
        }

        for (Node node : secondPane.getChildren()) {
            if (node instanceof Label && "Welcome!".equals(((Label) node).getText())) {
                node.setVisible(false);
                break;
            }
        }

        if (currentActiveButton != null) {
            TranslateTransition slideOut = new TranslateTransition(Duration.seconds(0.5), buttonContainer);
            slideOut.setToX(currentActiveButton.getWidth());

            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), buttonContainer);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            ParallelTransition slideAndFadeOut = new ParallelTransition(slideOut, fadeOut);
            slideAndFadeOut.setOnFinished(event -> {
                buttonContainer.setVisible(false);
                currentActiveButton.setVisible(true);
                showCRUDButtons(button);
            });

            slideAndFadeOut.play();
        } else {
            showCRUDButtons(button);
        }
    }

    public void createStudentTable(){
        export_button.setVisible(true);
        export_button_department.setVisible(false);
        controller = 1;
        judul.getText().equals("");
        tabel_data.getItems().clear();
        tabel_data.getColumns().clear();

        judul.setText("STUDENT");
        judul.setLayoutX(200);
        judul.setLayoutY(20);
        judul.setTextFill(Color.WHITE);
        judul.setFont(Font.font(25));

        history.setVisible(false);

        tabel_data.getColumns().removeAll();

        StudentDao studentDao = new StudentDao();

        TableColumn nrp = new TableColumn<Students, String>("NRP");
        nrp.setMinWidth(75);
        nrp.setCellValueFactory(new PropertyValueFactory<Students, String>("nrp"));

        TableColumn nameCol = new TableColumn<Students, String>("Name");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(new PropertyValueFactory<Students, String>("student_name"));

        TableColumn emailCol = new TableColumn<Students, String>("Email");
        emailCol.setMinWidth(125);
        emailCol.setCellValueFactory(new PropertyValueFactory<Students, String>("email"));

        TableColumn departmentId = new TableColumn<Students, String>("Department Name");
        departmentId.setMinWidth(90);

        departmentId.setCellValueFactory(new PropertyValueFactory<Students,String>("departmentName"));

        TableColumn managerName = new TableColumn<Students, String>("Manager");
        managerName.setMinWidth(90);

        managerName.setCellValueFactory(new PropertyValueFactory<Students, String>("managerName"));

        tabel_data.getColumns().addAll(nrp,nameCol,emailCol,departmentId,managerName);
        try {
            ArrayList<Students> listStudents = studentDao.getAll();
            ObservableList<Students> obListTable = FXCollections.observableArrayList(listStudents);
            tabel_data.setItems(obListTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createEmployeeTable(){
        export_button.setVisible(true);
        export_button_department.setVisible(false);
        controller = 2;
        judul.setText("");
        tabel_data.getItems().clear();
        tabel_data.getColumns().clear();

        judul.setText("EMPLOYEE");
        judul.setLayoutX(200);
        judul.setLayoutY(20);
        judul.setTextFill(Color.WHITE);
        judul.setFont(Font.font(25));

        history.setVisible(false);

        tabel_data.getColumns().removeAll();

        EmployeeDao employeeDao = new EmployeeDao();

        TableColumn nrp = new TableColumn<Employees, String>("NIP");
        nrp.setMinWidth(75);
        nrp.setCellValueFactory(new PropertyValueFactory<Employees, String>("nip"));

        TableColumn nameCol = new TableColumn<Employees, String>("Name");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(new PropertyValueFactory<Employees, String>("nama"));

        TableColumn emailCol = new TableColumn<Employees, String>("Email");
        emailCol.setMinWidth(125);
        emailCol.setCellValueFactory(new PropertyValueFactory<Employees, String>("email"));

        TableColumn departmentName = new TableColumn<Employees, String>("Department");
        departmentName.setMinWidth(90);
        departmentName.setCellValueFactory(new PropertyValueFactory<Employees,String>("departmentName"));

        TableColumn managerName = new TableColumn<Employees, String >("Manager");
        managerName.setMinWidth(90);
        managerName.setCellValueFactory(new PropertyValueFactory<Employees,String >("manager"));

        tabel_data.getColumns().addAll(nrp,nameCol,emailCol,departmentName,managerName);
        try {
            ArrayList<Employees> listEmployee = employeeDao.getAll();
            ObservableList<Employees> obListTable = FXCollections.observableArrayList(listEmployee);
            tabel_data.setItems(obListTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void createDepartmentTable() {
        export_button_department.setVisible(true);
        export_button.setVisible(false);
        controller = 3;
        judul.getText().equals("");
        tabel_data.getItems().clear();
        tabel_data.getColumns().clear();

        judul.setText("DEPARTMENT");
        judul.setLayoutX(200);
        judul.setLayoutY(20);
        judul.setTextFill(Color.WHITE);
        judul.setFont(Font.font(25));

        history.setVisible(true);

        DepartmentDao departmentDao = new DepartmentDao();
        tabel_data.getColumns().removeAll();


        tabel_data.getColumns().removeAll();

        TableColumn dept_id = new TableColumn<Departments, Integer>("Department Id");
        dept_id.setMinWidth(140);
        dept_id.setCellValueFactory(new PropertyValueFactory<Employees, Integer>("departmentId"));

        TableColumn name = new TableColumn<Departments, String>("Name");
        name.setMinWidth(200);
        name.setCellValueFactory(new PropertyValueFactory<Employees, String>("name"));

        TableColumn manager = new TableColumn<Departments, String>("Manager");
        manager.setMinWidth(140);
        manager.setCellValueFactory(new PropertyValueFactory<Employees, String>("manager"));

        tabel_data.getColumns().addAll(dept_id, name, manager);


        try {
            ArrayList<Departments> listDepartments = departmentDao.getAll();
            ObservableList<Departments> obListTable = FXCollections.observableArrayList();
            obListTable.addAll(listDepartments);
            tabel_data.setItems(obListTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void createStokTable() {
        export_button_department.setVisible(false);
        export_button.setVisible(false);
        controller = 4;
        tabel_data.getItems().clear();
        tabel_data.getColumns().clear();

        judul.setText("STOK");
        judul.setLayoutX(200);
        judul.setLayoutY(20);
        judul.setTextFill(Color.WHITE);
        judul.setFont(Font.font(25));

        history.setVisible(false);

        StockDao stockDao = new StockDao();

        tabel_data.getColumns().removeAll();

        TableColumn desc = new TableColumn<Stock, String>("Description");
        desc.setMinWidth(100);
        desc.setCellValueFactory(new PropertyValueFactory<Stock, String>("description"));

        TableColumn catName = new TableColumn<Stock, String>("Category");
        catName.setMinWidth(130);
        catName.setCellValueFactory(new PropertyValueFactory<Stock, String>("categoryName"));

        TableColumn buy = new TableColumn<Stock, Integer>("Buying Price");
        buy.setMinWidth(90);

        buy.setCellValueFactory(new PropertyValueFactory<Stock, String>("buyingPrice"));

        TableColumn sell = new TableColumn<Stock, Integer>("Selling Price");
        sell.setMinWidth(90);

        sell.setCellValueFactory(new PropertyValueFactory<Stock, String>("sellingPrice"));

        TableColumn quantity = new TableColumn<Stock, Integer>("quantity");
        quantity.setMinWidth(70);
        quantity.setCellValueFactory(new PropertyValueFactory<Stock, Integer>("quantity"));

        tabel_data.getColumns().addAll(desc,catName,buy,sell, quantity);

        try {
            ArrayList<Stock> listStock = stockDao.getAll();
            ObservableList<Stock> obListTable = FXCollections.observableArrayList();
            obListTable.addAll(listStock);
            tabel_data.setItems(obListTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void handleEmployeeButtonAction() throws IOException {
        handleButtonAction(employeeBtn);
        createEmployeeTable();
        tabel_data.setVisible(true);
        judul.setVisible(true);
    }

    @FXML
    private void handleMahasiswaButtonAction() {
        handleButtonAction(mahasiswaBtn);
        createStudentTable();
        tabel_data.setVisible(true);
        judul.setVisible(true);
    }

    @FXML
    private void handleDepartemenButtonAction(){
        handleButtonAction(departemenBtn);
        createDepartmentTable();
        tabel_data.setVisible(true);
        judul.setVisible(true);
    }

    public void handleStockButtonAction() {
        handleButtonAction(stokBtn);
        createStokTable();
        tabel_data.setVisible(true);
        judul.setVisible(true);
    }
    private void showCRUDButtons(Button button) {
        currentActiveButton = button;
        currentActiveButton.setVisible(false);

        buttonContainer.setLayoutX(currentActiveButton.getLayoutX());
        buttonContainer.setLayoutY(currentActiveButton.getLayoutY());

        buttonContainer.setVisible(true);

        TranslateTransition slideIn = new TranslateTransition(Duration.seconds(0.5), buttonContainer);
        slideIn.setFromX(-buttonContainer.getWidth());
        slideIn.setToX(0);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), buttonContainer);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        ParallelTransition slideAndFadeIn = new ParallelTransition(slideIn, fadeIn);
        slideAndFadeIn.play();
    }

    private void resetAnimations() {
        if (currentActiveButton != null) {
            currentActiveButton.setVisible(true);
            currentActiveButton = null;
        }
        buttonContainer.setVisible(false);
        tabel_data.setVisible(false);
        history.setVisible(false);
        judul.setVisible(false);
    }


    public void handleAdd(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader;
        if(controller == 1) {
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/uas/addForm/add-mahasiswa.fxml"));
        } else if (controller == 2){
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/uas/addForm/add-pegawai.fxml"));
        } else if (controller == 3){
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/uas/addForm/add-department.fxml"));
        } else {
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/uas/addForm/add-stock.fxml"));
        }
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) addBtn.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("add");
        stage.show();
    }
    @FXML
    private void handleEditAction(ActionEvent event) throws IOException {
        if(tabel_data.getSelectionModel().getSelectedItems().size() == 1) {
            Stage stage = (Stage) tabel_data.getScene().getWindow();
            FXMLLoader fxmlLoader;

            HashMap<String, String> data = new HashMap<>();
            data.put("mode", "edit");

            if(controller == 1) {
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/uas/addForm/add-mahasiswa.fxml"));

                String nrp = ((Students) tabel_data.getSelectionModel().getSelectedItem()).getNrp();
                data.put("id", nrp);

                Scene scene = new Scene((Parent) fxmlLoader.load());

                scene.setUserData(data);
                ((AddMahasiswaController) fxmlLoader.getController()).setParameters(scene);
                ((AddMahasiswaController) fxmlLoader.getController()).addParameter("id", nrp);

                stage.setScene(scene);
            } else if (controller == 2){
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/uas/addForm/add-pegawai.fxml"));

                String nip = ((Employees) tabel_data.getSelectionModel().getSelectedItem()).getNip();
                data.put("id", nip);

                Scene scene = new Scene((Parent) fxmlLoader.load());

                scene.setUserData(data);
                ((AddPegawaiController) fxmlLoader.getController()).setParameters(scene);
                ((AddPegawaiController) fxmlLoader.getController()).addParameter("id", nip);

                stage.setScene(scene);

            } else if (controller == 3){
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/uas/addForm/add-department.fxml"));

                String department_name = ((Departments) tabel_data.getSelectionModel().getSelectedItem()).getName();
                data.put("id", String.valueOf(department_name));

                Scene scene = new Scene((Parent) fxmlLoader.load());

                scene.setUserData(data);
                ((AddDepartmentController) fxmlLoader.getController()).setParameters(scene);
                ((AddDepartmentController) fxmlLoader.getController()).addParameter("id", department_name);

                stage.setScene(scene);
            } else if(controller == 4){
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/uas/addForm/add-stock.fxml"));

                String description= ((Stock) tabel_data.getSelectionModel().getSelectedItem()).getDescription();
                data.put("description", description);

                Scene scene = new Scene((Parent) fxmlLoader.load());

                scene.setUserData(data);
                ((AddStockController) fxmlLoader.getController()).setParameters(scene);
                ((AddStockController) fxmlLoader.getController()).addParameter("description", description);

                stage.setScene(scene);
            }
            stage.setTitle("Edit");
            stage.show();
        } else if(tabel_data.getSelectionModel().getSelectedItems().isEmpty()) {
            showWarningDialog("Error", "No data selected");
        }
    }
    public void handleDelete(ActionEvent event) throws SQLException {
        if(tabel_data.getSelectionModel().getSelectedItems().size() == 1) {
            if(controller == 1){
                Students students = (Students) tabel_data.getSelectionModel().getSelectedItem();
                StudentDao studentDao = new StudentDao();
                try{
                    studentDao.delete(students.getNrp());
                }catch (SQLException e) {
                    throw new SQLException();
                }
                createStudentTable();
            }
            else if(controller == 2) {
                Employees employees = (Employees) tabel_data.getSelectionModel().getSelectedItem();
                EmployeeDao employeeDao = new EmployeeDao();
                try {
                    employeeDao.delete(employees.getNip());
                } catch (SQLException e) {
                    throw new SQLException();
                }
                createEmployeeTable();
            }else if(controller == 3){
                Departments departments = (Departments) tabel_data.getSelectionModel().getSelectedItem();
                DepartmentDao departmentDao = new DepartmentDao();
                try{
                    departmentDao.delete(String.valueOf(departments.getDepartmentId()));
                }catch (SQLException e) {
                    throw new SQLException();
                }
                createDepartmentTable();
            }else if(controller == 4){
                Stock stock = (Stock) tabel_data.getSelectionModel().getSelectedItem();
                StockDao stockDao = new StockDao();
                try{
                    stockDao.delete(stock.getDescription());
                }catch (SQLException e) {
                    throw new SQLException();
                }
                createStokTable();
            }
        } else {
            showWarningDialog("Error", "No data selected");
        }
    }
    public void assignManagerBtn(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/uas/assignForm/assign-manager.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) addBtn.getScene().getWindow();
        stage.setMinWidth(727);
        stage.setScene(new Scene(root));
        stage.setTitle("Assign Manager");
        stage.show();
    }
    public void historyButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("viewForm/history-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) history.getScene().getWindow();
        stage.setTitle("History View");
        stage.setScene(new Scene(root));
    }

    public void transaksiButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("viewForm/transaksi-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) transaksiBtn.getScene().getWindow();
        stage.setTitle("Transaksi Home View");
        stage.setScene(new Scene(root));
    }
    private void showWarningDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void export(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("Portable Document Format files (*.pdf)", "*.pdf");
        chooser.getExtensionFilters().add(pdfFilter);

        chooser.setInitialDirectory(new File("C:\\Users"));
        File file = chooser.showSaveDialog(tabel_data.getScene().getWindow());
        FileChooser.ExtensionFilter selectedFilter = chooser.getSelectedExtensionFilter();

        if(file != null) {
            exportToPdf(file);
        }
    }

    private void exportToPdf(File file) {
        PdfDocument pdfDocument = null;
        try {
            pdfDocument = new PdfDocument(new PdfWriter(file.getAbsolutePath()));
            Document doc = new Document(pdfDocument);

            Paragraph mainTitle = new Paragraph("Keanggotan Koperasi");
            mainTitle.setBold();
            mainTitle.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
            mainTitle.setFontSize(21);





            Table table = new Table(UnitValue.createPercentArray(new float[] {20,20,20,15,15,10})).useAllAvailableWidth();
            com.itextpdf.layout.element.Cell emptyCell = new com.itextpdf.layout.element.Cell(1,1);
            emptyCell.setBorder(Border.NO_BORDER);
            for(int i = 0; i < tabel_data.getColumns().size(); i++) {
                TableColumn col = (TableColumn) tabel_data.getColumns().get(i);
                com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell();
                Paragraph title = new Paragraph(col.getText());
                title.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
                title.setBold();

                headerCell.add(title);
                table.addCell(headerCell);
            }
            table.addCell(emptyCell);

            createEmployeeTable();

            for(int i = 0; i < tabel_data.getItems().size(); i++) {
                Employees data = (Employees) tabel_data.getItems().get(i);

                Paragraph nipParagraph = new Paragraph(data.getNip());
                com.itextpdf.layout.element.Cell nipCell = new com.itextpdf.layout.element.Cell().add(nipParagraph);
                table.addCell(nipCell);

                Paragraph nameParagraph = new Paragraph(data.getNama());
                com.itextpdf.layout.element.Cell nameCell = new com.itextpdf.layout.element.Cell().add(nameParagraph);
                table.addCell(nameCell);

                Paragraph emailParagraph = new Paragraph(data.getEmail());
                com.itextpdf.layout.element.Cell emailCell = new com.itextpdf.layout.element.Cell().add(emailParagraph);
                table.addCell(emailCell);

                Paragraph departmentParagraph = new Paragraph(String.valueOf(data.getDepartmentName()));
                com.itextpdf.layout.element.Cell departmentCell = new com.itextpdf.layout.element.Cell().add(departmentParagraph);
                table.addCell(departmentCell);

                Paragraph managerParagraph = new Paragraph(String.valueOf(data.getManager()));
                com.itextpdf.layout.element.Cell managerCell = new com.itextpdf.layout.element.Cell().add(managerParagraph);
                table.addCell(managerCell);

                table.addCell(emptyCell);
            }


            doc.add(mainTitle);

            createStudentTable();
            for(int i = 0; i < tabel_data.getItems().size(); i++) {
                Students data = (Students) tabel_data.getItems().get(i);

                Paragraph nipParagraph = new Paragraph(data.getNrp());
                com.itextpdf.layout.element.Cell nipCell = new com.itextpdf.layout.element.Cell().add(nipParagraph);
                table.addCell(nipCell);

                Paragraph nameParagraph = new Paragraph(data.getStudent_name());
                com.itextpdf.layout.element.Cell nameCell = new com.itextpdf.layout.element.Cell().add(nameParagraph);
                table.addCell(nameCell);

                Paragraph emailParagraph = new Paragraph(data.getEmail());
                com.itextpdf.layout.element.Cell emailCell = new com.itextpdf.layout.element.Cell().add(emailParagraph);
                table.addCell(emailCell);

                Paragraph departmentParagraph = new Paragraph(String.valueOf(data.getDepartmentName()));
                com.itextpdf.layout.element.Cell departmentCell = new com.itextpdf.layout.element.Cell().add(departmentParagraph);
                table.addCell(departmentCell);

                Paragraph managerParagraph = new Paragraph(String.valueOf(data.getManagerName()));
                com.itextpdf.layout.element.Cell managerCell = new com.itextpdf.layout.element.Cell().add(managerParagraph);
                table.addCell(managerCell);

                table.addCell(emptyCell);
            }



            doc.add(table);

            doc.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void exportDepartment(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("Portable Document Format files (*.pdf)", "*.pdf");
        chooser.getExtensionFilters().add(pdfFilter);

        chooser.setInitialDirectory(new File("C:\\Users"));
        File file = chooser.showSaveDialog(tabel_data.getScene().getWindow());
        FileChooser.ExtensionFilter selectedFilter = chooser.getSelectedExtensionFilter();

        if(file != null) {
            exportToPdfDepartment(file);
        }
    }

    private void exportToPdfDepartment(File file) {
        tabel_data.getColumns().clear();
        tabel_data.getColumns().removeAll();
        PdfDocument pdfDocument = null;
        try {
            pdfDocument = new PdfDocument(new PdfWriter(file.getAbsolutePath()));
            Document doc = new Document(pdfDocument);

            Paragraph mainTitle = new Paragraph("Jumlah Anggota per Department");
            mainTitle.setBold();
            mainTitle.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
            mainTitle.setFontSize(21);


            getDepartmentData();


            Table table = new Table(UnitValue.createPercentArray(new float[] {20,15, 65})).useAllAvailableWidth();
            com.itextpdf.layout.element.Cell emptyCell = new com.itextpdf.layout.element.Cell(1,1);
            emptyCell.setBorder(Border.NO_BORDER);
            for(int i = 0; i < tabel_data.getColumns().size(); i++) {
                TableColumn col = (TableColumn) tabel_data.getColumns().get(i);
                com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell();
                Paragraph title = new Paragraph(col.getText());
                title.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
                title.setBold();

                headerCell.add(title);
                table.addCell(headerCell);
            }
            table.addCell(emptyCell);


            for(int i = 0; i < tabel_data.getItems().size(); i++) {
                Departments data = (Departments) tabel_data.getItems().get(i);

                Paragraph nameParagraph = new Paragraph(data.getName());
                com.itextpdf.layout.element.Cell nameCell = new com.itextpdf.layout.element.Cell().add(nameParagraph);
                table.addCell(nameCell);

                Paragraph countParagraph = new Paragraph(String.valueOf(data.getCount()));
                com.itextpdf.layout.element.Cell countCell = new com.itextpdf.layout.element.Cell().add(countParagraph);
                table.addCell(countCell);

                table.addCell(emptyCell);
            }

            doc.add(mainTitle);
            doc.add(table);
            doc.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void getDepartmentData() {

        tabel_data.getColumns().removeAll();

        TableColumn departmentName = new TableColumn<Departments, String>("Department Name");
        departmentName.setMinWidth(140);
        departmentName.setCellValueFactory(new PropertyValueFactory<Employees, String>("Name"));

        TableColumn count = new TableColumn<Departments, Integer>("jumlah employees");
        count.setMinWidth(200);
        count.setCellValueFactory(new PropertyValueFactory<Employees, Integer>("Count"));

        tabel_data.getColumns().addAll(departmentName, count);

        DepartmentDao departmentDao = new DepartmentDao();
        ArrayList<Departments> listDepartment = departmentDao.getDepartmentData();

        ObservableList<Departments> obListTable = FXCollections.observableArrayList();
        obListTable.addAll(listDepartment);
        tabel_data.setItems(obListTable);
    }
}