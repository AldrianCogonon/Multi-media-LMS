# BookRepository.java

**Source:** `src/main/java/com/library/repository/BookRepository.java`

## Purpose
Stores and retrieves book objects. The current implementation
uses an in-memory `ArrayList`, not a database.

## Methods
- `findAll()`: returns a copy of the current list.
- `findById(id)`: searches for a book by its ID.
- `save(book)`: adds a book.
- `update(book)`: updates a matching book.
- `deleteById(id)`: removes a book by ID.

## What should be improved
1. Prevent duplicate IDs.
2. Enforce ISBN uniqueness in the service or repository.
3. Consider a `Map<Integer, Book>` for efficient ID lookups if
   the collection grows.
4. Add unit tests for every repository operation.
5. Introduce a storage interface if file or database persistence
   is added in the future.
6. Document that all data is lost when the application exits.

## Design note
The repository handles storage and retrieval, not UI alerts or
JavaFX controls.

## Current limitation
All records exist only in memory. Restarting the app clears them.
