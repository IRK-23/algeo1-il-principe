package invers;
import matrix.Matrix;
import determinan.*;

public class InversResult {
    private Matrix matrix;
    private StringBuilder steps;
    
    public InversResult(Matrix matrix, StringBuilder steps){
        this.matrix = matrix;
        this.steps = steps;
    }
    
    public Matrix getMatrix(){
        return matrix;
    }
    
    public String getSteps(){
        return steps.toString();
    }

    public void saveToFile(String filename){
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(filename))){
            writer.print(steps.toString());
            System.out.println("Berhasil menyimpan langkah-langkah ke " + filename);
        } 
        catch (java.io.IOException e){
            System.err.println("Error menyimpan file: " + e.getMessage());
        }
    }
    
    public void printSteps(){
        System.out.println(steps.toString());
    }
}
