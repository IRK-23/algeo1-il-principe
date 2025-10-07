package spl;

import matrix.Matrix;

/**
 * Implementasi metode Kaidah Cramer untuk menyelesaikan SPL
 * CATATAN: Metode ini memerlukan modul Determinan dari teman kelompok
 * Untuk sementara, menggunakan implementasi determinan sederhana
 */
public class CramerRule implements SPLSolver {
    private static final double EPSILON = 1e-10;
    
    @Override
    public SPLResult solve(Matrix augmentedMatrix) {
        SPLResult result = new SPLResult();
        result.setMethodName("Kaidah Cramer");
        
        int n = augmentedMatrix.getRows();
        int cols = augmentedMatrix.getCols();
        
        // Cramer hanya untuk sistem n x n
        if (n != cols - 1) {
            result.setType(SPLResult.SolutionType.NO_SOLUTION);
            result.addStep("PERHATIAN: Kaidah Cramer hanya dapat digunakan untuk sistem n×n.");
            result.addStep(String.format("Sistem ini: %d persamaan, %d variabel", n, cols - 1));
            return result;
        }
        
        // Ekstrak matriks koefisien A
        Matrix A = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, augmentedMatrix.get(i, j));
            }
        }
        
        // Ekstrak vektor konstanta b
        double[] b = new double[n];
        for (int i = 0; i < n; i++) {
            b[i] = augmentedMatrix.get(i, cols - 1);
        }
        
        result.addStep("=== SISTEM PERSAMAAN ===");
        result.addStep("Matriks Koefisien A:");
        result.addStep(matrixToString(A));
        
        result.addStep("\nVektor Konstanta b:");
        StringBuilder bStr = new StringBuilder();
        for (int i = 0; i < n; i++) {
            bStr.append(String.format("b%d = %.4f\n", i+1, b[i]));
        }
        result.addStep(bStr.toString());
        
        // Hitung determinan A
        result.addStep("=== MENGHITUNG det(A) ===");
        double detA = calculateDeterminant(A, result, "A");
        result.addStep(String.format("det(A) = %.6f", detA));
        
        // Cek apakah det(A) = 0
        if (Math.abs(detA) < EPSILON) {
            result.setType(SPLResult.SolutionType.NO_SOLUTION);
            result.addStep("\nPERHATIAN: det(A) = 0");
            result.addStep("Kaidah Cramer tidak dapat digunakan.");
            result.addStep("Sistem mungkin tidak memiliki solusi atau memiliki tak hingga solusi.");
            return result;
        }
        
        // Hitung solusi menggunakan Cramer's Rule
        result.addStep("\n=== MENGHITUNG SOLUSI DENGAN KAIDAH CRAMER ===");
        double[] solution = new double[n];
        
        for (int i = 0; i < n; i++) {
            // Buat matriks Ai (ganti kolom ke-i dengan vektor b)
            Matrix Ai = A.copy();
            for (int row = 0; row < n; row++) {
                Ai.set(row, i, b[row]);
            }
            
            result.addStep(String.format("\nMatriks A%d (kolom %d diganti dengan b):", i+1, i+1));
            result.addStep(matrixToString(Ai));
            
            double detAi = calculateDeterminant(Ai, result, "A" + (i+1));
            result.addStep(String.format("det(A%d) = %.6f", i+1, detAi));
            
            solution[i] = detAi / detA;
            result.addStep(String.format("\nx%d = det(A%d) / det(A)", i+1, i+1));
            result.addStep(String.format("x%d = %.6f / %.6f", i+1, detAi, detA));
            result.addStep(String.format("x%d = %.6f", i+1, solution[i]));
        }
        
        result.setType(SPLResult.SolutionType.UNIQUE);
        result.setSolution(solution);
        
        return result;
    }
    
    /**
     * Menghitung determinan menggunakan ekspansi kofaktor
     * CATATAN: Ini adalah implementasi sementara
     * Nanti akan diganti dengan modul Determinan dari teman kelompok
     */
    private double calculateDeterminant(Matrix m, SPLResult result, String matrixName) {
        int n = m.getRows();
        
        if (n == 1) {
            return m.get(0, 0);
        }
        
        if (n == 2) {
            double det = m.get(0, 0) * m.get(1, 1) - m.get(0, 1) * m.get(1, 0);
            result.addStep(String.format("det(%s) = (%.4f)(%.4f) - (%.4f)(%.4f) = %.6f",
                matrixName, m.get(0, 0), m.get(1, 1), m.get(0, 1), m.get(1, 0), det));
            return det;
        }
        
        if (n == 3) {
            // Metode Sarrus untuk 3x3
            double det = 0;
            
            // Diagonal kanan bawah
            det += m.get(0, 0) * m.get(1, 1) * m.get(2, 2);
            det += m.get(0, 1) * m.get(1, 2) * m.get(2, 0);
            det += m.get(0, 2) * m.get(1, 0) * m.get(2, 1);
            
            // Diagonal kiri bawah (dikurangi)
            det -= m.get(0, 2) * m.get(1, 1) * m.get(2, 0);
            det -= m.get(0, 0) * m.get(1, 2) * m.get(2, 1);
            det -= m.get(0, 1) * m.get(1, 0) * m.get(2, 2);
            
            result.addStep(String.format("det(%s) = %.6f (metode Sarrus)", matrixName, det));
            return det;
        }
        
        // Ekspansi kofaktor baris pertama untuk n > 3
        double det = 0;
        result.addStep(String.format("\nEkspansi kofaktor baris pertama untuk det(%s):", matrixName));
        
        for (int j = 0; j < n; j++) {
            if (Math.abs(m.get(0, j)) < EPSILON) continue;
            
            Matrix minor = getMinor(m, 0, j);
            double minorDet = calculateDeterminantRecursive(minor);
            double cofactor = Math.pow(-1, j) * m.get(0, j) * minorDet;
            det += cofactor;
            
            result.addStep(String.format("C(0,%d) = (-1)^%d × %.4f × %.6f = %.6f",
                j, j, m.get(0, j), minorDet, cofactor));
        }
        
        return det;
    }
    
    /**
     * Versi rekursif untuk perhitungan minor (tanpa log)
     */
    private double calculateDeterminantRecursive(Matrix m) {
        int n = m.getRows();
        
        if (n == 1) {
            return m.get(0, 0);
        }
        
        if (n == 2) {
            return m.get(0, 0) * m.get(1, 1) - m.get(0, 1) * m.get(1, 0);
        }
        
        double det = 0;
        for (int j = 0; j < n; j++) {
            if (Math.abs(m.get(0, j)) < EPSILON) continue;
            
            Matrix minor = getMinor(m, 0, j);
            double minorDet = calculateDeterminantRecursive(minor);
            det += Math.pow(-1, j) * m.get(0, j) * minorDet;
        }
        
        return det;
    }
    
    private Matrix getMinor(Matrix m, int row, int col) {
        int n = m.getRows();
        Matrix minor = new Matrix(n - 1, n - 1);
        
        int minorRow = 0;
        for (int i = 0; i < n; i++) {
            if (i == row) continue;
            int minorCol = 0;
            for (int j = 0; j < n; j++) {
                if (j == col) continue;
                minor.set(minorRow, minorCol, m.get(i, j));
                minorCol++;
            }
            minorRow++;
        }
        
        return minor;
    }
    
    private String matrixToString(Matrix m) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getCols(); j++) {
                sb.append(String.format("%10.4f ", m.get(i, j)));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    @Override
    public String getMethodName() {
        return "Kaidah Cramer";
    }
}