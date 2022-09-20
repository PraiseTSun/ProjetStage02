package projet.projetstage02.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OffreDTO {

    private String nomDeCompagie;
    private String department;
    private String position;
    private int heureParSemaine;
    private String adresse;
    private File pdf;
}
