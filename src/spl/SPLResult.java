package spl;

import java.util.ArrayList;
import java.util.List;

//Class untuk menyimpan hasil penyelesaian SPL
public class SPLResult {
    //Tipe solusi SPL
    public enum SolutionType {
        UNIQUE,
        NO_SOLUTION,
        INFINITE
    }
    
    private SolutionType type;
    private double[] solution;              // Solusi tunggal
    private String parametricSolution;      // Solusi parametrik
    private List<String> steps;             // Langkah-langkah penyelesaian
    private String methodName;              // Metode yang digunakan
    
    public SPLResult() {
        steps = new ArrayList<>();
    }
    
    public void addStep(String step) {
        steps.add(step);
    }
    
    // Getters dan Setters
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