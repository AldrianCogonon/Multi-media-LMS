package com.library.view;

import com.library.controller.BorrowController;
import com.library.model.BorrowRecord;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
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

//This screen is for the library user. It lists the items that are currently borrowed,
//with the borrowed date and due date. It is read-only (no return or edit buttons).
public class BorrowedView {

    private BorrowController controller;

    private TableView<BorrowRecord> table = new TableView<>();
    private TextField searchField = new TextField();

    public BorrowedView(BorrowController controller) {
        this.controller = controller;
    }

    public Parent createView() {
        Label heading = new Label("Borrowed Items");
        heading.getStyleClass().add("page-title");

        Label subtitle = new Label(
                "See which media are borrowed, when they were borrowed, and when they are due."
        );
        subtitle.getStyleClass().add("page-subtitle");

        VBox header = new VBox(6, heading, subtitle);
        header.getStyleClass().add("page-header");

        // Search bar
        searchField.setPromptText("Search by borrower name or media title");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchField.setOnAction(event -> refresh());

        Button searchButton = new Button("Search");
        searchButton.getStyleClass().add("primary-button");
        searchButton.setOnAction(event -> refresh());

        Button resetButton = new Button("Show All");
        resetButton.setOnAction(event -> {
            searchField.clear();
            refresh();
        });

        HBox searchBar = new HBox(10, searchField, searchButton, resetButton);

        configureTable();

        Label tableTitle = new Label("Currently Borrowed");
        tableTitle.getStyleClass().add("section-title");

        VBox tableCard = new VBox(12, tableTitle, searchBar, table);
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
        TableColumn<BorrowRecord, String> itemColumn = new TableColumn<>("Media");
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("itemTitle"));

        TableColumn<BorrowRecord, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("itemType"));

        TableColumn<BorrowRecord, String> borrowerColumn =
                new TableColumn<>("Borrower");
        borrowerColumn.setCellValueFactory(
                new PropertyValueFactory<>("borrowerName")
        );

        TableColumn<BorrowRecord, LocalDate> borrowedColumn =
                new TableColumn<>("Borrowed Date");
        borrowedColumn.setCellValueFactory(
                new PropertyValueFactory<>("borrowDate")
        );

        TableColumn<BorrowRecord, LocalDate> dueColumn =
                new TableColumn<>("Due Date");
        dueColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        TableColumn<BorrowRecord, String> statusColumn =
                new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(
                itemColumn,
                typeColumn,
                borrowerColumn,
                borrowedColumn,
                dueColumn,
                statusColumn
        );

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No borrowed items."));

        // Highlight overdue items.
        table.setRowFactory(tv -> new TableRow<BorrowRecord>() {
            @Override
            protected void updateItem(BorrowRecord record, boolean empty) {
                super.updateItem(record, empty);

                if (empty || record == null
                        || !"Overdue".equals(record.getStatus())) {
                    setStyle("");
                } else {
                    setStyle("-fx-background-color: #fff0ef;");
                }
            }
        });
    }

    // Shows only items that are not returned yet ("Borrowed" filter).
    // Called by MainApp when the user switches to this tab.
    public void refresh() {
        table.setItems(
                FXCollections.observableArrayList(
                        controller.getRecords("Borrowed", searchField.getText())
                )
        );

        // Redraw all cells so the status (Borrowed / Overdue) is up to date.
        table.refresh();
    }
}