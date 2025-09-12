# Aljabar Linier dan Geometri Tubes 1 Template

## Requirements

Before building and running the **Matrix Calculator**, make sure you have the following installed:

### Java
- **Version:** 17 or higher
- **Download links (choose one that works):**
  - [Oracle JDK 17](https://www.oracle.com/java/technologies/downloads)
  - [Eclipse Temurin OpenJDK 17](https://adoptium.net/temurin/releases)
  - [Azul Zulu OpenJDK 17](https://www.azul.com/downloads)

### Maven
- **Version:** 3.2.5 or higher (recommended 3.6.3+)
- **Download links (choose one that works):**
  - [Direct Apache Maven Official Downloads](https://dlcdn.apache.org/maven/maven-3/3.9.11/binaries/apache-maven-3.9.11-bin.zip)
  - [Apache Maven Official Downloads](https://maven.apache.org/download.cgi) 
  - [Maven Repository for specific versions](https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.8.1)

### Additional installation info
For maven installation, download the .zip and it should contain a directory with
```
apache-maven-<version>/
├── bin/               <-- executable scripts (mvn, mvn.cmd)
├── boot/         
├── conf/          
├── lib/          
├── NOTICE
├── LICENSE
├── README.txt
```

Put bin/ in environment PATH to use in terminal

## How to develop

Using maven, the root development directory is `src/main/java/algeo`
There should not be any coding outside of that directory other than `test` using JUnit or other libraries.

Inside `src/main/java/algeo`, develop modules that are modular to use in the main program (`App.java`)

When running `mvn exec:java` later, `App.java` main program will be the one that is run.

## How to run
1. Compiling the program
The following command will produce a `target` directory with `matrix-calculator-1.0-SNAPSHOT.jar` in it
```bash
mvn clean package
```

alternatively, if you don't want to make a .jar file, you can use
```bash
mvn clean compile
```

2. Running the program
```bash
mvn exec:java
```

when the program is first run, it should print in terminal:
```bash
Hai 
Halo Algeo!
```

## Using the program as a library

Copy the .jar file that is in the `target` file (from running `mvn clean compile`) to `bin` for submission, this .jar file can be used in other projects to import modules in this current project