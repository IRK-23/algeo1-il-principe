package spl;

import matrix.Matrix;

/**
 * Implementasi metode Matriks Balikan untuk menyelesaikan SPL
 * CATATAN: Metode ini memerlukan modul Inverse dari teman kelompok
 * Untuk sementara, menggunakan implementasi inverse sederhana dengan Gauss-Jordan
 */
public class InverseMatrix implements SPLSolver {
    private static final double EPSILON = 1e-10;
    
    @Override
    public SPLResult solve(Matrix augmentedMatrix) {
        SPLResult result = new SPLResult();
        result.setMethodName("Metode Matriks Balikan");
        
        int n = augmentedMatrix.getRows();
        int cols = augmentedMatrix.getCols();
        
        // Metode inverse hanya untuk sistem n x n
        if (n != cols - 1) {
            result.setType(SPLResult.SolutionType.NO_SOLUTION);
            result.addStep("PERHATIAN: Metode Matriks Balikan hanya dapat digunakan untuk sistem n×n.");
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
        
        result.addStep("=== SISTEM PERSAMAAN: Ax = b ===\n");
        result.addStep("Matriks Koefisien A:");
        result.addStep(matrixToString(A));
        
        result.addStep("\nVektor Konstanta b:");
        StringBuilder bStr = new StringBuilder();
        for (int i = 0; i < n; i++) {
            bStr.append(String.format("b%d = %.4f\n", i+1, b[i]));
        }
        result.addStep(bStr.toString());
        
        // Hitung inverse dari A menggunakan Gauss-Jordan
        result.addStep("=== MENGHITUNG A^(-1) DENGAN GAUSS-JORDAN ===");
        Matrix AInv = calculateInverse(A, result);
        
        if (AInv == null) {
            result.setType(SPLResult.SolutionType.NO_SOLUTION);
            result.addStep("\nPERHATIAN: Matriks A tidak memiliki inverse (matriks singular).");
            result.addStep("Sistem mungkin tidak memiliki solusi atau memiliki tak hingga solusi.");
            return result;
        }
        
        result.addStep("\nMatriks A^(-1):");
        result.addStep(matrixToString(AInv));
        
        // Hitung x = A^(-1) * b
        result.addStep("\n=== MENGHITUNG x = A^(-1) × b ===");
        double[] solution = new double[n];
        
        for (int i = 0; i < n; i++) {
            solution[i] = 0;
            StringBuilder calculation = new StringBuilder();
            calculation.append(String.format("x%d = ", i+1));
            
            for (int j = 0; j < n; j++) {
                solution[i] += AInv.get(i, j) * b[j];
                
                if (j > 0) calculation.append(" + ");
                calculation.append(String.format("(%.4f)(%.4f)", AInv.get(i, j), b[j]));
            }
            
            calculation.append(String.format(" = %.6f", solution[i]));
            result.addStep(calculation.toString());
        }
        
        result.setType(SPLResult.SolutionType.UNIQUE);
        result.setSolution(solution);
        
        return result;
    }
    
    /**
     * Menghitung inverse matriks menggunakan metode Gauss-Jordan
     * CATATAN: Ini adalah implementasi sementara
     * Nanti akan diganti dengan modul Inverse dari teman kelompok
     */
    private Matrix calculateInverse(Matrix matrix, SPLResult result) {
        int n = matrix.getRows();
        
        // Buat augmented matrix [A|I]
        Matrix augmented = new Matrix(n, 2 * n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                augmented.set(i, j, matrix.get(i, j));
            }
            augmented.set(i, i + n, 1); // Identity matrix
        }
        
        result.addStep("\nMatriks Augmented [A|I]:");
        result.addStep(augmentedMatrixToString(augmented, n));
        
        // Gauss-Jordan elimination
        for (int i = 0; i < n; i++) {
            // Cari pivot terbesar
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(augmented.get(k, i)) > Math.abs(augmented.get(maxRow, i))) {
                    maxRow = k;
                }
            }
            
            // Cek singularitas
            if (Math.abs(augmented.get(maxRow, i)) < EPSILON) {
                return null; // Matriks singular
            }
            
            // Swap rows
            if (maxRow != i) {
                swapRows(augmented, i, maxRow);
                result.addStep(String.format("\nTukar baris %d dengan baris %d:", i+1, maxRow+1));
                result.addStep(augmentedMatrixToString(augmented, n));
            }
            
            // Normalize pivot row
            double pivot = augmented.get(i, i);
            result.addStep(String.format("\nNormalisasi baris %d (bagi dengan %.4f):", i+1, pivot));
            
            for (int j = 0; j < 2 * n; j++) {
                augmented.set(i, j, augmented.get(i, j) / pivot);
                
                if (Math.abs(augmented.get(i, j)) < EPSILON) {
                    augmented.set(i, j, 0);
                }
            }
            result.addStep(augmentedMatrixToString(augmented, n));
            
            // Eliminate column
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = augmented.get(k, i);
                    if (Math.abs(factor) >= EPSILON) {
                        result.addStep(String.format("\nOPS: Baris_%d = Baris_%d - (%.4f) × Baris_%d", 
                            k+1, k+1, factor, i+1));
                        
                        for (int j = 0; j < 2 * n; j++) {
                            augmented.set(k, j, augmented.get(k, j) - factor * augmented.get(i, j));
                            
                            if (Math.abs(augmented.get(k, j)) < EPSILON) {
                                augmented.set(k, j, 0);
                            }
                        }
                        result.addStep(augmentedMatrixToString(augmented, n));
                    }
                }
            }
        }
        
        // Ekstrak inverse dari bagian kanan
        Matrix inverse = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverse.set(i, j, augmented.get(i, j + n));
            }
        }
        
        return inverse;
    }
    
    private void swapRows(Matrix m, int row1, int row2) {
        int cols = m.getCols();
        for (int j = 0; j < cols; j++) {
            double temp = m.get(row1, j);
            m.set(row1, j, m.get(row2, j));
            m.set(row2, j, temp);
        }
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
    
    private String augmentedMatrixToString(Matrix m, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getCols(); j++) {
                if (j == n) sb.append(" | ");
                sb.append(String.format("%8.4f ", m.get(i, j)));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    @Override
    public String getMethodName() {
        return "Metode Matriks Balikan";
    }
}