package com.library.service;

import com.library.model.BorrowRecord;
import com.library.model.LibraryItem;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRepository;

import java.time.LocalDate;

//This contains the borrowing rules: borrow, return, late fee, and validation.
public class BorrowService {

    public static final int MAX_ACTIVE_LOANS_PER_BORROWER = 3;

    private BookRepository bookRepository;
    private BorrowRepository borrowRepository;
    private int nextId = 1;

    public BorrowService(BookRepository bookRepository,
                         BorrowRepository borrowRepository) {
        this.bookRepository = bookRepository;
        this.borrowRepository = borrowRepository;
    }

    public void borrowItem(int itemId, String borrowerName, LocalDate borrowDate) {
        String errors = "";

        if (borrowerName == null || borrowerName.trim().isEmpty()) {
            errors = errors + "Borrower name is required.\n";
        } else if (containsNumber(borrowerName)) {
            errors = errors + "Borrower name must not contain numbers.\n";
        }

        if (borrowDate == null) {
            errors = errors + "Borrow date is required.\n";
        } else if (borrowDate.isAfter(LocalDate.now())) {
            errors = errors + "Borrow date cannot be in the future.\n";
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors);
        }

        String borrower = borrowerName.trim();
        LibraryItem item = bookRepository.findById(itemId);

        if (item == null) {
            throw new IllegalArgumentException("Item not found.");
        }

        if (getAvailableCopies(itemId) <= 0) {
            throw new IllegalArgumentException(
                    "No copies of \"" + item.getTitle() + "\" are available."
            );
        }

        if (borrowRepository.hasActive(itemId, borrower)) {
            throw new IllegalArgumentException(
                    borrower + " already borrowed this item and has not returned it."
            );
        }

        if (borrowRepository.countActiveByBorrower(borrower)
                >= MAX_ACTIVE_LOANS_PER_BORROWER) {
            throw new IllegalArgumentException(
                    borrower + " already has " + MAX_ACTIVE_LOANS_PER_BORROWER
                    + " borrowed items. Return one first."
            );
        }

        borrowRepository.save(new BorrowRecord(nextId, item, borrower, borrowDate));
        nextId++;
    }

    // Returns the late fee that was charged (0 if returned on time).
    public double returnItem(int recordId, LocalDate returnDate) {
        BorrowRecord record = borrowRepository.findById(recordId);

        if (record == null) {
            throw new IllegalArgumentException("Borrow record not found.");
        }

        if (record.isReturned()) {
            throw new IllegalArgumentException("This item was already returned.");
        }

        if (returnDate == null) {
            throw new IllegalArgumentException("Return date is required.");
        }

        if (returnDate.isBefore(record.getBorrowDate())) {
            throw new IllegalArgumentException(
                    "Return date cannot be before the borrow date ("
                    + record.getBorrowDate() + ")."
            );
        }

        if (returnDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Return date cannot be in the future.");
        }

        double fee = calculateFee(record, returnDate);
        record.markReturned(returnDate, fee);

        return fee;
    }

    // Fee so far for an item that is still borrowed (shown in the table).
    public double estimateLateFee(BorrowRecord record) {
        if (record.isReturned()) {
            return record.getLateFee();
        }

        return calculateFee(record, LocalDate.now());
    }

    // POLYMORPHISM: the correct calculateLateFee() (Book, Magazine, or DVD)
    // runs automatically, depending on the real type of the item.
    private double calculateFee(BorrowRecord record, LocalDate endDate) {
        LibraryItem item = bookRepository.findById(record.getItemId());

        if (item == null) {
            return 0;
        }

        int daysLate = 0;

        if (endDate.isAfter(record.getDueDate())) {
            daysLate = (int) java.time.temporal.ChronoUnit.DAYS.between(
                    record.getDueDate(), endDate
            );
        }

        return item.calculateLateFee(daysLate);
    }

    // Total copies minus copies currently borrowed.
    public int getAvailableCopies(int itemId) {
        LibraryItem item = bookRepository.findById(itemId);

        if (item == null) {
            return 0;
        }

        return item.getQuantity() - borrowRepository.countActiveByItemId(itemId);
    }

    public int getBorrowedCopies(int itemId) {
        return borrowRepository.countActiveByItemId(itemId);
    }

    // filter: "All", "Borrowed" (not returned yet), "Overdue", or "Returned".
    // keyword: matches the borrower name or item title (empty = everything).
    public BorrowRecord[] getRecords(String filter, String keyword) {
        BorrowRecord[] all = borrowRepository.findAll();
        String search = keyword == null ? "" : keyword.trim().toLowerCase();

        int count = 0;

        for (int i = 0; i < all.length; i++) {
            if (matchesFilter(all[i], filter, search)) {
                count++;
            }
        }

        BorrowRecord[] results = new BorrowRecord[count];
        int index = 0;

        // Newest records first.
        for (int i = all.length - 1; i >= 0; i--) {
            if (matchesFilter(all[i], filter, search)) {
                results[index] = all[i];
                index++;
            }
        }

        return results;
    }

    private boolean matchesFilter(BorrowRecord record, String filter, String search) {
        boolean statusOk;

        if ("Borrowed".equals(filter)) {
            statusOk = !record.isReturned();
        } else if ("Overdue".equals(filter)) {
            statusOk = record.isOverdue(LocalDate.now());
        } else if ("Returned".equals(filter)) {
            statusOk = record.isReturned();
        } else {
            statusOk = true;
        }

        boolean textOk = search.isEmpty()
                || record.getBorrowerName().toLowerCase().contains(search)
                || record.getItemTitle().toLowerCase().contains(search);

        return statusOk && textOk;
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