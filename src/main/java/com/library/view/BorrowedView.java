package com.library.view;

import com.library.controller.BorrowController;
import com.library.model.BorrowRecord;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import java.util.ArrayList;
import java.util.List;

//This screen is for the library user. It lists the items that are currently borrowed
//(books, magazines, and DVDs), with the borrowed date, due date, and status.
//It is read-only (no return or edit buttons).
public class BorrowedView {

    private BorrowController controller;

    private TableView<BorrowRecord> table = new TableView<>();
    private ComboBox<String> typeFilterBox = new ComboBox<>();
    private TextField searchField = new TextField();
    private Label summaryLabel = new Label();

    public BorrowedView(BorrowController controller) {
        this.controller = controller;
    }

    public Parent createView() {
        Label heading = new Label("Borrowed Items");
        heading.getStyleClass().add("page-title");

        Label subtitle = new Label(
                "See which books, magazines, and DVDs are borrowed, "
                + "when they were borrowed, and when they are due."
        );
        subtitle.getStyleClass().add("page-subtitle");

        VBox header = new VBox(6, heading, subtitle);
        header.getStyleClass().add("page-header");

        // Filter by media type, plus search bar
        typeFilterBox.getItems().addAll("All", "Book", "Magazine", "DVD");
        typeFilterBox.setValue("All");
        typeFilterBox.setOnAction(event -> refresh());

        searchField.setPromptText("Search by borrower name or media title");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchField.setOnAction(event -> refresh());

        Button searchButton = new Button("Search");
        searchButton.getStyleClass().add("primary-button");
        searchButton.setOnAction(event -> refresh());

        Button resetButton = new Button("Show All");
        resetButton.setOnAction(event -> {
            searchField.clear();
            typeFilterBox.setValue("All");
            refresh();
        });

        HBox searchBar = new HBox(
                10, typeFilterBox, searchField, searchButton, resetButton
        );

        configureTable();

        Label tableTitle = new Label("Currently Borrowed");
        tableTitle.getStyleClass().add("section-title");

        summaryLabel.getStyleClass().add("helper-text");

        VBox tableCard = new VBox(
                12, tableTitle, summaryLabel, searchBar, table
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

        // Late fee so far (0.00 while the item is still on time).
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
                itemColumn,
                typeColumn,
                borrowerColumn,
                borrowedColumn,
                dueColumn,
                statusColumn,
                feeColumn
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

    // Shows only items that are not returned yet ("Borrowed" filter),
    // narrowed down by the selected media type and the search text.
    // Called by MainApp when the user switches to this tab.
    public void refresh() {
        BorrowRecord[] borrowed =
                controller.getRecords("Borrowed", searchField.getText());

        String selectedType = typeFilterBox.getValue();
        List<BorrowRecord> shown = new ArrayList<>();

        int books = 0;
        int magazines = 0;
        int dvds = 0;

        for (BorrowRecord record : borrowed) {
            if (selectedType == null
                    || "All".equals(selectedType)
                    || selectedType.equals(record.getItemType())) {
                shown.add(record);

                if ("Book".equals(record.getItemType())) {
                    books++;
                } else if ("Magazine".equals(record.getItemType())) {
                    magazines++;
                } else if ("DVD".equals(record.getItemType())) {
                    dvds++;
                }
            }
        }

        summaryLabel.setText(
                shown.size() + " borrowed  |  Books: " + books
                + "  Magazines: " + magazines + "  DVDs: " + dvds
        );

        table.setItems(FXCollections.observableArrayList(shown));

        // Redraw all cells so the status (Borrowed / Overdue) is up to date.
        table.refresh();
    }
}