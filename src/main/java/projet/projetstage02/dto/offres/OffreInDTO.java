package projet.projetstage02.dto.offres;

import com.sun.istack.NotNull;
import lombok.*;
import projet.projetstage02.model.AbstractUser;
import projet.projetstage02.model.Offre;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OffreInDTO {
    private long id;

    @NotBlank
    @Size(min = 2)
    private String nomDeCompagnie;
    private String session;
    @NotBlank
    @Pattern(regexp = "Techniques de linformatique|Techniques de la logistique du transport")
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
    private byte[] pdf;
    private String token;

    @NotBlank
    @Pattern(regexp = "^([0-2][0-9]|3[0-1])/(0[0-9]|1[0-2])/(19|20[0-9][0-9])$")
    private String dateStageDebut;
    @NotBlank
    @Pattern(regexp = "^([0-2][0-9]|3[0-1])/(0[0-9]|1[0-2])/(19|20[0-9][0-9])$")
    private String dateStageFin;
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
                .dateStageDebut(dateStageDebut)
                .dateStageFin(dateStageFin)
                .session(session)
                .adresse(adresse)
                .pdf(pdf)
                .valide(valide).build();
    }

    public OffreInDTO(Offre offre) {
        id = offre.getId();
        companyId = offre.getIdCompagnie();
        nomDeCompagnie = offre.getNomDeCompagnie();
        department = offre.getDepartment().departement;
        position = offre.getPosition();
        heureParSemaine = offre.getHeureParSemaine();
        adresse = offre.getAdresse();
        salaire = offre.getSalaire();
        dateStageDebut = offre.getDateStageDebut();
        dateStageFin = offre.getDateStageFin();
        session = offre.getSession();
        pdf = offre.getPdf();
        valide = offre.isValide();
    }
}
