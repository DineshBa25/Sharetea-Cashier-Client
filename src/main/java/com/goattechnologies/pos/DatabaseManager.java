package com.goattechnologies.pos;
import java.sql.*;


public class DatabaseManager {
    private Connection conn = null;
    private final String teamName = "07c";
    private final String dbName = "csce315331_" + teamName + "_db";
    private final String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
    private final dbSetup myCredentials = new dbSetup();

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
}

