package determinan;
import matrix.Matrix;
import java.lang.Math;

public class Determinan { 
    public double detM2x2(Matrix matrix){
        return (matrix.get(0,0) * matrix.get(1,1)) - (matrix.get(0,1) * matrix.get(1,0));
    }

    public Matrix minor(Matrix matrix, int rows, int cols){
        Matrix m = new Matrix(matrix.getRows()-1, matrix.getCols()-1);
        int k=0;
        for (int i=0;i<matrix.getRows();i++){
            int l=0;
            if (i!=rows){
                for (int j=0;j<matrix.getCols();j++){
                    if (j!=cols){
                        m.set(k, l, matrix.get(i, j));
                        l++;
                    }
                }
                k++;
            }
        }
        return m;
    }
    // tambahin kalo elemen yg diambil 0
    public double rekursiEkspansiKofaktor(Matrix matrix, StringBuilder steps){
        if (matrix.getRows() == 2){
            steps.append("det(2x2) = (").append(matrix.get(0, 0)).append(" * ").append(matrix.get(1, 1)).append(") - (").append(matrix.get(0, 1)).append(" * ").append(matrix.get(1, 0)).append(") = ").append(detM2x2(matrix)).append("\n");
            return detM2x2(matrix);
        }

        double hasil=0;
        steps.append("Ekspansi Matriks ").append(matrix.getRows()).append("x").append(matrix.getRows()).append("\n");
        for(int j=0;j<matrix.getCols();j++){
            double sign;
            if (j % 2 == 0){
                sign = 1;
            }
            else{
                sign = -1;
            }
            double koefisien = matrix.get(0, j);
            Matrix minorr = minor(matrix, 0, j);

            steps.append("Koefisien a1").append(j + 1).append(" = ").append(koefisien).append(" * Tanda ").append(sign).append("\n");
            steps.append("Minor M1").append(j + 1).append(":\n");
            steps.append(minorr.matrixToString(minorr)).append("\n");

            double detMinor = rekursiEkspansiKofaktor(minorr,steps);
            double temp = sign * koefisien * detMinor;
            hasil+=temp;

            steps.append("Kontribusi Kolom ").append(j + 1).append(" = ").append(koefisien).append(" * (").append(sign).append(") * (").append(detMinor).append(") = ").append(temp).append("\n\n");
        }
        steps.append("Determinan ").append(matrix.getRows()).append("x").append(matrix.getRows()).append(" akhir = ").append(hasil).append("\n");
        return hasil;
    }

    public DeterminanResult detEkspansiKofaktor(Matrix matrix){
        StringBuilder steps = new StringBuilder();
        
        steps.append("Menghitung Determinan dengan Ekspansi Kofaktor pada Baris Pertama\n");
        steps.append("Matriks Awal:\n");
        steps.append(matrix.matrixToString(matrix)).append("\n\n");
        
        double detValue = rekursiEkspansiKofaktor(matrix, steps);
        
        return new DeterminanResult(detValue, steps);
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
            if (j==matrix.getCols()-1){
                return n;
            }
        }
        return 0;
    }

    public DeterminanResult detOBE(Matrix matrix){
        StringBuilder steps = new StringBuilder();
        Matrix m = matrix.copy();
        int p = 0;

        steps.append("Matriks Awal:\n");
        steps.append(m.matrixToString(m)).append("\n\n");

        // bikin matrix segitiga
        for (int i=0;i<m.getRows();i++){
            do{
                int a = firstZeroTotal(m,i);
            
                if (a==m.getRows()){
                    steps.append("Terdapat baris yang semuanya berniali 0 sehingga salah satu diagonal utama akan bernilai 0,\n").append("maka Determinan = (-1)^p a'_11 ... a'_nn = 0\n");
                    return new DeterminanResult(0, steps);
                }

                if (m.get(i,i)==0){
                    int b = firstZeroTotal(m, a);
                    if (a>b){
                        steps.append("Tukar baris ").append(i+1).append(" dengan baris ").append(a+1).append("\n");
                        swapRow(m,i,a);
                        steps.append(m.matrixToString(m)).append("\n\n");
                        p++;
                    }
                    else{
                        steps.append("Terdapat nilai 0 pada diagonal utama sehingga Determinan = (-1)^p a'_11 ... a'_nn = 0\n");
                        return new DeterminanResult(0, steps);
                    }
                }
    
                if (a<i){
                    for (int j=0;j<i;j++){
                        if (m.get(i,j)!=0){
                            double x = m.get(i, j) / m.get(j, j);
                            steps.append("Baris ").append(i+1).append(" = Baris ").append(i+1).append(" - (").append(x).append(") * Baris ").append(j+1).append("\n");
                            m.subtractMultipliedRow(m, i, j, x);
                            steps.append(m.matrixToString(m)).append("\n\n");
                        }
                    }
                }
            } while (firstZeroTotal(m,i)!=i);
        }

        steps.append("Didapatkan Matriks Segitiga Atas:\n");
        steps.append(m.matrixToString(m)).append("\n\n");
        steps.append("Jumlah pertukaran baris: ").append(p).append("\n");

        // hitung determinan
        double det = Math.pow(-1,p);
        steps.append("Determinan = (-1)^").append(p);

        for (int i=0; i<m.getRows(); i++){
            det *= m.get(i, i);
            steps.append(" * ").append(m.get(i,i));
        }
        steps.append(" = ").append(det);
        return new DeterminanResult(det, steps);
    }
}
