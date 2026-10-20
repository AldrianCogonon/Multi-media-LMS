package com.library.model;

// ABSTRACTION (interface): any class that implements this promises
// it can tell whether it matches a search keyword.
public interface Searchable {

    boolean matches(String keyword);
}