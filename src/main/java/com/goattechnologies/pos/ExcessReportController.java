package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The ExcessReportController class displays the inventory items that
 * only sold less than 10% of their inventory from a given timestamp
 * to the current time.
 * It provides methods for retrieving the current inventory list and
 * queries through the orders to find out how much of an inventory
 * item was sold.
 *
 * @author Ilham Aryawan
 */
public class ExcessReportController {

    /**
     * Represents an ingredient and its percentage sold.
     */
    public static class IngredientPercentageSold {
        private String ingredientName;
        private String percentageSold;

        public IngredientPercentageSold(String ingredientName, String percentageSold) {
            this.ingredientName = ingredientName;
            this.percentageSold = percentageSold;
        }
        public String getIngredient() {
            return ingredientName;
        }

        public String getPercentageSold() {
            return percentageSold;
        }

        public int compareTo(IngredientPercentageSold other) {
            return other.percentageSold.compareTo(this.percentageSold);
        }
    }

    @FXML
    private TableView<IngredientPercentageSold> excessReportView;
    @FXML
    private TableColumn<IngredientPercentageSold, String> itemColumn;
    @FXML
    private TableColumn<IngredientPercentageSold, String> percentSoldColumn;

    /**
     * Initializes the ExcessReportController with a given timestamp.
     *
     * @param timestamp The timestamp from which to calculate excess inventory.
     */
    public void initialize(Timestamp timestamp) {
        List<Ingredient> inventoryItems = Main.dbManager.getIngredients();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        List<IngredientPercentageSold> ingredientPercentageSoldList = new ArrayList<>();
        int totalQuantitySold = 0;

        for (Ingredient item : inventoryItems) {

            totalQuantitySold = calculateTotalQuantitySold(item, timestamp, currentTime);

            // Check if the percentage sold is less than 10%
            if (totalQuantitySold < 100) {
                IngredientPercentageSold ingredientPercentageSold = new IngredientPercentageSold(item.getIngredientName(), ((double) totalQuantitySold / 10) + "%");
                ingredientPercentageSoldList.add(ingredientPercentageSold);
                //System.out.println("Item: " + item.getIngredientName() + ", Percentage Sold: " + (double) totalQuantitySold / 100 + "%");
            }
        }
        Collections.sort(ingredientPercentageSoldList, (a, b) -> b.compareTo(a));

        excessReportView.getItems().addAll(ingredientPercentageSoldList);
        excessReportView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        centerTextInStringColumn(itemColumn);
        centerTextInStringColumn(percentSoldColumn);
    }

    /**
     * Parses the CSV file containing product ingredients and returns a map of products to their ingredient IDs.
     *
     * @return A map of products to their ingredient IDs.
     */
    private HashMap<String, List<Integer>> getProductIngredientsMap() {
        HashMap<String, List<Integer>> productIngredients = new HashMap<String, List<Integer>>();
        String csvFilePath = "order_gen/products.csv";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));

            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                List<String> fields = parseCsvLine(line);

                String ingredientList = fields.get(1);
                if (ingredientList.startsWith("\"") && ingredientList.endsWith("\""))
                    ingredientList = ingredientList.substring(1, ingredientList.length() - 1);
                ingredientList = ingredientList.substring(1, ingredientList.length() - 1); // remove [ and ]
                String[] ingredientArray = ingredientList.split(",\s*");
                List<String> ingredientIds = Arrays.asList(ingredientArray);
                List<Integer> ingredientIdsInteger = new ArrayList<Integer>();
                for(int i = 0; i < ingredientIds.size(); i++) {
                    ingredientIdsInteger.add(Integer.parseInt(ingredientIds.get(i)));
                }

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

    /**
     * Parses a CSV line and returns a list of fields.
     *
     * @param line The CSV line to be parsed.
     * @return A list of fields from the CSV line.
     */
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

    /**
     * Calculates the total quantity of an ingredient sold between two timestamps.
     *
     * @param item      The ingredient to calculate sales for.
     * @param startTime The start time of the sales period.
     * @param endTime   The end time of the sales period.
     * @return The total quantity of the ingredient sold.
     */
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

                for(int i = 0; i < productsArrayList.size(); i++) {
                    List<Integer> ingredients = new ArrayList<Integer>();
                    if(!productsArrayList.get(i).equals("None"))
                        ingredients = productIngredients.get(productsArrayList.get(i));
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

    /**
     * Centers the text in a string column of a TableView.
     *
     * @param column The TableColumn to center the text in.
     */
    private void centerTextInStringColumn(TableColumn<IngredientPercentageSold, String> column) {
        column.setCellFactory(tc -> {
            TableCell<IngredientPercentageSold, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            };
            cell.setAlignment(Pos.CENTER);

            return cell;
        });
    }

    /**
     * Handles the "Back" button click and navigates back to the previous view.
     *
     * @throws IOException If an error occurs during navigation.
     */
    public void handleBackButton() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("excess-view.fxml")));
        Main.getMainController().setView(node);
    }
}
