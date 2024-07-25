package services;

import connection.Database;
import model.Sale;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaleManagementService {

    public SaleManagementService() {

    }
    // added a function to get data from resultSet in order to reduce redundancy
    public List<Sale> getSaleListFromResultSet(ResultSet rs) throws SQLException {
        List<Sale>sales=new ArrayList<>();
        while(rs.next())
        {
            //getting the data to store in sale than in the list
            int id=rs.getInt("id");
            int productId=rs.getInt("product_id");
            int quantitySold =rs.getInt("quantity_sold");
            double totalAmount=rs.getDouble("total_amount");
            Date date=rs.getDate("sale_date");
            Sale sale=new Sale(id,productId,quantitySold,date,totalAmount);
            sales.add(sale);
        }
        return sales;
    }

    // get the sales of the current day and the revenue and the quantity as
    // we will be sending data via map that contains list of sale, a double for revenue, integer for quantity
    public Map<String,Object> getSalesOfCurrentDayAndRevenueAndQuantity(){
        Map<String,Object> todayData=new HashMap<>();
       // get the day
        LocalDate today = LocalDate.now();
        Date todayDate=Date.valueOf(today);
        try(Connection conn= Database.getConnection();
            // the first query bring the  sales to store in a list for the day
            PreparedStatement stmt= conn.prepareStatement("SELECT id,product_id,quantity_sold,total_amount,sale_date from sales WHERE sale_date=?");
            // the second is to bring the total quantity and the revenue of the day
            PreparedStatement stmt2=conn.prepareStatement(" select SUM(quantity_sold) as quantity,SUM(total_amount) as revenue from sales where sale_date=?")
            ){
            stmt.setDate(1, todayDate);
            stmt2.setDate(1, todayDate);
            //here we execute the first query
            ResultSet rs= stmt.executeQuery();
            // here we  store the data of the query in a list
            List<Sale>sales=getSaleListFromResultSet(rs);
            // we store the list under the field sales
            todayData.put("sales",sales);
            rs.close();
            // now we execute the second query
             ResultSet rs1=stmt2.executeQuery();
             if(rs1.next()){
                 // get the data from the query
                 double revenue=rs1.getDouble("revenue");
                 int quantity=rs1.getInt("quantity");
                 // add the data to the map
                 todayData.put("revenue",revenue);
                 todayData.put("quantity",quantity);
             }
             rs1.close();
        }catch (SQLException e){e.printStackTrace();}
        return todayData;
    }

    // here we will retrieve all the sales between two dates
    public List<Sale> getSalesBetweenTwoDate(Date date1,Date date2){
        List<Sale> listOfSales=new ArrayList<>();

        try(Connection conn= Database.getConnection();
            PreparedStatement stmt= conn.prepareStatement("SELECT id,product_id,quantity_sold,total_amount,sale_date from sales WHERE sale_date between ? and ?");

        ){

            stmt.setDate(1, date1);
            stmt.setDate(2, date2);
            ResultSet rs= stmt.executeQuery();
            // here we add data to the list
            listOfSales=getSaleListFromResultSet(rs);
            rs.close();
        }catch (SQLException e){e.printStackTrace();}
        return listOfSales;
    }
    // displaying the 2 top-selling product
    public Map<Integer,Integer> displayTopSellingProduct(){
        Map<Integer,Integer> topSelling=new HashMap<>();
        try(Connection conn= Database.getConnection();
                Statement stmt= conn.createStatement();
                // here we get the productId and the total quantity sold
                // in the sales table, and we pick the 2 with the highest quantity selling
                ResultSet rs= stmt.executeQuery("SELECT product_id, SUM(sales.quantity_sold) AS total_sold FROM sales  JOIN products  ON sales.product_id = products.id GROUP BY sales.product_id ORDER BY total_sold DESC LIMIT 2")
        ){
            while(rs.next())
            {
                //here we add data to the map where the key is the product id and value is the total quantity
                int productId=rs.getInt("product_id");
                int quantitySold =rs.getInt("total_sold");
                topSelling.put(productId,quantitySold);
            }
         }catch (SQLException e){e.printStackTrace();}
    return topSelling;
    }
}
