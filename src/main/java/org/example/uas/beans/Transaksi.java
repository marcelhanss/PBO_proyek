package org.example.uas.beans;
public class Transaksi {
    String dateString;
    private Date date;
    private String seller;
    private int employeeId;
    private int studentId;
    private String namaBarang;
    private int stockId;
    private int quantity;
    private int totalJual;
    private int profit;
    String sellerType;

    public Transaksi() {

    }

    public Transaksi(Date date, String seller, int employeeId, int studentId, String namaBarang, int stockId, int quantity, int totalJual, int profit, String sellerType) {
        this.date = date;
        this.seller = seller;
        this.employeeId = employeeId;
        this.namaBarang = namaBarang;
        this.stockId = stockId;
        this.quantity = quantity;
        this.totalJual = totalJual;
        this.profit = profit;
        this.studentId = studentId;
        this.sellerType = sellerType;
    }

    public Transaksi(String dateString, String namaSeller, String namaBarang, int quantity, int totalJual, int profit) {
        this.dateString = dateString;
        this.seller = namaSeller;
        this.namaBarang = namaBarang;
        this.quantity = quantity;
        this.totalJual = totalJual;
        this.profit = profit;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalJual() {
        return totalJual;
    }

    public void setTotalJual(int totalJual) {
        this.totalJual = totalJual;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getSellerType() {
        return sellerType;
    }

    public void setSellerType(String sellerType) {
        this.sellerType = sellerType;
    }
}
