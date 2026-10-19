
package com.library.view;

import com.library.controller.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginView {

    private final LoginController controller;
    private final Runnable onLoginSuccess;

    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Label messageLabel = new Label();

    public LoginView(
            LoginController controller,
            Runnable onLoginSuccess
    ) {
        this.controller = controller;
        this.onLoginSuccess = onLoginSuccess;
    }

    public Parent createView() {
        Label title = new Label("Simple Library System");
        title.setStyle(
                "-fx-font-size: 22px; -fx-font-weight: bold;"
        );

        Label subtitle = new Label("Library User Login");

        usernameField.setPromptText("Enter username");
        usernameField.setMaxWidth(280);

        passwordField.setPromptText("Enter password");
        passwordField.setMaxWidth(280);

        messageLabel.setStyle("-fx-text-fill: #c62828;");
        messageLabel.setWrapText(true);

        Button loginButton = new Button("Login");
        loginButton.setMaxWidth(280);
        loginButton.setDefaultButton(true);

        loginButton.setOnAction(event -> handleLogin());
        passwordField.setOnAction(event -> handleLogin());

        VBox root = new VBox(
                14,
                title,
                subtitle,
                usernameField,
                passwordField,
                loginButton,
                messageLabel
        );

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f4f6f8;");

        return root;
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (controller.login(username, password)) {
            messageLabel.setText("");
            passwordField.clear();
            onLoginSuccess.run();
        } else {
            messageLabel.setText(
                    "Invalid username or password. Please try again."
            );

            passwordField.clear();
            passwordField.requestFocus();
        }
    }
}
