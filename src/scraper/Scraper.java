package scraper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//---jsoup imports
//https://jsoup.org/apidocs/
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {

    
    public static void main(String[] args) throws IOException {
        comp[] companies = new comp[120];
        //Start with 100 potential arrayists for all the possible company names
        String file, outputfile, lastCompanyID = "-", tempFirst = null, tempLast = null, tempTitle = null, tempContactID, tempCompanyID;
        //String arrays for headers of all sheets
        String[][] headers = new String[26][3];
        int i, j, compCount = 0, empCount = 0, rowCount, colCount,totalEmployees=0,foundEmployees=0,pageErrors=0,foundTitle=0;
        Scanner input = new Scanner(System.in);

        if (args.length == 0) {
            System.out.println("must enter filename");
            file="Job80.xlsx";
           // file = "Job.xlsx";
        } else {
            file = args[1];
        }

        int sheetNum = 0;

        //Need to read in excel sheet and 
        XSSFRow row;
        try (FileInputStream fis = new FileInputStream(new File(file))) {
            // A workbook reference is created from the chosen Excel file.
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            // A spreadsheet object is created to reference the Excel files pages.
            XSSFSheet spreadsheet;
            // Iterator for the rows
            Iterator< Row> rowIterator;
            // Iterator for the columns
            Iterator< Cell> cellIterator;
            spreadsheet = workbook.getSheetAt(sheetNum);
            //rowIterator = spreadsheet.iterator();
//Start reading in file//
            int rowStart = Math.min(0, spreadsheet.getFirstRowNum());
            int rowEnd = Math.max(200, spreadsheet.getLastRowNum());
            int lastColumn;
            /**
             * Sheet 0---------------------------------------------------------------------------------------------------------------
             */
            sheetNum = 0;
            for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                Row r = spreadsheet.getRow(rowNum);
                if (r != null) {
                    lastColumn = r.getLastCellNum();
                } else {
                    continue;
                }
                //lastColumn = Math.max(r.getLastCellNum(),26/**MAXCOLS**/);
                String temp;
                for (int cn = 0; cn < lastColumn; cn++) {
                    Cell cell = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                    if (cell == null) {
                        temp = null;
                        //System.out.printf("null"); 
                    } else {
                        temp = cell.getStringCellValue();
                        //System.out.println(" "+temp);
                    }
                    if (rowStart == rowNum) {
                        //System.out.println(" in grid "+temp);
                        headers[cn][sheetNum] = temp;
                    }
                    //employees
                    if (rowNum > rowStart) {
                        //System.out.println("Looking for companies at  ["+rowNum+"]["+cn+"]");
                        if (cn == 1) {//cn==2 contains companyID
                            if (cell == null) { temp = null;} 
                            else {tempCompanyID = cell.getStringCellValue();//System.out.println("-"+temp);
                                if (tempCompanyID.equals(lastCompanyID)) {    //Same company as last round
                                    totalEmployees++;
                                     //7=H=contactID
                                    cell = r.getCell(7, Row.RETURN_BLANK_AS_NULL);
                                    temp = cell.getStringCellValue();
                                    tempContactID = temp;
                                    //8=I=first name
                                    cell = r.getCell(8, Row.RETURN_BLANK_AS_NULL);
                                    temp = cell.getStringCellValue();
                                    tempFirst = temp;
                                    //9=J=lastname
                                    cell = r.getCell(9, Row.RETURN_BLANK_AS_NULL); 
                                    temp = cell.getStringCellValue();
                                    tempLast = temp;
                                    //10=K=title
                                    cell = r.getCell(10, Row.RETURN_BLANK_AS_NULL); 
                                    temp = cell.getStringCellValue();
                                    tempTitle = temp;
                                    
                                    
                                    companies[compCount - 1].addEmployee(tempContactID, tempFirst, tempLast, tempTitle);
                                } else {//NEW COMPANY
                                    totalEmployees++;
                                    comp tempComp = new comp();
                                   // System.out.println("compCount="+compCount);
                                    companies[compCount] = tempComp;
                                    //0=A=employee onPage:itiallyblank
                                    cell = r.getCell(0, Row.RETURN_BLANK_AS_NULL);
                                    //temp = cell.getStringCellValue();
                                    //1=B=accountID
                                    cell = r.getCell(1, Row.RETURN_BLANK_AS_NULL);
                                    temp = cell.getStringCellValue();
                                    companies[compCount].accountID = temp;
                                    //2=C=company name
                                    cell = r.getCell(2, Row.RETURN_BLANK_AS_NULL);
                                    temp = cell.getStringCellValue();
                                    companies[compCount].name = temp;
                                    //3=D=company locationID
                                    cell = r.getCell(3, Row.RETURN_BLANK_AS_NULL);
                                    tempCompanyID = cell.getStringCellValue();
                                    companies[compCount].locationID = tempCompanyID;    // tempCompanyID 
                                    //4=E=C-Suite URL
                                    cell = r.getCell(4, Row.RETURN_BLANK_AS_NULL);
                                    temp = cell.getStringCellValue();
                                    companies[compCount].URL = temp;
                                    //5=F=Parent
                                    cell = r.getCell(6, Row.RETURN_BLANK_AS_NULL);
                                    temp = cell.getStringCellValue();
                                    companies[compCount].parent = temp;
                                    //6=G=Parent C-Suite URL
                                    cell = r.getCell(5, Row.RETURN_BLANK_AS_NULL);
                                    temp = cell.getStringCellValue();
                                    companies[compCount].parentURL=temp;
                                    //7=H=contactID
                                    cell = r.getCell(7, Row.RETURN_BLANK_AS_NULL);
                                    temp = cell.getStringCellValue();
                                    tempContactID = temp;
                                    //8=I=first name
                                    cell = r.getCell(8, Row.RETURN_BLANK_AS_NULL);
                                    temp = cell.getStringCellValue();
                                    tempFirst = temp;
                                    //9=J=lastname
                                    cell = r.getCell(9, Row.RETURN_BLANK_AS_NULL); //LastName
                                    temp = cell.getStringCellValue();
                                    tempLast = temp;
                                    //10=K=title
                                    cell = r.getCell(10, Row.RETURN_BLANK_AS_NULL); //LastName
                                    temp = cell.getStringCellValue();
                                    tempTitle = temp;
                                    //11=L=updated Title
                                    //12=M=notes
                                    //13=N=updatedC-SuiteURL
                                    companies[compCount].addEmployee(tempContactID, tempFirst, tempLast, tempTitle);
                                    lastCompanyID = companies[compCount].accountID;
                                    compCount++;
                                }
                            }
                        }
                    }
                }
            }
            /*
             * Sheet 1/ Save headers and add to company Arraylist ----------------------------------------------------------------------------
             */
            sheetNum = 1;
            spreadsheet = workbook.getSheetAt(sheetNum);
            int cn = 0;
            for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                Row r = spreadsheet.getRow(rowNum);

                if (r != null) {
                    lastColumn = r.getLastCellNum();
                } else {
                    headers[cn][sheetNum + 1] = null;
                    continue;
                }
                //lastColumn = Math.max(r.getLastCellNum(),26/**MAXCOLS**/);
                String temp;
                for (cn = 0; cn < lastColumn; cn++) {
                    Cell cell = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                    if (cell == null) {
                        temp = null;
                    } else {
                        temp = cell.getStringCellValue();
                    }
                    //headers
                    if (rowStart == rowNum) {
                        headers[cn][sheetNum] = temp;
                    }
                    if (rowStart + 1 == rowNum) {
                        headers[cn][sheetNum + 1] = temp;
                    }
                }
            }
//----------------------------------------------------------------------------------------------------------------------------------            
            //sheet #2 Schhol to Ids.ID=String <14000 ,but close    
            sheetNum = 2;
            spreadsheet = workbook.getSheetAt(sheetNum);
            cn = 0;

        } catch (IOException Error) {
            System.out.println("Error loading the file: " + file);
        }

        //copy individual headers to headers
        for (j = 0; j < 3; j++) {
            System.out.println("\t");
            for (i = 0; i < 26; i++) {
                System.out.print("   " + headers[i][j]);
            }
        }
        String url = "  ", userInput, companyName = "  ",language;
        int count = 0, found = 0;
        //-------------------------------------------------------iterating companies--------------------------------------------------
        for (i = 0; i < compCount; i++) {
            System.out.println("At Company: "+companies[i].name);
            int firstRun=0;
            found=0;
            System.out.println(i + ". :");
            companies[i].printCompany();
            url = companies[i].URL;
            
            try {
                Document doc = Jsoup.connect(url).get();
                Jsoup.connect(url).header("Accept-Language", "en");
                //get language page is in
                Element taglang = doc.select("html").first();
                language = (taglang.attr("lang"));


                //trying to iterate through all strings of document individually    
                Elements elements = doc.body().select("*");
                String[] strArr = new String[10000];
                int iterator = 0;
                
                for (Element element : elements) {
                    String s = element.ownText();
                    if (s.trim().length() > 0) {
                        strArr[iterator] = element.ownText();
                        if (firstRun==0){System.out.println("["+iterator+"]" +s);}
                        iterator++;
                    }
                }
                firstRun=1;
                String text = doc.body().text();
                //System.out.println("\n" + text + "\n");
                count = 0; //count for employeesin individual company::used to return if no employees found
//---------------------------------------------------------------------------------------<EMPLOYEE ITEREATION>------------    
                for (j = 0; j < companies[i].numEmployees; j++) {
                    companies[i].list[j]=findEmployee(strArr, iterator, companies[i].list[j] );
                    
                }
    //----------------------------------------------------------------------------<END EMPLOYEE ITERATIONS>-----------------                
                } 
                catch (org.jsoup.UnsupportedMimeTypeException| javax.net.ssl.SSLHandshakeException |java.net.SocketTimeoutException|org.jsoup.HttpStatusException UMTE) {
                    System.out.println("\n::ERROR::URL: " + url + "\nInvalid for current programming :: May be pdf formatting\n");
                    System.out.println("\nTRY DIFFERENT URL (0 to quit, 1 for manual prompt/entry )\n");
                    userInput = input.next();
                    if (userInput.compareTo("0") == 0) { 
                        found = 1;
                        pageErrors++;
                    } 
                    else{
                        if(userInput.compareTo("1") == 0){
                            companies[i]=manualEnter(companies[i]);
                            found=1;
                        }
                        else {url = userInput;}
                    }
                }
            
            //end loop for finding all employees :: check for adds
            companies[i].printCompany();
            System.out.println("Would you like to edit?");
            userInput=input.next();
            
        }
//------------------------------------------------------------ends company iteration--------------------------------------
       
        
        
        
        System.out.println("\n\n TOTAL EMPLOYEES LISTED: "+totalEmployees);
        System.out.println("FOUND EMPLOYEES: "+foundEmployees+"   with Title: "+foundTitle);
        System.out.println("PAGE ERRORS: "+pageErrors);
    }
//------------------------------------------------------------------------------------------------- <END MAIN> -----------
  
    
    /**
     * 
     * @param a
     * @param arraySize
     * @param e : employee being looked for
     * @return returns the index between the name and the title of the employee being looked for
     */
    public static employee findEmployee(String[] a, int arraySize, employee e){
        int i, startInd=0 ,ind=99, inTryCatch=0, foundName=0;
        e.IndexNameToTitle=-99;//   set to -99 to use as baseline if found in end or not
        e.onPage="No";
        e.original=true;
        String name=e.first+" "+e.last;
        Scanner input = new Scanner(System.in);
        System.out.println("\nSearching for :: "+name+" : "+e.title);
        //find first name first
        for (i=0;i<arraySize;i++){
            //System.out.println("i: "+i+"::"+a[i]);
            if (a[i].contains(e.first+" ")){//found first name
                if (a[i].contains(e.last)){//contains full name
                    //System.out.println("found full name: "+a[i]+ " at index "+i);
                    e.onPage="Yes";
                    foundName=1;
                    startInd=i;
                    i=arraySize;
                }
                else{//last name not found at index, check next index
                    if(a[i+1].contains(e.last)){//last name is in the next index
                        //System.out.println("found full name: "+a[i]+ " and "+a[i+1]+ "  at indexs "+i+ " & "+(i+1));
                        startInd=i+1;
                        e.onPage="Yes";
                        foundName=1;
                        i=arraySize;
                    }
                    else{
                        //must add catch for not the name we are looking for (Ann != Annual)
                        String temp=" ";
                        System.out.println("Found first name:"+e.first+" at ["+i+"]: \""+a[i]+"\n"
                                + "Do any of the following replace last name(\""+e.last+"\"): "+a[i]+" ; "+a[i-1]+" ; "+a[i+1]+
                                "\ntype 0 if incorrect find");
                        temp=input.next();
                        if (temp.equals("0")){
                            foundName=0;
                            //continue thru file, false positive was detected
                        }
                        else{
                            foundName=1;
                            e.onPage="Yes";
                            e.last=temp;
                            if (a[i].contains(e.last)){startInd= i;}
                            if (a[i+1].contains(e.last)){startInd= i+1;}   
                            if (a[i-1].contains(e.last)){startInd= i;}
                            i=arraySize;
                        }
                    }
                }
            }//end found first name    
        }//end for loop iteration looking for first name :: look for last        
        if (foundName==0){//did not find first name :: look for last
            for (i=0;i<arraySize;i++){
            //System.out.println("i: "+i+"::"+a[i]);
                if (a[i].contains(" "+e.last)){//found first name
                    //must add catch for not the name we are looking for (Ann != Annual)
                    String temp=" ";
                    try{
                        System.out.println("Found last name:"+e.last+" at ["+i+"]: \""+a[i]+"\n"
                                + "Do any of the following replace first name(\""+e.first+"\"): "+a[i]+" ; "+a[i-1]+" ; "+a[i+1]+
                                "\ntype 0 if incorrect find");
                        temp=input.next();
                        if (temp.equals("0")){
                            foundName=0;
                            //continue thru file, false positive was detected
                        }
                        else{
                            foundName=1;
                            e.onPage="Yes";
                            e.last=temp;
                            if (a[i].contains(e.last)){startInd= i;}
                            if (a[i+1].contains(e.last)){startInd= i+1;}   
                            if (a[i-1].contains(e.last)){startInd= i;}
                            i=arraySize;
                        }
                    }
                    catch (java.lang.ArrayIndexOutOfBoundsException aioobe){
                        
                    }
                }//end found last name    
            }//end for loop iteration looking for first name :: look for last        
        
            
        }        
                
        if (foundName==1){
                //FOUND NAME LOOK FOR TITLE
            if (a[startInd].contains(e.title)){     e.IndexNameToTitle=0;   }
            if (a[startInd+1].contains(e.title)){   e.IndexNameToTitle=1;   }    
            else{//check next index
                if (a[startInd+2].contains(e.title)){e.IndexNameToTitle=2;}
                if (a[startInd-1].contains(e.title)){e.IndexNameToTitle=-1;}
                else{
                    inTryCatch=1;
                    int tempIndex=-99;
                    while (inTryCatch==1){
                        try{
                            System.out.println("Name Found :: Title Not Found  :: Name found here: "+a[startInd]);
                            //print indices around name to give option for title
                            System.out.println("Do any of the following indices have the title (Enter index or 0 to enter your own)");
                            System.out.println((startInd+1)+" : "+a[startInd+1]);
                            System.out.println((startInd+2)+" : "+a[startInd+2]); 
                            System.out.println((startInd+3)+" : "+a[startInd+3]); 
                            tempIndex =input.nextInt();
                            inTryCatch=0;  
                        }    
                        catch(java.util.InputMismatchException ime){
                            inTryCatch=1;
                            System.out.println("Incorrect input, please try again");
                            input.next();
                        }
                    }

                    if (tempIndex!=0){  e.title=a[tempIndex]; }
                    else{
                        System.out.println("Please enter title: ");
                        e.title=input.next();}
                    return e;
                    }
                }      
            }
    return e;
    }

    
    public static comp findAdds(int index, comp c){
        comp company=c;
        //list all keywords that would designate an add
        String[] keywords ={"executive", "founder", "president","board", "member","chief","cio","ceo","coo","cfo",
           "cto","chro","director","officer","head", "principal","dean" ,"Treasurer","Superintendant"};
        
        return company;
    }
    public static void outputNewFile(comp[] allCompanies){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  
    }
    private static comp manualEnter(comp company) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
