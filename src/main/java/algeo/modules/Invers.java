package algeo.modules;

public class Invers {
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
    
}
