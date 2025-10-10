package spl;

import matrix.Matrix;

//Interface solver Sistem Persamaan Linier
public interface SPLSolver {
    
    // Menyelesaikan sistem persamaan linier Ax = b
    // parameter augmentedMatrix Matriks augmented [A|b]
    // return Hasil penyelesaian SPL
    SPLResult solve(Matrix augmentedMatrix);
    
     // Mendapatkan nama metode solver
     // return Nama metode
    
    String getMethodName();
}