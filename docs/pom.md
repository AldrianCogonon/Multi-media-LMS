# pom.xml

**Source:** `pom.xml`

## Purpose
Defines the Maven project, Java version, dependencies, and build
plugins.

## What's inside
- Project coordinates: group ID, artifact ID, and version.
- Java compiler release set to 21.
- JavaFX Controls dependency.
- Maven Compiler Plugin.
- JavaFX Maven Plugin with `com.library.MainApp` as the entry point.

## What should be improved
1. Keep dependency versions centralized in Maven properties.
2. Add JUnit 5 and a test plugin when automated tests are created.
3. Configure packaging if a distributable application is needed.
4. Review JavaFX platform-specific packaging requirements before
   building installers for other operating systems.
5. Consider Maven Enforcer rules to help enforce Java/Maven
   version requirements on team machines.
6. Keep dependencies minimal and update versions deliberately.

## Useful commands
- `mvn validate`: checks basic Maven project structure.
- `mvn clean compile`: compiles the application.
- `mvn test`: runs automated tests, when added.
- `mvn clean javafx:run`: cleans, builds, and launches the app.
