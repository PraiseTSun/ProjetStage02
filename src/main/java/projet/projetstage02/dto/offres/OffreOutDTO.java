package projet.projetstage02.dto.offres;

import com.sun.istack.NotNull;
import lombok.*;
import projet.projetstage02.model.AbstractUser;
import projet.projetstage02.model.Offre;

import javax.validation.constraints.*;

import static projet.projetstage02.utils.ByteConverter.byteToString;
import static projet.projetstage02.utils.ByteConverter.stringToBytes;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OffreOutDTO {
    private long id;

    @NotBlank
    @Size(min = 2)
    private String nomDeCompagnie;
    private String session;
    @NotBlank
    @Pattern(regexp = ("Techniques de linformatique|Techniques de la logistique du transport"))
    private String department;
    @NotBlank
    @Size(min = 2)
    private String position;
    @NotNull
    @Min(0)
    @Max(40)
    private int heureParSemaine;

    @NotNull
    @Min(15)
    @Max(999)
    private int salaire;
    @NotBlank
    @Size(min = 2)
    private String adresse;

    private long companyId;
    @NotNull
    @ToString.Exclude
    private String pdf;
    private String token;

    @NotBlank
    private String dateStage;
    private boolean valide;

    public Offre toModel() {
        return Offre.builder()
                .id(id)
                .nomDeCompagnie(nomDeCompagnie)
                .idCompagnie(companyId)
                .department(AbstractUser.Department.getDepartment(department))
                .position(position)
                .heureParSemaine(heureParSemaine)
                .salaire(salaire)
                .session(session)
                .dateStage(dateStage)
                .adresse(adresse)
                .pdf(stringToBytes(pdf))
                .valide(valide).build();
    }

    public OffreOutDTO(Offre offre) {
        id = offre.getId();
        companyId = offre.getIdCompagnie();
        nomDeCompagnie = offre.getNomDeCompagnie();
        department = offre.getDepartment().departement;
        position = offre.getPosition();
        heureParSemaine = offre.getHeureParSemaine();
        adresse = offre.getAdresse();
        salaire = offre.getSalaire();
        session = offre.getSession();
        dateStage = offre.getDateStage();
        pdf = byteToString(offre.getPdf());
        valide = offre.isValide();
    }
}
