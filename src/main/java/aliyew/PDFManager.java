package aliyew;

import java.awt.Color;
import java.io.FileOutputStream;

import org.openpdf.text.*;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.openpdf.text.pdf.draw.LineSeparator;

import java.util.ArrayList;

public class PDFManager {

	public static String createPdf(Record rec,  ArrayList<Expense> allExpenses) {

		Document doc = new Document(PageSize.A4, 36, 36, 54, 54);
		try {

			PdfWriter.getInstance(doc, new FileOutputStream(rec.getRecordName() + ".pdf"));
			Font headerFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 32, Color.BLACK);
			Font contentFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Color.BLACK);
			Font contentFont2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Color.BLACK);


			doc.open();
			Paragraph headerParagraph = new Paragraph("Record Report", headerFont);
			headerParagraph.setAlignment(Element.ALIGN_CENTER);
			headerParagraph.setSpacingAfter(10f);

			doc.add(headerParagraph);

			LineSeparator line = new LineSeparator();
			line.setLineWidth(2f);
			line.setLineColor(new Color(26,54,93));
			doc.add(new Chunk(line));

			
			PdfPTable infoTable = new PdfPTable(2);
			PdfPCell info1 = new PdfPCell(new Phrase("Record Name", contentFont2));
			PdfPCell info2 = new PdfPCell(new Phrase(rec.getRecordName(), contentFont2));
			PdfPCell info3 = new PdfPCell(new Phrase("Record Income", contentFont2));
			PdfPCell info4 = new PdfPCell(new Phrase(rec.getRecordIncome()+"", contentFont2));
			PdfPCell info5 = new PdfPCell(new Phrase("Record Date", contentFont2));
			PdfPCell info6 = new PdfPCell(new Phrase((rec.getCreationDate()+"").substring(0,16), contentFont2));
			
			info2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			info4.setHorizontalAlignment(Element.ALIGN_RIGHT);
			info6.setHorizontalAlignment(Element.ALIGN_RIGHT);
			
			info1.setBorder(PdfPCell.NO_BORDER);
			info2.setBorder(PdfPCell.NO_BORDER);
			info3.setBorder(PdfPCell.NO_BORDER);
			info4.setBorder(PdfPCell.NO_BORDER);
			info5.setBorder(PdfPCell.NO_BORDER);
			info6.setBorder(PdfPCell.NO_BORDER);

			infoTable.addCell(info1);
			infoTable.addCell(info2);
			infoTable.addCell(info3);
			infoTable.addCell(info4);
			infoTable.addCell(info5);
			infoTable.addCell(info6);
			
			infoTable.setWidthPercentage(100f);
			
			doc.add(infoTable);
		

			PdfPTable expenseTable = new PdfPTable(3);
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

			for (int i = 0; i < allExpenses.size(); i++) {
				boolean isEven = (i % 2 == 0) ? true : false;

				addCell2Table(expenseTable, allExpenses.get(i).getExpenseDate() + "", false, isEven);
				addCell2Table(expenseTable, allExpenses.get(i).getExpenseCat(), false, isEven);
				addCell2Table(expenseTable, allExpenses.get(i).getExpenseAmt() + "", false, isEven);

			}

			doc.add(expenseTable);








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
		PdfPCell cell = new PdfPCell(new Phrase(phrase));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBackgroundColor(isColumnTitle ? Color.LIGHT_GRAY : (isEven ? new Color(152, 202, 255) : new Color(206, 230, 255)));
		table.addCell(cell);

	}
}
