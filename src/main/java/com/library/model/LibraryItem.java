package com.library.model;

// ABSTRACTION (abstract class): this is the general idea of "an item in the library".
// It cannot be created with "new". Only its child classes (Book, Magazine, DVD) can.
// It also implements the Searchable interface.
public abstract class LibraryItem implements Searchable {

    // ENCAPSULATION: all fields are private. Other classes can only
    // use the public getters and setters below.
    private final int id;          // final = cannot be changed after creation (no setter)
    private String title;
    private String author;
    private String isbn;
    private String category;
    private int quantity;

    public LibraryItem(int id, String title, String author,
                       String isbn, String category, int quantity) {
        this.id = id;

        // The constructor uses the setters so the same checks always apply.
        setTitle(title);
        setAuthor(author);
        setIsbn(isbn);
        setCategory(category);
        setQuantity(quantity);
    }

    // ABSTRACTION: these methods have no body here.
    // Every child class MUST write its own version.
    public abstract String getItemType();

    public abstract int getLoanPeriodDays();

    public abstract double calculateLateFee(int daysLate);

    // Normal method shared by all children (INHERITANCE = code reuse).
    // It calls the abstract methods, so the result depends on the real object type.
    public String getDetails() {
        return getItemType() + ": " + title + " by " + author
                + " (Loan period: " + getLoanPeriodDays() + " days)";
    }

    @Override
    public boolean matches(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return true;
        }

        String search = keyword.trim().toLowerCase();

        return title.toLowerCase().contains(search)
                || author.toLowerCase().contains(search)
                || isbn.toLowerCase().contains(search)
                || category.toLowerCase().contains(search)
                || getItemType().toLowerCase().contains(search);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters with protection: invalid data is rejected here too.
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required.");
        }
        this.title = title.trim();
    }

    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author is required.");
        }
        this.author = author.trim();
    }

    public void setIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN is required.");
        }
        this.isbn = isbn.trim();
    }

    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category is required.");
        }
        this.category = category.trim();
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        this.quantity = quantity;
    }
}