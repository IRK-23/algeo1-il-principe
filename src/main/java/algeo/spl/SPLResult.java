package algeo.spl;

import java.util.ArrayList;
import java.util.List;

public class SPLResult {
    public enum SolutionType {
        UNIQUE,
        NO_SOLUTION,
        INFINITE
    }
    
    private SolutionType type;
    private double[] solution;              
    private String parametricSolution;      
    private List<String> steps;            
    private String methodName;           
    
    public SPLResult() {
        steps = new ArrayList<>();
    }
    
    public void addStep(String step) {
        steps.add(step);
    }

    public SolutionType getType() {
        return type;
    }
    
    public void setType(SolutionType type) {
        this.type = type;
    }
    
    public double[] getSolution() {
        return solution;
    }
    
    public void setSolution(double[] solution) {
        this.solution = solution;
    }
    
    public String getParametricSolution() {
        return parametricSolution;
    }
    
    public void setParametricSolution(String parametricSolution) {
        this.parametricSolution = parametricSolution;
    }
    
    public List<String> getSteps() {
        return steps;
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}