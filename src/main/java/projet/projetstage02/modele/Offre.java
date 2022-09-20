package projet.projetstage02.modele;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.File;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Offre")
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;
    private String nomDeCompagie;
    private String department;
    private String position;
    private int heureParSemaine;
    private String adresse;
    @Lob
    private byte[] pdf;

    public Offre(String nomDeCompagie, String department, String position, int heureParSemaine, String adresse, byte[] pdf) {
        this.nomDeCompagie = nomDeCompagie;
        this.department = department;
        this.position = position;
        this.heureParSemaine = heureParSemaine;
        this.adresse = adresse;
        this.pdf = pdf;
    }
}
