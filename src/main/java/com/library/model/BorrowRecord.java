package com.library.model;

import java.time.LocalDate;

// Represents one borrowing transaction: who borrowed which item, and when.
// ENCAPSULATION: all fields are private. The only way to change the return
// information is markReturned(), so a record cannot be returned twice.
public class BorrowRecord {

    private final int id;
    private final int itemId;
    private final String itemTitle;     // saved copy, so history still shows the title
    private final String itemType;      // even if the item is edited or deleted later
    private final String borrowerName;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;

    private LocalDate returnDate;       // null while the item is still borrowed
    private double lateFee;

    public BorrowRecord(int id, LibraryItem item, String borrowerName,
                        LocalDate borrowDate) {

        if (item == null) {
            throw new IllegalArgumentException("Item is required.");
        }

        if (borrowerName == null || borrowerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Borrower name is required.");
        }

        if (borrowDate == null) {
            throw new IllegalArgumentException("Borrow date is required.");
        }

        this.id = id;
        this.itemId = item.getId();
        this.itemTitle = item.getTitle();
        this.itemType = item.getItemType();
        this.borrowerName = borrowerName.trim();
        this.borrowDate = borrowDate;

        // POLYMORPHISM: the due date depends on the real type of the item
        // (Book = 7 days, Magazine = 3 days, DVD = 2 days).
        this.dueDate = borrowDate.plusDays(item.getLoanPeriodDays());
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    public boolean isOverdue(LocalDate today) {
        return !isReturned() && today.isAfter(dueDate);
    }

    // How many days after the due date the item was (or is being) returned.
    public int getDaysLate(LocalDate asOf) {
        LocalDate end = isReturned() ? returnDate : asOf;

        if (!end.isAfter(dueDate)) {
            return 0;
        }

        return (int) java.time.temporal.ChronoUnit.DAYS.between(dueDate, end);
    }

    public void markReturned(LocalDate returnDate, double lateFee) {
        if (isReturned()) {
            throw new IllegalArgumentException("This item was already returned.");
        }

        if (returnDate == null) {
            throw new IllegalArgumentException("Return date is required.");
        }

        if (returnDate.isBefore(borrowDate)) {
            throw new IllegalArgumentException(
                    "Return date cannot be before the borrow date."
            );
        }

        this.returnDate = returnDate;
        this.lateFee = lateFee;
    }

    // Used by the table. Status is worked out from today's date.
    public String getStatus() {
        if (isReturned()) {
            return "Returned";
        }

        return isOverdue(LocalDate.now()) ? "Overdue" : "Borrowed";
    }

    public int getId() {
        return id;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public String getItemType() {
        return itemType;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public double getLateFee() {
        return lateFee;
    }
}