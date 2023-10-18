package com.goattechnologies.pos;
import javax.xml.transform.Result;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * The DatabaseManager class handles database connections, query execution,
 * and various database operations for a Point of Sale (POS) application.
 * It provides methods for retrieving and updating product, ingredient,
 * and order information in the database.
 *
 * @author Cole Broberg, Ilham Aryawan, Dinesh Balakrishnan, Mohsin Khan, Nicholas Dienstbier
 */
public class DatabaseManager {
    private Connection conn = null;

    private final String teamName = "07c";

    private final String dbName = "csce315331_" + teamName + "_db";

    private final String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;

    private final dbSetup myCredentials = new dbSetup();

    public static QueryLoader queryLoader;

    static {
        try {
            queryLoader = new QueryLoader("src/main/resources/com/goattechnologies/pos/queries.xml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes the database connection.
     * This method connects to the database using the credentials provided.
     */
    public void connect() {
        try {
            conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
        } catch (Exception e) {
            AlertUtil.showWarning("Warning!", "Unable to connect to database", e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Disconnects from the database.
     * This method closes the connection to the database.
     */
    public void disconnect() {
        try {
            conn.close();
        } catch (Exception e) {
            AlertUtil.showWarning("Warning!", "Unable to disconnect from database", e.getMessage());
        }
    }

    /**
     * Execute a SQL query and return the result as a ResultSet.
     *
     * @param queryId The identifier of the query to execute.
     * @return The result of the query as a ResultSet.
     */
    private ResultSet query(String queryId) {
        try {
            return conn.createStatement().executeQuery(queryLoader.getQuery(queryId));
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to execute query", e.getMessage());
            throw new RuntimeException();
        }
    }

    /**
     * Execute a SQL query with a string parameter and return the result as a ResultSet.
     *
     * @param queryId The identifier of the query to execute.
     * @param alt The string parameter for the query.
     * @return The result of the query as a ResultSet.
     */
    private ResultSet query(String queryId, String alt) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(queryLoader.getQuery(queryId));
            preparedStatement.setString(1, alt);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to execute query", e.getMessage());
            throw new RuntimeException();
        }
    }

    /**
     * Execute a SQL query with an integer parameter and return the result as a ResultSet.
     *
     * @param queryId The identifier of the query to execute.
     * @param alt The integer parameter for the query.
     * @return The result of the query as a ResultSet.
     */
    private ResultSet query(String queryId, int alt) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(queryLoader.getQuery(queryId));
            preparedStatement.setInt(1, alt);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to execute query", e.getMessage());
            throw new RuntimeException();
        }
    }

    /**
     * Retrieves a list of product names from the database.
     *
     * @return A list of product names.
     */
    public List<String> getProductNames() {
        List<String> names = new ArrayList<String>();
        ResultSet resultSet = this.query("getProducts");
        try {
            while (resultSet.next()) {
                String addOnName = resultSet.getString("productname");
                names.add(addOnName);
            }
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to get product names", e.getMessage());
            throw new RuntimeException();
        }
        return names;
    }

    /**
     * Retrieves a list of add-on names from the database.
     *
     * @return A list of add-on names.
     */
    public List<String> getAddOnNames() {
        List<String> names = new ArrayList<String>();
        ResultSet resultSet = this.query("getAddOns");
        try {
            while (resultSet.next()) {
                String addOnName = resultSet.getString("productname");
                names.add(addOnName);
            }
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to get add-on names", e.getMessage());
            throw new RuntimeException();
        }
        return names;
    }

    /**
     * Retrieves the price of a specific product from the database.
     *
     * @param product The name of the product.
     * @return The price of the product.
     */
    private double getProductPrice(String product) {
        double price;
        try {
            ResultSet resultSet = this.query("getProductPrice", product);
            resultSet.next();
            price = resultSet.getDouble("saleprice");
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to get product price", e.getMessage());
            throw new RuntimeException();
        }
        return price;
    }

    /**
     * Calculates the total price of a cart item, including drink and add-ons.
     *
     * @param item The cart item.
     * @return The total price of the cart item.
     */
    private double getItemPrice(CartItem item) {
        double price = 0;
        price += getProductPrice(item.getDrinkName());
        for (String addon : item.getAddOns()) {
            price += getProductPrice(addon);
        }
        return price;
    }

    /**
     * Calculates the total price of a list of cart items.
     *
     * @param items The list of cart items.
     * @return The total price of the list of cart items.
     */
    private double getOrderPrice(List<CartItem> items) {
        double price = 0;
        for (CartItem item : items) {
            price += getItemPrice(item);
        }
        return price;
    }

    /**
     * Updates the database to mark ingredients used in a list of cart items.
     *
     * @param items The list of cart items.
     */
    private void useOrderIngredients(List<CartItem> items) {
        for (CartItem item : items) {
            try {
                PreparedStatement preparedStatement = conn.prepareStatement(
                        queryLoader.getQuery("useIngredients")
                );
                preparedStatement.setString(1, item.getDrinkName());
                preparedStatement.execute();
                for (String addOnName : item.getAddOns()) {
                    preparedStatement = conn.prepareStatement(
                            queryLoader.getQuery("useIngredients")
                    );
                    preparedStatement.setString(1, addOnName);
                    preparedStatement.execute();
                }
            } catch (SQLException e) {
                AlertUtil.showWarning("Warning!", "Unable to use ingredients", e.getMessage());
                throw new RuntimeException();
            }

        }
    }

    /**
     * Retrieves the current timestamp.
     *
     * @return The current timestamp.
     */
    private Timestamp getOrderTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * Retrieves the product IDs for a list of cart items.
     *
     * @param items The list of cart items.
     * @return A list of product IDs.
     */
    private List<Integer> getOrderProductIds(List<CartItem> items) {
        List<Integer> productids = new ArrayList<Integer>();
        for (CartItem item : items) {
            try {
                ResultSet resultSet = query("getProductId", item.getDrinkName());
                resultSet.next();
                productids.add(resultSet.getInt("productid"));
                List<String> addOns = item.getAddOns();
                for (String addOn : addOns) {
                    resultSet = query("getProductId", addOn);
                    resultSet.next();
                    productids.add(resultSet.getInt("productid"));
                }
            } catch (SQLException e) {
                AlertUtil.showWarning("Warning!", "Unable to get order's product ids", e.getMessage());
                throw new RuntimeException();
            }
        }
        return productids;
    }

    /**
     * Adds an order to the database with the specified cart items and tip percentage.
     *
     * @param items         The list of cart items.
     * @param tipPercentage The tip percentage for the order.
     */
    public void addOrder(List<CartItem> items, double tipPercentage) {

        String cashier = Employee.getInstance().getEmployeeName();
        List<Integer> productids = getOrderProductIds(items);
        Timestamp time = getOrderTime();
        double orderPrice = getOrderPrice(items);
        DecimalFormat df = new DecimalFormat("0.00");
        if (orderPrice == 0) return;

        try {
            // SQL to add order to orders database, currently uses cost for price
            PreparedStatement preparedStatement = conn.prepareStatement(queryLoader.getQuery("insertOrder"));
            Array ingredients = conn.createArrayOf("INTEGER", productids.toArray());
            preparedStatement.setArray(1, ingredients);
            preparedStatement.setString(2, cashier);
            preparedStatement.setTimestamp(3, time);

            preparedStatement.setDouble(4, Double.parseDouble(df.format(orderPrice * (1 + tipPercentage))));
            preparedStatement.execute();

            // Decrements ingredient quantities for each item and addon in the order
            useOrderIngredients(items);
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to add order", e.getMessage());
            throw new RuntimeException();
        }
    }

    /**
     * Retrieves a list of ingredients from the database.
     *
     * @return A list of Ingredient objects.
     */
    public List<Ingredient> getIngredients() {
        List<Ingredient> ingredientsList = new ArrayList<>();
        ResultSet resultSet = this.query("getIngredients");
        try {
            while (resultSet.next()) {
                int ingredientId = resultSet.getInt("ingredientid");
                String ingredientName = resultSet.getString("ingredientname");
                int quantity = resultSet.getInt("quantity");
                double cost = resultSet.getDouble("cost");

                Ingredient ingredient = new Ingredient(ingredientId, ingredientName, quantity, cost);
                ingredientsList.add(ingredient);
            }
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to Get Ingredient", e.getMessage());
            throw new RuntimeException();
        }
        return ingredientsList;
    }

    /**
     * Adds a new ingredient to the database.
     *
     * @param ingredient The Ingredient object to be added.
     */
    public void addIngredient(Ingredient ingredient) {
        // Do not need to check name because controller checks if it is empty
        if (ingredient.getQuantity() < 0 || ingredient.getCost() < 0 || Objects.equals(ingredient.getIngredientName(), ""))
        {
            AlertUtil.showWarning("Warning!", "Invalid Ingredient", "Cost and Quantity must be greater than or equal to 0.");
            return;
        }

        try {
            String insertQuery = queryLoader.getQuery("addIngredient");
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.setString(1, ingredient.getIngredientName());
            preparedStatement.setInt(2, ingredient.getQuantity());
            preparedStatement.setDouble(3, ingredient.getCost());

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted <= 0) {
                AlertUtil.showWarning("Warning!", "Ingredient insertion failed for: ", ingredient.getIngredientName());
            }

            preparedStatement.close();
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to Add Ingredient", e.getMessage());
            throw new RuntimeException();
        }
    }

    /**
     * Adds a new product to the database.
     *
     * @param product The Product object to be added.
     */
    public void addProduct(Product product) {
        // Do not need to check name because controller checks if it is empty
        if (product.getSalePrice() < 0 || Objects.equals(product.getProductName(), ""))
        {
            AlertUtil.showWarning("Warning!", "Invalid Ingredient", "Sale Price must be greater than or equal to 0, and name must not be empty");
            return;
        }

        try {
            String insertQuery = queryLoader.getQuery("addProduct");
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.setString(1, product.getProductName());
            Integer[] integerArray = product.getIngredients().toArray(new Integer[0]);
            preparedStatement.setArray(2, conn.createArrayOf("integer",integerArray));
            preparedStatement.setDouble(3, product.getSalePrice());

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted <= 0) {
                AlertUtil.showWarning("Warning!", "Product insertion failed for: ", product.getProductName());
            }

            preparedStatement.close();
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to Add Product", e.getMessage());
            throw new RuntimeException();
        }
    }

    /**
     * Updates an ingredient's information in the database.
     *
     * @param ingredient The Ingredient object with updated information.
     */
    public void updateIngredient(Ingredient ingredient) {
        // Do not need to check name because controller checks if it is empty
        if (ingredient.getQuantity() < 0 || ingredient.getCost() < 0)
        {
            AlertUtil.showWarning("Warning!", "Invalid Ingredient", "Cost and Quantity must be greater than or equal to 0.");
            return;
        }

        try {
            String updateQuery = queryLoader.getQuery("updateInventory");
            PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);
            preparedStatement.setString(1, ingredient.getIngredientName());
            preparedStatement.setInt(2, ingredient.getQuantity());
            preparedStatement.setDouble(3, ingredient.getCost());
            preparedStatement.setInt(4, ingredient.getIngredientId());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated <= 0) {
                AlertUtil.showWarning("Warning!", "Ingredient update failed for: ", ingredient.getIngredientName());
            }

            preparedStatement.close();
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to Update Ingredient", e.getMessage());
            throw new RuntimeException();
        }
    }


    /**
     * Updates a product's information in the database.
     *
     * @param editedProduct The Product object with updated information.
     */
    public void updateProduct(Product editedProduct) {
        // Do not need to check name because controller checks if it is empty
        if (editedProduct.getSalePrice() < 0 || Objects.equals(editedProduct.getProductName(), ""))
        {
            AlertUtil.showWarning("Warning!", "Invalid Ingredient", "Cost and Quantity must be greater than or equal to 0.");
            return;
        }

        try {
            String updateQuery = queryLoader.getQuery("updateProduct");
            PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);
            preparedStatement.setString(1, editedProduct.getProductName());
            Integer[] integerArray = editedProduct.getIngredients().toArray(new Integer[0]);
            preparedStatement.setArray(2, conn.createArrayOf("integer",integerArray));
            preparedStatement.setDouble(3, editedProduct.getSalePrice());
            preparedStatement.setInt(4, editedProduct.getProductid());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated <= 0) {
                AlertUtil.showWarning("Warning!", "Product update failed for: ", editedProduct.getProductName());
            }

            preparedStatement.close();
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to Update Product", e.getMessage());
            throw new RuntimeException();
        }
    }

    /**
     * Deletes an ingredient from the database.
     *
     * @param ingredientID The ID of the ingredient to be deleted.
     */
    public void deleteIngredient(int ingredientID) {
        try {
            String deleteQuery = queryLoader.getQuery("deleteIngredient");
            PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, ingredientID);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted <= 0) {
                AlertUtil.showWarning("Warning!", "Ingredient deletion failed.", "The ingredient may not exist.");
            }

            preparedStatement.close();
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to delete Ingredient", e.getMessage());
            throw new RuntimeException();
        }
    }

    /**
     * Deletes a product from the database.
     *
     * @param productID The ID of the product to be deleted.
     */
    public void deleteProduct(int productID) {
        try {
            String deleteQuery = queryLoader.getQuery("deleteProduct");
            PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, productID);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted <= 0) {
                AlertUtil.showWarning("Warning!", "Product deletion failed.", "The product may not exist.");
            }

            preparedStatement.close();
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to delete product", e.getMessage());
            throw new RuntimeException();
        }
    }

    /**
     * Checks if an employee is a manager.
     *
     * @param employeeID The ID of the employee.
     * @return true if the employee is a manager, false otherwise.
     */
    public boolean isManager(int employeeID) {
        boolean isManager = false;
        try {
            ResultSet resultSet = this.query("isManager", employeeID);
            resultSet.next();
            isManager = resultSet.getBoolean("ismanager");
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to get employee manager status", e.getMessage());
            throw new RuntimeException();
        }
        return isManager;
    }

    /**
     * Retrieves the name of an employee based on their ID.
     *
     * @param employeeID The ID of the employee.
     * @return The name of the employee.
     */
    public String getEmployeeName(int employeeID) {
        String employeeName = "";
        try {
            ResultSet resultSet = this.query("getEmployeeName", employeeID);
            resultSet.next();
            employeeName = resultSet.getString("firstname");
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return employeeName;
    }

    /**
     * Retrieves a list of products.
     *
     * @return A list of Product objects.
     */

    /**
     * Retrieves a list of products.
     *
     * @return A list of Product objects.
     */
    public List<Product> getProductsList() {
        HashMap<Integer, String> ingredientNameMap = new HashMap<>();
        ResultSet resultSet2 = this.query("getIngredients");
        try {
            while(resultSet2.next()) {
                ingredientNameMap.put(resultSet2.getInt("ingredientid"),resultSet2.getString("ingredientname"));
            }
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to get ingredients", e.getMessage());
            throw new RuntimeException();
        }


        List<Product> productsList = new ArrayList<>();
        ResultSet resultSet = this.query("getProductsList");
        try {
            while (resultSet.next()) {
                int productId = resultSet.getInt("productid");
                String productName = resultSet.getString("productname");
                List<Integer> ingredients = new ArrayList<>();
                Array array = resultSet.getArray("ingredientids");
                List<String> ingredientNames = new ArrayList();
                if (array != null){
                    Integer[] arrayData = (Integer[]) array.getArray();
                    ingredients.addAll(Arrays.asList(arrayData));
                    for(Integer x : ingredients) {
                        ingredientNames.add(ingredientNameMap.get(x));
                    }
                }
                double price = resultSet.getDouble("price");
                double salePrice = resultSet.getDouble("saleprice");
                Product product = new Product(productId, productName, ingredients, price, salePrice, ingredientNames);
                productsList.add(product);
            }
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to get products", e.getMessage());
            throw new RuntimeException();
        }
        return productsList;
    }

    /**
     * Refreshes the product costs in the database.
     */
    public void refreshProductCosts() {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(queryLoader.getQuery("refreshProductCosts"));
            ResultSet resultSet = preparedStatement.executeQuery();

            preparedStatement.close();
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to refresh product costs", e.getMessage());
            throw new RuntimeException();
        }
    }

    /**
     * Retrieves a map of drinks where the key is the product ID and the value is the product name.
     *
     * @return A map of drinks.
     */
    public HashMap<Integer, String> getDrinks() {
        HashMap<Integer, String> drinks = new HashMap<>();
        try {
            ResultSet resultSet = query("getDrinks");
            while (resultSet.next()) {
                int productId = resultSet.getInt("productid");
                String productName = resultSet.getString("productname");
                drinks.put(productId, productName);
            }
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to get ingredient name", e.getMessage());
            throw new RuntimeException();
        }
        return drinks;
    }

    /**
     * Retrieves a list of multi-orders within a specified time range.
     *
     * @param startTime The start time for the time range.
     * @param endTime   The end time for the time range.
     * @return A list of lists, where each inner list contains the product IDs of multi-orders.
     */
    public List<List<Integer>> getMultiOrders(Timestamp startTime, Timestamp endTime) {
        List<List<Integer>> multiOrders = new ArrayList<>();
        HashMap<Integer, String> drinks = getDrinks();

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(queryLoader.getQuery("getMultiProdOrders"));
            preparedStatement.setTimestamp(1, startTime);
            preparedStatement.setTimestamp(2, endTime);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Array prodIds = resultSet.getArray("productids");
                List<Integer> productIds = Arrays.asList((Integer[]) prodIds.getArray());
                List<Integer> drinkIds = new ArrayList<>();
                for (int productId : productIds) {
                    if (drinks.containsKey(productId)) drinkIds.add(productId);
                }
                if (drinkIds.size() >= 2) multiOrders.add(drinkIds);
            }
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to get drinks from orders with multiple drinks",
                    e.getMessage());
            throw new RuntimeException();
        }
        return multiOrders;
    }

    /**
     * Retrieves a Map of a ProductNames with their Popularity within a specified time range for a specific number of items.
     *
     * @param startTime The start time for the time range.
     * @param endTime   The end time for the time range.
     * @param numItems The number of items for which we limit the query to
     * @return A Map of ProductName with its respective Popularity in terms of how often it was ordered
     */
    public HashMap<String, Integer> getPopularity(Timestamp startTime, Timestamp endTime, Integer numItems) {
        try {
            HashMap<Integer, Integer> productIDPopularity = new HashMap<>();
            HashMap<String, Integer> productNamesPopularity = new HashMap<>();
            PreparedStatement preparedStatement = conn.prepareStatement(queryLoader.getQuery("orderPopularity"));
            preparedStatement.setTimestamp(1, startTime);
            preparedStatement.setTimestamp(2, endTime);
            preparedStatement.setInt(3, numItems);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                productIDPopularity.put(resultSet.getInt("product_id"), resultSet.getInt("popularity"));
            }
            for (Integer productID : productIDPopularity.keySet()) {
                Integer popularity = productIDPopularity.get(productID);
                PreparedStatement preparedStatement2 = conn.prepareStatement(queryLoader.getQuery("getProductName"));
                preparedStatement2.setInt(1, productID);
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                if (resultSet2.next()) {
                    productNamesPopularity.put(resultSet2.getString("productname"), popularity);
                }
            }
            if (productNamesPopularity.size() < numItems) {
                AlertUtil.showWarning("Warning!", "Item Count Limitation", "Actual Item Count for TimeWindow is: " + productNamesPopularity.size());
            }
            else if (productNamesPopularity.isEmpty()) {
                AlertUtil.showWarning("Warning!", "No Popularity Data", "No Data for this TimeWindow");
            }
            return productNamesPopularity;
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to get products popularity", e.getMessage());
            throw new RuntimeException();
        }
    }


    /**
     * Retrieves a Map of all the products and the total sales prices for each product within a specified time range.
     * @param startTime The start time for the time range.
     * @param endTime The end time for the time range.
     * @return A Map of ProductName with its respective Total Sales Price
     */
    public HashMap<String, Integer> getSalesByItem(Timestamp startTime, Timestamp endTime) {
        try {
            HashMap<String, Integer> salesByItem = new HashMap<>();
            PreparedStatement preparedStatement = conn.prepareStatement(queryLoader.getQuery("salesByItem"));
            preparedStatement.setTimestamp(1, startTime);
            preparedStatement.setTimestamp(2, endTime);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                salesByItem.put(resultSet.getString("productname"), resultSet.getInt("total_sales"));
            }
            return salesByItem;
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to get sales by item", e.getMessage());
            throw new RuntimeException();
        }
    }
}


