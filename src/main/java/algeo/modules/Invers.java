package algeo.modules;

public class Invers {
    public double[][] matrixTranpos(double[][] matrix){
        double[][] m = new double[matrix.length][matrix.length];
        for (int i=0;i<matrix.length;i++){
            for (int j=0;j<matrix.length;j++){
                m[j][i] = matrix[i][j];
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
                //System.out.print(m[i][j] + " ");
            }
            //System.out.println();
        }
        return m;
    }
}
