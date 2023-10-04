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

            String insertSQL = "INSERT INTO employees(employeeid, ismanager, firstname, lastname) VALUES (?, ?, ?, ?)";

            /*try (PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {

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

                System.out.println("Data inserted successfully.");
            } 
            catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }*/

            insertSQL = "INSERT INTO finances(reportdate, revenue, profit, expenses, orders) VALUES (?, ?, ?, ?, ?)";
            /*
            try (PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
                BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));

                String line = reader.readLine();
                String[] fields = line.split(", ");
                String weekday = fields[0] + fields[1];
                float revenue = 0;
                float expenses = 0;
                float profit = 0;
                while ((line = reader.readLine()) != null) {
                    fields = line.split(", ");
                    String currday = fields[0] + fields[1];
                    if(weekday == currday)
                    {
                        revenue = Float.parseFloat(fields[5]);
                        expenses = Float.parseFloat(fields[7]);
                        profit = revenue - expenses;
                    }
                    String csvWeek = "2023W" + fields[0]; // Example: Week 40 of 2023
                    String csvDay = fields[1];      // Example: Tuesday
                    String csvTime = fields[3];
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy'W'ww E HH:mm:ss");
                    java.util.Date date = sdf.parse(csvWeek + " " + csvDay + " " + csvTime);
                    preparedStatement.setTimestamp(1, new Timestamp(date.getTime()));
                    preparedStatement.setFloat(2, Float.parseFloat(fields[5]));
                    preparedStatement.setFloat(4, Float.parseFloat(fields[7]));
                    revenue = Float.parseFloat(fields[5]);
                    expenses = Float.parseFloat(fields[7]);
                    profit = revenue - expenses;
                    preparedStatement.setFloat(3, profit);
                    Array orders = null;
                    preparedStatement.setArray(5, orders);

                    preparedStatement.executeUpdate();
                }
                reader.close();

                System.out.println("Data inserted successfully.");
            } 
            catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }*/

            insertSQL = "INSERT INTO orderproducts(productid, ingredientids, price) VALUES (?, ?, ?)";
            /*
            try (PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
                BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));

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
            }*/

            insertSQL = "INSERT INTO orders(orderid, cashier, transactiontime, price) VALUES (?, ?, ?, ?)";
            /*
            try (PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
                BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));

                String line = reader.readLine();
                int batch = 0;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(", ");
                    preparedStatement.setInt(1, Integer.parseInt(fields[2]));
                    preparedStatement.setString(2, fields[8]);

                    String csvWeek = "2023W" + fields[0]; // Example: Week 40 of 2023
                    String csvDay = fields[1];      // Example: Tuesday
                    String csvTime = fields[3];
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy'W'ww E HH:mm");
                    java.util.Date date = sdf.parse(csvWeek + " " + csvDay + " " + csvTime);
                    preparedStatement.setTimestamp(3, new Timestamp(date.getTime()));
                    preparedStatement.setFloat(4, Float.parseFloat(fields[5]) + Float.parseFloat(fields[7]));

                    if(batch < 1000)
                    {
                        preparedStatement.addBatch();
                        batch++;
                    }
                    else {
                        preparedStatement.executeBatch();
                        preparedStatement.clearBatch();
                        batch = 0;
                    }
                }
                preparedStatement.executeBatch();
                preparedStatement.clearBatch();
                reader.close();

                System.out.println("Data inserted successfully.");
            } 
            catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }*/

            insertSQL = "INSERT INTO ingredients(ingredientid, ingredientname, quantity, cost) VALUES (?, ?, ?, ?)";
            csvFilePath = "ingredients.csv";
            /*
            try (PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
                BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));

                String line = reader.readLine();
                int id = 0;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(",");
                    preparedStatement.setInt(1, id);
                    preparedStatement.setString(2, fields[0]);
                    preparedStatement.setInt(3, Integer.parseInt(fields[1]));
                    String formattedNumber = String.format("%.2f", Double.parseDouble(fields[2]));
                    double roundedNumber = Double.parseDouble(formattedNumber);
                    preparedStatement.setDouble(4, roundedNumber);
                    preparedStatement.executeUpdate();
                    id++;
                }
                reader.close();

                System.out.println("Data inserted successfully.");
            } 
            catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }*/

            insertSQL = "INSERT INTO products(productid, productname, ingredientids, price) VALUES (?, ?, ?, ?)";
            csvFilePath = "products.csv";
            /*
            try (PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
                BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));

                String line = reader.readLine();
                int id = 0;
                while ((line = reader.readLine()) != null) {
                    List<String> fields = parseCsvLine(line);
                    preparedStatement.setInt(1, id);
                    preparedStatement.setString(2, fields.get(0));
                
                    // Extracting the ingredientIds
                    String ingredientList = fields.get(1);
                    if (ingredientList.startsWith("\"") && ingredientList.endsWith("\"")) {
                        ingredientList = ingredientList.substring(1, ingredientList.length() - 1);
                    }
                    ingredientList = ingredientList.substring(1, ingredientList.length() - 1); // remove [ and ]
                    String[] ingredientArray = ingredientList.split(",\s*");
                    List<String> ingredientIds = Arrays.asList(ingredientArray);
                
                    Array ingredients = conn.createArrayOf("Varchar", ingredientIds.toArray());
                    preparedStatement.setArray(3, ingredients);
                    preparedStatement.setDouble(4, Double.parseDouble(fields.get(2)));
                
                    preparedStatement.executeUpdate();
                    id++;
                }
                reader.close();

                System.out.println("Data inserted successfully.");
            } 
            catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }*/
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        try {
            conn.close();
            System.out.println("Connection Closed.");
        } 
        catch(Exception e) {
            System.out.println("Connection NOT Closed.");
        }
    }

    private static List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();
    
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(field.toString().trim());
                field.setLength(0); // reset field
            } else {
                field.append(c);
            }
        }
        result.add(field.toString().trim()); // add last field
        return result;
    }
}

