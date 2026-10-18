package com.library.repository;

import com.library.model.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepository {
    private final List<Book> books = new ArrayList<>();

    public List<Book> findAll() {
        return new ArrayList<>(books);
    }

    public Optional<Book> findById(int id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst();
    }

    public void save(Book book) {
        books.add(book);
    }

    public boolean update(Book updatedBook) {
        Optional<Book> existing = findById(updatedBook.getId());

        if (existing.isEmpty()) {
            return false;
        }

        Book book = existing.get();
        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setIsbn(updatedBook.getIsbn());
        book.setCategory(updatedBook.getCategory());
        book.setQuantity(updatedBook.getQuantity());
        return true;
    }

    public boolean deleteById(int id) {
        return books.removeIf(book -> book.getId() == id);
    }
}
