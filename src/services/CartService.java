package services;

import connection.Database;
import model.CartItem;
import model.Product;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CartService {
    public CartService() {

    }

// this function will return what is the total amount of money needed to buy everything in the cart + shows that a discount is available when we pass a certain amount
    public double getTotalAmount(){
        try(Connection conn= Database.getConnection();
            Statement stmt= conn.createStatement();
            // it will return the sum of all the product in the cart
            ResultSet rs= stmt.executeQuery("SELECT SUM(products.price*cart_items.quantity) as total_amount from cart_items JOIN products on cart_items.product_id=products.id")){
           if(rs.next())
           {double totalAmount=rs.getDouble("total_amount");
             return totalAmount>=100?totalAmount*0.9:totalAmount;
         }
        }
        catch (SQLException e){e.printStackTrace();}
      return 0.0;
    }
    // this will return all the items in the shopping cart
    public List<CartItem> getAllCartItem(){
        List<CartItem> cartItems=new ArrayList<>();
        try(Connection conn= Database.getConnection();
            Statement stmt= conn.createStatement();
            ResultSet rs= stmt.executeQuery("SELECT products.id as product_id,products.name,products.price,products.quantity as product_quantity,products.reorderLevel,cart_items.quantity as cart_quantity,cart_items.id as cart_item_id from cart_items JOIN products on cart_items.product_id=products.id"))
        {
            while(rs.next()){
                // here we collect the data from the query
                int id=rs.getInt("cart_item_id");
                int productId=rs.getInt("product_id");
                String name=rs.getString("name");
                double price=rs.getDouble("price");
                int productQuantity=rs.getInt("product_quantity");
                int productReorderLevel=rs.getInt("reorderLevel");
                int quantity=rs.getInt("cart_quantity");
                // and here we create a new cartItem from the data collected than gets added to the list
                CartItem cartItem=new CartItem(id,new Product(productId,name,price,productQuantity,productReorderLevel),quantity);
                cartItems.add(cartItem);
            }
            return cartItems;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
// this will return the product by Id from the product table
    public Product getProductById(int id) {
        try(
                Connection conn= Database.getConnection();
                PreparedStatement stmt= conn.prepareStatement("SELECT id,name,price,quantity,reorderLevel from products WHERE id=?")

        ){
            stmt.setInt(1,id);
            ResultSet rs=stmt.executeQuery();
            if(rs.next()){
                return new Product(id,rs.getString("name"),rs.getDouble("price"),rs.getInt("quantity"),rs.getInt("reorderLevel"));
            }
        }catch (SQLException e){
            e.printStackTrace();

        }
        return null;
    }
// this adds product to the cart
    public void addProductToCart(int id,int quantity){
        Product product=getProductById(id);
        // first we check if the user exist
        if(product==null){
            System.out.println("this product doesnt exist");
        }
        else{
            // if exist we check if its out of stock or the quantity that we want isnt available currently
            if(product.getQuantity()==0|| quantity>product.getQuantity())
                System.out.println("its out of stock");
            else{
                // in case if the quantity are available to be added to the cart we now check for two condition
                // first condition check if the product already exist in the cart which will lead us to add the quantity of the item in the cart
                // second in case the product doesnt exist in the cart we create a new cartItem with the quantity asked for
                try (
                        Connection conn= Database.getConnection();
                        PreparedStatement stmt= conn.prepareStatement("SELECT id,quantity,product_id from cart_items WHERE product_id=?")
                ){
                    stmt.setInt(1,id);
                    ResultSet rs=stmt.executeQuery();
                    if(rs.next()){
                        // for the first condition we first check if we added the new quantity to the already existing in the cart is more than we already posses in the inventory
                        int newItemQuantity=rs.getInt("quantity")+quantity;
                        if(newItemQuantity<=product.getQuantity())
                        {
                            // if the new quantity in the cart less than in our inventory we can then update the database to the new quantity
                            try(PreparedStatement updateStatement = conn.prepareStatement("update cart_items Set quantity=? WHERE product_id=?")) {
                            updateStatement.setInt(1,newItemQuantity);
                            updateStatement.setInt(2,id);
                            updateStatement.executeUpdate();
                            System.out.println("added to the database");
                            }
                        }
                        // in case the new quantity is more  than in our inventory
                        else System.out.println("you've gone over the limit");
                    }
                    else{
                        // for the second condition we insert the new item in the cart
                        try(PreparedStatement insertStatement= conn.prepareStatement("INSERT INTO cart_items(product_id,quantity) values (?,?)")){
                            insertStatement.setInt(1,id);
                            insertStatement.setInt(2,quantity);
                            insertStatement.executeUpdate();
                            System.out.println("added to the database");
                        }
                    }

                    } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    // so here we remove the product from the shopping cart by productId
    public void removeProductFromCart(int productId){
        try(Connection conn= Database.getConnection(); PreparedStatement deleteStatement = conn.prepareStatement("Delete from cart_items  WHERE product_id=?")) {
            deleteStatement.setInt(1,productId);
            int count=deleteStatement.executeUpdate();
            if(count!=0)
                System.out.println("the product has been removed from the cart the cart");
            else
                System.out.println("the product doesnt exist in the cart");
    }catch (SQLException e){
            e.printStackTrace();
        }
    }
    // when we checkout we must store new records in our sales table
    // we update the products table since inventory has been sold
    // we remove all elements from the cart
    public void checkout(){
        // so in the first sql query we use so we can gett info of the cartItem and the product that we can use to store in our sales table and update our product tables
        String sql="SELECT cart_items.product_id,products.quantity as inventory_quantity,products.price,cart_items.quantity as sold_quantity from cart_items JOIN products on cart_items.product_id=products.id";
        //in the second query we will be inserting info from the cartItem to the sales  (Task 4 record sale)
        String sql2="INSERT INTO sales (product_id,quantity_sold,sale_date,total_amount) values (?,?,?,?)";
        // we will be updating the products quantity in the products table  (Task 3 reduce on sold)
        String sql3="UPDATE products Set quantity=? where id=? ";
        // delete items from the cart after sold
        String sql4="Delete from cart_items";
        try (Connection conn = Database.getConnection();
             Statement stmt1 = conn.createStatement();
             PreparedStatement stmt2=conn.prepareStatement(sql2);
             PreparedStatement stmt3=conn.prepareStatement(sql3);
             PreparedStatement stmt4=conn.prepareStatement(sql4);
             ResultSet rs=stmt1.executeQuery(sql)
        ) {
            // so first we get the data from the first query
            while (rs.next())
            {
                // get the product info
                int id=rs.getInt("product_id");
                int quantitySold=rs.getInt("sold_quantity");
                int quantityInventory=rs.getInt("inventory_quantity");
                // here we calculate the total revenue from the quantity we sold of certain item (this is used in sale)
                double totalAmount=rs.getDouble("price")*quantitySold;
                // we get the new quantity after the sell of certain item but we can stop the process
                // in case in the sale we're selling more than we have in our inventory
                int quantityLeft=quantityInventory-quantitySold;
                if(quantityLeft<0){
                    System.out.println("the sale cant proceed since we gone over stock");
                    conn.rollback();
                    return;
                }
                // in case if quantityLeft is more than 0 we will start preparing multiple batch of sales to all be stored  to the sales
                stmt2.setInt(1,id);
                stmt2.setInt(2,quantitySold);
                stmt2.setDate(3 ,java.sql.Date.valueOf(LocalDate.now()));
                stmt2.setDouble(4,totalAmount);
                stmt2.addBatch();
                // here we set the new quantity of the product after the sale and we add them in a batch so we can update all the product at the same time
                stmt3.setInt(1,quantityLeft);
                // selecting which product to be updated
                stmt3.setInt(2,id);
                stmt3.addBatch();
            }
            // executing the  insertion to the sales
            stmt2.executeBatch();
           // updating the product table
            stmt3.executeBatch();
            // deleting all the cartitem in the cart
            stmt4.executeUpdate();
    }catch (SQLException e){
        e.printStackTrace();}
    }

}
