# BookController.java

**Source:** `src/main/java/com/library/controller/BookController.java`

## Purpose
Provides a small interface between the JavaFX view and the
service layer.

## What's inside
- Holds a `BookService` reference.
- Delegates requests to retrieve, add, update, and delete books.
- Returns results or lets service exceptions propagate to the view.

## What should be improved
1. Keep UI-specific code out of this class.
2. Add search or other operations by delegating to the service
   when those features are implemented.
3. Consider a result or error model if more detailed feedback
   is needed.
4. Add tests verifying that requests are delegated correctly.

## Design note
The controller should coordinate operations, not duplicate
validation rules already implemented in the service.
