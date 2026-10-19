package com.library.service;

import com.library.model.Book;
import com.library.repository.BookRepository;
import java.util.concurrent.atomic.AtomicInteger;

public class BookService {

    private final BookRepository repository;
    private final AtomicInteger nextId = new AtomicInteger(1);

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Book[] getAllBooks() {
        return repository.findAll();
    }

    public void addBook(String title, String author, String isbn,
                        String category, int quantity) {

        validate(title, author, isbn, category, quantity);

        Book book = new Book(
                nextId.getAndIncrement(),
                title.trim(),
                author.trim(),
                isbn.trim(),
                category.trim(),
                quantity
        );

        repository.save(book);
    }

    public void updateBook(int id, String title, String author,
                           String isbn, String category, int quantity) {

        validate(title, author, isbn, category, quantity);

        Book book = new Book(
                id,
                title.trim(),
                author.trim(),
                isbn.trim(),
                category.trim(),
                quantity
        );

        if (!repository.update(book)) {
            throw new IllegalArgumentException("Book not found.");
        }
    }

    public void deleteBook(int id) {
        if (!repository.deleteById(id)) {
            throw new IllegalArgumentException("Book not found.");
        }
    }

    private void validate(String title, String author, String isbn,
                          String category, int quantity) {

        if (title == null || title.isBlank() || author == null || author.isBlank() || isbn == null || isbn.isBlank() || category == null || category.isBlank()) {
            throw new IllegalArgumentException(
                    "Please complete all book fields."
            );
        }

        if (quantity < 0) {
            throw new IllegalArgumentException(
                    "Quantity cannot be negative."
            );
        }
    }
}