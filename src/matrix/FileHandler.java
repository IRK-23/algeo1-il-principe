package matrix;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import spl.SPLResult;

public class FileHandler {
    
    //Membaca matriks augmented dari file untuk SPL
    public static Matrix readAugmentedMatrix(String filename) throws IOException {
        return readMatrix(filename);
    }
    
    //Membaca matriks umum dari file
    public static Matrix readMatrix(String filename) throws IOException {
        BufferedReader br = null;
        
        try {
            br = new BufferedReader(new FileReader(filename));
            List<double[]> rows = new ArrayList<>();
            String line;
            
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] values = line.split("\\s+");
                double[] row = new double[values.length];
                
                try {
                    for (int i = 0; i < values.length; i++) {
                        row[i] = Double.parseDouble(values[i]);
                    }
                    rows.add(row);
                } catch (NumberFormatException e) {
                    throw new IOException("Error: Format angka tidak valid di baris: " + line);
                }
            }
            
            if (rows.isEmpty()) {
                throw new IOException("File kosong atau tidak valid");
            }
            
            // VALIDASI: Cek konsistensi jumlah kolom
            int numCols = rows.get(0).length;
            for (int i = 1; i < rows.size(); i++) {
                if (rows.get(i).length != numCols) {
                    throw new IOException(
                        String.format("Error: Jumlah kolom tidak konsisten.\n" +
                                    "Baris 1: %d kolom\n" +
                                    "Baris %d: %d kolom", 
                                    numCols, i + 1, rows.get(i).length)
                    );
                }
            }
            
            int numRows = rows.size();
            Matrix matrix = new Matrix(numRows, numCols);
            
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    matrix.set(i, j, rows.get(i)[j]);
                }
            }
            
            return matrix;
            
        } catch (FileNotFoundException e) {
            throw new IOException("Error: File tidak ditemukan: " + filename);
        } catch (IOException e) {
            throw new IOException("Error membaca file: " + e.getMessage());
        } finally {
            // tutup file
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.err.println("Warning: Gagal menutup file: " + e.getMessage());
                }
            }
        }
    }
    
     
    public static void writeSPLOutput(String filename, SPLResult result, Matrix input) 
            throws IOException {
        BufferedWriter bw = null;
        
        try {
            bw = new BufferedWriter(new FileWriter(filename));

            bw.write("========================================");
            bw.newLine();
            bw.write("  HASIL PENYELESAIAN SISTEM PERSAMAAN LINIER");
            bw.newLine();
            bw.write("========================================");
            bw.newLine();
            bw.newLine();
            
            bw.write("Metode: " + result.getMethodName());
            bw.newLine();
            bw.newLine();
            
            // Input yg dipake
            bw.write("INPUT (Matriks Augmented):");
            bw.newLine();
            for (int i = 0; i < input.getRows(); i++) {
                for (int j = 0; j < input.getCols(); j++) {
                    bw.write(String.format("%10.4f ", input.get(i, j)));
                }
                bw.newLine();
            }
            bw.newLine();
            
            // Langkah-langkah
            bw.write("========================================");
            bw.newLine();
            bw.write("LANGKAH-LANGKAH PENYELESAIAN:");
            bw.newLine();
            bw.write("========================================");
            bw.newLine();
            for (String step : result.getSteps()) {
                bw.write(step);
                bw.newLine();
            }
            bw.newLine();
            
            // Hasil SPL
            bw.write("========================================");
            bw.newLine();
            bw.write("HASIL:");
            bw.newLine();
            bw.write("========================================");
            bw.newLine();
            
            switch(result.getType()) {
                case UNIQUE:
                    bw.write("Solusi Tunggal:");
                    bw.newLine();
                    double[] solution = result.getSolution();
                    for (int i = 0; i < solution.length; i++) {
                        bw.write(String.format("x%d = %.6f\n", i+1, solution[i]));
                    }
                    break;
                case NO_SOLUTION:
                    bw.write("TIDAK ADA SOLUSI");
                    bw.newLine();
                    bw.write("Sistem persamaan inkonsisten.");
                    bw.newLine();
                    break;
                case INFINITE:
                    bw.write("SOLUSI TAK HINGGA");
                    bw.newLine();
                    bw.write("Sistem memiliki tak hingga banyak solusi.");
                    bw.newLine();
                    bw.newLine();
                    bw.write(result.getParametricSolution());
                    bw.newLine();
                    break;
            }
            
            bw.write("========================================");
            bw.newLine();
            
            System.out.println("Hasil berhasil disimpan ke " + filename);
            
        } catch (IOException e) {
            throw new IOException("Error menulis ke file: " + e.getMessage());
        } finally {
            // Pastikan file selalu ditutup dan di-flush
            if (bw != null) {
                try {
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    System.err.println("Warning: Gagal menutup file: " + e.getMessage());
                }
            }
        }
    }

    
    // Menulis hasil SPL dengan metode Invers ke file
    // Format khusus untuk menampilkan detail perhitungan matriks invers
     
    public static void writeInverseMethodOutput(String filename, SPLResult result, Matrix input) 
            throws IOException {
        BufferedWriter bw = null;
        
        try {
            bw = new BufferedWriter(new FileWriter(filename));

            bw.write("========================================");
            bw.newLine();
            bw.write("  PENYELESAIAN SPL METODE MATRIKS BALIKAN");
            bw.newLine();
            bw.write("========================================");
            bw.newLine();
            bw.newLine();
            
            bw.write("Metode: " + result.getMethodName());
            bw.newLine();
            bw.write("Formula: x = A^(-1)b");
            bw.newLine();
            bw.newLine();
            
            // Input yg dipake
            bw.write("INPUT (Matriks Augmented [A|b]):");
            bw.newLine();
            for (int i = 0; i < input.getRows(); i++) {
                for (int j = 0; j < input.getCols(); j++) {
                    if (j == input.getCols() - 1) {
                        bw.write("| ");
                    }
                    bw.write(String.format("%10.4f ", input.get(i, j)));
                }
                bw.newLine();
            }
            bw.newLine();
            
            // Langkah-langkah
            bw.write("========================================");
            bw.newLine();
            bw.write("LANGKAH-LANGKAH PENYELESAIAN:");
            bw.newLine();
            bw.write("========================================");
            bw.newLine();
            bw.newLine();
            
            for (String step : result.getSteps()) {
                bw.write(step);
                bw.newLine();
            }
            bw.newLine();
            
            // Hasil SPL
            bw.write("========================================");
            bw.newLine();
            bw.write("HASIL AKHIR:");
            bw.newLine();
            bw.write("========================================");
            bw.newLine();
            
            switch(result.getType()) {
                case UNIQUE:
                    bw.write("Tipe Solusi: SOLUSI TUNGGAL");
                    bw.newLine();
                    bw.newLine();
                    double[] solution = result.getSolution();
                    for (int i = 0; i < solution.length; i++) {
                        bw.write(String.format("x%d = %.6f\n", i+1, solution[i]));
                    }
                    break;
                    
                case NO_SOLUTION:
                    bw.write("Tipe Solusi: TIDAK ADA SOLUSI");
                    bw.newLine();
                    bw.newLine();
                    bw.write("Sistem persamaan tidak memiliki solusi.");
                    bw.newLine();
                    bw.write("Matriks A adalah singular (det(A) = 0),");
                    bw.newLine();
                    bw.write("sehingga tidak memiliki matriks invers.");
                    bw.newLine();
                    break;
                    
                case INFINITE:
                    bw.write("Tipe Solusi: SOLUSI TAK HINGGA");
                    bw.newLine();
                    bw.newLine();
                    bw.write("Catatan: Metode matriks balikan tidak cocok");
                    bw.newLine();
                    bw.write("untuk sistem dengan solusi tak hingga.");
                    bw.newLine();
                    if (result.getParametricSolution() != null) {
                        bw.newLine();
                        bw.write(result.getParametricSolution());
                        bw.newLine();
                    }
                    break;
            }
            
            bw.newLine();
            bw.write("========================================");
            bw.newLine();
            bw.write("Catatan:");
            bw.newLine();
            bw.write("- Metode ini menggunakan adjoin untuk menghitung A^(-1)");
            bw.newLine();
            bw.write("- Metode ini hanya berlaku untuk SPL n x n");
            bw.newLine();
            bw.write("- Matriks A harus non-singular (det(A) ≠ 0)");
            bw.newLine();
            bw.write("========================================");
            bw.newLine();
            
            System.out.println("Hasil metode Invers berhasil disimpan ke " + filename);
            
        } catch (IOException e) {
            throw new IOException("Error menulis ke file: " + e.getMessage());
        } finally {
            if (bw != null) {
                try {
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    System.err.println("Warning: Gagal menutup file: " + e.getMessage());
                }
            }
        }
    }

    
    // Method overload untuk writeSPLOutput yang mendeteksi metode secara otomatis
    // Memilih format yang sesuai berdasarkan nama metode
    public static void writeSPLOutputAuto(String filename, SPLResult result, Matrix input) 
            throws IOException {
        // Jika menggunakan metode invers, gunakan format khusus
        if (result.getMethodName().contains("Invers") || 
            result.getMethodName().contains("Balikan")) {
            writeInverseMethodOutput(filename, result, input);
        } else {
            // Gunakan format standard untuk metode lain (Gauss, Gauss-Jordan, Cramer, dll)
            writeSPLOutput(filename, result, input);
        }
    }
}