package com.goattechnologies.pos;
import java.sql.*;
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
    public ResultSet query(String queryId) {
        try {
            return conn.createStatement().executeQuery(queryLoader.getQuery(queryId));
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
}

