package matrix;

public class Matrix {
    private double[][] data;
    private int rows;
    private int cols;
    
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }
    
    public double get(int i, int j) {
        return data[i][j];
    }
    
    public void set(int i, int j, double value) {
        data[i][j] = value;
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getCols() {
        return cols;
    }
    
    public double[][] getData() {
        return data;
    }
    
    public Matrix copy() {
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.set(i, j, this.get(i, j));
            }
        }
        return result;
    }
    
    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("%.2f ", data[i][j]);
            }
            System.out.println();
        }
    }
    
    public Matrix getSubMatrix(int startRow, int endRow, int startCol, int endCol) {
        Matrix sub = new Matrix(endRow - startRow + 1, endCol - startCol + 1);
        for (int i = startRow; i <= endRow; i++) {
            for (int j = startCol; j <= endCol; j++) {
                sub.set(i - startRow, j - startCol, this.get(i, j));
            }
        }
        return sub;
    }
}