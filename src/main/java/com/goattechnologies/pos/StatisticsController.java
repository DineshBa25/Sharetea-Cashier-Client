package com.goattechnologies.pos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import java.io.IOException;
import java.util.Objects;

/** This class serves as the controller for the statistics view in a Point of Sale (POS) application. It manages the display
 * of a table of ingredients that are low on stock and provides functionality for navigating to the sales, excess, restock,
 * popularity, and what sells reports views. It also provides functionality for navigating back to the manager view.
 * @see RestockController
 * @see SalesReportController
 * @see ExcessReportController
 * @see PopularityTableController
 * @see PairsController
 */
public class StatisticsController {
    @FXML
    protected void onSalesReportClick() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("sales-view.fxml")));
        Main.getMainController().setView(node);
    }

    @FXML
    protected void onExcessReportClick() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("excess-view.fxml")));
        Main.getMainController().setView(node);
    }

    @FXML
    protected void onRestockReportClick() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("restock-view.fxml")));
        Main.getMainController().setView(node);
    }

    @FXML
    protected void onPopularityClick() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("popularity-view.fxml")));
        Main.getMainController().setView(node);
    }

    @FXML
    protected void onWhatSellsClick() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("pairs-view.fxml")));
        Main.getMainController().setView(node);
    }


    public void handleBackButton() throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("manager-view.fxml")));
        Main.getMainController().setView(node);
    }

}
