package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import java.io.IOException;
import java.util.Objects;

/** This class serves as the controller for the Manager view in a Point of Sale (POS) application. It manages the
 * display and interaction with the screen a manager would see after logging in.
 * @Author Mohsin Khan
 */
public class ManagerViewController {
    /** Handles the "Inventory" button click event, going to inventory-view.
     * @throws IOException if the inventory-view cannot be loaded
     */
    @FXML
    protected void onInventoryClick() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("inventory-view.fxml")));
        Main.getMainController().setView(node);
    }

    /** Handles the "Products" button click event, going to products-view.
     * @throws IOException if the products-view cannot be loaded
     */
    @FXML
    protected void onProductsClick() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("products-view.fxml")));
        Main.getMainController().setView(node);
    }

    /** Handles the "Statistics" button click event, going to statistics-view.
     * @throws IOException if the statistics-view cannot be loaded
     */
    @FXML
    protected void onStatisticsClick() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("statistics-view.fxml")));
        Main.getMainController().setView(node);
    }

    /** Handles the "Back" button click event, returning to the third login view.
     * @throws IOException if the third login view cannot be loaded
     */
    public void handleBackButton() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login3-view.fxml")));
        Main.getMainController().setView(node);
    }

}
