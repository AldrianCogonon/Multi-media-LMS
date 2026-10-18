# BookService.java

**Source:** `src/main/java/com/library/service/BookService.java`

## Purpose
Contains the application's book-management operations and
business validation.

## What's inside
- Retrieves all books.
- Creates books and assigns IDs using `AtomicInteger`.
- Updates existing books.
- Deletes books.
- Checks required fields and validates quantity.
- Trims text input before storing it.

## What should be improved
1. Reject duplicate ISBN values.
2. Validate ISBN format if the project requires it.
3. Define consistent normalization rules for ISBN values.
4. Add unit tests for valid input, invalid input, duplicate ISBN,
   and missing book IDs.
5. Consider a dedicated validation method or validation class
   as the rules become more complex.
6. Consider how IDs should be generated if records later persist
   across application restarts.
7. Use specific exception types or result objects if the service
   needs to report different validation failures.

## Design note
Business rules belong here so they can be tested without starting
the JavaFX interface.

## Current limitation
ISBN uniqueness is not yet checked, and IDs restart from 1 when
a new application session begins.
