package aliyew;

import java.awt.Color;
import java.io.FileOutputStream;
import java.util.Map;

import org.openpdf.text.Document;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

public class PDFManager {

	public static String createPdf(Report report) {

		// Document doc = new Document(PageSize.A4, 36, 36, 54, 54);
		Document doc = new Document(PageSize.A4);
		try {
			String safeRecordName = report.getRecord().getRecordName().replaceAll("[\\\\/:*?\"<>|]", "_");
			PdfWriter.getInstance(doc, new FileOutputStream(safeRecordName + "_report.pdf"));
			Font headerFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 28, Color.BLACK);
			Font contentFont2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Color.BLACK);


			doc.open();
			Paragraph headerParagraph = new Paragraph("Record Report", headerFont);
			headerParagraph.setAlignment(Element.ALIGN_CENTER);
			headerParagraph.setSpacingAfter(20f);

			doc.add(headerParagraph);

			
			
			PdfPTable infoTable = new PdfPTable(2);
			PdfPCell info1 = new PdfPCell(new Phrase("Record Name", contentFont2));
			PdfPCell info2 = new PdfPCell(new Phrase(report.getRecord().getRecordName(), contentFont2));
			PdfPCell info3 = new PdfPCell(new Phrase("Record Income", contentFont2));
			PdfPCell info4 = new PdfPCell(new Phrase(report.getRecord().getRecordIncome()+"", contentFont2));
			PdfPCell info7 = new PdfPCell(new Phrase("Record Saving", contentFont2));
			PdfPCell info8 = new PdfPCell(new Phrase(report.getRecord().getRecordSaving()+"", contentFont2));
			PdfPCell info5 = new PdfPCell(new Phrase("Record Date", contentFont2));
			PdfPCell info6 = new PdfPCell(new Phrase((report.getRecord().getCreationDate()+"").substring(0,16), contentFont2));
			
			info2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			info4.setHorizontalAlignment(Element.ALIGN_RIGHT);
			info6.setHorizontalAlignment(Element.ALIGN_RIGHT);
			info8.setHorizontalAlignment(Element.ALIGN_RIGHT);
			
			info1.setBorder(PdfPCell.TOP);
			info2.setBorder(PdfPCell.TOP);
			info3.setBorder(PdfPCell.NO_BORDER);
			info4.setBorder(PdfPCell.NO_BORDER);
			info7.setBorder(PdfPCell.NO_BORDER);
			info8.setBorder(PdfPCell.NO_BORDER);
			info5.setBorder(PdfPCell.BOTTOM);
			info6.setBorder(PdfPCell.BOTTOM);
			info5.setPaddingBottom(5f);
			info6.setPaddingBottom(5f);

			infoTable.addCell(info1);
			infoTable.addCell(info2);
			infoTable.addCell(info3);
			infoTable.addCell(info4);
			infoTable.addCell(info7);
			infoTable.addCell(info8);
			infoTable.addCell(info5);
			infoTable.addCell(info6);
			
			infoTable.setWidthPercentage(100f);
			infoTable.setSpacingAfter(50f);
			
			
			
			doc.add(infoTable);
		

			PdfPTable expenseTable = new PdfPTable(3);
			expenseTable.setWidthPercentage(100f);
			PdfPCell headerCell = new PdfPCell(new Phrase("Expenses", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, new Color(26, 54, 93))));
			headerCell.setColspan(3);
			headerCell.setBackgroundColor(new Color(240, 244, 248));
			headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			headerCell.setPadding(8f);
			headerCell.setBorder(PdfPCell.RIGHT);

			expenseTable.addCell(headerCell);



			// Column Titles
			addCell2Table(expenseTable, "Date", true, true);
			addCell2Table(expenseTable, "Category", true, true);
			addCell2Table(expenseTable, "Amount", true, true);

			for (int i = 0; i < report.getAllExpenses().size(); i++) {
				boolean isEven = (i % 2 == 0);

				addCell2Table(expenseTable, report.getAllExpenses().get(i).getExpenseDate() + "", false, isEven);
				addCell2Table(expenseTable, report.getAllExpenses().get(i).getExpenseCat(), false, isEven);
				addCell2Table(expenseTable, report.getAllExpenses().get(i).getExpenseAmt() + "", false, isEven);

			}

			expenseTable.setSpacingAfter(50f);

			doc.add(expenseTable);

			PdfPTable categorizedTable = new PdfPTable(4);
			categorizedTable.setWidthPercentage(100f);

			PdfPCell headerCell2 = new PdfPCell(new Phrase("Category Based Expenses", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, new Color(26, 54, 93))));
			headerCell2.setColspan(4);
			headerCell2.setBackgroundColor(new Color(240, 244, 248));
			headerCell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			headerCell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			headerCell2.setPadding(8f);
			headerCell2.setBorder(PdfPCell.RIGHT);

			categorizedTable.addCell(headerCell2);


			for (Map.Entry<String, Double> entry : report.getCategorizedMap().entrySet()) {
				PdfPCell cell1 = new PdfPCell(new Phrase(entry.getKey(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Color.BLACK)));
				PdfPCell cell2 = new PdfPCell(new Phrase(entry.getValue()+"", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Color.BLACK)));

				cell1.setBackgroundColor(new Color(152, 202, 255));
				cell2.setBackgroundColor(new Color(206, 230, 255));
				cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell1.setPadding(4f);
				cell2.setPadding(4f);
				cell1.setBorder(PdfPCell.TOP);
				cell2.setBorder(PdfPCell.TOP);

				categorizedTable.addCell(cell1);
				categorizedTable.addCell(cell2);
			}
			
			categorizedTable.setSpacingAfter(50f);

			doc.add(categorizedTable);

			PdfPTable resultTable = new PdfPTable(4);
			resultTable.setWidthPercentage(100f);
			resultTable.setSpacingAfter(50f);
			PdfPCell headerCell3 = new PdfPCell(new Phrase("Results", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, new Color(26, 54, 93))));
			headerCell3.setColspan(4);
			headerCell3.setBackgroundColor(new Color(240, 244, 248));
			headerCell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
			headerCell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			headerCell3.setPadding(8f);
			headerCell3.setBorder(PdfPCell.RIGHT | PdfPCell.BOTTOM);

			resultTable.addCell(headerCell3);
			
			addCell2Table(resultTable, "Total Expenses", false, true);
			addCell2Table(resultTable, report.getTotalExpense()+"", false, false);
			addCell2Table(resultTable, "Balance", false, true);
			addCell2Table(resultTable, (Math.round((report.getTotalExpense()+report.getRecord().getRecordIncome()) * 100.0) / 100.0)+"", false, false);
			addCell2Table(resultTable, "Highest Expense", false, true);
			addCell2Table(resultTable, report.getHighestExpense() == null ? "0" : report.getHighestExpense().getExpenseAmt()+"" , false, false);
			addCell2Table(resultTable, "Smallest Expense", false, true);
			addCell2Table(resultTable, report.getSmallestExpense() == null ? "0" : report.getSmallestExpense().getExpenseAmt()+"", false, false);
			addCell2Table(resultTable, "First Expense", false, true);
			addCell2Table(resultTable, report.getFirstExpense() == null ? "0" : report.getFirstExpense().getExpenseDate()+"", false, false);
			addCell2Table(resultTable, "Last Expense", false, true);
			addCell2Table(resultTable, report.getLastExpense() == null ? "0" : report.getLastExpense().getExpenseDate()+"", false, false);
			

			doc.add(resultTable);
			






			return "Document Created Successfully";
		} catch (Exception e) {
			return e.getMessage() + "\n" + e.toString() + " createPdf Method";
		} finally {
			if (doc.isOpen()) {
				doc.close();
			}
		}
	}

	private static void addCell2Table(PdfPTable table, String phrase, boolean isColumnTitle, boolean isEven) {
		PdfPCell cell = new PdfPCell();
		if (isColumnTitle) {
			cell = new PdfPCell(new Phrase(phrase, FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Color.BLACK)));
		} else {
			cell = new PdfPCell(new Phrase(phrase, FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Color.BLACK)));
		}
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorder(PdfPCell.BOTTOM);
		cell.setBorderColor(Color.black);
		cell.setPadding(4f);

		cell.setBackgroundColor(isColumnTitle ? new Color(224, 224, 224) : (isEven ? new Color(152, 202, 255) : new Color(206, 230, 255)));
		table.addCell(cell);

	}
}
