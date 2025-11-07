package algeo.invers;
import algeo.matrix.Matrix;
import algeo.determinan.*;

public class Invers {
    private Determinan det = new Determinan();

    public Matrix matrixKofaktor(Matrix matrix){
        Matrix m = new Matrix(matrix.getRows(), matrix.getCols());
        for (int i=0;i<matrix.getRows();i++){
            for (int j=0;j<matrix.getCols();j++){
                int sign;
                if ((i+j)%2==1){
                    sign = -1;
                }
                else{
                    sign = 1;
                }
                m.set(i, j, sign * det.detOBE(det.minor(matrix, i, j)).getValue());
            }
        }
        return m;
    }

    public Matrix adjoin(Matrix matrix){
        Matrix m = new Matrix(matrix.getRows(), matrix.getCols());
        return m.matrixTranpos(matrixKofaktor(matrix));
    }

    public InversResult inversAdjoin(Matrix matrix){ 
        StringBuilder steps = new StringBuilder();

        steps.append("Hitung determinan matriks\n");
        DeterminanResult detResult = det.detOBE(matrix); 
        double determinan = detResult.getValue();
        
        steps.append("Determinan det(A) = ").append(String.format("%.6f", determinan)).append("\n");
        
        if (determinan == 0) {
            steps.append("Determinan nol. Matriks tidak memiliki invers.\n");
            return new InversResult(null, steps);
        }

        steps.append("Hitung matriks kofaktor\n");
        Matrix kof = matrixKofaktor(matrix); 
        steps.append("Hasil matriks kofaktor:\n").append(kof.matrixToString(kof)).append("\n");

        steps.append("Hitung matriks adjoin (transpos matriks kofaktor)\n");
        Matrix adj = adjoin(matrix);
        steps.append("Hasil matriks adjoin:\n").append(adj.matrixToString(adj)).append("\n");

        steps.append("Matriks invers = (1/det(A)) * Adj(A))\n");
        
        Matrix m = new Matrix(matrix.getRows(), matrix.getCols());
        for (int i=0;i<m.getRows();i++){
            for (int j=0;j<m.getCols();j++){
                m.set(i,j,adj.get(i,j)/determinan);
            }
        }

        steps.append("Hasil matriks invers:\n").append(m.matrixToString(m)).append("\n");
        return new InversResult(m, steps); 
    }

    public InversResult inversAugment(Matrix matrix){
        StringBuilder steps = new StringBuilder();
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

        steps.append("Matriks [A|I]:\n");
        steps.append(m.matrixToString(m)).append("\n\n");

        // fase maju
        for (int i=0;i<m.getRows();i++){
            int maxRow = i;
            double maxVal = Math.abs(m.get(i,i));
            for (int k=i+1;k<m.getRows();k++){
                if (Math.abs(m.get(k,i)) > maxVal){
                    maxVal = Math.abs(m.get(k,i));
                    maxRow = k;
                }
            }
            if (maxRow != i){
                steps.append("Tukar baris ").append(i+1).append(" dengan baris ").append(maxRow+1).append("\n");
                det.swapRow(m,i,maxRow);
                steps.append(m.matrixToString(m)).append("\n\n");
            }

            if (m.get(i,i)==0){
                steps.append("Pivot 0, matriks tidak memiliki invers.\n");
                return new InversResult(null, steps);
            }

            for (int j=i+1;j<m.getRows();j++){
                double x = m.get(j,i) / m.get(i,i);
                if (x!=0){
                    steps.append("Baris ").append(j+1).append(" = Baris ").append(j+1).append(" - (").append(String.format("%.4f",x)).append(") * Baris ").append(i+1).append("\n");
                    m.subtractMultipliedRow(m,j,i,x);
                    steps.append(m.matrixToString(m)).append("\n\n");
                }
            }
        }

        // fase mundur
        for (int i=m.getRows()-1;i>=0;i--){
            for (int j=i-1;j>=0;j--){
                double x = m.get(j,i) / m.get(i,i);
                if (x!=0){
                    steps.append("Baris ").append(j+1).append(" = Baris ").append(j+1).append(" - (").append(String.format("%.4f",x)).append(") * Baris ").append(i+1).append("\n");
                    m.subtractMultipliedRow(m,j,i,x);
                    steps.append(m.matrixToString(m)).append("\n\n");
                }
            }
        }

        // taroh inversnya
        for (int i=0;i<invers.getRows();i++){
            for (int j=0;j<invers.getCols();j++){
                invers.set(i,j,m.get(i,m.getRows()+j)/m.get(i,i));
            }
        }

        steps.append("Hasil Invers Matriks:\n");
        steps.append(invers.matrixToString(invers)).append("\n\n");
        return new InversResult(invers, steps);
    }
    
}
