package scraper;

public class comp {
    int numEmployeesOnPage=0;
    int numEmployeesAdded=0;
    int numEmployees=0;
    int indexNameToTitle=-99;
    
    String parent;
    String parentURL;
    String accountID;
    String name;
    String locationID;
    String URL;
    employee[] list= new employee[100];
    
    
    public void addEmployee(String contactID, String first, String last, String title){
        employee temp=new employee();
        this.list[this.numEmployees]=temp;
        this.list[this.numEmployees].contactID=contactID;
        this.list[this.numEmployees].first=first;
        this.list[this.numEmployees].last=last;
        this.list[this.numEmployees].title=title;
        this.numEmployees++;
   }
   
    
    public void printCompany(){
       System.out.println("Name: "+this.name+"     Account ID: "+this.accountID+"    \nlocation ID: "+this.locationID+"    URL: "+this.URL);
       int i=0;
       for (i=0;i<this.numEmployees;i++){
           System.out.print(i+".");
           this.list[i].printEmployee();
       }
    }
    public void printCompanyShort(){
        System.out.println("\nName: "+this.name+"     Account ID: "+this.accountID+"    \nlocation ID: "+this.locationID+"    URL: "+this.URL);
       int i=0;
       for (i=0;i<this.numEmployees;i++){
           System.out.print(i+".");
           this.list[i].printEmployeeShort();
       
        
    }   
   }
   
    
    public void printAllEmployees(comp c){
       System.out.println("Company: "+c.name);
       for(int i=0;i<c.numEmployees;i++){
           if(c.list[i].onPage.equalsIgnoreCase("No")){
                System.out.print("-NOT FOUND-");
           }
           System.out.print(" "+c.list[i].first+" "+c.list[i].last+": "+c.list[i].title+" ");
       }
       
       
       
   }
  
}
