import java.sql.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;

public class populate_tables {

    public static void main(String args[]) {

        // Credentials
        Connection conn = null;
        String teamNumber = "07c";
        String dbName = "csce315331" + "_" + teamNumber + "_db";
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;

        String csvFilePath = "orders.csv";

        // Connecting to the database
        try {
            conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Opened database successfully");

        // Inserting into tables
        try {
            BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));

            String insertSQL = "INSERT INTO employees(employeeid, ismanager, firstname, lastname) VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {

                List<String> firstnames = Arrays.asList("Dinesh", "Mohsin", "Nicholas", "Cole", "Ilham");
                List<String> lastnames = Arrays.asList("Balakrishnan", "Khan", "Dienstbier", "Broberg", "Aryawan");
                for(int i = 0; i < 5; i++) {
                    String firstname = firstnames.get(i);
                    String lastname = lastnames.get(i);

                    preparedStatement.setInt(1, i);
                    preparedStatement.setBoolean(2, false);
                    preparedStatement.setString(3, firstname);
                    preparedStatement.setString(4, lastname);

                    preparedStatement.executeUpdate();
                }
                reader.close();

                System.out.println("Data inserted successfully.");
            } 
            catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }

            insertSQL = "INSERT INTO products(productid, ingredientids, price) VALUES (?, ?, ?)";

            try (PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {

                String line = reader.readLine();
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(", ");
                    preparedStatement.setInt(1, Integer.parseInt(fields[2]));
                    Array ingredients = null;
                    preparedStatement.setArray(2, ingredients);
                    preparedStatement.setFloat(3, Float.parseFloat(fields[4]));

                    preparedStatement.executeUpdate();
                }
                reader.close();

                System.out.println("Data inserted successfully.");
            } 
            catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }

            // Closing the connection
            try {
                conn.close();
                System.out.println("Connection Closed.");
            } 
            catch (Exception e) {
                System.out.println("Connection NOT Closed.");
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
