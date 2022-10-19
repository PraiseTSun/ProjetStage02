package projet.projetstage02.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.projetstage02.model.AbstractUser.Department;

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

    @Min(15)
    @Max(999)
    private int salaire;

    @NotBlank
    @Size(min = 2)
    private String adresse;
    private boolean valide;
    @NotNull
    @Lob
    private byte[] pdf;
}
