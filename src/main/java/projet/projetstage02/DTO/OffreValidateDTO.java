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
public class OffreValidateDTO {
    private String id;
    private String nomDeCompagie;
    private String department;
    private String position;

    public OffreValidateDTO(Offre offre){
        id = String.valueOf(offre.getId());
        nomDeCompagie = offre.getNomDeCompagie();
        department = offre.getDepartment();
        position = offre.getPosition();
    }
}
