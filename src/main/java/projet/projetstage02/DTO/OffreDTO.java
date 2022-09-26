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

    public OffreDTO(Offre offre) {
        this.nomDeCompagnie = offre.getNomDeCompagie();
        this.department = offre.getDepartment().departement;
        this.position = offre.getPosition();
        this.heureParSemaine = offre.getHeureParSemaine();
        this.adresse = offre.getAdresse();
        this.pdf = offre.getPdf();
    }
}
