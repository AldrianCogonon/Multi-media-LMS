package com.library.model;

// INHERITANCE: Magazine is a child of LibraryItem.
// It gets the fields, getters, setters, getDetails() and matches() from the parent.
public class Magazine extends LibraryItem {

    private static final int LOAN_PERIOD_DAYS = 3;
    private static final double FEE_PER_DAY = 2.0;

    public Magazine(int id, String title, String author,
                    String isbn, String category, int quantity) {
        super(id, title, author, isbn, category, quantity);
    }

    // POLYMORPHISM (method overriding): same method name as the parent's
    // abstract methods, but Magazine gives its own behavior.
    @Override
    public String getItemType() {
        return "Magazine";
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