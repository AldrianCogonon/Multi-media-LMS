package com.library.repository;

import com.library.model.BorrowRecord;

//This stores borrow records in an array, the same way BookRepository stores items.
public class BorrowRepository {

    private BorrowRecord[] records = new BorrowRecord[0];

    // ENCAPSULATION: returns a copy so other classes cannot change the private array.
    public BorrowRecord[] findAll() {
        BorrowRecord[] copy = new BorrowRecord[records.length];

        for (int i = 0; i < records.length; i++) {
            copy[i] = records[i];
        }

        return copy;
    }

    public BorrowRecord findById(int id) {
        for (int i = 0; i < records.length; i++) {
            if (records[i].getId() == id) {
                return records[i];
            }
        }

        return null;
    }

    public void save(BorrowRecord record) {
        BorrowRecord[] newRecords = new BorrowRecord[records.length + 1];

        for (int i = 0; i < records.length; i++) {
            newRecords[i] = records[i];
        }

        newRecords[records.length] = record;
        records = newRecords;
    }

    // Number of copies of this item that are currently borrowed (not yet returned).
    public int countActiveByItemId(int itemId) {
        int count = 0;

        for (int i = 0; i < records.length; i++) {
            if (records[i].getItemId() == itemId && !records[i].isReturned()) {
                count++;
            }
        }

        return count;
    }

    // Number of items this person currently has (not yet returned).
    public int countActiveByBorrower(String borrowerName) {
        int count = 0;

        for (int i = 0; i < records.length; i++) {
            if (!records[i].isReturned()
                    && records[i].getBorrowerName().equalsIgnoreCase(borrowerName)) {
                count++;
            }
        }

        return count;
    }

    // True if this person already has this item and has not returned it.
    public boolean hasActive(int itemId, String borrowerName) {
        for (int i = 0; i < records.length; i++) {
            if (records[i].getItemId() == itemId
                    && !records[i].isReturned()
                    && records[i].getBorrowerName().equalsIgnoreCase(borrowerName)) {
                return true;
            }
        }

        return false;
    }
}