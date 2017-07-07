package scraper;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author angie
 */
class employee {
    String contactID;
    String first;
    String last;
    String title;
    public void printEmployee(){
        System.out.println("-----"+this.first+" "+this.last+"  Title: "+this.title);
    }
}
