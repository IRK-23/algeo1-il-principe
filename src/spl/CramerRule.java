package spl;

import matrix.Matrix;
import determinan.Determinan;

/**
 * Implementasi metode Cramer untuk menyelesaikan SPL
 * Metode Cramer: x_i = det(A_i) / det(A)
 * dimana A_i adalah matriks A dengan kolom ke-i diganti dengan vektor b
 */
public class CramerRule implements SPLSolver {
    
    private Determinan detCalculator;
    
    public CramerRule() {
        this.detCalculator = new Determinan();
    }
    
    @Override
    public String getMethodName() {
        return "Kaidah Cramer (Cramer's Rule)";
    }
    
    @Override
    public SPLResult solve(Matrix augmentedMatrix) {
        SPLResult result = new SPLResult();
        result.setMethodName(getMethodName());
        
        result.addStep("========================================");
        result.addStep("METODE KAIDAH CRAMER");
        result.addStep("========================================\n");
        
        int n = augmentedMatrix.getRows();
        int m = augmentedMatrix.getCols();
        
        // Validasi: harus sistem n x n
        if (m != n + 1) {
            result.addStep("ERROR: Metode Cramer hanya untuk sistem n×n");
            result.addStep("Ukuran matriks: " + n + "×" + m);
            result.addStep("Dibutuhkan: n×(n+1) untuk matriks augmented [A|b]");
            result.setType(SPLResult.SolutionType.NO_SOLUTION);
            return result;
        }
        
        // Pisahkan matriks A dan vektor b
        Matrix A = new Matrix(n, n);
        double[] b = new double[n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, augmentedMatrix.get(i, j));
            }
            b[i] = augmentedMatrix.get(i, n);
        }
        
        result.addStep("Matriks Koefisien A:");
        result.addStep(A.matrixToString(A));
        
        result.addStep("Vektor konstanta b:");
        for (int i = 0; i < n; i++) {
            result.addStep(String.format("b%d = %.4f", i + 1, b[i]));
        }
        result.addStep("");
        
        // Hitung det(A)
        result.addStep("========================================");
        result.addStep("LANGKAH 1: Hitung det(A)");
        result.addStep("========================================\n");
        
        StringBuilder detASteps = new StringBuilder();
        double detA = detCalculator.rekursiEkspansiKofaktor(A, detASteps);
        
        result.addStep(detASteps.toString());
        result.addStep(String.format("det(A) = %.6f\n", detA));
        
        // Cek apakah det(A) = 0
        if (Math.abs(detA) < 1e-10) {
            result.addStep("========================================");
            result.addStep("KESIMPULAN");
            result.addStep("========================================");
            result.addStep("det(A) = 0");
            result.addStep("Matriks A adalah SINGULAR");
            result.addStep("\nKaidah Cramer:");
            result.addStep("Jika det(A) = 0, maka SPL tidak memiliki solusi unik.");
            result.addStep("SPL mungkin memiliki:");
            result.addStep("  - Tidak ada solusi (inkonsisten), atau");
            result.addStep("  - Tak hingga banyak solusi");
            result.addStep("\nGunakan metode eliminasi Gauss untuk menentukan tipe solusi.");
            
            result.setType(SPLResult.SolutionType.NO_SOLUTION);
            return result;
        }
        
        // det(A) ≠ 0, maka ada solusi unik
        result.addStep("det(A) ≠ 0, maka SPL memiliki SOLUSI UNIK\n");
        
        // Hitung setiap x_i menggunakan Cramer's Rule
        double[] solution = new double[n];
        
        for (int i = 0; i < n; i++) {
            result.addStep("========================================");
            result.addStep(String.format("LANGKAH %d: Hitung x%d", i + 2, i + 1));
            result.addStep("========================================\n");
            
            // Buat matriks A_i (ganti kolom ke-i dengan vektor b)
            Matrix Ai = A.copy();
            for (int row = 0; row < n; row++) {
                Ai.set(row, i, b[row]);
            }
            
            result.addStep(String.format("Matriks A%d (kolom %d diganti dengan vektor b):", i + 1, i + 1));
            result.addStep(Ai.matrixToString(Ai));
            
            // Hitung det(A_i)
            StringBuilder detAiSteps = new StringBuilder();
            double detAi = detCalculator.rekursiEkspansiKofaktor(Ai, detAiSteps);
            
            result.addStep(detAiSteps.toString());
            result.addStep(String.format("det(A%d) = %.6f\n", i + 1, detAi));
            
            // Hitung x_i = det(A_i) / det(A)
            solution[i] = detAi / detA;
            
            result.addStep(String.format("x%d = det(A%d) / det(A)", i + 1, i + 1));
            result.addStep(String.format("x%d = %.6f / %.6f", i + 1, detAi, detA));
            result.addStep(String.format("x%d = %.6f\n", i + 1, solution[i]));
        }
        
        // Ringkasan hasil
        result.addStep("========================================");
        result.addStep("HASIL AKHIR");
        result.addStep("========================================");
        result.addStep("Solusi Unik:");
        for (int i = 0; i < n; i++) {
            result.addStep(String.format("x%d = %.6f", i + 1, solution[i]));
        }
        result.addStep("");
        
        // Verifikasi (opsional)
        result.addStep("========================================");
        result.addStep("VERIFIKASI");
        result.addStep("========================================");
        boolean verified = true;
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < n; j++) {
                sum += A.get(i, j) * solution[j];
            }
            double diff = Math.abs(sum - b[i]);
            result.addStep(String.format("Persamaan %d: %.6f ≈ %.6f (selisih: %.2e)", 
                     i + 1, sum, b[i], diff));
            
            if (diff > 1e-6) {
                verified = false;
            }
        }
        
        if (verified) {
            result.addStep("\n✓ Verifikasi BERHASIL - Solusi benar!");
        } else {
            result.addStep("\n⚠ Perhatian: Ada selisih kecil karena pembulatan numerik");
        }
        
        result.setType(SPLResult.SolutionType.UNIQUE);
        result.setSolution(solution);
        
        return result;
    }
    
    /**
     * Validasi apakah matriks cocok untuk metode Cramer
     */
    public static boolean isValidForCramer(Matrix augmentedMatrix) {
        int n = augmentedMatrix.getRows();
        int m = augmentedMatrix.getCols();
        
        // Harus n×(n+1)
        return m == n + 1;
    }
}