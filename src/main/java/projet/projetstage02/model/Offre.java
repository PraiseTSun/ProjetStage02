package projet.projetstage02.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.projetstage02.model.AbstractUser.Department;
import projet.projetstage02.utils.TimeUtil;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Offre")
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;
    @NotBlank
    @Size(min = 2)
    private String nomDeCompagnie;

    @Min(0)
    private long idCompagnie;
    @Size(min = 10)
    @Pattern(regexp = "^Hiver (\\d{4})$")
    private String session;
    @NotNull
    private Department department;
    @NotBlank
    @Size(min = 2)
    private String position;
    @Min(1)
    @Max(40)
    private int heureParSemaine;

    @DecimalMin("15.00")
    @DecimalMax("999.99")
    private float salaire;
    @NotBlank
    @Pattern(regexp = "^(19|20[0-9][0-9])-(0[0-9]|1[0-2])-([0-2][0-9]|3[0-1])$")
    private String dateStageFin;

    @NotBlank
    @Pattern(regexp = "^(19|20[0-9][0-9])-(0[0-9]|1[0-2])-([0-2][0-9]|3[0-1])$")
    private String dateStageDebut;
    @NotBlank
    @Size(min = 2)
    private String adresse;
    private boolean valide;
    @NotNull
    @Lob
    private byte[] pdf;

    public static String currentSession() {
        return "Hiver " + TimeUtil.getNextYear();
    }
}
