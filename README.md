# Medical Center Manager

Medical Center Manager is a Java 17 desktop application scaffold built with Gradle and JavaFX.

## Requirements

- Java 17 JDK
- Internet access for the first Gradle wrapper run, so Gradle and dependencies can be downloaded

## Running

```bash
./gradlew run
```

On Windows, this also works:

```powershell
.\gradlew.bat run
```

## Project Structure

```text
src/main/java/pl/kul/medicalcenter
+-- appointment
+-- doctor
+-- patient
+-- report
+-- statemachine
+-- storage
+-- ui
```

## Current Scope

- Initial Gradle project structure
- Java 17 configuration
- JavaFX application entry point
- Basic JavaFX window
- Package placeholders for planned business modules

## Planned Modules

- `patient` - patient data and patient workflows
- `doctor` - doctor data and availability
- `appointment` - appointment scheduling
- `statemachine` - workflow state transitions
- `report` - reporting and export features
- `storage` - persistence and repositories
- `ui` - JavaFX screens and view models
