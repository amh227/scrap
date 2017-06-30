package scraper;

import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author angie
 */
public class comp {
    int numEmployees=0;
    String accountID;
    String name;
    String locationID;
    String URL;
    employee[] list= new employee[100];
    public void addEmployee(String contactID, String first, String last, String title){
        this.numEmployees++;
        employee temp=new employee();
        this.list[this.numEmployees]=temp;
        this.list[this.numEmployees].contactID=contactID;
        this.list[this.numEmployees].first=first;
        this.list[this.numEmployees].last=last;
        this.list[this.numEmployees].title=title;
   }
   public void printCompany(){
       System.out.println("Name: "+this.name+"     Account ID: "+this.accountID+"    \nlocation ID: "+this.locationID+"    URL: "+this.URL);
       int i=0;
       for (i=0;i<this.numEmployees;i++){
           System.out.println("numEmployee-  "+i);
           this.list[i].printEmployee();
       }
   }
  
}
