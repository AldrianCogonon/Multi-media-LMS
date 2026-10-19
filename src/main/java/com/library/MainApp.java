
package com.library;

import com.library.controller.BookController;
import com.library.controller.LoginController;
import com.library.repository.BookRepository;
import com.library.service.AuthService;
import com.library.service.BookService;
import com.library.view.BookView;
import com.library.view.LoginView;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage stage;

    private LoginController loginController;
    private BookController bookController;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        // Library user authentication
        AuthService authService = new AuthService();
        loginController = new LoginController(authService);

        // Existing book management system
        BookRepository repository = new BookRepository();
        BookService bookService = new BookService(repository);
        bookController = new BookController(bookService);

        showLogin();
    }

    private void showLogin() {
        LoginView loginView = new LoginView(
                loginController,
                this::showLibrary
        );

        Parent root = loginView.createView();

        Scene scene = new Scene(root, 420, 380);

        stage.setTitle("Library User Login");
        stage.setScene(scene);
        stage.setMinWidth(420);
        stage.setMinHeight(380);
        stage.setWidth(420);
        stage.setHeight(380);
        stage.centerOnScreen();
        stage.show();
    }

    private void showLibrary() {
        BookView bookView = new BookView(bookController);

        BorderPane root = new BorderPane();

        root.setCenter(bookView.createView());

        Label heading = new Label("Library Management");
        heading.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold;"
        );

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(event -> showLogin());

        HBox toolbar = new HBox(12, heading, logoutButton);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(12));

        root.setTop(toolbar);

        Scene scene = new Scene(root, 1000, 650);

        stage.setTitle("Simple Library System Management");
        stage.setScene(scene);
        stage.setMinWidth(700);
        stage.setMinHeight(500);
        stage.setWidth(1000);
        stage.setHeight(650);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
