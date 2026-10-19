package com.library.repository;

import com.library.model.Book;
import java.util.Arrays;
import java.util.Optional;

public class BookRepository {

    private Book[] books = new Book[0];

    public Book[] findAll() {
        return Arrays.copyOf(books, books.length);
    }

    public Optional<Book> findById(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return Optional.of(book);
            }
        }

        return Optional.empty();
    }

    public void save(Book book) {
        books = Arrays.copyOf(books, books.length + 1);
        books[books.length - 1] = book;
    }

    public boolean update(Book updatedBook) {
        for (Book book : books) {
            if (book.getId() == updatedBook.getId()) {
                book.setTitle(updatedBook.getTitle());
                book.setAuthor(updatedBook.getAuthor());
                book.setIsbn(updatedBook.getIsbn());
                book.setCategory(updatedBook.getCategory());
                book.setQuantity(updatedBook.getQuantity());

                return true;
            }
        }

        return false;
    }

    public boolean deleteById(int id) {
        int index = -1;

        for (int i = 0; i < books.length; i++) {
            if (books[i].getId() == id) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false;
        }

        Book[] newBooks = new Book[books.length - 1];

        System.arraycopy(books, 0, newBooks, 0, index);

        System.arraycopy(
                books,
                index + 1,
                newBooks,
                index,
                books.length - index - 1
        );

        books = newBooks;

        return true;
    }
}