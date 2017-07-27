package scraper;


public class employee {
    boolean original=true;
    String onPage="No";
    String updatedURL=" ";
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
        if (onPage.equalsIgnoreCase("Yes")) {found="--FOUND--";}
        else                                {found="NOT Found";}
        System.out.println(" "+found+"  "+this.first+" "+this.last+"  Title: "+this.title+" ");
    }
}
