package com.library.view;

import com.library.controller.BorrowController;
import com.library.model.BorrowRecord;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;

//This screen shows all borrow records with a filter and search bar, and lets the user return an item.
public class BorrowView {

    private BorrowController controller;

    private TableView<BorrowRecord> table = new TableView<>();
    private ComboBox<String> filterBox = new ComboBox<>();
    private TextField searchField = new TextField();

    public BorrowView(BorrowController controller) {
        this.controller = controller;
    }

    public Parent createView() {
        Label heading = new Label("Borrowing");
        heading.getStyleClass().add("page-title");

        Label subtitle = new Label(
                "Track borrowed items, return them, and see late fees."
        );
        subtitle.getStyleClass().add("page-subtitle");

        VBox header = new VBox(6, heading, subtitle);
        header.getStyleClass().add("page-header");

        // Filter and search
        filterBox.getItems().addAll("All", "Borrowed", "Overdue", "Returned");
        filterBox.setValue("All");
        filterBox.setOnAction(event -> refresh());

        searchField.setPromptText("Search by borrower name or item title");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchField.setOnAction(event -> refresh());

        Button searchButton = new Button("Search");
        searchButton.getStyleClass().add("primary-button");
        searchButton.setOnAction(event -> refresh());

        Button resetButton = new Button("Show All");
        resetButton.setOnAction(event -> {
            searchField.clear();
            filterBox.setValue("All");
            refresh();
        });

        HBox searchBar = new HBox(
                10, filterBox, searchField, searchButton, resetButton
        );

        Button returnButton = new Button("Return");
        returnButton.getStyleClass().add("primary-button");
        returnButton.setOnAction(event -> returnSelected());

        configureTable();

        Label tableTitle = new Label("Borrow Records");
        tableTitle.getStyleClass().add("section-title");

        Label hint = new Label(
                "Select a record that is not yet returned, then click Return."
        );
        hint.getStyleClass().add("helper-text");

        VBox tableCard = new VBox(
                12, tableTitle, hint, searchBar, table, returnButton
        );
        tableCard.getStyleClass().add("section-card");
        VBox.setVgrow(tableCard, Priority.ALWAYS);
        VBox.setVgrow(table, Priority.ALWAYS);

        VBox root = new VBox(18, header, tableCard);
        root.setPadding(new Insets(24));
        root.getStyleClass().add("root-pane");

        URL stylesheet = getClass().getResource("/styles/library.css");

        if (stylesheet != null) {
            root.getStylesheets().add(stylesheet.toExternalForm());
        }

        refresh();

        return root;
    }

    private void configureTable() {
        TableColumn<BorrowRecord, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<BorrowRecord, String> borrowerColumn =
                new TableColumn<>("Borrower");
        borrowerColumn.setCellValueFactory(
                new PropertyValueFactory<>("borrowerName")
        );

        TableColumn<BorrowRecord, String> itemColumn = new TableColumn<>("Item");
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("itemTitle"));

        TableColumn<BorrowRecord, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        typeColumn.setCellFactory(column -> new BadgeCell<>());

        TableColumn<BorrowRecord, LocalDate> borrowedColumn =
                new TableColumn<>("Borrowed");
        borrowedColumn.setCellValueFactory(
                new PropertyValueFactory<>("borrowDate")
        );

        TableColumn<BorrowRecord, LocalDate> dueColumn = new TableColumn<>("Due");
        dueColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        TableColumn<BorrowRecord, LocalDate> returnedColumn =
                new TableColumn<>("Returned");
        returnedColumn.setCellValueFactory(
                new PropertyValueFactory<>("returnDate")
        );

        TableColumn<BorrowRecord, String> statusColumn =
                new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellFactory(column -> new BadgeCell<>());

        // Fee so far for borrowed items, final fee for returned items.
        TableColumn<BorrowRecord, String> feeColumn =
                new TableColumn<>("Late Fee (PHP)");
        feeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        String.format(
                                "%.2f",
                                controller.estimateLateFee(cellData.getValue())
                        )
                )
        );

        table.getColumns().addAll(
                idColumn,
                borrowerColumn,
                itemColumn,
                typeColumn,
                borrowedColumn,
                dueColumn,
                returnedColumn,
                statusColumn,
                feeColumn
        );

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No borrow records to show."));

        // Highlight overdue records.
        table.setRowFactory(tv -> new TableRow<BorrowRecord>() {
            @Override
            protected void updateItem(BorrowRecord record, boolean empty) {
                super.updateItem(record, empty);

                // "overdue-row" is colored in library.css.
                getStyleClass().remove("overdue-row");

                if (!empty && record != null
                        && "Overdue".equals(record.getStatus())) {
                    getStyleClass().add("overdue-row");
                }
            }
        });
    }

    private void returnSelected() {
        BorrowRecord selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Please select a borrow record to return.");
            return;
        }

        if (selected.isReturned()) {
            showError("This item was already returned.");
            return;
        }

        // The return date defaults to today. It can be changed to record a past return.
        DatePicker datePicker = new DatePicker(LocalDate.now());

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Return Item");
        dialog.setHeaderText(
                selected.getItemTitle() + " borrowed by "
                + selected.getBorrowerName()
                + "\nDue date: " + selected.getDueDate()
        );
        dialog.getDialogPane().setContent(
                new HBox(10, new Label("Return date:"), datePicker)
        );
        dialog.getDialogPane().setPadding(new Insets(10));
        dialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK, ButtonType.CANCEL
        );

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                double fee = controller.returnItem(
                        selected.getId(),
                        datePicker.getValue()
                );

                refresh();

                Alert info = new Alert(Alert.AlertType.INFORMATION);

                info.setTitle("Item Returned");
                info.setHeaderText(selected.getItemTitle() + " returned");
                info.setContentText(
                        fee > 0
                        ? "Returned late. Late fee: PHP "
                                + String.format("%.2f", fee)
                        : "Returned on time. No late fee."
                );

                info.showAndWait();

            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        }
    }

    // Called by MainApp when the user switches to this tab.
    public void refresh() {
        table.setItems(
                FXCollections.observableArrayList(
                        controller.getRecords(
                                filterBox.getValue(),
                                searchField.getText()
                        )
                )
        );

        // Redraw all cells so status, return date and late fee are up to date.
        table.refresh();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Invalid Input");
        alert.setHeaderText("Please check your input");
        alert.setContentText(message);

        alert.showAndWait();
    }
}