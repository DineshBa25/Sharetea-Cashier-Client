package com.goattechnologies.pos;


import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Array;
import java.io.BufferedReader;
import java.io.FileReader;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ExcessController {

    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField timeTextField;

    public void generateTimeWindow() {
        try {
            String dateStr = datePicker.getValue().toString();
            String timeStr = timeTextField.getText();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateTime = dateFormat.parse(dateStr + " " + timeStr + ":00");
            Timestamp timestamp = new Timestamp(dateTime.getTime());

            generateExcessReport(timestamp);

        } catch (ParseException e) {
            System.err.println("Invalid date/time format.");
        }
    }

    public void generateExcessReport(Timestamp timestamp) {
        List<Ingredient> inventoryItems = Main.dbManager.getIngredients();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        ArrayList<Ingredient> itemsSoldLessThan10Percent = new ArrayList<>();
        int totalQuantitySold = 0;
        int id = 0;

        for (Ingredient item : inventoryItems) {
            // Calculate the total quantity sold between the given timestamp and current time
            totalQuantitySold = calculateTotalQuantitySold(item, timestamp, currentTime);
            id++;

            // Check if the percentage sold is less than 10%
            if (totalQuantitySold < 1000) {
                itemsSoldLessThan10Percent.add(item);
                System.out.println("Item: " + item.getIngredientName() + ", Percentage Sold: " + (double) totalQuantitySold / 100 + "%");
            }
        }

    }

    private HashMap<String, List<Integer>> getProductIngredientsMap() {
        HashMap<String, List<Integer>> productIngredients = new HashMap<String, List<Integer>>();
        String csvFilePath = "order_gen/products.csv";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));

            String line = reader.readLine();
            int id = 0;
            while ((line = reader.readLine()) != null) {
                List<String> fields = parseCsvLine(line);

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

                productIngredients.put(fields.get(0), ingredientIdsInteger);
            }

            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return productIngredients;
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

    private int calculateTotalQuantitySold(Ingredient item, Timestamp startTime, Timestamp endTime) {
        int totalQuantitySold = 0;
        String csvFilePath = "order_gen/orders.csv";
        HashMap<String, List<Integer>> productIngredients = getProductIngredientsMap();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));

            String line = reader.readLine();
            line = reader.readLine();
            String[] fields = line.split(", ");

            String csvWeek = "2023W" + fields[0];
            String csvDay = fields[1];
            String csvTime = fields[3];
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy'W'ww E HH:mm");
            java.util.Date date = sdf.parse(csvWeek + " " + csvDay + " " + csvTime);
            Timestamp timestamp = new Timestamp(date.getTime());

            while(timestamp.compareTo(startTime) < 0) {
                line = reader.readLine();
                fields = line.split(", ");
                csvWeek = "2023W" + fields[0];
                csvDay = fields[1];
                csvTime = fields[3];
                sdf = new SimpleDateFormat("yyyy'W'ww E HH:mm");
                date = sdf.parse(csvWeek + " " + csvDay + " " + csvTime);
                timestamp = new Timestamp(date.getTime());
            }

            while ((timestamp.compareTo(endTime)) <= 0) {
                fields = line.split(", ");

                String productList = fields[4];
                String toppingList = fields[6];
                String[] productsArray = productList.split("; ");
                String[] toppingArray = toppingList.split("; ");
                List<String> products = Arrays.asList(productsArray);
                ArrayList<String> productsArrayList = new ArrayList<>(products);
                List<String> toppings = Arrays.asList(toppingArray);
                ArrayList<String> toppingsArrayList = new ArrayList<>(toppings);
                productsArrayList.addAll(toppingsArrayList);

                for(int i = 0; i < products.size(); i++) {
                    List<Integer> ingredients = productIngredients.get(products.get(i));
                    for(int j = 0; j < ingredients.size(); j++) {
                        if(ingredients.get(j) == item.getIngredientId())
                            totalQuantitySold++;
                    }
                }

                line = reader.readLine();
                fields = line.split(", ");
                csvWeek = "2023W" + fields[0];
                csvDay = fields[1];
                csvTime = fields[3];
                sdf = new SimpleDateFormat("yyyy'W'ww E HH:mm");
                date = sdf.parse(csvWeek + " " + csvDay + " " + csvTime);
                timestamp = new Timestamp(date.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return totalQuantitySold;
    }


    public void handleBackButton() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("statistics-view.fxml")));
        Main.getMainController().setView(node);
    }
}
