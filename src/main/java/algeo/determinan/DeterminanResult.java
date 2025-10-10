package algeo.determinan;
import algeo.matrix.Matrix;

public class DeterminanResult{
    private double value;
    private StringBuilder steps;
    
    public DeterminanResult(double value, StringBuilder steps){
        this.value = value;
        this.steps = steps;
    }
    
    public double getValue(){
        return value;
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
