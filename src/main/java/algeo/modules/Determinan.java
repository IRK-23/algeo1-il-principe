package algeo.modules;
// import java.util.Scanner;

public class Determinan {
    public double[][] matrixTranpos(double[][] matrix){
        double[][] m = new double[matrix.length][matrix.length];
        for (int i=0;i<matrix.length;i++){
            for (int j=0;j<matrix.length;j++){
                m[j][i] = matrix[i][j];
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }
        return m;
    }
    
    public double detM2x2(double[][] matrix){
        return (matrix[0][0]*matrix[1][1]) - (matrix[0][1]*matrix[1][0]);
    }

    public double[][] kofaktor(double[][] matrix, int rows, int cols){
        double[][] m = new double[matrix.length-1][matrix.length-1];
        int k=0;
        for (int i=0;i<matrix.length;i++){
            int l=0;
            if (i!=rows){
                for (int j=0;j<matrix[i].length;j++){
                    if (j!=cols){
                        if ((i+j)%2==1){
                            m[k][l] = -matrix[i][j];
                        }
                        else{
                            m[k][l] = matrix[i][j];
                        }
                        l++;
                    }
                }
                k++;
            }
        }
        return m;
    }

    public double detEkspansiKofaktor(double[][] matrix){
        if (matrix.length == 2){
            return detM2x2(matrix);
        }
        else{
            double hasil=0;
            for(int j=0;j<matrix.length;j++){
                double ek = detEkspansiKofaktor(kofaktor(matrix,0,j));
                double tempRes = matrix[0][j] * ek;
                //System.out.println(tempRes+" = "+matrix[0][j]+" * "+ek);
                hasil+=tempRes;
                //System.out.println(hasil);
            }
            return hasil;
        }
    }

    public double[][] matrixKofaktor(double[][] matrix){
        double[][] m = new double[matrix.length][matrix.length];
        for (int i=0;i<matrix.length;i++){
            for (int j=0;j<matrix.length;j++){
                m[i][j] = detEkspansiKofaktor(kofaktor(matrix, i, j));
                //System.out.print(m[i][j] + " ");
            }
            //System.out.println();
        }
        return m;
    }

    public double[][] adjoin(double[][] matrix){
        return matrixTranpos(matrixKofaktor(matrix));
    }

    public double[][] inversAdjoin(double[][] matrix){ 
        double[][] m = new double[matrix.length][matrix.length];
        double[][] adj = adjoin(matrix);
        double det = detEkspansiKofaktor(matrix);
        for (int i=0;i<matrix.length;i++){
            for (int j=0;j<matrix.length;j++){
                m[i][j] = adj[i][j]/det;
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }
        return m;
    }
    public void swapRow(double[][] matrix, int row1, int row2){
        if (row1!=row2){
            for (int j=0;j<matrix.length;j++){
                double temp =  matrix[row1][j];
                matrix[row1][j] = matrix[row2][j];
                matrix[row2][j] = temp;
            }
        }
    }

    public int firstZeroTotal(double[] matrixRow){
        int n=0;
        for (int j=0;j<matrixRow.length;j++){
            if (matrixRow[j]!=0){
                return n;
            }
            else{
                n+=1;
            }
        }
        return 0;
    }

    public void multiplyRow(double[] matrixRow, double x){
        for (int j=0;j<matrixRow.length;j++){
            matrixRow[j]*=x;
        }
    }

    public void subtractMultipliedRow(double[] matrixRow1, double[] matrixRow2, double x){
        for (int j=0;j<matrixRow1.length;j++){
            matrixRow1[j] -= x*matrixRow2[j];
        }
    }

    public double detOBE(double[][] matrix){
        double[][] m = matrix;
        int p = 0;

        // bikin matrix segitiga
        for (int i=0;i<m.length;i++){
            int a = firstZeroTotal(m[i]);
            System.out.println("firstZero="+a);
            if (a>i){
                swapRow(m,a,i);
                System.out.println("swap");
                p+=1;
            }
            for (int q=0;q<matrix.length;q++){
                for (int r=0;r<matrix.length;r++){
                    System.out.print(m[q][r] + " ");
                }
                System.out.println();
            }
            if (a<i){
                for (int j=0;j<i;j++){
                    double x = m[i][j]/m[j][j];
                    System.out.println(m[i][j] + " / "+m[j][j]+" = "+x);
                    subtractMultipliedRow(m[i], m[j],x);
                    for (int s=0;s<matrix.length;s++){
                        for (int t=0;t<matrix.length;t++){
                            System.out.print(m[s][t] + " ");
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
        for (int i=0;i<matrix.length;i++){
            det *= matrix[i][i];
        }
        return det;
    }

    public double[][] inversAugment(double[][] matrix){
        double[][] m = new double[matrix.length][2*matrix.length];
        double[][] invers = new double[matrix.length][matrix.length];

        // bikin matrix augment
        for (int i=0;i<matrix.length;i++){
            for (int j=0;j<2*matrix.length;j++){
                if (j<matrix.length){
                    m[i][j] = matrix[i][j];
                }
                else{
                    if (j-matrix.length==i){
                        m[i][j] = 1;
                    }
                    else{
                        m[i][j] = 0;
                    }
                }
            }
        }

        // OBE fase maju
        for (int i=0;i<m.length;i++){
            int a = firstZeroTotal(m[i]);
            System.out.println("firstZero="+a);
            if (a>i){
                swapRow(m,a,i);
                System.out.println("swap");
            }
            for (int q=0;q<matrix.length;q++){
                for (int r=0;r<2*matrix.length;r++){
                    System.out.print(m[q][r] + " ");
                }
                System.out.println();
            }
            if (a<i){
                for (int j=0;j<i;j++){
                    double x = m[i][j]/m[j][j];
                    System.out.println(m[i][j] + " / "+m[j][j]+" = "+x);
                    subtractMultipliedRow(m[i], m[j],x);
                    for (int s=0;s<matrix.length;s++){
                        for (int t=0;t<2*matrix.length;t++){
                            System.out.print(m[s][t] + " ");
                        }
                        System.out.println();
                    }
                }
            }
            if (m[i][i]!=1){
                double x = 1/m[i][i];
                multiplyRow(m[i],x);
            }
            for (int q=0;q<matrix.length;q++){
                for (int r=0;r<2*matrix.length;r++){
                    System.out.print(m[q][r] + " ");
                }
                System.out.println();
            }
        }

        // OBE fase mundur
        for (int i=0;i<matrix.length;i++){
            for(int j=i+1;j<matrix.length;j++){
                double x = m[i][j]/m[j][j];
                System.out.println(m[i][j] + " / "+m[j][j]+" = "+x);
                subtractMultipliedRow(m[i], m[j],x);
                for (int s=0;s<matrix.length;s++){
                    for (int t=0;t<2*matrix.length;t++){
                        System.out.print(m[s][t] + " ");
                    }
                    System.out.println();
                }
            }
        }

        // taroh inversnya
        for (int i=0;i<matrix.length;i++){
            for (int j=0;j<matrix.length;j++){
                invers[i][j] = m[i][matrix.length+j];
            }
        }

        for (int q=0;q<matrix.length;q++){
            for (int r=0;r<matrix.length;r++){
                System.out.print(invers[q][r] + " ");
            }
            System.out.println();
        }

        return m;
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
