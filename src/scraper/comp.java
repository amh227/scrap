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
    int numEmployees;
    String name;
    String locationID;
    String ContactID;
    String URL;
    employee[] list= new employee[100];
    public void addEmployee(String first, String last, String title){
        this.numEmployees++;
        this.list[this.numEmployees].first=first;
        this.list[this.numEmployees].last=last;
        this.list[this.numEmployees].title=title;
   }
  
}
