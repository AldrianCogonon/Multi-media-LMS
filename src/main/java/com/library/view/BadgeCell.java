package com.library.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;

//This table cell shows text as a small colored badge (pill).
//The color comes from the text, for example "Book", "DVD", "Borrowed", or "Overdue".
//The colors are set in library.css (badge-book, badge-dvd, badge-overdue, and so on).
public class BadgeCell<S> extends TableCell<S, String> {

    private final Label badge = new Label();

    public BadgeCell() {
        setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(String text, boolean empty) {
        super.updateItem(text, empty);

        if (empty || text == null) {
            setGraphic(null);
            return;
        }

        badge.setText(text);
        badge.getStyleClass().setAll(
                "badge",
                "badge-" + text.trim().toLowerCase().replace(' ', '-')
        );

        setGraphic(badge);
    }
}