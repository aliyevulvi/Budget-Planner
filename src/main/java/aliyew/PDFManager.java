package aliyew;

import java.awt.Color;
import java.io.FileOutputStream;

import org.openpdf.text.*;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

public class PDFManager {

    public static String createPdf(Record rec, Expense exp) {

        Document doc = new Document(PageSize.A4, 36, 36, 54, 54);
        try {

            PdfWriter.getInstance(doc, new FileOutputStream(rec.getRecordName()+".pdf"));
            Font headerFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Color.BLACK);
            Font contentFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Color.LIGHT_GRAY);
            Font tableFont1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Color.RED);

            doc.open();
            Paragraph headerParagraph = new Paragraph("Record Report", headerFont);
            headerParagraph.setAlignment(Element.ALIGN_CENTER);
            doc.add(headerParagraph);

            Paragraph recordName = new Paragraph("Record Name : " + rec.getRecordName());
            Paragraph recordIncome = new Paragraph("Record Income : " + rec.getRecordIncome());
            Paragraph recordDate = new Paragraph("Record Creation Date : " + rec.getCreationDate().substring(0, 16));
            recordName.setFont(tableFont1);
            recordIncome.setFont(tableFont1);
            recordDate.setFont(tableFont1);
            doc.add(recordName);
            doc.add(recordIncome);
            doc.add(recordDate);


            PdfPTable expenseTable = new PdfPTable(3);
            PdfPCell headerCell = new PdfPCell(new Phrase("Expenses", tableFont1));
            headerCell.setColspan(3);
            headerCell.setBackgroundColor(Color.LIGHT_GRAY);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            expenseTable.addCell(headerCell);



            // Column Titles
            addCell2Table(expenseTable, "Date", true, true);
            addCell2Table(expenseTable, "Date", true, true);
            addCell2Table(expenseTable, "Date", true, true);
            addCell2Table(expenseTable, "Date", true, true);
            addCell2Table(expenseTable, "Date", true, true);
            addCell2Table(expenseTable, "Date", true, true);
            addCell2Table(expenseTable, "Date", true, true);
            addCell2Table(expenseTable, "Date", true, true);
            addCell2Table(expenseTable, "Date", true, true);

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
            cell.setBackgroundColor(isColumnTitle ? Color.BLUE : (isEven ? Color.LIGHT_GRAY : Color.GRAY));
            table.addCell(cell);

    }
}
