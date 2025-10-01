package algeo.modules;

public class Determinan {
    // public double[][] matrixTranpos(double[][] matrix){
    //     double[][] m = new double[matrix.length][matrix.length];
    //     for (int i=0;i<matrix.length;i++){
    //         for (int j=0;j<matrix.length;j++){
    //             m[j][i] = matrix[i][j];
    //             System.out.print(m[i][j] + " ");
    //         }
    //         System.out.println();
    //     }
    //     return m;
    // }
    
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

    // public double[][] adjoin(double[][] matrix){
    //     return matrixTranpos(matrixKofaktor(matrix));
    // }

    // public double[][] inversAdjoin(double[][] matrix){ 
    //     double[][] m = new double[matrix.length][matrix.length];
    //     double[][] adj = adjoin(matrix);
    //     double det = detEkspansiKofaktor(matrix);
    //     for (int i=0;i<matrix.length;i++){
    //         for (int j=0;j<matrix.length;j++){
    //             m[i][j] = adj[i][j]/det;
    //             System.out.print(m[i][j] + " ");
    //         }
    //         System.out.println();
    //     }
    //     return m;
    // }

    // public static void main(String[] args){
    //     //Scanner inp = new Scanner(System.in);
    //     //int rows = inp.nextInt();
    //     //int cols = inp.nextInt();
    //     double[][] matrix = {{3,2,-1},{1,6,3},{2,-4,0}};//new double[rows][cols];
    //     int rows = matrix.length;
    //     int cols = matrix.length;
    //     /* 
    //     for(int i=0;i<rows;i++){
    //         for(int j=0;j<cols;j++){
    //             matrix[i][j] = inp.nextDouble();
    //         }
    //     }*/

    //     Determinan d = new Determinan();

    //     // double[][] newm = new double[rows][cols];
    //     // newm = d.inversAdjoin(matrix);
    //     // for(int i=0;i<rows;i++){
    //     //     for(int j=0;j<cols;j++){
    //     //         System.out.print(newm[i][j]+" ");
    //     //     }
    //     //     System.out.println();
    //     // }

    //     //System.out.println(d.detEkspansiKofaktor(matrix));
        
    //     //System.out.println(d.kofaktor(matrix,1,1));
    // }
}
