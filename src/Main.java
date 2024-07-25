import model.CartItem;
import model.Product;
import model.Sale;
import services.AuthenticationService;
import services.CartService;
import services.ProductManagementService;
import services.SaleManagementService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // here we create the services
        AuthenticationService auth=new AuthenticationService();
        ProductManagementService pm= new ProductManagementService(auth);
        SaleManagementService sm=new SaleManagementService();
        CartService cs= new CartService();

        //Task 1: Basic Product Management

//        //Get the list of all the product
//        List<Product> productList=pm.getProductList();
//        for(Product product:productList)
//            System.out.println(product.toString());
//        Map<String,Object>updates=new HashMap<>();
//        updates.put("name","IPHONE Phone");
//        updates.put("quantity",2);
//        updates.put("price",15.00);
//        // update the product in the db pass in the function the product id and map of the field to update
//        pm.updateProduct(3,updates);
//        // get a product by ID
//        System.out.println(pm.getProductById(3).toString());

//        // Worth noting that the add and delete Product will be displayed in Task 5 since it requires user authentication



//        //Task 2: Simple Checkout Process
//
//        // display list of item in the cart
//        List<CartItem> cartItems=cs.getAllCartItem();
//        for(CartItem cartItem:cartItems)
//            System.out.println("Cart Item, Id: "+ cartItem.getId()+" Quantity: "+ cartItem.getQuantity()+ " { "+cartItem.getProduct().toString()+" }");
//
//      // add A product to the cart takes ( the productid and quantity)
//        cs.addProductToCart(4,4);
//
//
//        // delete a  product from the cart pass productId
//        cs.removeProductFromCart(1);
//
//        // calculate the total price of the whole cart it is worth noting over 100 dollar will get 10% discount
//        System.out.println(cs.getTotalAmount());


//        // Checkout (sell the item in cart) it takes all the cartItems  and delete them
//        // as well update the product table by reducing the quantity after selling (Task3)
//        // as well  add Sale record of each cartItem before they get deleted (Task 4)
//        cs.checkout();


        //Task 3 Inventory Management

//        // alert for shortage in products
//         System.out.println(pm.checkStockLevels());

//        // report of products needs to be order
//        List<Product> productList= pm.reportOfProductToReorder();
//        for(Product product: productList)
//            System.out.println(product.getName()+" is low on stock with only"+product.getQuantity());
//        if(productList.size()==0)
//            System.out.println("there is no shortage");
//


//        //Task 4 : Sales Report Generation
//
//        //Generate Sale report for the current day +revenue+ total Sales
//        // this return a map with field of sales (List of sales) revenue(Double) quantity(int)
//        Map<String,Object>dailySale=sm.getSalesOfCurrentDayAndRevenueAndQuantity();
//        List<Sale> sales= (List<Sale>) dailySale.getOrDefault("sales",new ArrayList<>());
//        double revenue= (Double) dailySale.getOrDefault("revenue",0.0);
//        int quantity=(Integer)dailySale.getOrDefault("quantity",0);
//        for(Sale sale:sales )
//            System.out.println(sale.toString());
//
//        System.out.println("Revenue: "+revenue+" quantity: "+quantity);
//
//        // filter sales Report  by date (date2 > date1)
//        LocalDate date1= LocalDate.of(2024,7,22);
//        LocalDate date2= LocalDate.of(2024,7,26);
//        List<Sale> saleList=sm.getSalesBetweenTwoDate(Date.valueOf(date1),Date.valueOf(date2));
//        for(Sale sale:saleList )
//            System.out.println(sale.toString());
//
//        // get two top selling product
//        Map<Integer,Integer> map=sm.displayTopSellingProduct();
//        System.out.println(" the two top product are:");
//          for(Map.Entry<Integer,Integer> sale:map.entrySet())
//              System.out.println(sale.getKey()+" "+pm.getProductById(sale.getKey()).getName()+"   "+sale.getValue());



//         Task 5:User Authentication


//        // register a new user in case the username exist it wont regestir (password hashed using md5)
//        auth.registerUser("georgesHayek","georges123");
//
//
//        // user doesnt exist session will become equal null and wrong credential message will appear
//       String session=auth.AuthenticateUser("nobu999","qwerty123");
//       //Succesful login
//       session=auth.AuthenticateUser("nobu99","qwerty123");
//
//        // testing the session
//        // now we create a product and delete one as mentioned in Task 1 its wroth noting if any of the numbers negative it will threw exceptiion due to validation
//        Product product1=new Product("LG Phone",16.0,6,2);
//        pm.addProduct(product1,session);
//
//        //now we delete product
//            for(Product product: pm.getProductList())
//                System.out.println(product.toString());
//            pm.deleteProductById(3,session);

    }
}