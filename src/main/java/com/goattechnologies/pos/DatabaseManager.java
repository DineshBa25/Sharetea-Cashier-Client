package com.goattechnologies.pos;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
        return;
    }
}

