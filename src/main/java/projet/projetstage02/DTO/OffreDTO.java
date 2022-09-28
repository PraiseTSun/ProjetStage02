package projet.projetstage02.DTO;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.projetstage02.model.Offre;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OffreDTO {
    private long id;

    @NotBlank
    @Size(min = 2)
    private String nomDeCompagnie;
    @NotBlank
    private String department;
    @NotBlank
    @Size(min = 2)
    private String position;
    @NotNull
    @Min(0)
    @Max(40)
    private int heureParSemaine;
    @NotBlank
    @Size(min = 2)
    private String adresse;
    @NotNull
    private byte[] pdf;
    private boolean valide;

    public OffreDTO(Offre offre){
        id = offre.getId();
        nomDeCompagnie = offre.getNomDeCompagie();
        department = offre.getDepartment().departement;
        position = offre.getPosition();
        heureParSemaine = offre.getHeureParSemaine();
        adresse = offre.getAdresse();
        pdf = offre.getPdf();
        valide = offre.isValide();
    }
}
