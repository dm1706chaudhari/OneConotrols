package com.onecontrols.usermanagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;


public class ReplaceFileTokens {

	private static int QuatationCounter = 0;
	public static void main(String[] args) throws Exception {
		
		FileInputStream fis = new FileInputStream(new File("/Users/DineshChaudhari/Downloads/test123.xls"));
		HSSFWorkbook wb = new HSSFWorkbook(fis);
		HSSFSheet worksheet = wb.getSheetAt(0);
		Cell cell1 = null;
		cell1 = worksheet.getRow(12).getCell(0);
		String AddressOfCustomer = "Dinesh Electricals,trying to create xml documet,hyderabad - 50089";
		String [] arr = AddressOfCustomer.split(",");
		
		
		cell1 = worksheet.getRow(12).getCell(0);
		cell1.setCellValue(arr[0]);
		
		
		cell1 = worksheet.getRow(13).getCell(0);
		cell1.setCellValue(arr[1]);
		
		
		cell1 = worksheet.getRow(14).getCell(0);
		cell1.setCellValue(arr[2]);
		
		String customer = "Dinesh Chaudhari -7348857372";
		
		cell1 = worksheet.getRow(18).getCell(1);
		cell1.setCellValue(customer);
		
	    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    
		cell1 = worksheet.getRow(7).getCell(6);
		cell1.setCellValue(strDate);
		cell1 = worksheet.getRow(8).getCell(6);
		cell1.setCellValue(QuatationCounter++);
		
		cell1 = worksheet.getRow(23).getCell(0);
		cell1.setCellValue("HEATER");
		
		cell1 = worksheet.getRow(23).getCell(2);
		cell1.setCellValue("1200 kw");
		
		cell1 = worksheet.getRow(23).getCell(5);
		cell1.setCellValue("5");
		cell1 = worksheet.getRow(23).getCell(6);
		cell1.setCellValue("2300");
	    
		System.out.println(cell1.getRichStringCellValue());
		fis.close();
		FileOutputStream output_file =new FileOutputStream(new File("/Users/DineshChaudhari/Downloads/pqr123.xls"));  //Open FileOutputStream to write updates
        wb.write(output_file);
        output_file.close(); 
        

        
        
   
	}
}
