package com.library.controller;

import com.library.model.Book;
import com.library.service.BookService;
import java.util.List;

public class BookController {
    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    public List<Book> getAllBooks() {
        return service.getAllBooks();
    }

    public void addBook(String title, String author, String isbn,
                        String category, int quantity) {
        service.addBook(title, author, isbn, category, quantity);
    }

    public void updateBook(int id, String title, String author,
                           String isbn, String category, int quantity) {
        service.updateBook(id, title, author, isbn, category, quantity);
    }

    public void deleteBook(int id) {
        service.deleteBook(id);
    }
}
