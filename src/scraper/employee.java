package scraper;


class employee {
    boolean original;
    String onPage;
    String updatedURL;
    String contactID;
    String first;
    String last;
    String title;
    String updatedTitle;
    
    public void printEmployee(){
        System.out.println("-----"+this.first+" "+this.last+"  Title: "+this.title);
    }
}
