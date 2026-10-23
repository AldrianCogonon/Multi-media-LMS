package com.library.controller;

import com.library.model.BorrowRecord;
import com.library.service.BorrowService;

import java.time.LocalDate;

//The controller passes borrowing requests from the interface to the service.
public class BorrowController {

    private BorrowService service;

    public BorrowController(BorrowService service) {
        this.service = service;
    }

    public void borrowItem(int itemId, String borrowerName, LocalDate borrowDate) {
        service.borrowItem(itemId, borrowerName, borrowDate);
    }

    public double returnItem(int recordId, LocalDate returnDate) {
        return service.returnItem(recordId, returnDate);
    }

    public double estimateLateFee(BorrowRecord record) {
        return service.estimateLateFee(record);
    }

    public BorrowRecord[] getRecords(String filter, String keyword) {
        return service.getRecords(filter, keyword);
    }
}