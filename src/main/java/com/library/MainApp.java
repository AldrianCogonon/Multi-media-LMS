package com.library;

import com.library.controller.BookController;
import com.library.controller.BorrowController;
import com.library.controller.LoginController;
import com.library.model.User;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRepository;
import com.library.service.AuthService;
import com.library.service.BookService;
import com.library.service.BorrowService;
import com.library.view.BookView;
import com.library.view.BorrowView;
import com.library.view.LoginView;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
    private BorrowController borrowController;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        AuthService authService = new AuthService();
        loginController = new LoginController(authService);

        BookRepository repository = new BookRepository();
        BorrowRepository borrowRepository = new BorrowRepository();

        BookService bookService = new BookService(repository, borrowRepository);
        bookController = new BookController(bookService);

        BorrowService borrowService =
                new BorrowService(repository, borrowRepository);
        borrowController = new BorrowController(borrowService);

        showLogin();
    }

    private void showLogin() {
        LoginView loginView = new LoginView(
                loginController,
                user -> showLibrary(user)
        );

        Parent root = loginView.createView();
        Scene scene = new Scene(root, 460, 420);

        addStylesheet(scene);

        stage.setTitle("Multi-Media LMS");
        stage.setScene(scene);
        stage.setMinWidth(420);
        stage.setMinHeight(380);
        stage.setWidth(460);
        stage.setHeight(420);
        stage.centerOnScreen();
        stage.show();
    }

    private void showLibrary(User user) {
        BookView bookView =
                new BookView(bookController, borrowController, user);

        BorderPane root = new BorderPane();

        if (user.canManageBorrowing()) {
            // Admin: Collection tab and Borrowing tab.
            BorrowView borrowView = new BorrowView(borrowController);

            Tab collectionTab = new Tab("Collection", bookView.createView());
            collectionTab.setClosable(false);

            Tab borrowingTab = new Tab("Borrowing", borrowView.createView());
            borrowingTab.setClosable(false);

            TabPane tabs = new TabPane(collectionTab, borrowingTab);

            // Refresh the tables so availability and loans are always up to date.
            tabs.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldTab, newTab) -> {
                        if (newTab == collectionTab) {
                            bookView.refresh();
                        } else {
                            borrowView.refresh();
                        }
                    }
            );

            root.setCenter(tabs);
        } else {
            // Library user: only the Collection page.
            root.setCenter(bookView.createView());
        }

        Label heading = new Label("Multi-Media Library Management System");
        heading.getStyleClass().add("toolbar-title");

        Label userLabel = new Label(
                "Logged in as " + user.getUsername()
                + " (" + user.getRole() + ")"
        );
        userLabel.getStyleClass().add("helper-text");

        Button logoutButton = new Button("Log Out");
        logoutButton.getStyleClass().add("logout-button");

        logoutButton.setOnAction(event -> showLogin());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox toolbar = new HBox(
                12,
                heading,
                spacer,
                userLabel,
                logoutButton
        );

        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(12, 24, 12, 24));
        toolbar.getStyleClass().add("app-toolbar");

        root.setTop(toolbar);

        Scene scene = new Scene(root, 1000, 650);
        addStylesheet(scene);

        stage.setTitle("Multi-Media Library Management System");
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