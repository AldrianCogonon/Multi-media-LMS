# Book.java

**Source:** `src/main/java/com/library/model/Book.java`

## Purpose
Represents one book in the library system.

## Fields
- `id`: internal book identifier.
- `title`: book title.
- `author`: author's name.
- `isbn`: ISBN identifier.
- `category`: book category or genre.
- `quantity`: number of available copies recorded.

## What's inside
- Fields for book data.
- A constructor to initialize all fields.
- Getters and setters for reading and updating fields.

## What should be improved
1. Validate required values and non-negative quantity.
2. Decide whether fields should be immutable or editable.
3. Consider `equals()` and `hashCode()` if books need value-based
   equality.
4. Add `toString()` for debugging and logging.
5. Consider separating total copies from available copies if the
   application later tracks borrowing and returns.
6. Avoid allowing arbitrary changes to the ID after creation.

## Design note
Keep this class focused on representing a book. Complex application
rules should remain in `BookService`.
