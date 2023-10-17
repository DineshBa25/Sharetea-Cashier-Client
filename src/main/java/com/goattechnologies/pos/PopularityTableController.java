package com.goattechnologies.pos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import java.util.HashMap;

public class PopularityTableController {
    @FXML
    private TableView<ProductPopularity> productTable;

    private HashMap<String, Integer> productNamesPopularity;

    public void setPopularityMap(HashMap<String, Integer> productMap) {
        productNamesPopularity = productMap;
    }

    public void loadData() {
        ObservableList<ProductPopularity> data = FXCollections.observableArrayList();
        for (HashMap.Entry<String, Integer> entry : productNamesPopularity.entrySet()) {
            data.add(new ProductPopularity(entry.getKey(), entry.getValue()));
        }
        productTable.setItems(data);
    }

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

