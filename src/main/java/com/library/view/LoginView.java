package com.library.view;

import com.library.controller.LoginController;
import com.library.model.User;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.function.Consumer;

//This displays the login form and opens the library screen after successful authentication.
//The left side is a brand panel, and the right side is the login form.
public class LoginView {

    private LoginController controller;
    private Consumer<User> onLoginSuccess;

    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Label messageLabel = new Label();

    public LoginView(LoginController controller,
                     Consumer<User> onLoginSuccess) {

        this.controller = controller;
        this.onLoginSuccess = onLoginSuccess;
    }

    public Parent createView() {
        // ----- Left side: brand panel -----
        Label brandTitle = new Label("Multi-Media Library");
        brandTitle.getStyleClass().add("login-brand-title");
        brandTitle.setWrapText(true);

        Label brandText = new Label(
                "Books, magazines, and DVDs, organized in one place."
        );
        brandText.getStyleClass().add("login-brand-text");
        brandText.setWrapText(true);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox brandPanel = new VBox(
                14,
                brandTitle,
                brandText,
                spacer,
                featureRow("Track every item and its copies"),
                featureRow("Borrow, return, and late fees"),
                featureRow("Search the whole collection")
        );
        brandPanel.getStyleClass().add("login-brand-panel");

        // ----- Right side: login form -----
        Label title = new Label("Welcome back");
        title.getStyleClass().add("login-title");

        Label subtitle = new Label("Log in to continue");
        subtitle.getStyleClass().add("login-subtitle");

        Label usernameLabel = new Label("Username");
        usernameLabel.getStyleClass().add("field-label");

        usernameField.setPromptText("Enter username");

        Label passwordLabel = new Label("Password");
        passwordLabel.getStyleClass().add("field-label");

        passwordField.setPromptText("Enter password");

        // The error box is hidden until a login fails.
        messageLabel.getStyleClass().add("login-error");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(Double.MAX_VALUE);
        showMessage("");

        Button loginButton = new Button("Log In");
        loginButton.getStyleClass().add("login-button");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setDefaultButton(true);

        loginButton.setOnAction(event -> handleLogin());
        passwordField.setOnAction(event -> handleLogin());

        VBox titleBox = new VBox(4, title, subtitle);

        VBox usernameBox = new VBox(6, usernameLabel, usernameField);
        VBox passwordBox = new VBox(6, passwordLabel, passwordField);

        VBox formBox = new VBox(
                18,
                titleBox,
                usernameBox,
                passwordBox,
                loginButton,
                messageLabel
        );
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.setMaxWidth(340);

        StackPane formPanel = new StackPane(formBox);
        formPanel.getStyleClass().add("login-form-panel");
        HBox.setHgrow(formPanel, Priority.ALWAYS);

        // ----- Whole screen -----
        HBox root = new HBox(brandPanel, formPanel);
        root.getStyleClass().add("login-root");

        URL stylesheet =
                getClass().getResource("/styles/library.css");

        if (stylesheet != null) {
            root.getStylesheets().add(
                    stylesheet.toExternalForm()
            );
        }

        return root;
    }

    // One line in the brand panel: a small dot and a short text.
    private HBox featureRow(String text) {
        Region dot = new Region();
        dot.getStyleClass().add("feature-dot");

        Label label = new Label(text);
        label.getStyleClass().add("login-feature");

        HBox row = new HBox(10, dot, label);
        row.setAlignment(Pos.CENTER_LEFT);

        return row;
    }

    // Shows the error box when there is a message, and hides it when empty.
    private void showMessage(String text) {
        boolean hasMessage = text != null && !text.isEmpty();

        messageLabel.setText(text);
        messageLabel.setVisible(hasMessage);
        messageLabel.setManaged(hasMessage);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = controller.login(username, password);

        if (user != null) {
            showMessage("");
            passwordField.clear();

            onLoginSuccess.accept(user);
        } else {
            showMessage("Invalid username or password. Please try again.");

            passwordField.clear();
            passwordField.requestFocus();
        }
    }
}