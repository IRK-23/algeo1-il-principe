package algeo.spl;

import algeo.matrix.Matrix;
import algeo.invers.Invers;
import algeo.invers.InversResult;
 //Solver SPL menggunakan metode matriks balikan (invers)
 //Menyelesaikan SPL Ax = b dengan rumus x = A^(-1)b
public class InversMethod implements SPLSolver {
    
    private Invers inversCalculator;
    
    public InversMethod() {
        this.inversCalculator = new Invers();
    }
    
    @Override
    public SPLResult solve(Matrix augmentedMatrix) {
        SPLResult result = new SPLResult();
        result.setMethodName(getMethodName());
        
        int n = augmentedMatrix.getRows();
        int m = augmentedMatrix.getCols();
        
        // Validasi: jumlah kolom harus n+1 (n kolom untuk koefisien + 1 kolom untuk konstanta)
        if (m != n + 1) {
            result.setType(SPLResult.SolutionType.NO_SOLUTION);
            result.addStep("Error: Matriks augmented tidak valid untuk metode invers.");
            result.addStep("Metode invers hanya dapat digunakan untuk sistem SPL n x n.");
            return result;
        }
        
        result.addStep("=== METODE MATRIKS BALIKAN (INVERS) ===\n");
        result.addStep("Menyelesaikan SPL Ax = b dengan rumus x = A^(-1)b\n");
        
        // Pisahkan matriks A dan vektor b dari augmented matrix
        Matrix A = new Matrix(n, n);
        double[] b = new double[n];
        
        result.addStep("Memisahkan matriks koefisien A dan vektor konstanta b:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A.set(i, j, augmentedMatrix.get(i, j));
            }
            b[i] = augmentedMatrix.get(i, n);
        }
        
        result.addStep("Matriks A:");
        result.addStep(A.matrixToString(A));
        
        result.addStep("Vektor b:");
        StringBuilder bVector = new StringBuilder();
        for (int i = 0; i < n; i++) {
            bVector.append(String.format("%.6f", b[i]));
            if (i < n - 1) bVector.append("\n");
        }
        result.addStep(bVector.toString() + "\n");
        
        // Hitung invers matriks A menggunakan metode adjoin
        result.addStep("Menghitung A^(-1) menggunakan metode adjoin:\n");
        InversResult inversResult = inversCalculator.inversAdjoin(A);
        
        // Tambahkan langkah-langkah dari perhitungan invers
        result.addStep(inversResult.getSteps().toString());
        
        Matrix AInverse = inversResult.getMatrix();
        
        // Cek apakah invers ada (determinan != 0)
        if (AInverse == null) {
            result.setType(SPLResult.SolutionType.NO_SOLUTION);
            result.addStep("Matriks A tidak memiliki invers (det(A) = 0).");
            result.addStep("Sistem persamaan tidak memiliki solusi unik atau tidak konsisten.");
            return result;
        }
        
        // Hitung solusi x = A^(-1) * b
        result.addStep("Menghitung solusi x = A^(-1)b:");
        double[] x = new double[n];
        
        for (int i = 0; i < n; i++) {
            x[i] = 0;
            for (int j = 0; j < n; j++) {
                x[i] += AInverse.get(i, j) * b[j];
            }
        }
        
        // Hasil perkalian
        result.addStep("\nPerkalian matriks A^(-1) dengan vektor b:");
        StringBuilder calculation = new StringBuilder();
        for (int i = 0; i < n; i++) {
            calculation.append(String.format("x%d = ", i + 1));
            for (int j = 0; j < n; j++) {
                if (j > 0) {
                    calculation.append(b[j] >= 0 ? " + " : " - ");
                    calculation.append(String.format("%.6f * %.6f", 
                        AInverse.get(i, j), Math.abs(b[j])));
                } else {
                    calculation.append(String.format("%.6f * %.6f", 
                        AInverse.get(i, j), b[j]));
                }
            }
            calculation.append(String.format(" = %.6f\n", x[i]));
        }
        result.addStep(calculation.toString());
        
        // Set hasil
        result.setType(SPLResult.SolutionType.UNIQUE);
        result.setSolution(x);
        
        // Solusi akhir
        result.addStep("\n=== SOLUSI AKHIR ===");
        StringBuilder finalSolution = new StringBuilder();
        for (int i = 0; i < n; i++) {
            finalSolution.append(String.format("x%d = %.6f\n", i + 1, x[i]));
        }
        result.addStep(finalSolution.toString());
        
        return result;
    }
    
    @Override
    public String getMethodName() {
        return "Metode Matriks Balikan (Invers)";
    }
}