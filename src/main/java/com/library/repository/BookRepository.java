package com.library.repository;

import com.library.model.LibraryItem;

//This stores library items in an array. When adding or deleting an item, it creates a new array and copies the items using a for loop.
//The array type is LibraryItem, so it can hold Book, Magazine, and DVD objects together (POLYMORPHISM).
public class BookRepository {

    private LibraryItem[] items = new LibraryItem[0];

    // ENCAPSULATION: returns a copy so other classes cannot change the private array.
    public LibraryItem[] findAll() {
        LibraryItem[] copy = new LibraryItem[items.length];

        for (int i = 0; i < items.length; i++) {
            copy[i] = items[i];
        }

        return copy;
    }

    public LibraryItem findById(int id) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].getId() == id) {
                return items[i];
            }
        }

        return null;
    }

    public void save(LibraryItem item) {
        LibraryItem[] newItems = new LibraryItem[items.length + 1];

        for (int i = 0; i < items.length; i++) {
            newItems[i] = items[i];
        }

        newItems[items.length] = item;
        items = newItems;
    }

    // Replaces the old item with the updated one (the item type may change too).
    public boolean update(LibraryItem updatedItem) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].getId() == updatedItem.getId()) {
                items[i] = updatedItem;
                return true;
            }
        }

        return false;
    }

    public boolean deleteById(int id) {
        int index = -1;

        // Find the position of the item.
        for (int i = 0; i < items.length; i++) {
            if (items[i].getId() == id) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false;
        }

        // Create an array with one less element.
        LibraryItem[] newItems = new LibraryItem[items.length - 1];

        int newIndex = 0;

        // Copy all items except the deleted item.
        for (int i = 0; i < items.length; i++) {
            if (i != index) {
                newItems[newIndex] = items[i];
                newIndex++;
            }
        }

        items = newItems;

        return true;
    }
}