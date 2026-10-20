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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.net.URL;
//This starts the program, shows the login screen, opens the library interface after login,
//and returns to login when the user logs out.
public class MainApp extends Application {

    private Stage stage;

    private LoginController loginController;
    private BookController bookController;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        AuthService authService = new AuthService();
        loginController = new LoginController(authService);

        BookRepository repository = new BookRepository();
        BookService bookService = new BookService(repository);
        bookController = new BookController(bookService);

        showLogin();
    }

    private void showLogin() {
        LoginView loginView = new LoginView(
                loginController,
                new Runnable() {
                    @Override
                    public void run() {
                        showLibrary();
                    }
                }
        );

        Parent root = loginView.createView();
        Scene scene = new Scene(root, 460, 420);

        addStylesheet(scene);

        stage.setTitle("Library User Login");
        stage.setScene(scene);
        stage.setMinWidth(420);
        stage.setMinHeight(380);
        stage.setWidth(460);
        stage.setHeight(420);
        stage.centerOnScreen();
        stage.show();
    }

    private void showLibrary() {
        BookView bookView = new BookView(bookController);

        BorderPane root = new BorderPane();
        root.setCenter(bookView.createView());

        Label heading = new Label("Library Management");
        heading.getStyleClass().add("toolbar-title");

        Button logoutButton = new Button("Log Out");
        logoutButton.getStyleClass().add("logout-button");

        logoutButton.setOnAction(event -> showLogin());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox toolbar = new HBox(
                12,
                heading,
                spacer,
                logoutButton
        );

        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(12, 24, 12, 24));
        toolbar.getStyleClass().add("app-toolbar");

        root.setTop(toolbar);

        Scene scene = new Scene(root, 1000, 650);
        addStylesheet(scene);

        stage.setTitle("Simple Library System Management");
        stage.setScene(scene);
        stage.setMinWidth(700);
        stage.setMinHeight(500);
        stage.setWidth(1000);
        stage.setHeight(650);
        stage.centerOnScreen();
        stage.show();
    }

    private void addStylesheet(Scene scene) {
        URL stylesheet =
                getClass().getResource("/styles/library.css");

        if (stylesheet != null) {
            scene.getStylesheets().add(
                    stylesheet.toExternalForm()
            );
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
