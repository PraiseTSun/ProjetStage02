package projet.projetstage02.DTO;

import com.sun.istack.NotNull;
import lombok.*;
import projet.projetstage02.model.AbstractUser;
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
    @NotNull
    @ToString.Exclude
    private byte[] pdf;
    private String token;
    private boolean valide;

    public Offre toModel() {
        return Offre.builder()
                .id(id)
                .nomDeCompagnie(nomDeCompagnie)
                .department(AbstractUser.Department.getDepartment(department))
                .position(position)
                .heureParSemaine(heureParSemaine)
                .salaire(salaire)
                .session(session)
                .adresse(adresse)
                .pdf(pdf)
                .valide(valide).build();
    }

    public OffreDTO(Offre offre) {
        id = offre.getId();
        nomDeCompagnie = offre.getNomDeCompagnie();
        department = offre.getDepartment().departement;
        position = offre.getPosition();
        heureParSemaine = offre.getHeureParSemaine();
        adresse = offre.getAdresse();
        salaire = offre.getSalaire();
        session = offre.getSession();
        pdf = offre.getPdf();
        valide = offre.isValide();
    }
}
