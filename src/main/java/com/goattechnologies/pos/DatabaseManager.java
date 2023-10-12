package com.goattechnologies.pos;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


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

    public void connect() {
        try {
            conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
            System.out.println("Opened database successfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void disconnect() {
        try {
            conn.close();
            System.out.println("Connection Closed.");
        } catch (Exception e) {
            System.out.println("Connection NOT Closed.");
        }
    }

    // Add other database methods here, such as executing queries, etc.
    private ResultSet query(String queryId) {
        try {
            return conn.createStatement().executeQuery(queryLoader.getQuery(queryId));
        } catch (SQLException e) {
            System.out.println("database query failed: " + queryId);
            throw new RuntimeException();
        }
    }

    private ResultSet query(String queryId, String alt) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(queryLoader.getQuery(queryId));
            preparedStatement.setString(1, alt);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println("database query failed: " + queryId);
            throw new RuntimeException();
        }
    }

    private ResultSet query(String queryId, int alt) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(queryLoader.getQuery(queryId));
            preparedStatement.setInt(1, alt);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println("database query failed: " + queryId);
            throw new RuntimeException();
        }
    }

    public List<String> getProductNames() {
        List<String> names = new ArrayList<String>();
        ResultSet resultSet = this.query("getProducts");
        try {
            while (resultSet.next()) {
                String addOnName = resultSet.getString("productname");
                names.add(addOnName);
            }
        } catch (SQLException e) {
            System.out.println("Unable to get product names");
            throw new RuntimeException();
        }
        return names;
    }

    public List<String> getAddOnNames() {
        List<String> names = new ArrayList<String>();
        ResultSet resultSet = this.query("getAddOns");
        try {
            while (resultSet.next()) {
                String addOnName = resultSet.getString("productname");
                names.add(addOnName);
            }
        } catch (SQLException e) {
            System.out.println("Unable to get addon names");
            throw new RuntimeException();
        }
        return names;
    }

    private double getProductPrice(String product) {
        double price = 0;
        try {
            ResultSet resultSet = this.query("getProductPrice", product);
            resultSet.next();
            price = resultSet.getDouble("price");
        } catch (SQLException e) {
            System.out.println("Unable to get product price");
            throw new RuntimeException();
        }
        return price;
    }
    
    private double getItemPrice(CartItem item) {
        double price = 0;
        price += getProductPrice(item.getDrinkName());
        for (String addon : item.getAddOns()) {
            price += getProductPrice(addon);
        }
        return price;
    }
    
    private double getOrderPrice(List<CartItem> items) {
        double price = 0;
        for (CartItem item : items) {
            price += getItemPrice(item);
        }
        return price;
    }

    private int getNextOrderId() {
        int id = 0;
        try {
            ResultSet resultSet = this.query("getLastOrderId");
            resultSet.next();
            id = resultSet.getInt("orderid") + 1;
        } catch (SQLException e) {
            System.out.println("Unable to get next id");
            throw new RuntimeException();
        }
        return id;
    }

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
                System.out.println("could not use ingredients");
                throw new RuntimeException();
            }

        }
    }

    private Timestamp getOrderTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    public void addOrder(List<CartItem> items, double tipPercentage /* TODO: deal with real worker in the future*/) {

        int id = getNextOrderId();
        String cashier = "Cole";
        Timestamp time = getOrderTime();
        double orderPrice = getOrderPrice(items);
        DecimalFormat df = new DecimalFormat("0.00");

        try {
            // SQL to add order to orders database, currently uses cost for price
            PreparedStatement preparedStatement = conn.prepareStatement(queryLoader.getQuery("insertOrder"));
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, cashier);
            preparedStatement.setTimestamp(3, time);

            preparedStatement.setDouble(4, Double.parseDouble(df.format(orderPrice * (1 + tipPercentage))));
            preparedStatement.execute();
            System.out.println("Order added to db");

            // Decrements ingredient quantities for each item and addon in the order
            useOrderIngredients(items);
            System.out.println("Ingredients consumed");
        } catch (SQLException e) {
            System.out.println("could not add order to database");
            throw new RuntimeException();
        }
    }

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

    public void addIngredient(Ingredient ingredient) {
        // Do not need to check name because controller checks if it is empty
        if (ingredient.getQuantity() < 0 || ingredient.getCost() < 0)
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
                System.out.println("Insertion failed.");
                AlertUtil.showWarning("Warning!", "Ingredient insertion failed for: ", ingredient.getIngredientName());
            }

            preparedStatement.close();
        } catch (SQLException e) {
            AlertUtil.showWarning("Warning!", "Unable to Add Ingredient", e.getMessage());
            throw new RuntimeException();
        }
    }

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

    public boolean isManager(int employeeID) {
        boolean isManager = false;
        try {
            ResultSet resultSet = this.query("isManager", employeeID);
            resultSet.next();
            isManager = resultSet.getBoolean("ismanager");
        } catch (SQLException e) {
            System.out.println("Unable to get product price");
            throw new RuntimeException();
        }
        return isManager;
    }

    public String getEmployeeName(int employeeID) {
        String employeeName = "";
        try {
            ResultSet resultSet = this.query("getEmployeeName", employeeID);
            resultSet.next();
            employeeName = resultSet.getString("firstname");
        } catch (SQLException e) {
            System.out.println("Unable to get product price");
            throw new RuntimeException();
        }
        return employeeName;
    }

    public List<Product> getProductsList() {
        HashMap<Integer, String> ingredientNameMap = new HashMap<>();
        ResultSet resultSet2 = this.query("getIngredients");
        try {
            while(resultSet2.next()) {
                ingredientNameMap.put(resultSet2.getInt("ingredientid"),resultSet2.getString("ingredientname"));
            }
        } catch (SQLException e) {
            System.out.println("Unable to get ingredients within getProductsList()");
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
            System.out.println("Unable to get products");
            throw new RuntimeException();
        }
        return productsList;
    }

    public String getIngredientNameByID(int ingredientId) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(queryLoader.getQuery("getIngredientNameByID"));
            preparedStatement.setInt(1, ingredientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("ingredientname");
            } else {
                return "Ingredient not found"; // Change this to your desired behavior.
            }

        } catch (SQLException e) {
            System.out.println("Unable to get ingredient name from id");
            throw new RuntimeException();
        }
    }
}


