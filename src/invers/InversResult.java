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
    
    public void printSteps(){
        System.out.println(steps.toString());
    }
} 
