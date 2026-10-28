package aliyew;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Color;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

public class XlsxManager {
	public static void createXlsx(Record record, ArrayList<Expense> allExpenses, HashMap<String, Double> categorizedMap) {
		String safeRecordName = record.getRecordName().replaceAll("[\\\\/:*?\"<>|]", "_");
		safeRecordName += ".xlsx";

		try (Workbook wb = new XSSFWorkbook();
					FileOutputStream fileOut = new FileOutputStream("xlsx\'s/"+safeRecordName)) {
			Sheet sheet = wb.createSheet("Expenses");

			CellStyle headerStyle1 = wb.createCellStyle();
			CellStyle headerStyle2 = wb.createCellStyle();

			headerStyle1.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
			headerStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			headerStyle2.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
			headerStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			headerStyle1.setAlignment(HorizontalAlignment.LEFT);
			headerStyle1.setVerticalAlignment(VerticalAlignment.CENTER);

			headerStyle2.setAlignment(HorizontalAlignment.RIGHT);
			headerStyle2.setVerticalAlignment(VerticalAlignment.CENTER);

			headerStyle1.setBorderBottom(BorderStyle.THIN);
			headerStyle2.setBorderBottom(BorderStyle.THIN);
			//sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 3));
//			sheet.addMergedRegion(new CellRangeAddress(2, 2,  2, 3));
//			sheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 3));
//			sheet.addMergedRegion(new CellRangeAddress(4, 4, 2, 3));


			Row row1 = sheet.createRow(1);
			Row row2 = sheet.createRow(2);
			Row row3 = sheet.createRow(3);
			Row row4 = sheet.createRow(4);

			Cell cell11 = row1.createCell(1);
			cell11.setCellValue("Record Name");
			Cell cell21 = row2.createCell(1);
			cell21.setCellValue("Record Income");
			Cell cell31 = row3.createCell(1);
			cell31.setCellValue("Record Saving");
			Cell cell41 = row4.createCell(1);
			cell41.setCellValue("Record Date");

			Cell cell12 = row1.createCell(2);
			cell12.setCellValue("");
			Cell cell22 = row2.createCell(2);
			cell22.setCellValue("");
			Cell cell32 = row3.createCell(2);
			cell32.setCellValue("");
			Cell cell42 = row4.createCell(2);
			cell42.setCellValue("");

			Cell cell13 = row1.createCell(3);
			cell13.setCellValue(record.getRecordName());
			Cell cell23 = row2.createCell(3);
			cell23.setCellValue(record.getRecordIncome());
			Cell cell33 = row3.createCell(3);
			cell33.setCellValue(record.getRecordSaving());
			Cell cell43 = row4.createCell(3);
			cell43.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").withZone(ZoneId.of("Europe/Istanbul")).format(record.getRecordCreationDate()));                    

			cell11.setCellStyle(headerStyle1);
			cell21.setCellStyle(headerStyle1);
			cell31.setCellStyle(headerStyle1);
			cell41.setCellStyle(headerStyle1);
			cell12.setCellStyle(headerStyle1);
			cell22.setCellStyle(headerStyle1);
			cell32.setCellStyle(headerStyle1);
			cell42.setCellStyle(headerStyle1);
			cell13.setCellStyle(headerStyle2);
			cell23.setCellStyle(headerStyle2);
			cell33.setCellStyle(headerStyle2);
			cell43.setCellStyle(headerStyle2);

			CellStyle dataStyle1 = wb.createCellStyle();
			dataStyle1.setAlignment(HorizontalAlignment.CENTER);
			dataStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
			XSSFColor c1 = new XSSFColor(new Color(152, 202, 255), null);
			dataStyle1.setFillForegroundColor(c1);
			dataStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			dataStyle1.setBorderBottom(BorderStyle.THIN);
			CellStyle dataStyle2 = wb.createCellStyle();
			dataStyle2.setAlignment(HorizontalAlignment.CENTER);
			dataStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
			XSSFColor c2 = new XSSFColor(new Color(206, 230, 255), null);
			dataStyle2.setFillForegroundColor(c2);
			dataStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			dataStyle2.setBorderBottom(BorderStyle.THIN);
			CellStyle dataStyle3 = wb.createCellStyle();
			dataStyle3.setAlignment(HorizontalAlignment.CENTER);
			dataStyle3.setVerticalAlignment(VerticalAlignment.CENTER);
			XSSFColor c3 = new XSSFColor(new Color(224, 224, 224), null);
			dataStyle3.setFillForegroundColor(c3);
			dataStyle3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			dataStyle3.setBorderBottom(BorderStyle.THIN);

			Row headerRow = sheet.createRow(6);
			Cell hCell1 = headerRow.createCell(1);
			hCell1.setCellValue("DATE");
			hCell1.setCellStyle(dataStyle3);
			Cell hCell2 = headerRow.createCell(2);
			hCell2.setCellValue("CATEGORY");
			hCell2.setCellStyle(dataStyle3);
			Cell hCell3 = headerRow.createCell(3);
			hCell3.setCellValue("AMOUNT");
			hCell3.setCellStyle(dataStyle3);


			for (int i = 7; i <= allExpenses.size() + 6; i++) {
				Row dataRow = sheet.createRow(i);
				Cell dataCell1 = dataRow.createCell(1);
				Cell dataCell2 = dataRow.createCell(2);
				Cell dataCell3 = dataRow.createCell(3);

				if (i % 2 == 0) {
					dataCell1.setCellStyle(dataStyle1);
					dataCell2.setCellStyle(dataStyle1);
					dataCell3.setCellStyle(dataStyle1);
				} else {
					dataCell1.setCellStyle(dataStyle2);
					dataCell2.setCellStyle(dataStyle2);
					dataCell3.setCellStyle(dataStyle2);
				}

				dataCell1.setCellValue(allExpenses.get(i - 7).getExpenseDate() + "");
				dataCell2.setCellValue(allExpenses.get(i - 7).getExpenseCat());
				dataCell3.setCellValue(allExpenses.get(i - 7).getExpenseAmt() + "");
			}

			sheet.addMergedRegion(new CellRangeAddress(1, 1, 5, 7));
			Cell cbe = row1.createCell(5);
			row1.createCell(6);
			row1.createCell(7);
			row1.createCell(8);
			cbe.setCellStyle(dataStyle3);
			cbe.setCellValue("Category Based Expenses");

			int counter = 2;
			for (HashMap.Entry<String, Double> entry : categorizedMap.entrySet()) {
				Row dataRow = sheet.getRow(counter);
				
				if (dataRow == null) {
				    dataRow = sheet.createRow(counter);
				}
				
				sheet.addMergedRegion(new CellRangeAddress(counter, counter, 5, 6));
				//sheet.addMergedRegion(new CellRangeAddress(counter, counter, 7, 8));            
				
				Cell dataCell1 = dataRow.createCell(5);
				dataRow.createCell(6);
				Cell dataCell2 = dataRow.createCell(7);
				dataRow.createCell(8);

				dataCell1.setCellStyle(dataStyle1);
				dataCell2.setCellStyle(dataStyle2);

				dataCell1.setCellValue(entry.getKey());
				dataCell2.setCellValue(entry.getValue());

				counter++;
			}
			
			CellStyle totalStyle = wb.createCellStyle();
			totalStyle.setAlignment(HorizontalAlignment.CENTER);
			totalStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			XSSFColor c4 = new XSSFColor(Color.RED, null);
			totalStyle.setFillForegroundColor(c4);
			totalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			totalStyle.setBorderBottom(BorderStyle.THIN);
			
			CellStyle remStyle = wb.createCellStyle();
			remStyle.setAlignment(HorizontalAlignment.CENTER);
			remStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			XSSFColor c5 = new XSSFColor(Color.GREEN, null);
			remStyle.setFillForegroundColor(c5);
			remStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			remStyle.setBorderBottom(BorderStyle.THIN);
			
			Font font = wb.createFont();
			font.setColor(IndexedColors.WHITE.getIndex());
			totalStyle.setFont(font);
			remStyle.setFont(font);
			
			counter = 6 + allExpenses.size();
			
			Cell totalCell = sheet.getRow(counter).createCell(4);
			totalCell.setCellStyle(totalStyle);
			totalCell.setCellValue((new Report(record, allExpenses, categorizedMap)).getTotalExpense());
			
			Cell remBalance = sheet.getRow(counter).createCell(5);
			remBalance.setCellStyle(remStyle);
			remBalance.setCellValue((new Report(record, allExpenses, categorizedMap)).getTotalExpense()+record.getRecordIncome());

			for (int i = 0; i < 20; i++) {
				sheet.autoSizeColumn(i);
				int currentWidth = sheet.getColumnWidth(i);
				sheet.setColumnWidth(i, currentWidth + 1000);
			}

			wb.write(fileOut);
			wb.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}