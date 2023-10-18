package com.goattechnologies.pos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

import java.io.IOException;
import java.util.*;


public class SalesReportController {

    /**
     * This class is used to store the data for each row in the table.
     * It is used in the initialize() method below.
     */
    public static class ProductSales {
        private String productName;
        private double totalSales;

        /**
         * Constructor for ProductSales
         * @param productName The name of the product
         * @param totalSales The total sales of the product
         */
        public ProductSales(String productName, double totalSales) {
            this.productName = productName;
            this.totalSales = totalSales;
        }

        /**
         * Getter for productName
         * @return The name of the product
         */
        public String getProductName() {
            return productName;
        }

        /**
         * Getter for totalSales
         * @return The total sales of the product
         */
        public double getTotalSales() {
            return totalSales;
        }
    }

    @FXML
    private TableView<ProductSales> salesReportView;

    @FXML
    private TableColumn<ProductSales, String> itemColumn;

    @FXML
    private TableColumn<ProductSales, Double> salesColumn;


    /**
     * This method initializes the table with the data from the database.
     * It is called by SalesController.generateSalesReport().
     * @param startTime The start time of the time window.
     * @param endTime The end time of the time window.
     */
    @FXML
    public void initialize(Timestamp startTime, Timestamp endTime) {
        HashMap<String, Integer> multiDrinkOrders = Main.dbManager.getSalesByItem(startTime, endTime);

        ObservableList<ProductSales> data = FXCollections.observableArrayList();
        for (HashMap.Entry<String, Integer> entry : multiDrinkOrders.entrySet()) {
            data.add(new ProductSales(entry.getKey(), entry.getValue()));
        }
        itemColumn = new TableColumn<>("Item");
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        salesColumn = new TableColumn<>("Total Sales");
        salesColumn.setCellValueFactory(new PropertyValueFactory<>("totalSales"));
        // Compare in Descending order
        salesColumn.setComparator((sales1, sales2) -> Double.compare(sales2, sales1));

        salesReportView.getColumns().setAll(itemColumn, salesColumn);
        salesReportView.setItems(data);
        salesReportView.getSortOrder().add(salesColumn);
        salesReportView.sort();
        salesReportView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        centerTextInStringColumn(itemColumn);
        centerTextInDoubleColumn(salesColumn);
    }

    /**
     * This method is used to center the text in a String column.
     * @param column The column to center the text in.
     */
    private void centerTextInStringColumn(TableColumn<ProductSales, String> column) {
        column.setCellFactory(tc -> {
            TableCell<ProductSales, String> cell = new TableCell<>() {
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
     * This method is used to center the text in a Double column.
     * @param column The column to center the text in.
     */
    private void centerTextInDoubleColumn(TableColumn<ProductSales, Double> column) {
        column.setCellFactory(tc -> {
            TableCell<ProductSales, Double> cell = new TableCell<>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("$%.2f", item));
                    }
                }
            };
            cell.setAlignment(Pos.CENTER);

            return cell;
        });
    }

    /**
     * This method is called when the user clicks the "Back" button.
     * It will return the user to the Sales View.
     * @throws IOException
     */
    public void handleBackButton() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("sales-view.fxml")));
        Main.getMainController().setView(node);
    }
}
