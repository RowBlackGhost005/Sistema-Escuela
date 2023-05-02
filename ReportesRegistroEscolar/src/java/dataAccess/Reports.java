package dataAccess;

import java.util.ArrayList;

public class Reports {
    
    private static ArrayList<String> reports = new ArrayList<String>();
    
    public Reports(){
    }
    
    public void addReport(String report){
        this.reports.add(report);
    }
    
    public ArrayList<String> getReports(){
        return reports;
    }
    
    public String getReport(int pos){
        if(reports.size() <= pos){
            return null;
        }
        
        return reports.get(pos);
    }
}
