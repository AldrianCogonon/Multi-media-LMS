package com.library.service;

import com.library.model.Book;
import com.library.model.DVD;
import com.library.model.LibraryItem;
import com.library.model.Magazine;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRepository;

//This contains the main rules of the system: add, update, delete, search, late fee, and validation.
public class BookService {

    private BookRepository repository;
    private BorrowRepository borrowRepository;
    private int nextId = 1;

    public BookService(BookRepository repository,
                       BorrowRepository borrowRepository) {
        this.repository = repository;
        this.borrowRepository = borrowRepository;
    }

    // Total copies minus copies that are currently borrowed.
    public int getAvailableCopies(int id) {
        LibraryItem item = repository.findById(id);

        if (item == null) {
            return 0;
        }

        return item.getQuantity() - borrowRepository.countActiveByItemId(id);
    }

    public LibraryItem[] getAllBooks() {
        return repository.findAll();
    }

    // Returns only the items that match the keyword.
    // An empty keyword returns all items.
    public LibraryItem[] searchBooks(String keyword) {
        LibraryItem[] all = repository.findAll();

        int count = 0;

        for (int i = 0; i < all.length; i++) {
            if (all[i].matches(keyword)) {
                count++;
            }
        }

        LibraryItem[] results = new LibraryItem[count];
        int index = 0;

        for (int i = 0; i < all.length; i++) {
            if (all[i].matches(keyword)) {
                results[index] = all[i];
                index++;
            }
        }

        return results;
    }

    public void addBook(String type, String title, String author,
                        String isbn, String category, String quantityText) {

        int quantity = validate(type, title, author, isbn, category, quantityText);

        LibraryItem item = createItem(
                type, nextId, title, author, isbn, category, quantity
        );

        repository.save(item);

        nextId++;
    }

    // Looks for an existing record with the same type, title, author, ISBN,
    // and category (ignoring capital letters and extra spaces).
    // Returns that record, or null if this is a new item.
    // The input is validated first, so invalid data is reported as usual.
    public LibraryItem findExistingMatch(String type, String title, String author,
                                         String isbn, String category,
                                         String quantityText) {

        validate(type, title, author, isbn, category, quantityText);

        LibraryItem[] all = repository.findAll();

        for (int i = 0; i < all.length; i++) {
            if (all[i].getItemType().equals(type.trim())
                    && sameText(all[i].getTitle(), title)
                    && sameText(all[i].getAuthor(), author)
                    && sameText(all[i].getIsbn(), isbn)
                    && sameText(all[i].getCategory(), category)) {
                return all[i];
            }
        }

        return null;
    }

    // Adds more copies to an existing record instead of creating a new one.
    // The Available count updates by itself (quantity minus borrowed copies).
    public void addCopiesToExisting(int id, String quantityText) {
        LibraryItem existing = repository.findById(id);

        if (existing == null) {
            throw new IllegalArgumentException("Item not found.");
        }

        if (isBlank(quantityText)) {
            throw new IllegalArgumentException("Quantity is required.");
        }

        int copiesToAdd;

        try {
            copiesToAdd = Integer.parseInt(quantityText.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Quantity must be a whole number.");
        }

        if (copiesToAdd <= 0) {
            throw new IllegalArgumentException(
                    "Enter a quantity greater than 0 to add to the existing record."
            );
        }

        long newTotal = (long) existing.getQuantity() + copiesToAdd;

        if (newTotal > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("The total quantity is too large.");
        }

        existing.setQuantity((int) newTotal);
    }

    public void updateBook(int id, String type, String title, String author,
                           String isbn, String category, String quantityText) {

        int quantity = validate(type, title, author, isbn, category, quantityText);

        LibraryItem existing = repository.findById(id);

        if (existing == null) {
            throw new IllegalArgumentException("Item not found.");
        }

        // Protect items that are currently borrowed.
        int borrowed = borrowRepository.countActiveByItemId(id);

        if (borrowed > 0) {
            if (quantity < borrowed) {
                throw new IllegalArgumentException(
                        "Quantity cannot be lower than the " + borrowed
                        + " cop" + (borrowed == 1 ? "y" : "ies")
                        + " currently borrowed."
                );
            }

            if (!existing.getItemType().equals(type)) {
                throw new IllegalArgumentException(
                        "The item type cannot be changed while copies are borrowed."
                );
            }
        }

        LibraryItem item = createItem(
                type, id, title, author, isbn, category, quantity
        );

        if (repository.update(item) == false) {
            throw new IllegalArgumentException("Item not found.");
        }
    }

    public void deleteBook(int id) {
        if (borrowRepository.countActiveByItemId(id) > 0) {
            throw new IllegalArgumentException(
                    "This item cannot be deleted while copies are borrowed."
            );
        }

        if (repository.deleteById(id) == false) {
            throw new IllegalArgumentException("Item not found.");
        }
    }

    // POLYMORPHISM: the service only knows it is working with a LibraryItem.
    // The correct calculateLateFee() (Book, Magazine, or DVD) runs automatically.
    public double calculateLateFee(int id, int daysLate) {
        if (daysLate < 0) {
            throw new IllegalArgumentException("Days late cannot be negative.");
        }

        LibraryItem item = repository.findById(id);

        if (item == null) {
            throw new IllegalArgumentException("Item not found.");
        }

        return item.calculateLateFee(daysLate);
    }

    // Creates the correct child object based on the selected type.
    private LibraryItem createItem(String type, int id, String title,
                                   String author, String isbn,
                                   String category, int quantity) {

        if ("Magazine".equals(type)) {
            return new Magazine(id, title, author, isbn, category, quantity);
        } else if ("DVD".equals(type)) {
            return new DVD(id, title, author, isbn, category, quantity);
        } else {
            return new Book(id, title, author, isbn, category, quantity);
        }
    }

    // Checks all fields. Returns the quantity as a number if everything is valid.
    private int validate(String type, String title, String author,
                         String isbn, String category, String quantityText) {

        String errors = "";

        if (isBlank(type)) {
            errors = errors + "Item type is required.\n";
        }

        // The title may contain numbers (example: "1984", "Catch-22").
        if (isBlank(title)) {
            errors = errors + "Title is required.\n";
        }

        if (isBlank(author)) {
            errors = errors + "Author is required.\n";
        }

        if (isBlank(isbn)) {
            errors = errors + "ISBN is required.\n";
        }

        if (isBlank(category)) {
            errors = errors + "Category is required.\n";
        }

        if (!isBlank(author) && containsNumber(author)) {
            errors = errors + "Author must not contain numbers.\n";
        }

        if (!isBlank(category) && containsNumber(category)) {
            errors = errors + "Category must not contain numbers.\n";
        }

        int quantity = 0;

        if (isBlank(quantityText)) {
            errors = errors + "Quantity is required.\n";
        } else {
            try {
                quantity = Integer.parseInt(quantityText.trim());

                if (quantity < 0) {
                    errors = errors + "Quantity cannot be negative.\n";
                }

            } catch (NumberFormatException e) {
                errors = errors + "Quantity must be a whole number.\n";
            }
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors);
        }

        return quantity;
    }

    private boolean sameText(String first, String second) {
        return first.trim().equalsIgnoreCase(second.trim());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean containsNumber(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (Character.isDigit(value.charAt(i))) {
                return true;
            }
        }

        return false;
    }
}