package scraper;


class employee {
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
