package com.library.model;

// ABSTRACTION (abstract class): this is the general idea of "an item in the library".
// It cannot be created with "new". Only its child classes (Book, Magazine, DVD) can.
// It also implements the Searchable interface.
public abstract class LibraryItem implements Searchable {

    // ENCAPSULATION: all fields are private. Other classes can only
    // use the public getters and setters below.
    private final int id;          // final = cannot be changed after creation (no setter)
    private String title;
    private String author;
    private String isbn;
    private String category;
    private int quantity;

    public LibraryItem(int id, String title, String author,
                       String isbn, String category, int quantity) {
        this.id = id;

        // The constructor uses the setters so the same checks always apply.
        setTitle(title);
        setAuthor(author);
        setIsbn(isbn);
        setCategory(category);
        setQuantity(quantity);
    }

    // ABSTRACTION: these methods have no body here.
    // Every child class MUST write its own version.
    public abstract String getItemType();

    public abstract int getLoanPeriodDays();

    public abstract double calculateLateFee(int daysLate);

    // Normal method shared by all children (INHERITANCE = code reuse).
    // It calls the abstract methods, so the result depends on the real object type.
    public String getDetails() {
        return getItemType() + ": " + title + " by " + author
                + " (Loan period: " + getLoanPeriodDays() + " days)";
    }

    @Override
    public boolean matches(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return true;
        }

        String search = keyword.trim().toLowerCase();

        return title.toLowerCase().contains(search)
                || author.toLowerCase().contains(search)
                || isbn.toLowerCase().contains(search)
                || (!search.replace("-", "").isEmpty()
                    && isbn.replace("-", "").toLowerCase()
                           .contains(search.replace("-", "")))
                || category.toLowerCase().contains(search)
                || getItemType().toLowerCase().contains(search);
    }


    // ----- Identifier rules (ISBN for Book, ISSN for Magazine, VPC for DVD) -----

    // The name of the identifier for each item type.
    public static String getIdentifierLabel(String type) {
        if ("Magazine".equals(type)) {
            return "ISSN";
        } else if ("DVD".equals(type)) {
            return "VPC";
        } else {
            return "ISBN";
        }
    }

    public String getIdentifierLabel() {
        return getIdentifierLabel(getItemType());
    }

    // Characters allowed while typing (the value may still be incomplete).
    // Book (ISBN-13): digits 0-9 and hyphens only, up to 13 digits. No letters.
    // Magazine (ISSN): digits 0-9 and hyphens, up to 8 characters.
    //                  An X or x is allowed only as the 8th (last) character.
    // DVD (VPC): digits 0-9 only, up to 12.
    // Hyphens are optional for Book and Magazine.
    public static boolean isAllowedWhileTyping(String type, String text) {
        if (text == null) {
            return true;
        }

        if ("DVD".equals(type)) {
            return text.matches("[0-9]{0,12}");
        }

        if ("Book".equals(type)) {
            return text.matches("[0-9-]*") && removeHyphens(text).length() <= 13;
        }

        if ("Magazine".equals(type)) {
            if (!text.matches("[0-9-]*[Xx]?")) {
                return false;
            }

            String characters = removeHyphens(text);

            // X is only allowed as the 8th character.
            return characters.length() <= 8
                    && (!characters.matches(".*[Xx]") || characters.length() == 8);
        }

        // No type selected yet.
        return text.matches("[0-9-]*[Xx]?");
    }

    // Full check used when saving. Returns an error message, or null if valid.
    public static String getIdentifierError(String type, String value) {
        String label = getIdentifierLabel(type);

        if (value == null || value.trim().isEmpty()) {
            return label + " is required.";
        }

        String id = value.trim();

        if ("DVD".equals(type)) {
            if (!id.matches("[0-9]{12}")) {
                return "VPC must be exactly 12 digits (0-9 only). "
                        + "Letters, hyphens, and negative numbers are not allowed.";
            }
        } else if ("Magazine".equals(type)) {
            // 7 digits followed by a digit or X (example: 0040-781X).
            if (!id.matches("[0-9-]*[Xx]?")
                    || !removeHyphens(id).matches("[0-9]{7}[0-9Xx]")) {
                return "ISSN must have exactly 8 characters: 7 digits followed by "
                        + "a digit or an X (example: 0040-781X). "
                        + "Hyphens are optional.";
            }
        } else {
            // Book: ISBN-13, numbers only, exactly 13 digits (hyphens not counted).
            if (!id.matches("[0-9-]*")
                    || !removeHyphens(id).matches("[0-9]{13}")) {
                return "ISBN must have exactly 13 digits, numbers only "
                        + "(example: 123-4-567-89101-2). Hyphens are optional.";
            }
        }

        return null;
    }

    // Puts the hyphens in automatically, whether or not the user typed them.
    // Book:     123-4-567-89101-2  (3-1-3-5-1)
    // Magazine: 0040-781X          (4-4, X in capital letter)
    // DVD:      no hyphens (12 digits)
    // If the value is not valid, it is returned as typed (the error is shown elsewhere).
    public static String formatIdentifier(String type, String value) {
        if (value == null) {
            return "";
        }

        String id = value.trim();

        if (getIdentifierError(type, id) != null) {
            return id;
        }

        String characters = removeHyphens(id).toUpperCase();

        if ("DVD".equals(type)) {
            return characters;
        }

        if ("Magazine".equals(type)) {
            return characters.substring(0, 4) + "-" + characters.substring(4);
        }

        return characters.substring(0, 3) + "-"
                + characters.substring(3, 4) + "-"
                + characters.substring(4, 7) + "-"
                + characters.substring(7, 12) + "-"
                + characters.substring(12);
    }

    private static String removeHyphens(String text) {
        return text.replace("-", "");
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters with protection: invalid data is rejected here too.
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required.");
        }
        this.title = title.trim();
    }

    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author is required.");
        }
        this.author = author.trim();
    }

    public void setIsbn(String isbn) {
        String error = getIdentifierError(getItemType(), isbn);

        if (error != null) {
            throw new IllegalArgumentException(error);
        }
        // Saved with the hyphens in the correct places.
        this.isbn = formatIdentifier(getItemType(), isbn);
    }

    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category is required.");
        }
        this.category = category.trim();
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must not be 0 or negative.");
        }
        this.quantity = quantity;
    }
}