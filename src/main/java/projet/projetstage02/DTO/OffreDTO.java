package projet.projetstage02.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OffreDTO {

    private String nomDeCompagnie;
    private String department;
    private String position;
    private int heureParSemaine;
    private String adresse;
    private byte[] pdf;
}
