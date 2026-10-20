package com.library.view;

import com.library.controller.BookController;
import com.library.model.LibraryItem;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Optional;

//This screen includes the item form, search bar, collection table, and Add, Update, Delete, Late Fee, and Clear buttons.
public class BookView {

    private BookController controller;

    private TableView<LibraryItem> table = new TableView<>();

    private ComboBox<String> typeBox = new ComboBox<>();
    private TextField titleField = new TextField();
    private TextField authorField = new TextField();
    private TextField isbnField = new TextField();
    private TextField categoryField = new TextField();
    private TextField quantityField = new TextField();

    private TextField searchField = new TextField();

    public BookView(BookController controller) {
        this.controller = controller;
    }

    public Parent createView() {
        // Header
        Label heading = new Label("Simple Library System");
        heading.getStyleClass().add("page-title");

        Label subtitle = new Label(
                "Manage your library collection in one place."
        );
        subtitle.getStyleClass().add("page-subtitle");

        VBox header = new VBox(6, heading, subtitle);
        header.getStyleClass().add("page-header");

        // Input placeholders
        titleField.setPromptText("Enter title");
        authorField.setPromptText("Enter author name");
        isbnField.setPromptText("Enter ISBN");
        categoryField.setPromptText("Enter category");
        quantityField.setPromptText("Enter quantity");

        // Item type choices
        typeBox.getItems().addAll("Book", "Magazine", "DVD");
        typeBox.setValue("Book");
        typeBox.setMaxWidth(Double.MAX_VALUE);

        // Item form
        GridPane form = new GridPane();
        form.setHgap(14);
        form.setVgap(12);

        form.add(new Label("Title"), 0, 0);
        form.add(titleField, 1, 0);

        form.add(new Label("Author"), 0, 1);
        form.add(authorField, 1, 1);

        form.add(new Label("ISBN"), 0, 2);
        form.add(isbnField, 1, 2);

        form.add(new Label("Category"), 2, 0);
        form.add(categoryField, 3, 0);

        form.add(new Label("Quantity"), 2, 1);
        form.add(quantityField, 3, 1);

        form.add(new Label("Type"), 2, 2);
        form.add(typeBox, 3, 2);

        GridPane.setHgrow(titleField, Priority.ALWAYS);
        GridPane.setHgrow(authorField, Priority.ALWAYS);
        GridPane.setHgrow(isbnField, Priority.ALWAYS);
        GridPane.setHgrow(categoryField, Priority.ALWAYS);
        GridPane.setHgrow(quantityField, Priority.ALWAYS);
        GridPane.setHgrow(typeBox, Priority.ALWAYS);

        // Buttons
        Button addButton = new Button("Add Item");
        addButton.getStyleClass().add("primary-button");

        Button updateButton = new Button("Update Selected");

        Button deleteButton = new Button("Delete Selected");
        deleteButton.getStyleClass().add("danger-button");

        Button lateFeeButton = new Button("Late Fee");

        Button clearButton = new Button("Clear Form");

        addButton.setOnAction(event -> addBook());
        updateButton.setOnAction(event -> updateBook());
        deleteButton.setOnAction(event -> deleteBook());
        lateFeeButton.setOnAction(event -> showLateFee());
        clearButton.setOnAction(event -> clearForm());

        HBox buttons = new HBox(
                10,
                addButton,
                updateButton,
                deleteButton,
                lateFeeButton,
                clearButton
        );

        // Form card
        Label formTitle = new Label("Item Details");
        formTitle.getStyleClass().add("section-title");

        Label formHint = new Label(
                "Fill in the item information. "
                + "Select a row to edit an item."
        );
        formHint.getStyleClass().add("helper-text");

        VBox formCard = new VBox(
                14,
                formTitle,
                formHint,
                form,
                buttons
        );
        formCard.getStyleClass().add("section-card");

        // Search bar
        searchField.setPromptText(
                "Search by title, author, ISBN, category, or type"
        );
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button searchButton = new Button("Search");
        searchButton.getStyleClass().add("primary-button");

        Button resetButton = new Button("Show All");

        searchButton.setOnAction(event -> refreshTable());
        searchField.setOnAction(event -> refreshTable());
        resetButton.setOnAction(event -> {
            searchField.clear();
            refreshTable();
        });

        HBox searchBar = new HBox(10, searchField, searchButton, resetButton);

        // Item table
        configureTable();

        Label tableTitle = new Label("Library Collection");
        tableTitle.getStyleClass().add("section-title");

        VBox tableCard = new VBox(12, tableTitle, searchBar, table);
        tableCard.getStyleClass().add("section-card");
        VBox.setVgrow(tableCard, Priority.ALWAYS);
        VBox.setVgrow(table, Priority.ALWAYS);

        // Show selected item details in the form.
        table.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldItem, selectedItem) -> {
                    if (selectedItem != null) {
                        populateForm(selectedItem);
                    }
                });

        // Main layout
        VBox root = new VBox(
                18,
                header,
                formCard,
                tableCard
        );

        root.setPadding(new Insets(24));
        root.getStyleClass().add("root-pane");

        URL stylesheet =
                getClass().getResource("/styles/library.css");

        if (stylesheet != null) {
            root.getStylesheets().add(
                    stylesheet.toExternalForm()
            );
        }

        refreshTable();

        return root;
    }

    private void configureTable() {
        TableColumn<LibraryItem, Integer> idColumn =
                new TableColumn<>("ID");
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        // Calls getItemType(). Each child class returns its own value.
        TableColumn<LibraryItem, String> typeColumn =
                new TableColumn<>("Type");
        typeColumn.setCellValueFactory(
                new PropertyValueFactory<>("itemType")
        );

        TableColumn<LibraryItem, String> titleColumn =
                new TableColumn<>("Title");
        titleColumn.setCellValueFactory(
                new PropertyValueFactory<>("title")
        );

        TableColumn<LibraryItem, String> authorColumn =
                new TableColumn<>("Author");
        authorColumn.setCellValueFactory(
                new PropertyValueFactory<>("author")
        );

        TableColumn<LibraryItem, String> isbnColumn =
                new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(
                new PropertyValueFactory<>("isbn")
        );

        TableColumn<LibraryItem, String> categoryColumn =
                new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<>("category")
        );

        TableColumn<LibraryItem, Integer> quantityColumn =
                new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(
                new PropertyValueFactory<>("quantity")
        );

        // Calls getLoanPeriodDays(). Each child class returns its own value.
        TableColumn<LibraryItem, Integer> loanColumn =
                new TableColumn<>("Loan Days");
        loanColumn.setCellValueFactory(
                new PropertyValueFactory<>("loanPeriodDays")
        );

        table.getColumns().addAll(
                idColumn,
                typeColumn,
                titleColumn,
                authorColumn,
                isbnColumn,
                categoryColumn,
                quantityColumn,
                loanColumn
        );

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        table.setPlaceholder(
                new Label("No items to show.")
        );
    }

    private void addBook() {
        try {
            controller.addBook(
                    typeBox.getValue(),
                    titleField.getText(),
                    authorField.getText(),
                    isbnField.getText(),
                    categoryField.getText(),
                    quantityField.getText()
            );

            refreshTable();
            clearForm();

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void updateBook() {
        LibraryItem selectedItem =
                table.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showError("Please select an item to update.");
            return;
        }

        try {
            controller.updateBook(
                    selectedItem.getId(),
                    typeBox.getValue(),
                    titleField.getText(),
                    authorField.getText(),
                    isbnField.getText(),
                    categoryField.getText(),
                    quantityField.getText()
            );

            refreshTable();
            clearForm();

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void deleteBook() {
        LibraryItem selectedItem =
                table.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showError("Please select an item to delete.");
            return;
        }

        // Ask for confirmation before deleting.
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);

        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Are you sure you want to delete this item?");
        confirm.setContentText(
                selectedItem.getDetails()
                + "\n\nThis action cannot be undone."
        );

        Optional<ButtonType> result = confirm.showAndWait();

        // Delete only if the user clicked OK.
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                controller.deleteBook(selectedItem.getId());

                refreshTable();
                clearForm();

            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        }
    }

    // POLYMORPHISM in action: the same button gives a different fee
    // depending on whether the selected item is a Book, Magazine, or DVD.
    private void showLateFee() {
        LibraryItem selectedItem =
                table.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showError("Please select an item to compute the late fee.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog("0");

        dialog.setTitle("Late Fee");
        dialog.setHeaderText(selectedItem.getDetails());
        dialog.setContentText("Days late:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                int daysLate = Integer.parseInt(result.get().trim());

                double fee = controller.calculateLateFee(
                        selectedItem.getId(),
                        daysLate
                );

                Alert info = new Alert(Alert.AlertType.INFORMATION);

                info.setTitle("Late Fee");
                info.setHeaderText(selectedItem.getItemType() + " late fee");
                info.setContentText(
                        daysLate + " day(s) late = PHP "
                        + String.format("%.2f", fee)
                );

                info.showAndWait();

            } catch (NumberFormatException e) {
                showError("Days late must be a whole number.");
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        }
    }

    private void populateForm(LibraryItem item) {
        typeBox.setValue(item.getItemType());
        titleField.setText(item.getTitle());
        authorField.setText(item.getAuthor());
        isbnField.setText(item.getIsbn());
        categoryField.setText(item.getCategory());
        quantityField.setText(
                String.valueOf(item.getQuantity())
        );
    }

    private void clearForm() {
        table.getSelectionModel().clearSelection();

        typeBox.setValue("Book");
        titleField.clear();
        authorField.clear();
        isbnField.clear();
        categoryField.clear();
        quantityField.clear();
    }

    // Shows all items, or only the items that match the search keyword.
    private void refreshTable() {
        table.setItems(
                FXCollections.observableArrayList(
                        controller.searchBooks(searchField.getText())
                )
        );
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Invalid Input");
        alert.setHeaderText("Please check your input");
        alert.setContentText(message);

        alert.showAndWait();
    }
}