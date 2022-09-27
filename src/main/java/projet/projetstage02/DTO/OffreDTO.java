package projet.projetstage02.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.projetstage02.model.Offre;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OffreDTO {

    private String nomDeCompagnie;
    private String department;
    private String position;
    private int heureParSemaine;
    private String adresse;
    private byte[] pdf;
    private boolean valide;

    public OffreDTO(Offre offre){
        nomDeCompagnie = offre.getNomDeCompagie();
        department = offre.getDepartment().toString();
        position = offre.getPosition();
        heureParSemaine = offre.getHeureParSemaine();
        adresse = offre.getAdresse();
        pdf = offre.getPdf();
        valide = offre.isValide();
    }
}
