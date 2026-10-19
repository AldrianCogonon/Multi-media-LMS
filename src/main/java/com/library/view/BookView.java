package com.library.view;

import com.library.controller.BookController;
import com.library.model.Book;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

public class BookView {

    private final BookController controller;
    private final TableView<Book> table = new TableView<>();

    private final TextField titleField = new TextField();
    private final TextField authorField = new TextField();
    private final TextField isbnField = new TextField();
    private final TextField categoryField = new TextField();
    private final TextField quantityField = new TextField();

    public BookView(BookController controller) {
        this.controller = controller;
    }

    public Parent createView() {
        Label heading = new Label("Simple Library System");

        configureTable();

        titleField.setPromptText("Title");
        authorField.setPromptText("Author");
        isbnField.setPromptText("ISBN");
        categoryField.setPromptText("Category");
        quantityField.setPromptText("Quantity");

        GridPane form = new GridPane();
        form.setHgap(8);
        form.setVgap(8);

        form.addRow(0, new Label("Title"), titleField);
        form.addRow(1, new Label("Author"), authorField);
        form.addRow(2, new Label("ISBN"), isbnField);
        form.addRow(3, new Label("Category"), categoryField);
        form.addRow(4, new Label("Quantity"), quantityField);

        Button addButton = new Button("Add");
        Button updateButton = new Button("Update Selected");
        Button deleteButton = new Button("Delete Selected");
        Button clearButton = new Button("Clear");

        addButton.setOnAction(event -> addBook());
        updateButton.setOnAction(event -> updateBook());
        deleteButton.setOnAction(event -> deleteBook());
        clearButton.setOnAction(event -> clearForm());

        table.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldBook, selectedBook) -> {
                    if (selectedBook != null) {
                        populateForm(selectedBook);
                    }
                });

        HBox buttons = new HBox(
                8,
                addButton,
                updateButton,
                deleteButton,
                clearButton
        );

        VBox root = new VBox(12, heading, form, buttons, table);
        root.setPadding(new javafx.geometry.Insets(16));

        refreshTable();

        return root;
    }

    private void configureTable() {
        TableColumn<Book, Integer> idColumn =
                new TableColumn<>("ID");

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        TableColumn<Book, String> titleColumn =
                new TableColumn<>("Title");

        titleColumn.setCellValueFactory(
                new PropertyValueFactory<>("title")
        );

        TableColumn<Book, String> authorColumn =
                new TableColumn<>("Author");

        authorColumn.setCellValueFactory(
                new PropertyValueFactory<>("author")
        );

        TableColumn<Book, String> isbnColumn =
                new TableColumn<>("ISBN");

        isbnColumn.setCellValueFactory(
                new PropertyValueFactory<>("isbn")
        );

        TableColumn<Book, String> categoryColumn =
                new TableColumn<>("Category");

        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<>("category")
        );

        TableColumn<Book, Integer> quantityColumn =
                new TableColumn<>("Quantity");

        quantityColumn.setCellValueFactory(
                new PropertyValueFactory<>("quantity")
        );

        table.getColumns().addAll(
                idColumn,
                titleColumn,
                authorColumn,
                isbnColumn,
                categoryColumn,
                quantityColumn
        );

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        table.setPlaceholder(
                new Label("No books added yet.")
        );

        VBox.setVgrow(table, Priority.ALWAYS);
    }

    private void addBook() {
        try {
            controller.addBook(
                    titleField.getText(),
                    authorField.getText(),
                    isbnField.getText(),
                    categoryField.getText(),
                    readQuantity()
            );

            refreshTable();
            clearForm();

        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void updateBook() {
        Book selected =
                table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Select a book to update.");
            return;
        }

        try {
            controller.updateBook(
                    selected.getId(),
                    titleField.getText(),
                    authorField.getText(),
                    isbnField.getText(),
                    categoryField.getText(),
                    readQuantity()
            );

            refreshTable();
            clearForm();

        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void deleteBook() {
        Book selected =
                table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Select a book to delete.");
            return;
        }

        try {
            controller.deleteBook(selected.getId());

            refreshTable();
            clearForm();

        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private int readQuantity() {
        try {
            return Integer.parseInt(
                    quantityField.getText().trim()
            );

        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "Quantity must be a whole number."
            );
        }
    }

    private void populateForm(Book book) {
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        isbnField.setText(book.getIsbn());
        categoryField.setText(book.getCategory());

        quantityField.setText(
                String.valueOf(book.getQuantity())
        );
    }

    private void clearForm() {
        table.getSelectionModel().clearSelection();

        titleField.clear();
        authorField.clear();
        isbnField.clear();
        categoryField.clear();
        quantityField.clear();
    }

    private void refreshTable() {
        table.setItems(
                FXCollections.observableArrayList(
                        controller.getAllBooks()
                )
        );
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Library System");
        alert.setHeaderText("Unable to complete operation");
        alert.setContentText(message);

        alert.showAndWait();
    }
}