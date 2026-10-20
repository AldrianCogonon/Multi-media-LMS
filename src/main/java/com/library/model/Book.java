package com.library.model;

// INHERITANCE: Book is a child of LibraryItem.
// It gets the fields, getters, setters, getDetails() and matches() from the parent.
public class Book extends LibraryItem {

    private static final int LOAN_PERIOD_DAYS = 7;
    private static final double FEE_PER_DAY = 5.0;

    public Book(int id, String title, String author,
                String isbn, String category, int quantity) {
        super(id, title, author, isbn, category, quantity);
    }

    // POLYMORPHISM (method overriding): same method name as the parent's
    // abstract methods, but Book gives its own behavior.
    @Override
    public String getItemType() {
        return "Book";
    }

    @Override
    public int getLoanPeriodDays() {
        return LOAN_PERIOD_DAYS;
    }

    @Override
    public double calculateLateFee(int daysLate) {
        if (daysLate <= 0) {
            return 0;
        }

        return daysLate * FEE_PER_DAY;
    }
}