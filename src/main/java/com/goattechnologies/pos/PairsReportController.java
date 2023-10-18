package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.*;

import java.io.IOException;
import java.util.*;

/**
 * This class serves as the controller for the pairs report view in a Point of Sale (POS) application. It manages the
 * display and interaction with the pairs report view, which displays the drink combos that were ordered most frequently
 * during a given time window.
 * @Author Cole Broberg
 */
public class PairsReportController {
    public static class PairFrequency {
        private String comboName;
        private int frequency;

        /**
         * Constructor for PairFrequency
         * @param comboName The name of the drink combo
         * @param frequency The frequency of the drink combo
         */
        public PairFrequency(String comboName, int frequency) {
            this.comboName = comboName;
            this.frequency = frequency;
        }

        /**
         * Gets the name of the drink combo
         * @return The name of the drink combo
         */
        public String getComboName() {
            return comboName;
        }

        /**
         * Gets the frequency of the drink combo
         * @return The frequency of the drink combo
         */
        public int getFrequency() {
            return frequency;
        }
    }

    @FXML
    private TableView<PairFrequency> pairsReportView;
    @FXML
    private TableColumn<PairFrequency, String> itemComboColumn;
    @FXML
    private TableColumn<PairFrequency, Integer> frequencyColumn;


    /**
     * Initializes the PairsReportController, setting up the pairs report view.
     * @param startTime The start time of the time window.
     * @param endTime   The end time of the time window.
     */
    @FXML
    public void initialize(Timestamp startTime, Timestamp endTime) {
        List<List<Integer>> multiDrinkOrders = Main.dbManager.getMultiOrders(startTime, endTime);
        List<Map.Entry<String, Integer>> pairFreqArray = getDrinkCombos(multiDrinkOrders);

        pairFreqArray.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        List<PairFrequency> pairFrequencyList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : pairFreqArray) {
            PairFrequency pairFrequency = new PairFrequency(entry.getKey(), entry.getValue());
            pairFrequencyList.add(pairFrequency);
        }
        Main.ingredients = Main.dbManager.getIngredients();
        pairsReportView.getItems().addAll(pairFrequencyList);
        pairsReportView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        centerTextInStringColumn(itemComboColumn);
        centerTextInIntegerColumn(frequencyColumn);
    }

    /**
     * Gets the drink combos from the database
     * @param multiDrinkOrders The list of multi-drink orders
     * @return The list of drink combos
     */
    private static List<Map.Entry<String, Integer>> getDrinkCombos(List<List<Integer>> multiDrinkOrders) {
        HashMap<Integer, String> drinks = Main.dbManager.getDrinks();
        HashMap<String, Integer> pairFrequencies = new HashMap<>();
        for (List<Integer> order : multiDrinkOrders) {
            for (int i = 0; i < order.size(); i++) {
                for (int j = i + 1; j < order.size(); j++) {
                    String combo = drinks.get(order.get(i)) + ", " + drinks.get(order.get(j));
                    if (!pairFrequencies.containsKey(combo)) {
                        String combo2 = drinks.get(order.get(j)) + ", " + drinks.get(order.get(i));
                        if (!pairFrequencies.containsKey(combo2)) {
                            pairFrequencies.put(combo, 1);
                            continue;
                        }
                        pairFrequencies.put(combo2, pairFrequencies.get(combo2) + 1);
                        continue;
                    }
                    pairFrequencies.put(combo, pairFrequencies.get(combo) + 1);
                }
            }
        }
        return new ArrayList<>(pairFrequencies.entrySet());
    }

    /**
     * Centers the text in a string column
     * @param column The column to center the text in
     */
    private void centerTextInStringColumn(TableColumn<PairFrequency, String> column) {
        column.setCellFactory(tc -> {
            TableCell<PairFrequency, String> cell = new TableCell<>() {
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
     * Centers the text in an integer column
     * @param column The column to center the text in
     */
    private void centerTextInIntegerColumn(TableColumn<PairFrequency, Integer> column) {
        column.setCellFactory(tc -> {
            TableCell<PairFrequency, Integer> cell = new TableCell<>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
            cell.setAlignment(Pos.CENTER);

            return cell;
        });
    }

    /**
     * Handles the event when the user clicks the "Back" button.
     * Navigates the user back to the initial pairs report screen.
     *
     * @throws IOException if there is an error while navigating back to the previous screen.
     */
    public void handleBackButton() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("pairs-view.fxml")));
        Main.getMainController().setView(node);
    }
}
