package com.goattechnologies.pos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * This class serves as the controller for the popularity table view in a Point of Sale (POS) application. It manages
 * the display and interaction with the popularity table view, which displays the popularity of each product in the
 * database.
 * @Author Mohsin Khan
 */
public class PopularityTableController {
    @FXML
    private TableView<ProductPopularity> productTable;

    private HashMap<String, Integer> productNamesPopularity;

    /**
     * Sets the popularity map to be used to generate the popularity report.
     */
    public void setPopularityMap(HashMap<String, Integer> productMap) {
        productNamesPopularity = productMap;
    }

    /**
     * Loads the data into the popularity table.
     */
    public void loadData() {
        ObservableList<ProductPopularity> data = FXCollections.observableArrayList();
        for (HashMap.Entry<String, Integer> entry : productNamesPopularity.entrySet()) {
            data.add(new ProductPopularity(entry.getKey(), entry.getValue()));
        }
        TableColumn<ProductPopularity, String> productNameColumn = new TableColumn<>("Product Name");
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        TableColumn<ProductPopularity, Integer> popularityColumn = new TableColumn<>("Popularity in Order Count");
        popularityColumn.setCellValueFactory(new PropertyValueFactory<>("popularity"));
        // Compare in Descending order
        popularityColumn.setComparator((pop1, pop2) -> Integer.compare(pop2, pop1));
        productTable.getColumns().setAll(productNameColumn, popularityColumn);
        productTable.setItems(data);
        productTable.getSortOrder().add(popularityColumn);
        productTable.sort();
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        centerTextInStringColumn(productNameColumn);
        centerTextInIntegerColumn(popularityColumn);
    }

    /**
     * Centers the text in a string column.
     * @param column The column to center the text in.
     */
    private void centerTextInStringColumn(TableColumn<ProductPopularity, String> column) {
        column.setCellFactory(tc -> {
            TableCell<ProductPopularity, String> cell = new TableCell<>() {
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
     * Centers the text in an integer column.
     * @param column The column to center the text in.
     */
    private void centerTextInIntegerColumn(TableColumn<ProductPopularity, Integer> column) {
        column.setCellFactory(tc -> {
            TableCell<ProductPopularity, Integer> cell = new TableCell<>() {
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
     * Handles the back button being pressed.
     */
    public void handleBackButton() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("popularity-view.fxml")));
        Main.getMainController().setView(node);
    }

    /**
     * A helper class to store the product name and its popularity.
     */
    public static class ProductPopularity {
        private final String productName;
        private final int popularity;

        public ProductPopularity(String productName, int popularity) {
            this.productName = productName;
            this.popularity = popularity;
        }

        public String getProductName() {
            return productName;
        }

        public int getPopularity() {
            return popularity;
        }
    }
}
