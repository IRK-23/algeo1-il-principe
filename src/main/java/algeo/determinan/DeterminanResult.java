package algeo.determinan;


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
    
    public void printSteps(){
        System.out.println(steps.toString());
    }
}
