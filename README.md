# Tetris

A Tetris game featuring a Java game engine and a JavaFX user interface, built with Gradle.

## Build and Run

### Prerequisites

- JDK 21 or later
- Gradle Wrapper is included, so no separate Gradle installation is needed.
- Linux: JavaFX 21 requires GTK 3 (version 3.8 or later) and its supporting native libraries. Most desktop Linux distributions already provide these dependencies.

For Nix users, an optional shell.nix is provided to set up the Java development environment and native library paths.

### Clone the Repository

```bash
git clone https://github.com/nakani2623/Tetris.git
cd Tetris
```

### macOS / Linux

Build the project:

```bash
./gradlew build
```

Run the game:

```bash
./gradlew :app:run
```

### Windows

Build the project:

```powershell
.\gradlew.bat build
```

Run the game:

```powershell
.\gradlew.bat :app:run
```

## Contributing

Contributions are welcome! You can:

- Open an [issue](https://github.com/nakani2623/Tetris/issues) to report bugs or suggest improvements.
- Submit a pull request with fixes or new features.

Before submitting a pull request, please ensure the build passes and include a brief description of your changes.