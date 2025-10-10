# Tugas Besar 1 - Aljabar Linier dan Geometri
**IF2123 - Semester I 2025/2026**

## 📋 Deskripsi

Program ini adalah implementasi berbagai metode komputasi aljabar linier meliputi:
- **Sistem Persamaan Linier (SPL)** - 4 metode
    - **Eliminasi Gauss**
    - **Eliminasi Gauss-Jordan**
    - **Kaidah Cramer**
    - **Metode Matriks Balikan**
- **Determinan** - 2 metode
    - **Metode Ekspansi Kofaktor**
    - **Metode Reduksi Baris(OBE)**
- **Matriks Balikan (Inverse)** - 2 metode
    - **Metode Augment**
    - **Metode Adjoin**
- **Interpolasi Polinomial** - 2 metode
    - **Interpolasi Polinomial**
    - **Interpolasi splina B ́ezier kubik**
- **Regresi Polinomial Berganda**

Program dikembangkan dalam bahasa Java dengan pendekatan Object-Oriented Programming (OOP).

---

## 👥 Anggota Kelompok

| NIM | Nama | Kontribusi |
|-----|------|------------|
| 13524124 | Zahran Alvan Putra Winarko | Penyelesaian Sistem Persamaan Linier |
| 13524128| Safira Berlianti | Determinan dan Invers |
| 13524123 | Yuhan Fanzuri Nizar | Interpolasi Polinomial dan Regresi Polinomial Berganda |



## 🚀 Requirements

Sebelum menjalankan program, pastikan Anda telah menginstall:

### Java
- **Version:** 8 atau lebih tinggi (recommended: 17+)
- **Download:**
  - [Oracle JDK](https://www.oracle.com/java/technologies/downloads)
  - [OpenJDK](https://openjdk.org/)

### Cara Cek Instalasi Java
```bash
java -version
javac -version
```

### Maven
- **Version:** 3.2.5 atau lebih tinggi (recommended 3.6.3+)
- **Download:**
  - [Direct Apache Maven Official Downloads](https://dlcdn.apache.org/maven/maven-3/3.9.11/binaries/apache-maven-3.9.11-bin.zip)

### Cara Cek Instalasi Maven
```bash
mvn -v
```

## 📁 Struktur Project
```bash
algeo1-il-principe/
├── .github/
│   ├── .keep/                                                            
├── .vscode/
│   ├── .settings.json/       
├── bin/
│   ├── matrix/
│   │    ├── FileHandler.class
│   │    ├── FileHandler$1.class
│   │    ├── Matrix.class                        
│   ├── spl/
│   │     ├── CramerRule.class
│   │     ├── GaussElimination.class
│   │     ├── GaussJordan.class
│   │    ├── InversMethod.class
│   │     ├── SPLResult.class
│   │    ├── SPLResult$SolutionType.class
│   │    ├── SPLSolver.class
│   ├── determinan/   
│   │    ├── Determinan.class
│   │    ├── DeterminanResult.class                        
│   ├── invers/     
│   │    ├── Invers.class
│   │    ├──InversResult.class
│   ├── interpolasi/                          
│   ├── regresi/
│   ├── Main.class/                          
│   ├── invers$1.class/                                    
├── docs/         
├── src/
│   ├── Main.java                      
│   ├── matrix/
│   │   ├── Matrix.java                 
│   │   └── FileHandler.java          
│   ├── spl/
│   │   ├── SPLSolver.java        
│   │   ├── SPLResult.java         
│   │   ├── GaussElimination.java     
│   │   ├── GaussJordan.java           
│   │   ├── CramerRule.java            
│   │   └── InverseMethod.java        
│   ├── determinant/
│   │   ├── DeterminantCalculator.java  
│   │   ├── DeterminantResult.java    
│   ├── invers/
│   │   ├── Invers.java  
│   │   ├── InversResult.java
│   ├── interpolation/
│   │   └── (Modul Interpolasi)
│   └── regression/
│       └── (Modul Regresi)
├── test/
│   ├── spl/                          
│   ├── determinant/                 
│   ├── inverse/                     
│   ├── interpolation/               
│   └── regression/                                   
└── README.md
├── pom.xml                       
└── README.md
```
## How to Compile dan Run

### Windows Compile
```bash
javac -d bin -encoding UTF-8 src\Main.java src\matrix\*.java src\spl\*.java src\determinan\*.java src\invers\*.java src\interpolasi\*.java src\regresi\*.java
```
### Mac/Linux Compile
```bash
javac -d bin -encoding UTF-8 src/Main.java src/matrix/*.java src/spl/*.java src/determinan/*.java src/invers/*.java src/interpolasi/*.java src/regresi/*.java
```
### Run
```bash
java -cp bin Main
```


## Referensi

### Rinaldi Munir. Slide Kuliah IF2123 Aljabar Linier dan Geometri.
```bash
https://informatika.stei.itb.ac.id/~rinaldi.munir/AljabarGeometri/2025-2026/algeo25-26.htm
```