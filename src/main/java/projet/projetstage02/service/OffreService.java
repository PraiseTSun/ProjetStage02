package projet.projetstage02.service;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.stereotype.Component;
import projet.projetstage02.DTO.OffreDTO;
import projet.projetstage02.modele.Offre;
import projet.projetstage02.repository.OffreRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class OffreService {
    private OffreRepository offreRepository;

    public OffreService(OffreRepository offreRepository) {
        this.offreRepository = offreRepository;
    }

    public long createOffre(OffreDTO offreDTO){

        Offre offre = new Offre(offreDTO.getNomDeCompagnie(), offreDTO.getDepartment(), offreDTO.getPosition(),
                offreDTO.getHeureParSemaine(), offreDTO.getAdresse(), offreDTO.getPdf());
        return offreRepository.save(offre).getId();

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
            input = new FileInputStream(pdf);
            // charger un document pdf
            PDFParser parser = new PDFParser(input);
            parser.parse();
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
            return pdfNom.endsWith(".pdf");
        }
        return false;
    }
}
