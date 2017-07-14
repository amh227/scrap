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

    int MAXCOLS = 26;

    /**
     * @param args the command line arguments args[1]=filename;
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        comp[] companies = new comp[120];
        //Start with 100 potential arrayists for all the possible company names
        String file, outputfile, lastCompanyID = "-", tempFirst = null, tempLast = null, tempTitle = null, tempContactID, tempCompanyID;
        //String arrays for headers of all sheets
        String[][] headers = new String[26][3];
        int i, j, compCount = 0, empCount = 0, rowCount, colCount,totalEmployees=0,foundEmployees=0,pageErrors=0;
        Scanner input = new Scanner(System.in);

        if (args.length == 0) {
            System.out.println("must enter filename");
            file = "Job.xlsx";
        } else {
            file = args[1];
        }

        //headers=readFile(file, companies);  //class below
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
                        if (cn == 2) {//cn==2 contains companyID
                            if (cell == null) { temp = null;} 
                            else {tempCompanyID = cell.getStringCellValue();//System.out.println("-"+temp);
                                if (tempCompanyID.equals(lastCompanyID)) {    //Same company as last round
                                    totalEmployees++;
                                    cell = r.getCell(5, Row.RETURN_BLANK_AS_NULL);  //contactID
                                    temp = cell.getStringCellValue();
                                    tempContactID = temp;//col 7
                                    cell = r.getCell(7, Row.RETURN_BLANK_AS_NULL); //FirstName
                                    temp = cell.getStringCellValue();
                                    tempFirst = temp;//col 7
                                    cell = r.getCell(8, Row.RETURN_BLANK_AS_NULL); //LastName
                                    temp = cell.getStringCellValue();
                                    tempLast = temp;//col 8
                                    cell = r.getCell(9, Row.RETURN_BLANK_AS_NULL); //LastName
                                    temp = cell.getStringCellValue();
                                    tempTitle = temp;//col 9
                                    companies[compCount - 1].addEmployee(tempContactID, tempFirst, tempLast, tempTitle);
                                } else {//NEW COMPANY
                                    totalEmployees++;
                                    comp tempComp = new comp();
                                    companies[compCount] = tempComp;
                                    cell = r.getCell(2, Row.RETURN_BLANK_AS_NULL);
                                    temp = cell.getStringCellValue();
                                    companies[compCount].accountID = temp;
                                    cell = r.getCell(3, Row.RETURN_BLANK_AS_NULL);
                                    temp = cell.getStringCellValue();
                                    companies[compCount].name = temp;
                                    cell = r.getCell(4, Row.RETURN_BLANK_AS_NULL);
                                    temp = cell.getStringCellValue();
                                    companies[compCount].locationID = temp;
                                    cell = r.getCell(6, Row.RETURN_BLANK_AS_NULL);
                                    temp = cell.getStringCellValue();
                                    companies[compCount].URL = temp;
                                    cell = r.getCell(5, Row.RETURN_BLANK_AS_NULL);
                                    temp = cell.getStringCellValue();
                                    tempContactID = temp;//col 7
                                    cell = r.getCell(7, Row.RETURN_BLANK_AS_NULL);
                                    temp = cell.getStringCellValue();
                                    tempFirst = temp;//col 7
                                    cell = r.getCell(8, Row.RETURN_BLANK_AS_NULL);
                                    temp = cell.getStringCellValue();
                                    tempLast = temp;//col 8
                                    cell = r.getCell(9, Row.RETURN_BLANK_AS_NULL); //LastName
                                    temp = cell.getStringCellValue();
                                    tempTitle = temp;//col 9      
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

        //Interate through companies and print titles== testing
        String url = "  ", userInput, companyName = "  ",language;
        int count = 0, found = 0;
        for (i = 0; i < compCount; i++) {
            found=0;
            System.out.println(i + ". :");
            companies[i].printCompany();
            url = companies[i].URL;
            while (found == 0) {
                try {
                    Document doc = Jsoup.connect(url).get();
                    Jsoup.connect(url).header("Accept-Language", "en");
                    //get language page is in
                    Element taglang = doc.select("html").first();
                    language = (taglang.attr("lang"));
                    if (language.contains("en") != true) {
                        //page is not in english : prompt user to follow url and paste in new english url
                        System.out.println("\nURL: " + url + " \nNot in english, Please enter english url:");
                        userInput = input.next();
                        url = userInput;
                        try {
                            doc = Jsoup.connect(url).get();
                        } catch (org.jsoup.HttpStatusException | java.lang.IllegalArgumentException IAE) {
                            System.out.println("\nURL INVALID\n");
                        }
                    }

                    //trying to iterate through all strings of document individually    
                    Elements elements = doc.body().select("*");
                    String[] strArr = new String[10000];
                    int iterator = 0;
                    for (Element element : elements) {
                        String s = element.ownText();
                        if (s.trim().length() > 0) {
                            strArr[iterator] = element.ownText();
                            System.out.println(" "+s);
                            iterator++;
                        }
                    }
                    String text = doc.body().text();
                    System.out.println("\n" + text + "\n");
                    count = 0;
                    for (j = 0; j < companies[i].numEmployees; j++) {
                        //get name of employee
                        String first = companies[i].list[j].first;
                        String last = companies[i].list[j].last;
                        String name = first + " " + last;
                        //look for index of name withing the text
                        int index = text.indexOf(name);
                        int index2 = text.indexOf(first);
                        int index3 = text.indexOf(last);
                        
                        if (index3-index2<3+first.length()&&index==-1){//name broken by somethingother than one space
                            index=index2;
                        }
//found employee
                        int indexNameToTitle=findIndexNameToTitle(strArr, iterator, first,  last,  name, companies[i].list[j].title);
                        if (indexNameToTitle!=0){count++;}
                        /** REPLACING WITH INDEXING METHOD    
                        if (index != -1) {
                            System.out.println("Found name \"" + name + "\" at index: " + index);
                            foundEmployees++;
                            int newIndex = index + name.length() + 1;
                            int titleLength = (companies[i].list[j].title).length();
                            String foundTitle = text.substring(newIndex, newIndex + titleLength);
                            System.out.println("\tFollowed by found title: " + foundTitle);
                            count++;
                            found=1;
                        } 
                        * 
                        * 
                        */
//didnt find employee
                        //else { System.out.println("Name: " + name + "  NOT FOUND");}
                        if (j == companies[i].numEmployees - 1 && count == 0) {
                            System.out.println("\nNO EMPLOYEES FOUND - TRY DIFFERENT URL (or type 0 to quit)\n");
                            userInput = input.next();
                            if (userInput.compareTo("0") == 0) { found = 1;} 
                            else {url = userInput;}
                        }
                    }
                } 
                catch (org.jsoup.UnsupportedMimeTypeException| org.jsoup.HttpStatusException UMTE) {
                    System.out.println("\n::ERROR::URL: " + url + "\nInvalid for current programming :: May be pdf formatting\n");
//pause program until user validates
                    System.out.println("\nTRY DIFFERENT URL (or type 0 to quit)\n");
                    userInput = input.next();
                    if (userInput.compareTo("0") == 0) { 
                        found = 1;
                        pageErrors++;
                    } 
                    else {url = userInput;}
                    
                }
            }
        }//ends company count
        System.out.println("\n\n TOTAL EMPLOYEES LISTED: "+totalEmployees);
        System.out.println("FOUND EMPLOYEES: "+foundEmployees);
        System.out.println("PAGE ERRORS: "+pageErrors);
    }

    //Reads in entire excel file and organize it by companies with all employees listed
    public static String[][] readFile(String file, ArrayList<comp>[] companies) {
        int sheetNum = 0;
        String[] header1 = new String[30];
        String[] header2 = new String[30];
        String[] header2b = new String[30];
        String[][] headers = new String[30][3];
        int compCount = 0, empCount = 0, i = 0, j = 0, rowCount, colCount;
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
             * Sheet 0
             *
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
                        System.out.println("Looking for companies at  [" + rowNum + "][" + cn + "]");
                        if (cell == null) {
                            temp = null;
                            //System.out.printf("null"); 
                        } else {
                            temp = cell.getStringCellValue();
                            //System.out.println("-"+temp);
                        }
                    }
                }
            }
            /*
             * Sheet 2/ Save headers and add to company Arraylist 
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

            //sheet #2 Schhol to Ids.ID=String <14000 ,but close    
            sheetNum = 2;
            spreadsheet = workbook.getSheetAt(sheetNum);
            cn = 0;

        } catch (IOException Error) {
            System.out.println("Error loading the file: " + file);
        }

        //copy individual headers to headers
        for (i = 0; i < 15; i++) {
            System.out.println("\t");
            for (j = 0; j < 3; j++) {
                // System.out.print("   "+headers[i][j]);
            }
        }
        return headers;
    }
    
    
    public static int findIndexNameToTitle(String[] a, int arraySize, String first, String last, String name, String title){
        int i, ind=0;
        
        //find first name first
        for (i=0;i<arraySize;i++){
            System.out.println("i: "+i+ "   length:"+a.length);
            if (a[i].contains(first)){//found first name
                if (a[i].contains(last)){//contains full name
                    System.out.println("found full name: "+a[i]+ " at index "+i);
                    ind=i;
                }
                else{//last name not found at index, check next index
                    if(a[i+1].contains(last)){//last name is in the next index
                        System.out.println("found full name: "+a[i]+ " and "+a[i+1]+ "  at indexs "+i+ " & "+(i+1));
                        ind=i+1;
                    }
                    else{
                        System.out.println("First name found, but last name not, please check URL");
                        return 0;
                    }
                }
            }
        }  
            //FOUND NAME LOOK FOR TITLE
            //FIRST CHECK FOLLOWING INDEX
            //begin by checking for entire string at next index
            
            
            
        
        
        
        return ind;
    }
}
