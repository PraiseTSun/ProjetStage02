package projet.projetstage02.service;

import com.itextpdf.text.pdf.PdfReader;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.stereotype.Component;
import projet.projetstage02.DTO.OffreDTO;
import projet.projetstage02.modele.Offre;
import projet.projetstage02.repository.OffreRepository;

import java.io.*;
import java.util.List;

@Component
public class OffreService {
    private OffreRepository offreRepository;

    public OffreService(OffreRepository offreRepository) {
        this.offreRepository = offreRepository;
    }

     public long createOffre(OffreDTO offreDTO){
         
//         File file = new File("nom.pdf");
//         System.out.println("ici");
//         PrintWriter writer = null;
//         try {
//             writer = new PrintWriter(new FileOutputStream("nom.pdf"));
//
//             StringBuffer content = new StringBuffer(offreDTO.getPdf()); // 存放读取出的文档内容
//
//             writer.write(content.toString());// 写入文件内容
//             writer.flush();
//             writer.close();
//         } catch (IOException e) {
//             e.printStackTrace();
//         }

        // File file = toFile(offreDTO.getPdf(),"nom.pdf");
         Offre offre = new Offre(offreDTO.getNomDeCompagnie(), offreDTO.getDepartment(), offreDTO.getPosition(),
                 offreDTO.getHeureParSemaine(), offreDTO.getAdresse(), offreDTO.getPdf());
         return offreRepository.save(offre).getId();

     } /* */
    private File toFile(String content,String filePath) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                f.createNewFile();
            }
            System.out.println("Write PDF Content to txt file ...");
            BufferedWriter output = new BufferedWriter(new FileWriter(f));
            output.write(content);
            output.flush();
            output.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("nom.pdf")));
            System.out.println("lire : "+in.readLine());
            return f;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Offre> findOffre(){
        return offreRepository.findAll();
    }


    public boolean isVide(File pdf) throws IOException {
        File file = pdf;

        boolean sort = false;// si fait ordre
        int startPage = 1;// page commence
        int endPage = Integer.MAX_VALUE;   // Fin du nombre de pages d'extraction
        String content = null;

        InputStream input = null;
        PDDocument document = null;

        try {
            System.out.println("1");
            input = new FileInputStream(pdf);
            // charger un document pdf
            PDFParser parser = new PDFParser(input);

            parser.parse();
            System.out.println("2");
            document = parser.getPDDocument();
            // Obtenir des informations sur le contenu
            PDFTextStripper pts = new PDFTextStripper();
            pts.setSortByPosition(sort);
            endPage = document.getNumberOfPages();
            System.out.println("Total Page: " + endPage);
            pts.setStartPage(startPage);
            pts.setEndPage(endPage);
            try {
                content = pts.getText(document);
            }catch(Exception e) {
                throw e;
            }
            System.out.println("Get PDF Content ...");
        }catch(Exception e){
            throw e;
        } finally {

            if (null != input)
                input.close();
            if (null != document)
                document.close();
        }


        return content == null;
    }

    public boolean valide(File pdf) {
        if(pdf.isFile()) {
            String pdfNom = pdf.getName().toLowerCase();
            System.out.println("pdfNOm : " + pdfNom);
            return pdfNom.endsWith(".pdf");
        }
        return false;
    }
}
