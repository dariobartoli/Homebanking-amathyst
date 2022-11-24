package com.mindhub.homebanking.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mindhub.homebanking.models.Transaction;

import java.io.FileOutputStream;
import java.util.Set;
import java.util.stream.Stream;

public class PDFMethod {

    public PDFMethod() {
    }

    public static void createPDF (Set<Transaction> transactionArray) throws Exception {
        var doc = new Document();
        String route = System.getProperty("user.home");
        PdfWriter.getInstance(doc, new FileOutputStream("movimientos.pdf"));
        PdfWriter.getInstance(doc, new FileOutputStream(route + "./Downloads/TransactionInfo.pdf"));
        doc.open();

        var bold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        try
        {
            Image foto = Image.getInstance("C:\\Users\\Dario\\Downloads\\logoeditado.png");
            foto.scaleToFit(100, 100);
            foto.setAlignment(Chunk.ALIGN_MIDDLE);
            doc.add(foto);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        var paragraph = new Paragraph( "Tus Movimientos", bold);


        var table = new PdfPTable(4);
        Stream.of("Monto", "Descripción", "Fecha", "Tipo").forEach(table::addCell);

        transactionArray.forEach(transaction -> {
            table.addCell(String.valueOf(transaction.getAmount()));
            table.addCell(transaction.getDescription());
            table.addCell(String.valueOf(transaction.getDate()));
            table.addCell(String.valueOf(transaction.getType()));
        });

        doc.add(paragraph);
        doc.add(table);
        doc.close();
    }
}