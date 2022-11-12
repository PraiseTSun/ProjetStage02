package projet.projetstage02.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public class PDFCreationUtil {
    /***
     * @param title
     * @param paragraphs <pre>Paragraphes a ajouter dans le PDF. <br>La clé est le titre du paragraphe et la valeur est le contenu<br>
     *(clé = nom du champs, valeur = valeur du champs)<br>
     *Example du formattage:<br>
     *        {<br>
     *          "Informations":<br>
     *           {<br>
     *              "Nom":"NomDeLaPersonne",<br>
     *              "Prenom":"PrenomDeLaPersonne"<br>
     *           },<br>
     *           "Information2":<br>
     *           {<br>
     *              "Nom2":"NomDeLaPersonne2",<br>
     *              "Prenom2":"PrenomDeLaPersonne2"<br>
     *           },<br>
     *        }<pre/>
     * @throws DocumentException
     */
    public static String createPDFFromMap(String title, Map<String, Map<String, String>> paragraphs) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 25, 25, 25, 25);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);

        document.open();
        addCenteredTitle(document, title, 30);
        addText(document, paragraphs);
        document.close();
        writer.close();
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    private static void addText(Document document, Map<String, Map<String, String>> paragraphs) {
        paragraphs.forEach((paragraphTitle, paragraph) -> {
            try {
                if (paragraphTitle.contains("_signature_")) {
                    addSignature(document, paragraph);
                    return;
                }
                addUncenteredTitle(document, paragraphTitle, 16);
                addParagraph(document, paragraph);
            } catch (DocumentException | IOException ignored) {
            }
        });
    }

    private static void addSignature(Document document, Map<String, String> paragraph) throws DocumentException, IOException {
        byte[] bytes = ByteConverter.stringToBytes(paragraph.get("signature"));
        Image image = Image.getInstance(bytes);
        image.setAlt("Signature");
        image.scaleAbsolute(100, 50);
        document.add(image);
    }

    private static void addParagraph(Document document, Map<String, String> paragraph) {
        paragraph.forEach((fieldName, fieldValue) -> {
            try {
                addKeyValueLine(document, fieldName, fieldValue);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        });
    }

    private static void addKeyValueLine(Document document, String key, String value) throws DocumentException {
        document.add(new Paragraph(
                key +
                        (key.length() > 0 ? " : " : "")
                        + value,
                new Font(Font.FontFamily.HELVETICA, 11)));
    }

    private static void addCenteredTitle(Document document, String title, int size) throws DocumentException {
        Paragraph p = new Paragraph(title, new Font(Font.FontFamily.HELVETICA, size, Font.BOLD));
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
    }

    private static void addUncenteredTitle(Document document, String title, int size) throws DocumentException {
        Paragraph p = new Paragraph(title, new Font(Font.FontFamily.HELVETICA, size, Font.BOLD));
        document.add(p);
    }
}
