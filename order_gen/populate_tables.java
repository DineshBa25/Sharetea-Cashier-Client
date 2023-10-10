import java.sql.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.util.HashMap;


public class populate_tables {

    public static void main(String args[]) {

        // Credentials
        Connection connection = null;
        String teamNumber = "07c";
        String dbName = "csce315331" + "_" + teamNumber + "_db";
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;

        // Connecting to the database
        try {
            connection = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Opened database successfully");

        // Inserting into tables
        try {
            // populate_employees(connection);
            //populate_finances(connection);
            populate_ingredients(connection);
            //populate_orders(connection);
            //populate_products(connection);
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        // Ensure connection closes
        try {
            connection.close();
            System.out.println("Connection Closed.");
        } 
        catch(Exception e) {
            System.out.println("Connection NOT Closed.");
        }
    }

    public static void populate_employees(Connection conn) {

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

            System.out.println("Data inserted successfully.");
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void populate_finances(Connection conn) {

        String insertSQL = "INSERT INTO finances(reportdate, revenue, profit, expenses, ordercount) VALUES (?, ?, ?, ?, ?)";
        String csvFilePath = "orders.csv";
        HashMap<String, Double> productPrices = getProducts("products.csv");
            
            try (PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
                BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
                String line = reader.readLine();
                line = reader.readLine();
                String[] fields = line.split(", ");

                String weekday = fields[0] + fields[1];
                double revenue = Double.parseDouble(fields[5]) + Double.parseDouble(fields[7]);
                double expenses = productPrices.get(fields[4]);
                double profit = revenue - expenses;

                int orders = 0;
                String csvWeek = "2023W" + fields[0]; // Example: Week 40 of 2023
                String csvDay = fields[1];      // Example: Tuesday
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy'W'ww E");
                java.util.Date date = sdf.parse(csvWeek + " " + csvDay);

                while ((line = reader.readLine()) != null) {
                    fields = line.split(", ");
                    String currday = fields[0] + fields[1];
                    if(weekday.equals(currday)) {
                        revenue += Double.parseDouble(fields[5]) + Double.parseDouble(fields[7]);
                        expenses += productPrices.get(fields[4]);
                        profit = revenue - expenses;
                        orders++;
                        continue;
                    }
                    else {
                        preparedStatement.setTimestamp(1, new Timestamp(date.getTime()));
                        preparedStatement.setDouble(2, revenue);

                        String formattedNumber = String.format("%.2f", expenses);
                        double roundedNumber = Double.parseDouble(formattedNumber);
                        preparedStatement.setDouble(4, roundedNumber);

                        formattedNumber = String.format("%.2f", profit);
                        roundedNumber = Double.parseDouble(formattedNumber);
                        preparedStatement.setDouble(3, roundedNumber);

                        preparedStatement.setInt(5, orders);
                        preparedStatement.executeUpdate();

                        weekday = currday;
                        revenue = Double.parseDouble(fields[5]) + Double.parseDouble(fields[7]);
                        expenses = productPrices.get(fields[4]);
                        profit = revenue - expenses;
                        orders = 0;
                        csvWeek = "2023W" + fields[0]; // Example: Week 40 of 2023
                        csvDay = fields[1];      // Example: Tuesday
                        sdf = new SimpleDateFormat("yyyy'W'ww E");
                        date = sdf.parse(csvWeek + " " + csvDay);
                    }
                }
                reader.close();
                System.out.println("Data inserted successfully.");
            } 
            catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
    }

    public static void populate_ingredients(Connection conn) {
        String insertSQL = "INSERT INTO ingredients(ingredientid, ingredientname, quantity, cost) VALUES (?, ?, ?, ?)";
        String csvFilePath = "ingredients.csv";
            
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
            }
    }

    public static void populate_orders(Connection conn) {
        String insertSQL = "INSERT INTO orders(orderid, cashier, transactiontime, price) VALUES (?, ?, ?, ?)";
        String csvFilePath = "orders.csv";
            
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

                if(batch < 1000) {
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
        }
    }

    public static void populate_products(Connection conn) {
        String insertSQL = "INSERT INTO products(productid, productname, ingredientids, price) VALUES (?, ?, ?, ?)";
        String csvFilePath = "products.csv";
            
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
                if (ingredientList.startsWith("\"") && ingredientList.endsWith("\""))
                    ingredientList = ingredientList.substring(1, ingredientList.length() - 1);
                ingredientList = ingredientList.substring(1, ingredientList.length() - 1); // remove [ and ]
                String[] ingredientArray = ingredientList.split(",\s*");
                List<String> ingredientIds = Arrays.asList(ingredientArray);
                List<Integer> ingredientIdsInteger = new ArrayList<Integer>();
                for(int i = 0; i < ingredientIds.size(); i++)
                    ingredientIdsInteger.add(Integer.parseInt(ingredientIds.get(i)));
            
                Array ingredients = conn.createArrayOf("INTEGER", ingredientIdsInteger.toArray());
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
        }
    }

    private static HashMap<String, Double> getProducts(String csvFile) {
        HashMap<String, Double> productPriceMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                // Split the CSV line by comma
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String productName = parts[0].trim();
                    double price = Double.parseDouble(parts[parts.length-1].trim());
                    // Add the product name and price to the HashMap
                    productPriceMap.put(productName, price);
                }
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return productPriceMap;
    }

    private static List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();
    
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"')
                inQuotes = !inQuotes;
            else if (c == ',' && !inQuotes) {
                result.add(field.toString().trim());
                field.setLength(0); // reset field
            } 
            else
                field.append(c);
        }
        result.add(field.toString().trim()); // add last field
        return result;
    }
}

