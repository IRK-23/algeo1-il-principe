package invers;
import matrix.Matrix;
import determinan.Determinan;

public class Invers {
    public Matrix matrixKofaktor(Matrix matrix){
        Matrix m = new Matrix(matrix.getRows(), matrix.getCols());
        for (int i=0;i<matrix.getRows();i++){
            for (int j=0;j<matrix.getCols();j++){
                m.set(i, j, detEkspansiKofaktor(kofaktor(matrix, i, j)));
            }
        }
        return m;
    }

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

    public InversResult inversAugment(Matrix matrix){
        StringBuilder steps = new StringBuilder();
        Matrix m = new Matrix(matrix.getRows(), 2*matrix.getCols());
        Matrix invers = new Matrix(matrix.getRows(), matrix.getCols());

        steps.append("--- MENGHITUNG INVERS DENGAN METODE AUGMENT ---\n");

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

        steps.append("Matriks [M|I]:\n");
        steps.append(m.toString()).append("\n\n");

        // OBE fase maju
        for (int i=0;i<m.getRows();i++){
            int a = firstZeroTotal(m,i);
            if (a>i){
                steps.append("Tukar baris ").append(i+1).append(" dengan baris ").append(a+1).append("\n");
                swapRow(m,a,i);
                steps.append(m.toString()).append("\n\n");
            }

            else if (a<i){
                for (int j=0;j<i;j++){
                    double x = m.get(i,j)/m.get(j,j);
                    steps.append("Baris ").append(i+1).append(" = Baris ").append(i+1).append(" - (").append(String.format("%.4f", x)).append(") * Baris ").append(j+1).append("\n");
                    subtractMultipliedRow(m,i,j,x);
                    steps.append(m.toString()).append("\n\n");
                }
            }

            if (m.get(i,i)!=1){
                double x = 1/m.get(i,i);
                steps.append("Baris ").append(i+1).append(" = Baris ").append(i+1).append(" * ").append(String.format("%.4f", x));
                multiplyRow(m,i,x);
                steps.append(m.toString()).append("\n\n");
            }
        }

        // OBE fase mundur
        for (int i=0;i<m.getRows();i++){
            for(int j=i+1;j<m.getRows();j++){
                double x = m.get(i,j)/m.get(j,j);
                steps.append("Baris ").append(i+1).append(" = Baris ").append(i+1).append(" - (").append(String.format("%.4f", x)).append(") * Baris ").append(j+1).append("\n");
                subtractMultipliedRow(m,i,j,x);
                steps.append(m.toString()).append("\n\n");
            }
        }

        // taroh inversnya
        for (int i=0;i<invers.getRows();i++){
            for (int j=0;j<invers.getCols();j++){
                invers.set(i,j,m.get(i,m.getRows()+j));
            }
        }

        steps.append("Didapatkan Matriks Invers:\n");
        steps.append(invers.toString()).append("\n\n");
        return new InversResult(invers, steps);
    }
    
}
