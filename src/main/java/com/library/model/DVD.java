package com.library.model;

// INHERITANCE: DVD is a child of LibraryItem.
// It gets the fields, getters, setters, getDetails() and matches() from the parent.
public class DVD extends LibraryItem {

    private static final int LOAN_PERIOD_DAYS = 2;
    private static final double FEE_PER_DAY = 10.0;

    public DVD(int id, String title, String author,
               String isbn, String category, int quantity) {
        super(id, title, author, isbn, category, quantity);
    }

    // POLYMORPHISM (method overriding): same method name as the parent's
    // abstract methods, but DVD gives its own behavior.
    @Override
    public String getItemType() {
        return "DVD";
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