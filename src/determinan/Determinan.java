package determinan;
import matrix.Matrix;
import java.lang.Math;

public class Determinan { 
    public double detM2x2(Matrix matrix){
        return (matrix.get(0,0) * matrix.get(1,1)) - (matrix.get(0,1) * matrix.get(1,0));
    }

    public Matrix kofaktor(Matrix matrix, int rows, int cols){
        Matrix m = new Matrix(matrix.getRows()-1, matrix.getCols()-1);
        int k=0;
        for (int i=0;i<matrix.getRows();i++){
            int l=0;
            if (i!=rows){
                for (int j=0;j<matrix.getCols();j++){
                    if (j!=cols){
                        if ((i+j)%2==1){
                            m.set(k, l, -1 * matrix.get(i, j));
                        }
                        else{
                            m.set(k, l, matrix.get(i, j));
                        }
                        l++;
                    }
                }
                k++;
            }
        }
        return m;
    }

    public double detEkspansiKofaktor(Matrix matrix){
        if (matrix.getRows() == 2){
            return detM2x2(matrix);
        }
        else{
            double hasil=0;
            for(int j=0;j<matrix.getCols();j++){
                double ek = detEkspansiKofaktor(kofaktor(matrix,0,j));
                double tempRes = matrix.get(0, j) * ek;
                hasil+=tempRes;
            }
            return hasil;
        }
    }

    public void swapRow(Matrix matrix, int row1, int row2){
        if (row1!=row2){
            for (int j=0;j<matrix.getCols();j++){
                double temp = matrix.get(row1, j);
                matrix.set(row1, j, matrix.get(row2, j));
                matrix.set(row2, j, temp);
            }
        }
    }

    public int firstZeroTotal(Matrix matrix, int row){
        int n=0;
        for (int j=0;j<matrix.getCols();j++){
            if (matrix.get(row, j)!=0){
                return n;
            }
            else{
                n+=1;
            }
        }
        return 0;
    }

    public DeterminanResult detOBE(Matrix matrix){
        StringBuilder steps = new StringBuilder();
        Matrix m = matrix.copy();
        int p = 0;

        steps.append("--- MENGHITUNG DETERMINAN DENGAN METODE REDUKSI BARIS (OBE) ---\n");
        steps.append("Matriks Awal:\n");
        steps.append(m.toString()).append("\n\n");

        // bikin matrix segitiga
        for (int i=0;i<m.getRows();i++){
            int a = firstZeroTotal(m,i);
            if (a>i){
                steps.append("Tukar baris ").append(i+1).append(" dengan baris ").append(a+1).append("\n");
                swapRow(m,a,i);
                steps.append(m.toString()).append("\n\n");
                p++;
            }
  
            else if (a<i){
                for (int j=0;j<i;j++){
                    double x = m.get(i, j) / m.get(j, j);
                    steps.append("Baris ").append(i+1).append(" = Baris ").append(i+1).append(" - (").append(String.format("%.4f", x)).append(") * Baris ").append(j+1).append("\n");
                    subtractMultipliedRow(m, i, j, x);
                    steps.append(m.toString()).append("\n\n");
                }
            }
        }

        steps.append("Didapatkan Matriks Segitiga Bawah:\n");
        steps.append(m.toString()).append("\n\n");
        steps.append("Jumlah pertukaran baris: ").append(p).append("\n");

        // hitung determinan
        double det = Math.pow(-1,p);
        steps.append("Determinan = (-1)^").append(p);

        for (int i=0; i<m.getRows(); i++){
            det *= m.get(i, i);
            steps.append(" * ").append(m.get(i,i));
        }
        steps.append(" = ").append(det);
        return new DeterminantResult(det, steps);
    }
}
