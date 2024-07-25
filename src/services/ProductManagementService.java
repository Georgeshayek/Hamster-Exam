package services;

import connection.Database;
import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductManagementService {
    // here we added authentication service in order to add authentication requirement for the product add and delete
    private final AuthenticationService auth;

    public ProductManagementService(AuthenticationService auth){
        this.auth=auth;
    }

    // first here we delete product by its id and in order to be able to do that you must be authenticated
    public void deleteProductById(int id,String sessionToken){
        // here we first check if your authenticated
        if(sessionToken==null|| !auth.checkIfUserIsAuthenticated(sessionToken))
        {
            System.out.println("unAuthorized Please Login first");
            return;
        }
        // in case you are authenticated you are then allowed to delete the product by id
        try (
                Connection conn= Database.getConnection();
                PreparedStatement stmt= conn.prepareStatement("Delete from  products where id=?")
        ){
            stmt.setInt(1,id);
            int count=stmt.executeUpdate();
            if(count!=0)
                System.out.println("deleted");
            else{
                System.out.println("id doesnt exist");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    // so here I created a function that will retrieve data from result set
    // and store them in list of product to avoid code redundancy
    public List<Product> getListOfProductFromResultSet(ResultSet rs) throws SQLException {
        List<Product> productList=new ArrayList<>();
        while (rs.next()){
            int id= rs.getInt("id");
            String name= rs.getString("name");
            double price=rs.getDouble("price");
            int quantity= rs.getInt("quantity");
            int reorderLevel=rs.getInt("reorderLevel");
            Product product=new Product(id,name,price,quantity,reorderLevel);
            productList.add(product);
        }
        return productList;

    }
// get list of all products
    public List<Product> getProductList() {
        List<Product>productList=new ArrayList<>();
        try (
                Connection conn= Database.getConnection();
                Statement stmt= conn.createStatement();
                ResultSet rs=stmt.executeQuery("SELECT id,name,price,quantity,reorderLevel from products")
        ){
            // here we get the data from ResultSet and store it in our list
            productList=getListOfProductFromResultSet(rs);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return productList;
    }
// we get the product by id
    public Product getProductById(int id){
        try(
                Connection conn= Database.getConnection();
                PreparedStatement stmt= conn.prepareStatement("SELECT id,name,price,quantity,reorderLevel from products WHERE id=?")

        ){
            stmt.setInt(1,id);
            ResultSet rs=stmt.executeQuery();
            if(rs.next()){
                return new  Product(id,rs.getString("name"),rs.getDouble("price"),rs.getInt("quantity"),rs.getInt("reorderLevel"));

            }
        }catch (SQLException e){
            e.printStackTrace();

        }
        return null;
    }

    //  adding product to the products table
    public void addProduct(Product product,String sessionToken){
        // check if user is logged in
        if(sessionToken==null|| !auth.checkIfUserIsAuthenticated(sessionToken))
        {
            System.out.println("unAuthorized Please Login first");
            return;
        }
        // if yes we insert  product to the products table
        try (
                Connection conn= Database.getConnection();
                PreparedStatement stmt= conn.prepareStatement("INSERT INTO products(name, price, quantity,reorderLevel) VALUES(?, ?, ?,?)")
        ){
            // entering the data to be inserted
            stmt.setString(1,product.getName());
            stmt.setDouble(2,product.getPrice());
            stmt.setInt(3,product.getQuantity());
            stmt.setInt(4,product.getReorderLevel());
            stmt.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    // here we update our product depending on dynamic fields we use map to store the fields to be updated
    public void updateProduct(int id, Map<String,Object> updates){
        // we first start by validating if there is any type of negative data being stored
        if(updates.containsKey("quantity")&&(Integer)updates.get("quantity")<0)
            throw new IllegalArgumentException("quantity cannot be negative");

        if(updates.containsKey("reorderLevel")&&(Integer)updates.get("reorderLevel")<0)
            throw new IllegalArgumentException("reorderLevel cannot be negative");

        if(updates.containsKey("price")&&(Double)updates.get("price")<0)
            throw new IllegalArgumentException("price cannot be negative");
        // StringBuilder was used because we will be looping through the updates map
        // in order to get all the fields to be updated and the new values since in each for loop we are updating the string
        // i ve implemented the StringBuilder since its considered way faster that using String +
        StringBuilder sb=new StringBuilder("UPDATE products SET ");
        // the new values used for product update
        List<Object> parameters=new ArrayList<>();
        for(Map.Entry<String,Object> entry: updates.entrySet()){
            // here we append the key to be updated, and we add =? as to represent the value that needs to be allocated
            sb.append(entry.getKey()).append("=?,");
            // storing the new values
            parameters.add(entry.getValue());
           }
        // we delete the last ',' before the condition to avoid the error
        sb.deleteCharAt(sb.length()-1);
        // here we append the condition as which product to update
        sb.append(" where id = ?");
        try (
                Connection conn= Database.getConnection();
                PreparedStatement stmt= conn.prepareStatement(sb.toString())
        ){
            // here we loop through the  new values and allocate them in the statement
            int i=0;
            while (i<parameters.size()){
                stmt.setObject(i+1,parameters.get(i));
                i++;
            }
            // here we allocate the id of the product, and then we execute the query
            stmt.setInt(i+1,id);
            int count=stmt.executeUpdate();
            if(count!=0)
                System.out.println("product updated");
            else{
                System.out.println("the product doesnt exist");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }// here we check if the stock is lower than the reorderLevel that we set
    public String checkStockLevels(){
        try(
                Connection conn= Database.getConnection();
                Statement stmt=conn.createStatement();
                 ResultSet rs= stmt.executeQuery("SELECT id, name from products WHERE quantity<reorderLevel")
        ){


            StringBuilder sb= new StringBuilder();
            while (rs.next()){
                 sb.append(rs.getString("name")).append(",");
            }
            return sb.isEmpty()?"we have no stock shortage": "warning stock shortage in "+sb.toString();

        }catch (SQLException e){
            e.printStackTrace();

        }
        return null;
    }
    // here we create a report representing all the  product that needs to be restocked
    public List<Product> reportOfProductToReorder() {
        List<Product> productList=new ArrayList<>();
        try (
                Connection conn = Database.getConnection();
                Statement stmt = conn.createStatement();
                // we check if  the reorder level is more than the quantity available
                ResultSet rs = stmt.executeQuery("SELECT id,name,price,quantity,reorderLevel from products where quantity<reorderLevel")
        ) {
            // here we add the product that needs to be restocked to a list
            productList=getListOfProductFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }
}



