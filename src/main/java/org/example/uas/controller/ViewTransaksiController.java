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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.uas.HelloApplication;
import org.example.uas.beans.Transaksi;
import org.example.uas.dao.TransaksiDao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

public class ViewTransaksiController implements Initializable {

    @FXML
    private Button back_button;
    @FXML
    private TableView<Transaksi> tabel_transaksi;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createTransactionTable();
    }

    public void backButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("viewForm/transaksi-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) back_button.getScene().getWindow();
        stage.setTitle("Transaksi");
        stage.setScene(new Scene(root));


    }

    private void createTransactionTable() {
        tabel_transaksi.getColumns().clear();

        TransaksiDao transaksiDao = new TransaksiDao();

        TableColumn transactionDate = new TableColumn<Transaksi, String>("Transaction Date");
        transactionDate.setMinWidth(75);
        transactionDate.setCellValueFactory(new PropertyValueFactory<Transaksi, String>("dateString"));

        TableColumn seller = new TableColumn<Transaksi, String>("Name");
        seller.setMinWidth(125);
        seller.setCellValueFactory(new PropertyValueFactory<Transaksi, String>("seller"));

        TableColumn namaBarang = new TableColumn<Transaksi, String>("Nama Barang");
        namaBarang.setMinWidth(145);
        namaBarang.setCellValueFactory(new PropertyValueFactory<Transaksi, String>("namaBarang"));

        TableColumn quantity = new TableColumn<Transaksi, Integer>("Quantity");
        quantity.setMinWidth(75);
        quantity.setCellValueFactory(new PropertyValueFactory<Transaksi, Integer>("quantity"));

        TableColumn totalJual = new TableColumn<Transaksi, Integer>("Total Jual");
        totalJual.setMinWidth(125);
        totalJual.setCellValueFactory(new PropertyValueFactory<Transaksi, Integer>("totalJual"));

        TableColumn profit = new TableColumn<Transaksi, Integer>("Profit");
        profit.setMinWidth(125);
        profit.setCellValueFactory(new PropertyValueFactory<Transaksi, Integer>("profit"));

        tabel_transaksi.getColumns().addAll(transactionDate, seller, namaBarang, quantity, totalJual, profit);

        try {
            ArrayList<Transaksi> listTransaksi = transaksiDao.getAll();
            ObservableList<Transaksi> obListTable = FXCollections.observableArrayList(listTransaksi);
            tabel_transaksi.setItems(obListTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void export(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("Portable Document Format files (*.pdf)", "*.pdf");
        chooser.getExtensionFilters().add(pdfFilter);

        chooser.setInitialDirectory(new File("C:\\Users"));
        File file = chooser.showSaveDialog(tabel_transaksi.getScene().getWindow());
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

            Paragraph mainTitle = new Paragraph("Laporan Transaksi");
            mainTitle.setBold();
            mainTitle.setTextAlignment(TextAlignment.CENTER);
            mainTitle.setFontSize(21);

            Table table = new Table(UnitValue.createPercentArray(new float[] {20,20,20,5,15,15,5})).useAllAvailableWidth();
            Cell emptyCell = new Cell(1,1);
            emptyCell.setBorder(Border.NO_BORDER);
            for(int i = 0; i < tabel_transaksi.getColumns().size(); i++) {
                TableColumn col = (TableColumn) tabel_transaksi.getColumns().get(i);
                Cell headerCell = new Cell();
                Paragraph title = new Paragraph(col.getText());
                title.setTextAlignment(TextAlignment.CENTER);
                title.setBold();

                headerCell.add(title);
                table.addCell(headerCell);
            }
            table.addCell(emptyCell);

            for(int i = 0; i < tabel_transaksi.getItems().size(); i++) {
                Transaksi data = (Transaksi) tabel_transaksi.getItems().get(i);

                Paragraph dateParagraph = new Paragraph(data.getDateString());
                dateParagraph.setTextAlignment(TextAlignment.CENTER);
                Cell dateCell = new Cell().add(dateParagraph);
                table.addCell(dateCell);

                Paragraph nameParagraph = new Paragraph(data.getSeller());
                Cell nameCell = new Cell().add(nameParagraph);
                table.addCell(nameCell);

                Paragraph barangParagraph = new Paragraph(data.getNamaBarang());
                Cell barangCell = new Cell().add(barangParagraph);
                table.addCell(barangCell);

                Paragraph quantityParagraph = new Paragraph(String.valueOf(data.getQuantity()));
                Cell quantityCell = new Cell().add(quantityParagraph);
                quantityCell.setTextAlignment(TextAlignment.CENTER);
                table.addCell(quantityCell);

                Paragraph totalJualParagraph = new Paragraph(String.valueOf(data.getTotalJual()));
                Cell totalJualCell = new Cell().add(totalJualParagraph);
                totalJualCell.setTextAlignment(TextAlignment.CENTER);
                table.addCell(totalJualCell);

                Paragraph profitParagraph = new Paragraph(String.valueOf(data.getProfit()));
                Cell profitCell = new Cell().add(profitParagraph);
                profitCell.setTextAlignment(TextAlignment.CENTER);
                table.addCell(profitCell);
                table.addCell(emptyCell);
            }
            TransaksiDao transaksiDao = new TransaksiDao();

            Paragraph totalProfitParagraph = new Paragraph("Total Profit : " + String.valueOf(transaksiDao.getTotalProfit()));
            totalProfitParagraph.setBold();
            Cell totalProfitCell = new Cell(1, 7).add(totalProfitParagraph);
            totalProfitCell.setBorder(Border.NO_BORDER);
            totalProfitCell.setFontSize(18);
            table.addCell(totalProfitCell);

            String bestSeller = "";
            for (int i = 0; i < transaksiDao.getBestSellers().size(); i++) {
                bestSeller += transaksiDao.getBestSellers().get(i);
                if(i != transaksiDao.getBestSellers().size() - 1) {
                    bestSeller += ", ";
                }
            }

            Paragraph bestSellerParagraph = new Paragraph("Best Seller : " + bestSeller);
            bestSellerParagraph.setBold();
            Cell bestSellerCell = new Cell(1, 7).add(bestSellerParagraph);
            bestSellerCell.setBorder(Border.NO_BORDER);
            bestSellerCell.setFontSize(18);
            table.addCell(bestSellerCell);

            String bestSellings = "";
            for (int i = 0; i < transaksiDao.getBestSellingItem().size(); i++) {
                bestSellings += transaksiDao.getBestSellingItem().get(i);

                if(i != transaksiDao.getBestSellingItem().size() - 1) {
                    bestSellings += ", ";
                }
            }

            Paragraph bestSellingParagraph = new Paragraph("Best Selling Item : " + bestSellings);
            bestSellingParagraph.setBold();
            Cell bestSellingCell = new Cell(1, 7).add(bestSellingParagraph);
            bestSellingCell.setBorder(Border.NO_BORDER);
            bestSellingCell.setFontSize(18);
            table.addCell(bestSellingCell);

            doc.add(mainTitle);
            doc.add(table);
            doc.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
