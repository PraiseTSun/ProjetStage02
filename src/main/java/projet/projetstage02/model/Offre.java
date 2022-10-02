package projet.projetstage02.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.projetstage02.model.AbstractUser.Department;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="Offre")
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;
    private String nomDeCompagnie;
    private Department department;
    private String position;
    private int heureParSemaine;
    private String adresse;
    private boolean valide;
    @Lob
    private byte[] pdf;


    public Offre(String nomDeCompagie, Department department, String position, int heureParSemaine, String adresse, byte[] pdf) {
        this.nomDeCompagnie = nomDeCompagie;
        this.department = department;
        this.position = position;
        this.heureParSemaine = heureParSemaine;
        this.adresse = adresse;
        this.pdf = pdf;
        valide = false;
    }
}
