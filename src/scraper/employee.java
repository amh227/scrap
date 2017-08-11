package scraper;


public class employee {
    boolean original=true;
    String onPage="No";
    String contactID="000";
    String first="first";
    String last="last";
    String title="title";
    int IndexNameToTitle=-99;
    
    public void printEmployeeShort(){
        System.out.println("---"+this.first+" "+this.last+"  Title: "+this.title+" ");
        }

    
    public void printEmployee(){
        String found;
        if (this.onPage.equalsIgnoreCase("Yes")) {found="--FOUND--";}
        else                                {found="NOT Found";}
        System.out.println(" "+found+"  "+this.first+" "+this.last+"  Title: "+this.title+" "+this.contactID);
    }
    
    public void printEmployeeLong(){
        String orig;
        if(this.original){   orig="Original "; }
        else{                orig="Added    ";}
        String found;
        if (this.onPage.equalsIgnoreCase("Yes")) {found="--FOUND--";}
        else                                {found="NOT Found";}
        System.out.println(this.first+" "+this.last+"\tTitle: "+this.title+" "+orig+" "+found+" Index name to title:"+this.IndexNameToTitle);
        
    }
}
