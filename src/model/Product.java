package model;

public class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private int reorderLevel;

    public Product( String name, double price, int quantity, int reorderLevel) {
        if(price<0)
            throw  new IllegalArgumentException("price cant be negative");
        if(quantity<0)
            throw  new IllegalArgumentException("quantity cant be negative");
        if(reorderLevel<0)
            throw  new IllegalArgumentException("reorderLevel cant be negative");
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.reorderLevel=reorderLevel;
    }

    public Product(int id, String name, double price, int quantity, int reorderLevel) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
    }

    public Product() {
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if(price<0)
            throw  new IllegalArgumentException("price cant be negative");
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if(quantity<0)
            throw  new IllegalArgumentException("quantity cant be negative");
        this.quantity = quantity;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        if(reorderLevel<0)
            throw  new IllegalArgumentException("reorderLevel cant be negative");
        this.reorderLevel = reorderLevel;
    }
    @Override
    public String toString(){
        return "Product:"+this.id+" Name: "+this.name+" price: "+this.price+" quantity: "+this.quantity;
    }
}
