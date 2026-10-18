package com.library;

import com.library.controller.BookController;
import com.library.repository.BookRepository;
import com.library.service.BookService;
import com.library.view.BookView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        BookRepository repository = new BookRepository();
        BookService service = new BookService(repository);
        BookController controller = new BookController(service);
        BookView view = new BookView(controller);

        Scene scene = new Scene(view.createView(), 900, 600);

        stage.setTitle("Simple Library System Management");
        stage.setMinWidth(700);
        stage.setMinHeight(500);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
