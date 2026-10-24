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
import com.library.view.BorrowedView;
import com.library.view.LoginView;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
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
        Scene scene = new Scene(root, 860, 540);

        addStylesheet(scene);

        stage.setTitle("Multi-Media LMS");
        stage.setScene(scene);
        stage.setMinWidth(760);
        stage.setMinHeight(500);
        stage.setWidth(860);
        stage.setHeight(540);
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
            tabs.getStyleClass().add("main-tabs");

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
            // Library user: Collection tab and Borrowed Items tab (read-only).
            BorrowedView borrowedView = new BorrowedView(borrowController);

            Tab collectionTab = new Tab("Collection", bookView.createView());
            collectionTab.setClosable(false);

            Tab borrowedTab =
                    new Tab("Borrowed Items", borrowedView.createView());
            borrowedTab.setClosable(false);

            TabPane tabs = new TabPane(collectionTab, borrowedTab);
            tabs.getStyleClass().add("main-tabs");

            // Refresh the tables so availability and loans are always up to date.
            tabs.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldTab, newTab) -> {
                        if (newTab == collectionTab) {
                            bookView.refresh();
                        } else {
                            borrowedView.refresh();
                        }
                    }
            );

            root.setCenter(tabs);
        }

        // Left side: app name
        Label heading = new Label("Multi-Media Library");
        heading.getStyleClass().add("toolbar-title");

        HBox brand = new HBox(10, heading);
        brand.setAlignment(Pos.CENTER_LEFT);

        // Right side: user (initial, name, role badge) and Log Out
        Label avatar = new Label(
                user.getUsername().substring(0, 1).toUpperCase()
        );
        avatar.getStyleClass().add("avatar");

        Label nameLabel = new Label(user.getUsername());
        nameLabel.getStyleClass().add("user-name");

        Label roleBadge = new Label(user.getRole());
        roleBadge.getStyleClass().add("role-badge");

        VBox userInfo = new VBox(2, nameLabel, roleBadge);
        userInfo.setAlignment(Pos.CENTER_LEFT);

        HBox userChip = new HBox(10, avatar, userInfo);
        userChip.setAlignment(Pos.CENTER_LEFT);

        Button logoutButton = new Button("Log Out");
        logoutButton.getStyleClass().add("logout-button");

        logoutButton.setOnAction(event -> confirmLogout());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox toolbar = new HBox(
                16,
                brand,
                spacer,
                userChip,
                logoutButton
        );

        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(10, 24, 10, 24));
        toolbar.getStyleClass().add("app-toolbar");

        root.setTop(toolbar);

        Scene scene = new Scene(root, 1100, 720);
        addStylesheet(scene);

        stage.setTitle("Multi-Media Library Management System");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(580);
        stage.setWidth(1100);
        stage.setHeight(720);
        stage.centerOnScreen();
        stage.show();
    }

    // Asks the user to confirm before going back to the login screen.
    // This is used by both the admin and the library user.
    private void confirmLogout() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);

        confirm.initOwner(stage);
        confirm.setTitle("Confirm Log Out");
        confirm.setHeaderText("Are you sure you want to log out?");
        confirm.setContentText("You will go back to the login screen.");

        Optional<ButtonType> result = confirm.showAndWait();

        // Log out only if the user clicked OK.
        if (result.isPresent() && result.get() == ButtonType.OK) {
            showLogin();
        }
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