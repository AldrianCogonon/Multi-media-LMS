package com.library.controller;

import com.library.model.LibraryItem;
import com.library.service.BookService;

//The controller passes requests from the interface to the service.
public class BookController {

    private BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    public LibraryItem[] getAllBooks() {
        return service.getAllBooks();
    }

    public LibraryItem[] searchBooks(String keyword) {
        return service.searchBooks(keyword);
    }

    public void addBook(String type, String title, String author,
                        String isbn, String category, String quantityText) {

        service.addBook(type, title, author, isbn, category, quantityText);
    }

    // Returns the existing record that has the same information, or null.
    public LibraryItem findExistingMatch(String type, String title, String author,
                                         String isbn, String category,
                                         String quantityText) {

        return service.findExistingMatch(
                type, title, author, isbn, category, quantityText
        );
    }

    // Adds copies to an existing record instead of creating a new one.
    public void addCopiesToExisting(int id, String quantityText) {
        service.addCopiesToExisting(id, quantityText);
    }

    public void updateBook(int id, String type, String title, String author,
                           String isbn, String category, String quantityText) {

        service.updateBook(id, type, title, author, isbn, category, quantityText);
    }

    public void deleteBook(int id) {
        service.deleteBook(id);
    }

    public int getAvailableCopies(int id) {
        return service.getAvailableCopies(id);
    }

    public double calculateLateFee(int id, int daysLate) {
        return service.calculateLateFee(id, daysLate);
    }
}