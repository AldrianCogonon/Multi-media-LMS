# BookView.java

**Source:** `src/main/java/com/library/view/BookView.java`

## Purpose
Builds the JavaFX user interface for managing books.

## What's inside
- Text fields for title, author, ISBN, category, and quantity.
- A `TableView` that displays book records.
- Buttons for Add, Update Selected, Delete Selected, and Clear.
- Event handlers that call controller methods.
- Form population when a table row is selected.
- Error alerts for invalid operations.
- A refresh method that reloads the displayed records.

## What should be improved
1. Add a confirmation dialog before deleting a book.
2. Add search and filtering.
3. Improve layout, spacing, and responsive sizing.
4. Add input hints and clearer validation messages.
5. Disable Update and Delete when no row is selected.
6. Improve focus handling after adding or editing a record.
7. Consider extracting form creation and table setup into
   smaller methods or separate UI components.
8. Add automated tests for business behavior and manual UI
   testing for JavaFX interactions.
9. Consider JavaFX properties or a dedicated view model if
   live table updates become necessary.

## Design note
Keep storage and business rules out of this class. The view
should collect input, display data, and report operation results.

## Current limitation
The table refreshes after each successful operation. Search,
sorting configuration, and delete confirmation are not yet
implemented.
