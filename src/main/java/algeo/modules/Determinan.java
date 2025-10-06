package algeo.modules;
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
                //System.out.println(tempRes+" = "+matrix.get(0, j)+" * "+ek);
                hasil+=tempRes;
                //System.out.println(hasil);
            }
            return hasil;
        }
    }

    public Matrix matrixKofaktor(Matrix matrix){
        Matrix m = new Matrix(matrix.getRows(), matrix.getCols());
        for (int i=0;i<matrix.getRows();i++){
            for (int j=0;j<matrix.getCols();j++){
                m.set(i, j, detEkspansiKofaktor(kofaktor(matrix, i, j)));
                //System.out.print(matrix.get(i, j) + " ");
            }
            //System.out.println();
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

    public void multiplyRow(Matrix matrix, int row, double x){
        for (int j=0; j<matrix.getCols(); j++){
            matrix.set(row, j, matrix.get(row, j) * x);
        }
    }

    public void subtractMultipliedRow(Matrix matrix, int row1, int row2, double x){
        for (int j=0; j<matrix.getCols(); j++){
            matrix.set(row1, j, matrix.get(row1, j) - x * matrix.get(row2, j));
        }
    }

    public double detOBE(Matrix matrix){
        Matrix m = matrix.copy();
        int p = 0;

        // bikin matrix segitiga
        for (int i=0;i<m.getRows();i++){
            int a = firstZeroTotal(m,i);
            System.out.println("firstZero="+a);
            if (a>i){
                swapRow(m,a,i);
                System.out.println("swap");
                p+=1;
            }
            for (int q=0;q<m.getRows();q++){
                for (int r=0;r<m.getCols();r++){
                    System.out.print(m.get(q,r) + " ");
                }
                System.out.println();
            }
            if (a<i){
                for (int j=0;j<i;j++){
                    double x = m.get(i, j) / m.get(j, j);
                    System.out.println(x);
                    subtractMultipliedRow(m, i, j, x);
                    for (int q=0;q<m.getRows();q++){
                        for (int r=0;r<m.getCols();r++){
                            System.out.print(m.get(q,r) + " ");
                        }
                        System.out.println();
                    }
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

    // public static void main(String[] args){
    // //     Scanner inp = new Scanner(System.in);
    // //     int rows = inp.nextInt();
    // //     int cols = inp.nextInt();
    // //     double[][] matrix = new double[rows][cols];
    //     double[][] matrix = {{1,2,3},{2,5,3},{1,0,8}};//new double[rows][cols];
    //     int rows = matrix.length;
    //     int cols = matrix.length;
        
    //     // for(int i=0;i<rows;i++){
    //     //     for(int j=0;j<cols;j++){
    //     //         String token = inp.next();
    //     //         if (token.contains("/")) {
    //     //             String[] parts = token.split("/");
    //     //             double numerator = Double.parseDouble(parts[0]);
    //     //             double denominator = Double.parseDouble(parts[1]);
    //     //             matrix[i][j] = numerator / denominator;
    //     //         } else {
    //     //             matrix[i][j] = Double.parseDouble(token);
    //     //         }
    //     //     }
    //     // }

    //     Determinan d = new Determinan();

    //     double[][] newm = new double[rows][cols];
    //     newm = d.inversAugment(matrix);
    //     // for(int i=0;i<rows;i++){
    //     //     for(int j=0;j<cols;j++){
    //     //         System.out.print(newm[i][j]+" ");
    //     //     }
    //     //     System.out.println();
    //     // }
    //     // double det = d.detEkspansiKofaktor(matrix);
    //     // System.out.println("ini det "+det);

    // //     //System.out.println(d.detEkspansiKofaktor(matrix));
        
    // //     //System.out.println(d.kofaktor(matrix,1,1));
    // }
}
