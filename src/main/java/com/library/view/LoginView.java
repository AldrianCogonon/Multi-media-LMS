package com.library.view;

import com.library.controller.LoginController;
import com.library.model.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.function.Consumer;

//This displays the login form and opens the library screen after successful authentication.
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
        Label title = new Label("Multi-Media LMS");
        title.getStyleClass().add("login-title");

        Label subtitle = new Label("Log in to continue");
        subtitle.getStyleClass().add("login-subtitle");

        usernameField.setPromptText("Enter username");
        usernameField.setMaxWidth(300);

        passwordField.setPromptText("Enter password");
        passwordField.setMaxWidth(300);

        messageLabel.setStyle("-fx-text-fill: #b42318;");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(300);

        Button loginButton = new Button("Log In");
        loginButton.getStyleClass().add("login-button");
        loginButton.setMaxWidth(300);
        loginButton.setDefaultButton(true);

        loginButton.setOnAction(event -> handleLogin());
        passwordField.setOnAction(event -> handleLogin());

        VBox loginCard = new VBox(
                14,
                title,
                subtitle,
                usernameField,
                passwordField,
                loginButton,
                messageLabel
        );

        loginCard.setAlignment(Pos.CENTER);
        loginCard.setMaxWidth(380);
        loginCard.getStyleClass().add("login-card");

        VBox root = new VBox(loginCard);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
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

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = controller.login(username, password);

        if (user != null) {
            messageLabel.setText("");
            passwordField.clear();

            onLoginSuccess.accept(user);
        } else {
            messageLabel.setText(
                    "Invalid username or password. Please try again."
            );

            passwordField.clear();
            passwordField.requestFocus();
        }
    }
}