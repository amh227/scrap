package scraper;

import java.util.Scanner;

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
    String updatedURL="  ";
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
    public void editEmployee(){
        this.printAllEmployees();
        System.out.println("Please enter the number of the employee you would like to edit:\n");
        Scanner input = new Scanner(System.in);
        int employeeNum=input.nextInt();
        //add catch for not equal to number les than number of employees-1
        this.list[employeeNum].printEmployee();
        //System.out.println("Verify that this is the correct Employee to edit (0 to exit /1 to continue)");
        System.out.println("Enter number for one of the following:");
        System.out.println(""
                        + "\n1. Edit first name"
                        + "\n2. Edit last name"
                        + "\n3. Edit title"
                        + "\n4. Edit OnPage"
                        + "\n5. Edit original"
                        + "\n6. Edit ContactID"
                        + "\n7. Delete"
                        + "\n8. Exit");
                
        String userInput=input.next();

        if (userInput.equalsIgnoreCase("1")){
            System.out.println("Please Enter first name to replace "+this.list[employeeNum].first);
            userInput=input.next();
            this.list[employeeNum].first=userInput;
            return;
        }
        if (userInput.equalsIgnoreCase("2")){
            System.out.println("Please Enter last name to replace "+this.list[employeeNum].last);
            userInput=input.next();
            this.list[employeeNum].last=userInput;
            return;
        }
        if (userInput.equalsIgnoreCase("3")){
            System.out.println("Please Enter title to replace "+this.list[employeeNum].title);
            userInput=input.next();
            this.list[employeeNum].title=userInput;
            return;
        }
        if (userInput.equalsIgnoreCase("4")){
            System.out.println("Please Enter onPage resule to replace "+this.list[employeeNum].onPage);
            userInput=input.next();
            this.list[employeeNum].onPage=userInput;
            return;
        }
        if (userInput.equalsIgnoreCase("5")){
            System.out.println("Reversing Original boolean value");
            if (this.list[employeeNum].original==true){
                this.list[employeeNum].original=false;} 
            else{
                this.list[employeeNum].original=true;}
            return;
        }
        if (userInput.equalsIgnoreCase("6")){
            System.out.println("Please Enter contactID to replace "+this.list[employeeNum].contactID);
            userInput=input.next();
            this.list[employeeNum].contactID=userInput;
            return;
        }
        if (userInput.equalsIgnoreCase("7")){
            this.removeEmployee(employeeNum);
            return;
        }
        else{return;}
        
   
   
   }
    public void printCompany(){
       System.out.println("Name: "+this.name+"     Account ID: "+this.accountID+"    \nlocation ID: "+this.locationID+"    URL: "+this.URL);
       System.out.println("Found Listed Employees : "+this.numEmployeesOnPage);
       System.out.println("Added Employees        : "+this.numEmployeesAdded);
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
    public void printAllEmployees(){
       System.out.println("Company: "+this.name);
       for(int i=0;i<this.numEmployees;i++){
           System.out.println("   ");
           if(this.list[i].onPage.equalsIgnoreCase("No")){
                System.out.print("-NOT FOUND-");
           }
           System.out.print(i+". "+this.list[i].first+" "+this.list[i].last+": "+this.list[i].title+" ");
       }
    }
    private void removeEmployee(int employeeNum) {
        if (this.list[employeeNum].original==true){     this.numEmployeesOnPage--;                }
        else{                                           this.numEmployeesAdded--;                 }
        for (int i=employeeNum;i<this.numEmployees;i++){
            this.list[i]=this.list[i+1];
        }
        this.numEmployees--;
        
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
  
}
