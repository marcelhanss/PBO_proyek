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
import org.example.uas.beans.Stock;
import org.example.uas.dao.StockDao;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AddStockController implements Initializable {
    public Button submit_Button;
    public Button back_Button;
    private HashMap<String, String> parameters = new HashMap<>();
    @FXML
    public TextField description_field;
    @FXML
    public TextField buy_field;
    @FXML
    public TextField sell_field;
    @FXML
    public TextField quantity_field;
    @FXML
    public ComboBox<String> cetegory_combo_box;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setComboBoxStock();
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
    private void setComboBoxStock() throws SQLException {
        StockDao stockDao = new StockDao();

        ArrayList<String> listCategory = stockDao.getAllCategory();

        ObservableList<String> obListCategory = FXCollections.observableArrayList();
        obListCategory.addAll(listCategory);

        cetegory_combo_box.setItems(obListCategory);

        cetegory_combo_box.setButtonCell(new ListCell<String>() {
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
        if(containsParameter("description")) {
            Stock stock = StockDao.get(getParameter("description"));
            description_field.setText(stock.getDescription());

            buy_field.setText(String.valueOf(stock.getBuyingPrice()));
            sell_field.setText(String.valueOf(stock.getSellingPrice()));
            quantity_field.setText(String.valueOf(stock.getQuantity()));
            cetegory_combo_box.setValue(stock.getCategoryName());
        }
    }

    public void addParameter(String key, String value) {parameters.put(key,value);}
    public void setParameters(Scene scene) {
        parameters = (HashMap<String, String>) scene.getUserData();
        processParameters();
    }

    protected boolean containsParameter(String key) {
        return parameters.containsKey(key);
    }

    protected String getParameter(String key) {return parameters.get(key);}


    public Stock getDataInternal(StockDao stockDao) throws SQLException {
        Stock stock = new Stock();
        stock.setDescription(description_field.getText());
        stock.setBuyingPrice(Integer.parseInt(buy_field.getText()));
        stock.setSellingPrice(Integer.parseInt(sell_field.getText()));
        stock.setQuantity(Integer.parseInt(quantity_field.getText()));

        int category_id = stockDao.findRightCategoryId(cetegory_combo_box.getValue());
        stock.setCategoryId(category_id);
        return stock;
    }

    public void submitButton(ActionEvent actionEvent) throws SQLException, IOException {
        StockDao stockDao = new StockDao();
        Stock stock= getDataInternal(stockDao);
        if(stock.getStock_id() == 0 || stock.getDescription().isEmpty() || stock.getCategoryName().isEmpty() ||
        stock.getBuyingPrice() == 0 || stock.getSellingPrice() == 0 || stock.getQuantity() == 0){
            showWarningDialog("Error", "Please fill the column correctly");
        }else {
            if (containsParameter("mode") && getParameter("mode").equalsIgnoreCase("edit")) {
                String description = (getParameter("description"));
                stockDao.update(stock, description);
            } else {
                stockDao.save(stock);

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
