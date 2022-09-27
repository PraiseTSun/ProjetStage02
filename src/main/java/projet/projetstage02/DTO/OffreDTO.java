package projet.projetstage02.DTO;

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

    public OffreDTO(Offre offre) {
        this.nomDeCompagnie = offre.getNomDeCompagie();
        this.department = offre.getDepartment().departement;
        this.position = offre.getPosition();
        this.heureParSemaine = offre.getHeureParSemaine();
        this.adresse = offre.getAdresse();
        this.pdf = offre.getPdf();
    }
}
