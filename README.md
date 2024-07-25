Note: it is worth noting that the project’s data is stored in a SQLite database that can be found in the project in the form of a SQLite file. Only the session of the user is stored in the memory and not the database. Also, the authenticated user's additional features are to add and delete products, as for the checkout method there is only one cart in the database and there is no database relation between the user and the cart.
For testing purposes, I left commented code in the main class related to each task for you to uncomment and test.
you can find a photo of the database schema here


![Database](https://github.com/user-attachments/assets/d0c4c2cb-0f50-4a24-84f5-cf0f635102f2)


A Guide:
For Task 1: Time took me to complete(1h)
-	The method pm.getProductList() returns a List of all the products and a for loop to print all the data.
-	 The method pm.upDateProduct() updates a product, I’ve added a map as a parameter where you can decide which field of the product you want to update and it updates by using the product ID as a second function parameter .
-	Also a pm.getProductByID() allows you to get the product by using the ID.
-	The section concerning the deleting and adding of products will be covered in Task 5 since it requires user Authentication.
For Task2: Time took me to complete(1h 30 min)
-	 The method cs.getAllCartItem()  gets all the items in the cart
-	 The method cs.addToCart() allows the user to add a product by using the productID and the quantity you want to add to the cart( you cannot add a product to a cart if it is out of stock).
-	The method cs.removeProductFromCart() allows the user to remove products from the cart using the productID as a search parameter.
-	The method cs.getTotalAmount() calculates the total sum of the price in the cart and offers discounts if the price exceeds a certain amount (100$).
-	The method cs.checkout() is responsible for the checkout of the cart by removing all the items from the cart,  it deducts the quantity of the sold products in the cart from the stock of the shop( task 3), and it also creates sales Records for every item sold (Task 4).
For Task3: Time took me to complete(30 min)
-	 The method pm.checkStockLevels() notifies the shop manager when the stock of a product falls below a certain threshold.
-	The method pm.reportOfProductToReorder() returns a list of products with low stocks which is displayed as a report.
For Task4: Time took me to complete(30 min- 1h)
-	The method sm.getSalesOfCurrentDayAndRevenueAndQuantity() returns a map containing a list of daily sales, revenue, and Quantity of items sold.
-	The method sm.getSalesBetweenTwoDate() returns the list of all sales that occurred between two dates.
-	The method sm.displayTopSellingProduct()  returns a map containing two entries with the key being productId and the value being the totalAmount and these entries are the two most sold products.

For Task5: Time took me to complete(30min - 1h)
-	The method auth.register() requests a username and a password to create a new user that is stored in the database (the password is hashed using the md5 algorithm).
-	The method auth.AuthenticateUser() allows the user to log in by inputting his username and password. If the entered credentials are valid the method will return a session which will be used to access methods that are only available to authenticated users.
-	As mentioned in task1 pm.addProduct() takes a product and a string (session) as parameters in order to add a product to the products table. As for the deleteProductById() method which takes an integer and string as parameters it deletes a product by ID. As mentioned above these two methods are only available to authenticated users.
Code optimization and commenting Time took me to complete(1h-2h)
Thank you! please if you have any question feel free to ask!
