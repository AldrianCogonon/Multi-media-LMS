# MainApp.java

**Source:** `src/main/java/com/library/MainApp.java`

## Purpose
The entry point of the JavaFX application. It starts the program
and connects the repository, service, controller, and view.

## What's inside
- Extends JavaFX `Application`.
- Implements `start(Stage stage)`.
- Creates the application layers and injects dependencies.
- Creates the JavaFX scene and configures the main window.
- Calls `launch(args)` from `main`.

## Dependencies
- `BookRepository`
- `BookService`
- `BookController`
- `BookView`
- JavaFX `Application`, `Stage`, and `Scene`

## What should be improved
1. Move application wiring into a separate application factory
   if the project becomes larger.
2. Add application-level exception handling and logging.
3. Configure a stylesheet for consistent UI appearance.
4. Set an application icon and window behavior.
5. Add startup tests where practical.

## Important note
Keep `MainApp` focused on startup and dependency wiring.
Business rules belong in the service layer, not here.
