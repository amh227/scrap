package scraper;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Scraper {
    int MAXCOLS=26;
    
    
    
    /**
     * @param args the command line arguments
     * args[1]=filename;
     */
    public static void main(String[] args) {
        comp[] companies=new comp[120];
       //Start with 100 potential arrayists for all the possible company names
       String file,outputfile,lastCompanyID="-",tempFirst = null,tempLast = null,tempTitle = null,tempContactID,tempCompanyID;
       //String arrays for headers of all sheets
       String[][] headers=new String[26][3];
       int i,j, compCount=0,empCount=0, rowCount, colCount;
       
       if(args.length == 0){ 
            System.out.println("must enter filename");
            file="Job.xlsx";
        }
        else{ file=args[1]; }
        
       //headers=readFile(file, companies);  //class below
       int sheetNum=0;
       
        //Need to read in excel sheet and 
        XSSFRow row;
        try (FileInputStream fis = new FileInputStream(new File(file))) {
            // A workbook reference is created from the chosen Excel file.
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            // A spreadsheet object is created to reference the Excel files pages.
            XSSFSheet spreadsheet;
            // Iterator for the rows
            Iterator < Row > rowIterator;
            // Iterator for the columns
            Iterator < Cell > cellIterator;
            spreadsheet = workbook.getSheetAt(sheetNum);
            //rowIterator = spreadsheet.iterator();
//Start reading in file//
            int rowStart = Math.min(0, spreadsheet.getFirstRowNum());
            int rowEnd =   Math.max(200, spreadsheet.getLastRowNum());
            int lastColumn;
/** 
 *      Sheet 0
 **/ 
            sheetNum=0;
            for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                Row r = spreadsheet.getRow(rowNum);
                
                if(r!=null)  {lastColumn = r.getLastCellNum();}
                else{continue;}
                //lastColumn = Math.max(r.getLastCellNum(),26/**MAXCOLS**/);
                String temp;
                for (int cn = 0; cn < lastColumn; cn++) {
                    Cell cell = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                    if (cell == null) {
                        temp=null;
                        //System.out.printf("null"); 
                    } 
                    else {
                        temp=cell.getStringCellValue();
                        //System.out.println(" "+temp);
                    }
                    if (rowStart==rowNum){
                        //System.out.println(" in grid "+temp);
                        headers[cn][sheetNum]=temp;
                    }
                    //employees
                    if (rowNum>rowStart){
                        System.out.println("Looking for companies at  ["+rowNum+"]["+cn+"]");
                        if(cn==2){//cn==2 contains companyID
                             if (cell == null) {
                                temp=null; //System.out.printf("null"); 
                            } 
                            else {
                                tempCompanyID=cell.getStringCellValue();//System.out.println("-"+temp);
                                if (tempCompanyID.equals(lastCompanyID)){    //Same company as last round
                                    cell = r.getCell(5, Row.RETURN_BLANK_AS_NULL);
                                    temp=cell.getStringCellValue();
                                    tempContactID=temp;//col 7
                                    cell = r.getCell(7, Row.RETURN_BLANK_AS_NULL);
                                    temp=cell.getStringCellValue();
                                    tempFirst=temp;//col 7
                                    cell = r.getCell(8, Row.RETURN_BLANK_AS_NULL);
                                    temp=cell.getStringCellValue();
                                    tempLast="";//col 8
                                    tempTitle="";        
                                    companies[compCount].addEmployee(tempContactID,tempFirst, tempLast, tempTitle);
                                }
                                else{//NEW COMPANY
                                    System.out.println("HERE");
                                    comp tempComp=new comp();
                                    companies[compCount]=tempComp;
                                    cell = r.getCell(2, Row.RETURN_BLANK_AS_NULL);
                                    temp=cell.getStringCellValue();
                                    companies[compCount].accountID=temp;
                                    cell = r.getCell(3, Row.RETURN_BLANK_AS_NULL);
                                    temp=cell.getStringCellValue();
                                    companies[compCount].accountID=temp;
                                    cell = r.getCell(4, Row.RETURN_BLANK_AS_NULL);
                                    temp=cell.getStringCellValue();
                                    companies[compCount].locationID=temp;
                                    cell = r.getCell(6, Row.RETURN_BLANK_AS_NULL);
                                    temp=cell.getStringCellValue();
                                    companies[compCount].URL=temp;
                                    cell = r.getCell(5, Row.RETURN_BLANK_AS_NULL);
                                    temp=cell.getStringCellValue();
                                    tempContactID=temp;//col 7
                                    cell = r.getCell(7, Row.RETURN_BLANK_AS_NULL);
                                    temp=cell.getStringCellValue();
                                    tempFirst=temp;//col 7
                                    cell = r.getCell(8, Row.RETURN_BLANK_AS_NULL);
                                    temp=cell.getStringCellValue();
                                    tempLast="";//col 8
                                    tempTitle="";        
                                    companies[compCount].addEmployee(tempContactID,tempFirst, tempLast, tempTitle);
                                    lastCompanyID=companies[compCount].accountID;
                                    compCount++;
                                }
                                        
                            }
                             
                        }
                        /**
                         * Looking for companies at  [1][2] found: 0013600000q7TV3AAM
Looking for companies at  [1][3]
Looking for companies at  [1][3] found: TUI AG
Looking for companies at  [1][4]
Looking for companies at  [1][4] found: a0936000004EFHDAA4
Looking for companies at  [1][5]
Looking for companies at  [1][5] found: 0033600000eGHwWAAW
Looking for companies at  [1][6]
Looking for companies at  [1][6] found: http://www.tuigroup.com/de-de/investoren/corporate-governance/management
Looking for companies at  [1][7]
Looking for companies at  [1][7] found: Horst
Looking for companies at  [1][8]
Looking for companies at  [1][8] found: Baier
Looking for companies at  [1][9]
Looking for companies at  [1][9] found: Chief Financial Officer (CFO)
                         */
                        
                        System.out.println("Looking for companies at  ["+rowNum+"]["+cn+"] found: "+temp);
                    }
                }
            }
/*
 * Sheet 1/ Save headers and add to company Arraylist ----------------------------------------------------------------------------
 */
        sheetNum=1;
        spreadsheet = workbook.getSheetAt(sheetNum);
            int cn=0;
            for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                Row r = spreadsheet.getRow(rowNum);
                
                if(r!=null)  {lastColumn = r.getLastCellNum();}
                else{
                    headers[cn][sheetNum+1]=null;
                    continue;}
                //lastColumn = Math.max(r.getLastCellNum(),26/**MAXCOLS**/);
                String temp;
                for (cn = 0; cn < lastColumn; cn++) {
                    Cell cell = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                    if (cell == null) { temp=null;} 
                    else { temp=cell.getStringCellValue(); }
                    //headers
                    if (rowStart==rowNum){
                        headers[cn][sheetNum]=temp; }
                    if (rowStart+1==rowNum){
                        headers[cn][sheetNum+1]=temp;}
                }
            }
//----------------------------------------------------------------------------------------------------------------------------------            
        //sheet #2 Schhol to Ids.ID=String <14000 ,but close    
        sheetNum=2;
        spreadsheet = workbook.getSheetAt(sheetNum);
        cn=0;
        
        
        
        
        }
        catch(IOException Error){ System.out.println("Error loading the file: "+file); }
        
       //copy individual headers to headers
       
        
       for (j=0;j<3;j++){
            System.out.println("\t");
           for(i=0;i<26;i++){
               System.out.print("   "+headers[i][j]);
           }
       }
       
 //String to look for
        
        
        String companyName = "  ";
        
        for(i=0;i<compCount;i++){
            System.out.println(i+". :");
            companies[i].printCompany();
        }
        
        
    }
    
    
    //Reads in entire excel file and organize it by companies with all employees listed
    public static String[][] readFile(String file, ArrayList<comp>[] companies){
        int sheetNum=0;
       String[] header1 = new String[30];
       String[] header2 = new String[30];
       String[] header2b = new String[30]; 
       String[][] headers=new String[30][3];
       int compCount=0,empCount=0, i=0, j=0, rowCount, colCount;
        //Need to read in excel sheet and 
        XSSFRow row;
        try (FileInputStream fis = new FileInputStream(new File(file))) {
            // A workbook reference is created from the chosen Excel file.
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            // A spreadsheet object is created to reference the Excel files pages.
            XSSFSheet spreadsheet;
            // Iterator for the rows
            Iterator < Row > rowIterator;
            // Iterator for the columns
            Iterator < Cell > cellIterator;
            spreadsheet = workbook.getSheetAt(sheetNum);
            //rowIterator = spreadsheet.iterator();
//Start reading in file//
            int rowStart = Math.min(0, spreadsheet.getFirstRowNum());
            int rowEnd =   Math.max(200, spreadsheet.getLastRowNum());
            int lastColumn;
/** 
 *      Sheet 0
 **/ 
            sheetNum=0;
            for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                Row r = spreadsheet.getRow(rowNum);
                
                if(r!=null)  {lastColumn = r.getLastCellNum();}
                else{continue;}
                //lastColumn = Math.max(r.getLastCellNum(),26/**MAXCOLS**/);
                String temp;
                for (int cn = 0; cn < lastColumn; cn++) {
                    Cell cell = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                    if (cell == null) {
                        temp=null;
                        //System.out.printf("null"); 
                    } 
                    else {
                        temp=cell.getStringCellValue();
                        //System.out.println(" "+temp);
                    }
                    if (rowStart==rowNum){
                        //System.out.println(" in grid "+temp);
                        headers[cn][sheetNum]=temp;
                    }
                    //employees
                    if (rowNum>rowStart){
                        System.out.println("Looking for companies at  ["+rowNum+"]["+cn+"]");
                        if (cell == null) {
                            temp=null;
                            //System.out.printf("null"); 
                        } 
                        else {
                            temp=cell.getStringCellValue();
                            //System.out.println("-"+temp);
                        }
                    }
                }
            }
/*
 * Sheet 2/ Save headers and add to company Arraylist 
 */
        sheetNum=1;
        spreadsheet = workbook.getSheetAt(sheetNum);
            int cn=0;
            for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                Row r = spreadsheet.getRow(rowNum);
                
                if(r!=null)  {lastColumn = r.getLastCellNum();}
                else{
                    headers[cn][sheetNum+1]=null;
                    continue;}
                //lastColumn = Math.max(r.getLastCellNum(),26/**MAXCOLS**/);
                String temp;
                for (cn = 0; cn < lastColumn; cn++) {
                    Cell cell = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                    if (cell == null) { temp=null;} 
                    else { temp=cell.getStringCellValue(); }
                    //headers
                    if (rowStart==rowNum){headers[cn][sheetNum]=temp; }
                    if (rowStart+1==rowNum){headers[cn][sheetNum+1]=temp;}
                    
                }
            }
            
        //sheet #2 Schhol to Ids.ID=String <14000 ,but close    
        sheetNum=2;
        spreadsheet = workbook.getSheetAt(sheetNum);
        cn=0;
        
        
        
        
        }
        catch(IOException Error){ System.out.println("Error loading the file: "+file); }
        
       //copy individual headers to headers
       
        for(i=0;i<15;i++){
           System.out.println("\t");
           for (j=0;j<3;j++){
              // System.out.print("   "+headers[i][j]);
           }
       }
       return headers;
    }
    public void writeFile(String file){
        
        
    }
    
    /**
     * EXample from apache poi website
     * http://poi.apache.org/spreadsheet/quick-guide.html#Iterator
     * 
   public void readme(){
    int rowStart = Math.min(15, sheet.getFirstRowNum());
    int rowEnd = Math.max(1400, sheet.getLastRowNum());

    for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
       Row r = sheet.getRow(rowNum);
       if (r == null) {
          // This whole row is empty
          // Handle it as needed
          continue;
       }

       int lastColumn = Math.max(r.getLastCellNum(), MY_MINIMUM_COLUMN_COUNT);

       for (int cn = 0; cn < lastColumn; cn++) {
          Cell c = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
          if (c == null) {
             // The spreadsheet is empty in this cell
          } else {
             // Do something useful with the cell's contents
          }
       }
    }
   }
   * */
}
