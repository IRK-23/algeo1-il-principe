package algeo;

import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import algeo.matrix.Matrix;
import algeo.matrix.FileHandler;
import algeo.spl.*;
import algeo.determinan.*;
import algeo.invers.*;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        boolean running = true;
        
        while (running) {
            displayMainMenu();
            
            try {
                int choice = getIntInput("Pilih menu: ");
                
                switch(choice) {
                    case 1:
                        handleSPL();
                        break;
                    case 2:
                        handleDeterminant();
                        break;
                    case 3:
                        handleInverse();
                        break;
                    case 4:
                        handleInterpolation();
                        break;
                    case 5:
                        handleRegression();
                        break;
                    case 6:
                        running = false;
                        System.out.println("\n  **************************************");
                        System.out.println("  *  Terima kasih telah menggunakan    *");
                        System.out.println("  *        Program SPL & Geometri      *");
                        System.out.println("  **************************************\n");
                        break;
                    default:
                        System.out.println("\nPilihan tidak valid!");
                }
            } catch (Exception e) {
                System.err.println("\nError: " + e.getMessage());
                e.printStackTrace();
            }
            
            if (running) {
                System.out.println("\nTekan Enter untuk melanjutkan...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    private static void displayMainMenu() {
        System.out.println("*****************************************************");
        System.out.println("*                                                   *");
        System.out.println("*     SISTEM PERSAMAAN LINIER DAN GEOMETRI          *");
        System.out.println("*     IF2123 Aljabar Linier dan Geometri            *");
        System.out.println("*                                                   *");
        System.out.println("*****************************************************");
        System.out.println();
        System.out.println("  1. Sistem Persamaan Linier (SPL)");
        System.out.println("  2. Determinan");
        System.out.println("  3. Matriks Balikan (Inverse)");
        System.out.println("  4. Interpolasi");
        System.out.println("  5. Regresi Polinomial");
        System.out.println("  6. Keluar");
        System.out.println();                                                   
        System.out.println("=".repeat(55) );
    }
    
    private static void handleSPL() throws IOException {
        System.out.println("=====================================================");
        System.out.println("        SISTEM PERSAMAAN LINIER (SPL)                ");
        System.out.println("=====================================================\n");
        
        System.out.println("Pilih Metode Penyelesaian:");
        System.out.println("  1. Eliminasi Gauss");
        System.out.println("  2. Eliminasi Gauss-Jordan");
        System.out.println("  3. Kaidah Cramer");
        System.out.println("  4. Metode Matriks Balikan");
        System.out.println();
        
        int method = getIntInput("Pilih metode (1-4): ");
        
        if (method < 1 || method > 4) {
            System.out.println("Metode tidak valid!");
            return;
        }
        
        System.out.println();
        System.out.println("Pilih Jenis Input:");
        System.out.println("  1. Input dari Keyboard");
        System.out.println("  2. Input dari File");
        System.out.println();
        
        int inputType = getIntInput("Pilih jenis input (1-2): ");
        
        Matrix augmented;
        
        if (inputType == 1) {
            augmented = inputMatrixFromKeyboard();
        } else if (inputType == 2) {
            System.out.print("\nMasukkan nama file (contoh: test/spl/spl_case1.txt): ");
            String filename = scanner.nextLine();
            augmented = FileHandler.readAugmentedMatrix(filename);
            System.out.println("File berhasil dibaca!");
        } else {
            System.out.println("Jenis input tidak valid!");
            return;
        }
        
        // Pilih solver berdasarkan metode
        SPLSolver solver = null;
        
        switch(method) {
            case 1:
                solver = new GaussElimination();
                break;
            case 2:
                solver = new GaussJordan();
                break;
            case 3:
                 solver = new CramerRule();
                 break;
            case 4:
                 solver = new InversMethod();
                 break;
        }
        
       if (solver != null) {
            System.out.println("\n" + "=".repeat(55));
            System.out.println("Memproses dengan metode: " + solver.getMethodName());
            System.out.println("=".repeat(55) + "\n");
            
            // Solve dan Display Result
            SPLResult result = solver.solve(augmented);
            
            displaySPLResult(result);
            
            // Save File?
            System.out.println();
            System.out.print("Simpan hasil ke file? (y/n): ");
            String save = scanner.nextLine().trim();

            if (save.equalsIgnoreCase("y")) {
                System.out.println("\nPilih lokasi penyimpanan:");
                System.out.println("1. Folder saat ini (root)");
                System.out.println("2. Folder test/spl");
                System.out.print("Pilih (1/2): ");
                
                int choice = getIntInput("");
                String folder = (choice == 2) ? "test/spl/" : "";
                
                System.out.print("Nama file output: ");
                String outputFile = scanner.nextLine().trim();
                
                String fullPath = folder + outputFile;
                
                // Buat folder jika pilih opsi 2 dan folder belum ada
                if (choice == 2) {
                    File dir = new File("test/spl");
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                }
                
                FileHandler.writeSPLOutput(fullPath, result, augmented);
            }
        }
    }

    private static Matrix inputMatrixFromKeyboard() {
        System.out.println();
        System.out.println("=====================================================");
        System.out.println("           INPUT MATRIKS AUGMENTED [A|b]");
        System.out.println("=====================================================");
        
        int n = getIntInput("Jumlah persamaan (baris): ");
        int m = getIntInput("Jumlah variabel: ");
        
        Matrix matrix = new Matrix(n, m + 1); // matriks augmented
        
        System.out.println("\nMasukkan elemen matriks augmented [A|b]:");
        System.out.println("(Format: koef_x1 koef_x2 ... koef_xn konstanta)");
        System.out.println();
        
        for (int i = 0; i < n; i++) {
            System.out.print("Baris " + (i+1) + ": ");
            String line = scanner.nextLine().trim();
            String[] values = line.split("\\s+");
            
            if (values.length != m + 1) {
                System.out.println("Error: Jumlah elemen tidak sesuai!");
                System.out.println("   Diharapkan: " + (m + 1) + " elemen");
                System.out.println("   Diterima: " + values.length + " elemen");
                i--; 
                continue;
            }
            
            for (int j = 0; j < m + 1; j++) {
                try {
                    matrix.set(i, j, Double.parseDouble(values[j]));
                } catch (NumberFormatException e) {
                    System.out.println("Error: Format angka tidak valid!");
                    i--; 
                    break;
                }
            }
        }
        
        System.out.println("\nMatriks berhasil diinput!");
        System.out.println("\nMatriks Augmented [A|b]:");
        matrix.print();
        
        return matrix;
    }
    
    private static void displaySPLResult(SPLResult result) {
        System.out.println();
        System.out.println("=====================================================");
        System.out.println("                  HASIL SOLUSI SPL                   ");
        System.out.println("=====================================================");
        System.out.println();
        System.out.println("Metode: " + result.getMethodName());
        System.out.println();
        
        // Langkah-langkah
        if (result.getSteps().size() <= 50) {
            System.out.println("=".repeat(55));
            System.out.println("Langkah-langkah Penyelesaian:");
            System.out.println("=".repeat(55));
            for (String step : result.getSteps()) {
                System.out.println(step);
            }
        } else {
            System.out.println("ℹ Langkah-langkah terlalu panjang, hanya menampilkan hasil akhir.");
            System.out.println("  (Simpan ke file untuk melihat detail lengkap)");
        }
        
        System.out.println();
        System.out.println("=====================================================");
        System.out.println("                    HASIL AKHIR");
        System.out.println("=====================================================");
        
        switch(result.getType()) {
            case UNIQUE:
                System.out.println("Solusi Tunggal:\n");
                double[] solution = result.getSolution();
                for (int i = 0; i < solution.length; i++) {
                    System.out.printf("   x%-2d = %.6f\n", i+1, solution[i]);
                }
                break;
                
            case NO_SOLUTION:
                System.out.println("TIDAK ADA SOLUSI");
                System.out.println("   Sistem persamaan inkonsisten.");
                break;
                
            case INFINITE:
                System.out.println("SOLUSI TAK HINGGA");
                System.out.println("   Sistem memiliki tak hingga banyak solusi.\n");
                System.out.println(result.getParametricSolution());
                break;
        }
        
        System.out.println("=".repeat(55));
    }
    
    
    private static void handleDeterminant() throws IOException{
        System.out.println("=====================================================");
        System.out.println("                  DETERMINAN MATRIKS                 ");
        System.out.println("=====================================================\n");
        System.out.println("Pilih Metode Penyelesaian:");
        System.out.println("  1. Metode Ekspansi Kofaktor");
        System.out.println("  2. Metode Reduksi Baris (OBE)");
        System.out.println();
        
        int method = getIntInput("Pilih metode (1-2): ");
        
        if (method < 1 || method > 2) {
            System.out.println("Metode tidak valid!");
            return;
        }
        
        System.out.println();
        System.out.println("Pilih Jenis Input:");
        System.out.println("  1. Input dari Keyboard");
        System.out.println("  2. Input dari File");
        System.out.println();
        
        int inputType = getIntInput("Pilih jenis input (1-2): ");
        
        Matrix matrix = null;
        
        if (inputType == 1) {
            matrix = inputMatrix();
        } 
        else if (inputType == 2) {
            System.out.print("\nMasukkan nama file (contoh: test/determinan/determinan_case1.txt): ");
            String filename = scanner.nextLine();
            matrix = FileHandler.readMatrix(filename);
            System.out.println("File berhasil dibaca!");
        } 
        else {
            System.out.println("Jenis input tidak valid!");
            return;
        }

        Determinan det = new Determinan();
        DeterminanResult result;
        
        System.out.println("\n" + "=".repeat(55));
        if (method == 1) {
            System.out.println("Menghitung dengan Metode Ekspansi Kofaktor...");
            result = det.detEkspansiKofaktor(matrix);
        } 
        else {
            System.out.println("Menghitung dengan Metode Reduksi Baris (OBE)...");
            result = det.detOBE(matrix);
        }
        System.out.println("=".repeat(55));

        result.printSteps();

        System.out.println();
        System.out.print("Simpan hasil ke file? (y/n): ");
        String save = scanner.nextLine().trim();

        if (save.equalsIgnoreCase("y")) {
            System.out.println("\nPilih lokasi penyimpanan:");
            System.out.println("1. Folder saat ini (root)");
            System.out.println("2. Folder test/determinan");
            System.out.print("Pilih (1/2): ");
                
            int choice = getIntInput("");
            String folder = (choice == 2) ? "test/determinan/" : "";
                
            System.out.print("Nama file output: ");
            String outputFile = scanner.nextLine().trim();
                
            String fullPath = folder + outputFile;
                
            // Buat folder jika pilih opsi 2 dan folder belum ada
            if (choice == 2) {
                File dir = new File("test/determinan");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
                
            FileHandler.writeDeterminanOutput(fullPath, result, matrix);
        }
    }
    
    private static Matrix inputMatrix() {
        System.out.println();
        System.out.println("=====================================================");
        System.out.println("           INPUT MATRIKS");
        System.out.println("=====================================================");
        
        int n = getIntInput("Ukuran matriks (n*n): ");
        
        Matrix matrix = new Matrix(n,n);
        
        System.out.println("\nMasukkan elemen matriks:");
        System.out.println();
        
        for (int i = 0; i < n; i++) {
            System.out.print("Baris " + (i+1) + ": ");
            String line = scanner.nextLine().trim();
            String[] values = line.split("\\s+");
            
            if (values.length != n) {
                System.out.println("Error: Jumlah elemen tidak sesuai!");
                System.out.println("   Diharapkan: " + n + " elemen");
                System.out.println("   Diterima: " + values.length + " elemen");
                i--;
                continue;
            }
            
            for (int j = 0; j < n; j++) {
                try {
                    String val = values[j].replace(",", ".");
                    if (val.contains("/")) {
                        String[] parts = val.split("/");
                        if (parts.length == 2) {
                            double pembilang = Double.parseDouble(parts[0]);
                            double penyebut = Double.parseDouble(parts[1]);
                            matrix.set(i, j, pembilang/penyebut);
                        } 
                        else {
                            throw new NumberFormatException();
                        }
                    } 
                    else {
                        matrix.set(i, j, Double.parseDouble(val));
                    }
                } 
                catch (NumberFormatException e) {
                    System.out.println("Error: Format angka tidak valid!");
                    i--;
                    break;
                }
            }
        }
        
        System.out.println("\nMatriks berhasil diinput!");
        System.out.println("\nMatriks:");
        matrix.print();
        
        return matrix;
    }

    private static void handleInverse() throws IOException{
        System.out.println("=====================================================");
        System.out.println("            MATRIKS BALIKAN (INVERSE)                ");
        System.out.println("=====================================================\n");
        System.out.println("Pilih Metode Penyelesaian:");
        System.out.println("  1. Metode Augment");
        System.out.println("  2. Metode Adjoin");
        System.out.println();
        
        int method = getIntInput("Pilih metode (1-2): ");
        
        if (method < 1 || method > 2) {
            System.out.println("Metode tidak valid!");
            return;
        }
        
        System.out.println();
        System.out.println("Pilih Jenis Input:");
        System.out.println("  1. Input dari Keyboard");
        System.out.println("  2. Input dari File");
        System.out.println();
        
        int inputType = getIntInput("Pilih jenis input (1-2): ");
        
        Matrix matrix = null;
        
        if (inputType == 1) {
            matrix = inputMatrix();
        } 
        else if (inputType == 2) {
            System.out.print("\nMasukkan nama file (contoh: test/invers/invers_case1.txt): ");
            String filename = scanner.nextLine();
            matrix = FileHandler.readMatrix(filename);
            System.out.println("File berhasil dibaca!");
        } 
        else {
            System.out.println("Jenis input tidak valid!");
            return;
        }

        Invers inv = new Invers();
        InversResult result;
        
        System.out.println("\n" + "=".repeat(55));
        if (method == 1) {
            System.out.println("Menghitung dengan Metode Augment...");
            result = inv.inversAugment(matrix);
        } else {
            System.out.println("Menghitung dengan Metode Adjoin...");
            result = inv.inversAdjoin(matrix);
        }
        System.out.println("=".repeat(55));

        result.printSteps();

        System.out.println();
        System.out.print("Simpan hasil ke file? (y/n): ");
        String save = scanner.nextLine().trim();

        if (save.equalsIgnoreCase("y")) {
            System.out.println("\nPilih lokasi penyimpanan:");
            System.out.println("1. Folder saat ini (root)");
            System.out.println("2. Folder test/invers");
            System.out.print("Pilih (1/2): ");
                
            int choice = getIntInput("");
            String folder = (choice == 2) ? "test/invers/" : "";
                
            System.out.print("Nama file output: ");
            String outputFile = scanner.nextLine().trim();
                
            String fullPath = folder + outputFile;
                
            // Buat folder jika pilih opsi 2 dan folder belum ada
            if (choice == 2) {
                File dir = new File("test/invers");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
                
            FileHandler.writeInversOutput(fullPath, result, matrix);
        }
    }
    
    private static void handleInterpolation() {
        System.out.println("=====================================================");
        System.out.println("                   INTERPOLASI                       ");
        System.out.println("=====================================================\n");
        System.out.println("Lnajut...");
        System.out.println("  (Modul Interpolasi)");
    }
    
    private static void handleRegression() {
        System.out.println("======================================================");
        System.out.println("            REGRESI POLINOMIAL BERGANDA              ");
        System.out.println("=====================================================\n");
        System.out.println("Lanjut...");
        System.out.println("  (Modul Regresi)");
    }
    
    // Helper methods
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid! Masukkan angka.");
            }
        }
    }
}