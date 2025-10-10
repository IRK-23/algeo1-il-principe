package algeo.spl;

import algeo.matrix.Matrix;
// Implementasi metode Eliminasi Gauss-Jordan untuk menyelesaikan SPL
// Menggunakan eliminasi penuh (forward dan backward) untuk menghasilkan reduced row echelon form
public class GaussJordan implements SPLSolver {
    private static final double EPSILON = 1e-10;
    
    @Override
    public SPLResult solve(Matrix augmentedMatrix) {
        SPLResult result = new SPLResult();
        result.setMethodName("Eliminasi Gauss-Jordan");
        
        Matrix m = augmentedMatrix.copy();
        int n = m.getRows();
        int cols = m.getCols();
        int numVars = cols - 1;
        
        if (n < numVars) {
            result.setType(SPLResult.SolutionType.INFINITE);
            result.addStep("PERHATIAN: Sistem underdetermined");
            result.addStep(String.format("Jumlah persamaan (%d) < jumlah variabel (%d)", n, numVars));
            return result;
        }
        
        result.addStep("=== MATRIKS AUGMENTED AWAL ===");
        result.addStep(matrixToString(m));
        result.addStep("\n=== ELIMINASI GAUSS-JORDAN ===");
        result.addStep("(Eliminasi dilakukan ke atas DAN ke bawah dari satu utama/pivot)\n");
        
        int lead = 0;
        
        for (int r = 0; r < Math.min(n, numVars); r++) {
            if (lead >= numVars) break;
            
            int i = r;
            
            // Cari satu utama/pivot
            while (Math.abs(m.get(i, lead)) < EPSILON) {
                i++;
                if (i == n) {
                    i = r;
                    lead++;
                    if (lead == numVars) break;
                }
            }
            
            if (lead == numVars) break;
            
            // Tukar baris
            if (i != r) {
                swapRows(m, i, r);
                result.addStep(String.format("Tukar baris %d ↔ baris %d:", r+1, i+1));
                result.addStep(matrixToString(m));
            }
            
            // Normalisasi baris pivot
            double pivot = m.get(r, lead);
            if (Math.abs(pivot) >= EPSILON) {
                result.addStep(String.format("\nNormalisasi baris %d (bagi dengan %.4f):", r+1, pivot));
                
                for (int j = 0; j < cols; j++) {
                    m.set(r, j, m.get(r, j) / pivot);
                    if (Math.abs(m.get(r, j)) < EPSILON) {
                        m.set(r, j, 0);
                    }
                }
                result.addStep(matrixToString(m));
                
                // ELIMINASI KOLOM (ATAS DAN BAWAH) - Karakteristik Gauss-Jordan
                result.addStep(String.format("Eliminasi kolom %d (ke atas DAN ke bawah):", lead+1));
                
                for (int k = 0; k < n; k++) {
                    if (k != r) {
                        double factor = m.get(k, lead);
                        if (Math.abs(factor) >= EPSILON) {
                            result.addStep(String.format("\nBaris_%d = Baris_%d - (%.4f) × Baris_%d", 
                                k+1, k+1, factor, r+1));
                            
                            for (int j = 0; j < cols; j++) {
                                m.set(k, j, m.get(k, j) - factor * m.get(r, j));
                                if (Math.abs(m.get(k, j)) < EPSILON) {
                                    m.set(k, j, 0);
                                }
                            }
                            result.addStep(matrixToString(m));
                        }
                    }
                }
            }
            
            lead++;
        }
        
        result.addStep("\n=== MATRIKS ESELON BARIS TEREDUKSI ===");
        result.addStep("(Reduced Row Echelon Form)");
        result.addStep(matrixToString(m));
        
        // Cek konsistensi
        int rank = 0;
        for (int i = 0; i < n; i++) {
            boolean rowAllZero = true;
            for (int j = 0; j < numVars; j++) {
                if (Math.abs(m.get(i, j)) >= EPSILON) {
                    rowAllZero = false;
                    break;
                }
            }
            
            if (rowAllZero && Math.abs(m.get(i, cols - 1)) >= EPSILON) {
                result.setType(SPLResult.SolutionType.NO_SOLUTION);
                result.addStep(String.format("\nSistem inkonsisten pada baris %d", i+1));
                return result;
            }
            
            if (!rowAllZero) rank++;
        }
        
        if (rank < numVars) {
            result.setType(SPLResult.SolutionType.INFINITE);
            result.setParametricSolution(buildParametricSolution(m, rank, numVars));
            return result;
        }
        
        // Baca solusi langsung (ga perlu back substitution)
        result.addStep("\n=== SOLUSI ===");
        result.addStep("(Langsung terbaca dari matriks tereduksi)");
        double[] solution = new double[numVars];
        
        for (int i = 0; i < numVars; i++) {
            solution[i] = m.get(i, cols - 1);
            result.addStep(String.format("x%d = %.6f", i+1, solution[i]));
        }
        
        result.setType(SPLResult.SolutionType.UNIQUE);
        result.setSolution(solution);
        
        return result;
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
    
    private String buildParametricSolution(Matrix m, int rank, int numVars) {
        StringBuilder sb = new StringBuilder();
        int numFree = numVars - rank;
        
        sb.append("Sistem memiliki ").append(numFree).append(" variabel bebas.\n");
        sb.append("Solusi parametrik:\n\n");
        
        boolean[] isPivot = new boolean[numVars];
        int[] pivotRow = new int[numVars];
        
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < numVars; j++) {
                if (Math.abs(m.get(i, j) - 1.0) < EPSILON && !isPivot[j]) {
                    boolean isLeading = true;
                    for (int k = 0; k < j; k++) {
                        if (Math.abs(m.get(i, k)) >= EPSILON) {
                            isLeading = false;
                            break;
                        }
                    }
                    if (isLeading) {
                        isPivot[j] = true;
                        pivotRow[j] = i;
                        break;
                    }
                }
            }
        }
        
        for (int j = 0; j < numVars; j++) {
            if (isPivot[j]) {
                int row = pivotRow[j];
                sb.append(String.format("x%d = %.4f", j + 1, m.get(row, numVars)));
                
                for (int k = j + 1; k < numVars; k++) {
                    if (!isPivot[k] && Math.abs(m.get(row, k)) >= EPSILON) {
                        double coeff = -m.get(row, k);
                        if (coeff >= 0) {
                            sb.append(String.format(" + %.4f*t%d", coeff, k + 1));
                        } else {
                            sb.append(String.format(" - %.4f*t%d", -coeff, k + 1));
                        }
                    }
                }
                sb.append("\n");
            } else {
                sb.append(String.format("x%d = t%d (parameter bebas)\n", j + 1, j + 1));
            }
        }
        
        return sb.toString();
    }
    
    @Override
    public String getMethodName() {
        return "Eliminasi Gauss-Jordan";
    }
}