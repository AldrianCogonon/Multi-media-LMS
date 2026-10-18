# Simple Library System Management

A desktop Library Management System built with **Java 21, JavaFX, and Maven**.

This project currently supports basic book CRUD operations (Create, Read, Update, Delete) using in-memory storage.

## Features

- Add new books
- View books in a table
- Update selected books
- Delete selected books
- Basic input validation
- Layered architecture using Model, Repository, Service, Controller, and View

> **Note:** Book records are currently stored in memory. All records are lost when the application closes. Persistent storage is not implemented yet.

---

## 1. Prerequisites

Install the following tools before running the project.

| Requirement | Version |
|---|---|
| Java JDK | 21 |
| Apache Maven | 3.9 or newer recommended |
| Git | Any recent version |
| Code editor | Code - OSS, VS Code, IntelliJ IDEA, or another Java-compatible IDE |

### Check your installations

Open a terminal or command prompt and run:

```bash
java -version
javac -version
mvn -version
git --version
```

Confirm that Java and `javac` use JDK 21, and that Maven is using Java 21.

If a command is not recognized, install the missing tool and configure your system's `PATH` or `JAVA_HOME` as appropriate.

---

## 2. Clone the repository

Open a terminal and navigate to the directory where you keep your projects.

Replace `YOUR_REPOSITORY_URL` with the actual Git clone URL.

```bash
git clone YOUR_REPOSITORY_URL
cd Simple-Library-System-GUI
```

If you have already cloned the repository, skip this step and open a terminal in the project directory.

---

## 3. Install Java 21 and Maven

### Arch Linux

Update the system and install the required packages:

```bash
sudo pacman -Syu
sudo pacman -S jdk21-openjdk maven git
```

Check the installed Java versions:

```bash
java -version
javac -version
mvn -version
```

If multiple Java versions are installed, check the available Java environments:

```bash
archlinux-java status
```

If necessary, select Java 21:

```bash
sudo archlinux-java set java-21-openjdk
```

Open a new terminal and verify the versions again.

### Windows

1. Install a JDK 21 distribution, such as Eclipse Temurin 21.
2. Install Apache Maven using the official Maven installation instructions, or a trusted Windows package manager.
3. Configure `JAVA_HOME` to point to your JDK 21 installation if your installer has not already done so.
4. Add the JDK's `bin` directory and Maven's `bin` directory to `PATH` if needed.
5. Open a new terminal.

Verify the installations:

```powershell
java -version
javac -version
mvn -version
```

Ensure Maven reports Java 21.

Official resources:

- JDK distributions: https://adoptium.net/
- Maven installation: https://maven.apache.org/install.html

---

## 4. JavaFX setup

This project uses JavaFX through Maven dependencies declared in `pom.xml`.

The project currently declares:

- JavaFX Controls `21.0.6`
- JavaFX Maven Plugin `0.0.8`
- Maven Compiler Plugin `3.13.0`

**You do not normally need to download a separate JavaFX SDK or manually configure `module-path`.**

Maven downloads the required JavaFX libraries and platform-specific dependencies when you build or run the project.

Make sure you have an internet connection the first time you build the application.

---

## 5. Open the project in your editor

From the repository root, run:

```bash
code .
```

This works if the `code` command is available on your system. Otherwise, open your editor and select the cloned project folder.

Recommended Java extensions for Code - OSS or VS Code:

- **Language Support for Java by Red Hat**
- **Debugger for Java** (optional)
- **Maven for Java** (optional)

These extensions help with editing, debugging, and Maven integration, but they are not required to run the project from the terminal.

---

## 6. Build the project

From the directory containing `pom.xml`, run:

```bash
mvn clean compile
```

This removes previous build output and compiles the Java source files.

A successful build should end with:

```text
BUILD SUCCESS
```

If the build fails, check the error output and verify that Java 21 and Maven are correctly installed.

---

## 7. Run the application

Run:

```bash
mvn clean javafx:run
```

Maven will compile the application, resolve JavaFX dependencies, and launch the JavaFX window.

The configured entry point is:

```text
com.library.MainApp
```

The application should display the book management interface.

### Basic CRUD test

1. Enter a title, author, ISBN, category, and quantity.
2. Click **Add** and check that the book appears in the table.
3. Select the book, change a field, and click **Update Selected**.
4. Select the book and click **Delete Selected** to remove it.
5. Try leaving a required field empty to check the validation.

---

## 8. Project structure

```text
Simple-Library-System-GUI/
├── pom.xml
├── README.md
├── .gitignore
├── docs/
│   ├── MainApp.md
│   ├── Book.md
│   ├── BookRepository.md
│   ├── BookService.md
│   ├── BookController.md
│   ├── BookView.md
│   └── pom.md
└── src/
    └── main/
        └── java/
            └── com/
                └── library/
                    ├── MainApp.java
                    ├── model/
                    │   └── Book.java
                    ├── repository/
                    │   └── BookRepository.java
                    ├── service/
                    │   └── BookService.java
                    ├── controller/
                    │   └── BookController.java
                    └── view/
                        └── BookView.java
```

### Layer responsibilities

| Component | Responsibility |
|---|---|
| `MainApp` | Starts JavaFX and connects the application layers |
| `Book` | Represents a book |
| `BookRepository` | Stores and retrieves books |
| `BookService` | Handles business logic and validation |
| `BookController` | Connects the view to the service |
| `BookView` | Builds the JavaFX interface |

See the `docs/` directory for individual class documentation and suggested improvements.

---

## 9. Common problems

### `mvn: command not found`

Maven is missing or not configured in `PATH`. Install Maven and reopen the terminal.

### `java: command not found`

Install a JDK and configure `PATH`.

### Maven uses the wrong Java version

Check:

```bash
java -version
mvn -version
```

Maven reports the Java version it actually uses. Correct `JAVA_HOME` or your system's selected Java environment if needed.

### JavaFX display or graphics errors on Linux

Confirm that you are running in a graphical desktop session and that your system has the required graphics libraries. If the error persists, share the complete Maven error output.

### Dependency download errors

Check your internet connection and rerun:

```bash
mvn clean compile
```

Avoid deleting your entire Maven cache unless you have a specific reason to do so.

### The application launches but no books remain after restarting

This is expected in the current implementation. Books are held in memory and are not saved to a file or database.

---

## 10. Development commands

| Command | Purpose |
|---|---|
| `mvn validate` | Validates the Maven project structure |
| `mvn clean compile` | Cleans and compiles the project |
| `mvn test` | Runs automated tests when tests are added |
| `mvn clean javafx:run` | Builds and launches the application |
| `git status` | Shows changed and untracked files |

---

## 11. Current limitations and future improvements

Planned improvements include:

- Duplicate ISBN validation
- Automated unit tests
- Book search and filtering
- Delete confirmation dialogs
- Improved interface styling
- File-based persistence or database support
- Borrowing, returning, and member management

Contributions and suggestions are welcome.

