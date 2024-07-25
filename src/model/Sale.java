package model;

import java.util.Date;

public class Sale {
    private int id;
    private int productId;
    private int quantitySold;
    private Date saleDate;
    private  double totalAmount;

    public Sale(int id,int productId, int quantitySold, Date saleDate, double totalAmount) {
        this.id=id;
        this.productId = productId;
        this.quantitySold = quantitySold;
        this.saleDate = saleDate;
        this.totalAmount = totalAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }


    public String toString(){
         return "id: "+ this.id+" productId: "+this.productId+" totalAmount: "+this.totalAmount+" SaleDate: "+this.saleDate+" quantity: "+this.quantitySold;
    }
}
