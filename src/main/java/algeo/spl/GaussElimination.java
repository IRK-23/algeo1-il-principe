package algeo.spl;

import algeo.matrix.Matrix;
// Implementasi metode Eliminasi Gauss untuk menyelesaikan SPL
// Menggunakan Forward Elimination (Fase Maju) dan Back Substitution (Substitusi/Penyulihan Mundur)
public class GaussElimination implements SPLSolver {
    private static final double EPSILON = 1e-10;
    
    @Override
    public SPLResult solve(Matrix augmentedMatrix) {
        SPLResult result = new SPLResult();
        result.setMethodName("Eliminasi Gauss");
        
        Matrix m = augmentedMatrix.copy();
        int n = m.getRows();
        int cols = m.getCols();
        int numVars = cols - 1;
        
        // VALIDASI: Sistem underdetermined
        if (n < numVars) {
            result.setType(SPLResult.SolutionType.INFINITE);
            result.addStep("PERHATIAN: Sistem underdetermined");
            result.addStep(String.format("Jumlah persamaan (%d) < jumlah variabel (%d)", n, numVars));
            result.addStep("Sistem kemungkinan memiliki tak hingga solusi.");
            return result;
        }
        
        result.addStep("=== MATRIKS AUGMENTED AWAL ===");
        result.addStep(matrixToString(m));
        
        // ============================================
        // FASE MAJU (FORWARD ELIMINATION)
        // Mengubah matriks menjadi bentuk eselon baris
        // Eliminasi elemen DI BAWAH satu utama/pivot
        // ============================================
        result.addStep("\n=== FASE MAJU (FORWARD ELIMINATION) ===");
        result.addStep("Tujuan: Bentuk matriks segitiga atas (upper triangular)");
        result.addStep("Eliminasi hanya dilakukan pada baris di BAWAH satu utama/pivot\n");
        
        // Proses hanya sampai min(n, numVars) untuk overdetermined system
        int maxPivot = Math.min(n, numVars);
        
        for (int i = 0; i < maxPivot; i++) {
            result.addStep(String.format("--- Pivot pada baris %d, kolom %d ---", i+1, i+1));
            
            // Cari pivot terbesar
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(m.get(k, i)) > Math.abs(m.get(maxRow, i))) {
                    maxRow = k;
                }
            }
            
            // Tukar baris jika perlu
            if (maxRow != i) {
                swapRows(m, i, maxRow);
                result.addStep(String.format("Tukar baris %d ↔ baris %d:", i+1, maxRow+1));
                result.addStep(matrixToString(m));
            }
            
            // Cek jika pivot adalah nol
            if (Math.abs(m.get(i, i)) < EPSILON) {
                // Cek apakah baris ini semua nol
                boolean allZero = true;
                for (int j = i; j < numVars; j++) {
                    if (Math.abs(m.get(i, j)) >= EPSILON) {
                        allZero = false;
                        break;
                    }
                }
                
                if (allZero) {
                    // Cek nilai konstanta
                    if (Math.abs(m.get(i, cols - 1)) < EPSILON) {
                        // Solusi tak hingga
                        result.setType(SPLResult.SolutionType.INFINITE);
                        result.setParametricSolution(buildParametricSolution(m, i));
                        return result;
                    } else {
                        // Tidak ada solusi
                        result.setType(SPLResult.SolutionType.NO_SOLUTION);
                        result.addStep(String.format("\nSistem inkonsisten: 0 = %.4f", m.get(i, cols - 1)));
                        return result;
                    }
                }
                continue;
            }
            
            // HANYA ELIMINASI BARIS DI BAWAH SATU UTAMA/PIVOT (FASE MAJU)
            result.addStep(String.format("Eliminasi elemen di BAWAH pivot m[%d][%d] = %.4f", i+1, i+1, m.get(i, i)));
            
            for (int k = i + 1; k < n; k++) {
                double factor = m.get(k, i) / m.get(i, i);
                
                if (Math.abs(factor) > EPSILON) {
                    result.addStep(String.format("\nBaris_%d = Baris_%d - (%.4f) × Baris_%d", 
                        k+1, k+1, factor, i+1));
                    
                    for (int j = i; j < cols; j++) {
                        m.set(k, j, m.get(k, j) - factor * m.get(i, j));
                        
                        // Bersihkan nilai yang sangat kecil
                        if (Math.abs(m.get(k, j)) < EPSILON) {
                            m.set(k, j, 0);
                        }
                    }
                    
                    result.addStep(matrixToString(m));
                }
            }
        }
        
        result.addStep("\n=== HASIL FASE MAJU: MATRIKS ESELON BARIS ===");
        result.addStep("(Bentuk segitiga atas - upper triangular form)");
        result.addStep(matrixToString(m));
        
        // Cek konsistensi sistem untuk semua baris
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
                result.addStep(String.format("\nSistem inkonsisten pada baris %d: 0 = %.4f", i+1, m.get(i, cols - 1)));
                return result;
            }
        }
        
        // Hitung rank
        int rank = 0;
        for (int i = 0; i < n; i++) {
            boolean nonZero = false;
            for (int j = 0; j < numVars; j++) {
                if (Math.abs(m.get(i, j)) >= EPSILON) {
                    nonZero = true;
                    break;
                }
            }
            if (nonZero) rank++;
        }
        
        if (rank < numVars) {
            result.setType(SPLResult.SolutionType.INFINITE);
            result.setParametricSolution(buildParametricSolution(m, rank));
            return result;
        }
        
        // ============================================
        // SUBSTITUSI MUNDUR (BACK SUBSTITUTION)
        // Menghitung nilai variabel dari bawah ke atas
        // ============================================
        result.addStep("\n=== SUBSTITUSI/PENYULLIHAN MUNDUR (BACK SUBSTITUTION) ===");
        result.addStep("Menghitung nilai variabel dari x_n ke x_1\n");
        
        double[] solution = new double[numVars];
        
        // Mulai dari variabel terakhir (paling bawah)
        for (int i = numVars - 1; i >= 0; i--) {
            double sum = m.get(i, cols - 1);
            
            // Kurangi dengan variabel yang sudah diketahui
            for (int j = i + 1; j < numVars; j++) {
                sum -= m.get(i, j) * solution[j];
            }
            
            if (Math.abs(m.get(i, i)) < EPSILON) {
                result.setType(SPLResult.SolutionType.INFINITE);
                result.setParametricSolution(buildParametricSolution(m, i));
                return result;
            }
            
            solution[i] = sum / m.get(i, i);
            
            // Tampilkan proses substitusi
            if (i == numVars - 1) {
                // Variabel terakhir
                result.addStep(String.format("x%d = %.4f / %.4f = %.6f", 
                    i+1, m.get(i, cols-1), m.get(i, i), solution[i]));
            } else {
                // Variabel lain yang perlu substitusi
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("x%d = (%.4f", i+1, m.get(i, cols-1)));
                for (int j = i + 1; j < numVars; j++) {
                    if (Math.abs(m.get(i, j)) >= EPSILON) {
                        sb.append(String.format(" - %.4f×%.6f", m.get(i, j), solution[j]));
                    }
                }
                sb.append(String.format(") / %.4f = %.6f", m.get(i, i), solution[i]));
                result.addStep(sb.toString());
            }
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
    
    private String buildParametricSolution(Matrix m, int rank) {
        int n = m.getRows();
        int numVars = m.getCols() - 1;
        int numFree = numVars - rank;
        
        StringBuilder sb = new StringBuilder();
        sb.append("Sistem memiliki ").append(numFree).append(" variabel bebas.\n");
        sb.append("Solusi parametrik:\n\n");
        
        // Identifikasi variabel pivot dan bebas
        boolean[] isPivot = new boolean[numVars];
        int[] pivotCol = new int[rank];
        int pivotCount = 0;
        
        for (int i = 0; i < rank && i < n; i++) {
            for (int j = 0; j < numVars; j++) {
                if (Math.abs(m.get(i, j)) >= EPSILON && !isPivot[j]) {
                    isPivot[j] = true;
                    pivotCol[pivotCount++] = j;
                    break;
                }
            }
        }
        
        // Tulis solusi parametrik
        for (int i = 0; i < numVars; i++) {
            if (isPivot[i]) {
                // Variabel pivot
                int row = -1;
                for (int r = 0; r < rank && r < n; r++) {
                    if (Math.abs(m.get(r, i)) >= EPSILON) {
                        row = r;
                        break;
                    }
                }
                
                if (row >= 0) {
                    sb.append(String.format("x%d = ", i + 1));
                    double constant = m.get(row, numVars) / m.get(row, i);
                    sb.append(String.format("%.4f", constant));
                    
                    for (int j = i + 1; j < numVars; j++) {
                        if (!isPivot[j] && Math.abs(m.get(row, j)) >= EPSILON) {
                            double coeff = -m.get(row, j) / m.get(row, i);
                            if (coeff >= 0) {
                                sb.append(String.format(" + %.4f*x%d", coeff, j + 1));
                            } else {
                                sb.append(String.format(" - %.4f*x%d", -coeff, j + 1));
                            }
                        }
                    }
                    sb.append("\n");
                }
            } else {
                // Variabel bebas
                sb.append(String.format("x%d = t%d (parameter bebas)\n", i + 1, i + 1));
            }
        }
        
        return sb.toString();
    }
    
    @Override
    public String getMethodName() {
        return "Eliminasi Gauss";
    }
}