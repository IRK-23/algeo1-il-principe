package invers;
import matrix.Matrix;

public class Invers {
    public Matrix adjoin(Matrix matrix){
        return matrixTranpos(matrixKofaktor(matrix));
    }

    public Matrix inversAdjoin(Matrix matrix){ 
        Matrix m = new Matrix(matrix.getRows(), matrix.getCols());
        Matrix adj = adjoin(matrix);
        double det = detEkspansiKofaktor(matrix);
        for (int i=0;i<m.getRows();i++){
            for (int j=0;j<m.getCols();j++){
                m.set(i,j,adj.get(i,j)/det);
            }
        }
        return m;
    }

    public Matrix inversAugment(Matrix matrix){
        Matrix m = new Matrix(matrix.getRows(), 2*matrix.getCols());
        Matrix invers = new Matrix(matrix.getRows(), matrix.getCols());

        // bikin matrix augment
        for (int i=0;i<m.getRows();i++){
            for (int j=0;j<m.getCols();j++){
                if (j<m.getRows()){
                    m.set(i,j,matrix.get(i,j));
                }
                else{
                    if (j-m.getRows()==i){
                        m.set(i,j,1);
                    }
                    else{
                        m.set(i,j,0);
                    }
                }
            }
        }

        // OBE fase maju
        for (int i=0;i<m.getRows();i++){
            int a = firstZeroTotal(m,i);
            if (a>i){
                swapRow(m,a,i);
            }

            else if (a<i){
                for (int j=0;j<i;j++){
                    double x = m.get(i,j)/m.get(j,j);
                    subtractMultipliedRow(m,i,j,x);
                }
            }

            if (m.get(i,i)!=1){
                double x = 1/m.get(i,i);
                multiplyRow(m,i,x);
            }
        }

        // OBE fase mundur
        for (int i=0;i<m.getRows();i++){
            for(int j=i+1;j<m.getRows();j++){
                double x = m.get(i,j)/m.get(j,j);
                subtractMultipliedRow(m,i,j,x);
            }
        }

        // taroh inversnya
        for (int i=0;i<invers.getRows();i++){
            for (int j=0;j<invers.getCols();j++){
                invers.set(i,j,m.get(i,m.getRows()+j));
            }
        }
        return m;
    }
    
}
