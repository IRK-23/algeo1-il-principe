package determinan;
import matrix.Matrix;

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

    public Matrix matrixKofaktor(Matrix matrix){
        Matrix m = new Matrix(matrix.getRows(), matrix.getCols());
        for (int i=0;i<matrix.getRows();i++){
            for (int j=0;j<matrix.getCols();j++){
                m.set(i, j, detEkspansiKofaktor(kofaktor(matrix, i, j)));
            }
        }
        return m;
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

    public double detOBE(Matrix matrix){
        Matrix m = matrix.copy();
        int p = 0;

        // bikin matrix segitiga
        for (int i=0;i<m.getRows();i++){
            int a = firstZeroTotal(m,i);
            if (a>i){
                swapRow(m,a,i);
                p+=1;
            }
  
            else if (a<i){
                for (int j=0;j<i;j++){
                    double x = m.get(i, j) / m.get(j, j);
                    subtractMultipliedRow(m, i, j, x);
                }
            }
        }

        // hitung determinan
        double det;
        if (p%2==0){
            det = 1;
        }
        else{
            det = -1;
        }
        for (int i=0; i<m.getRows(); i++){
            det *= m.get(i, i);
        }
        return det;
    }
}
