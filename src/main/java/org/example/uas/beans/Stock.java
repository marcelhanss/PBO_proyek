package org.example.uas.beans;

public class Stock {
    private int stock_id;
    private String description;
    private String categoryName;
    private int buyingPrice;
    private int sellingPrice;
    private int quantity;

    private int categoryId;
    public Stock(){

    }

    public Stock(int stock_id, String description, String categoryName, int buyingPrice, int sellingPrice, int quantity, int categoryId) {
        this.stock_id = stock_id;
        this.description = description;
        this.categoryName = categoryName;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
        this.categoryId = categoryId;
    }

    public Stock(String description, String categoryName, int buyingPrice, int sellingPrice, int quantity) {
        this.description = description;
        this.categoryName = categoryName;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
    }

    public int getStock_id() {
        return stock_id;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(int buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
