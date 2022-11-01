package projet.projetstage02.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StageContract {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;
    @Min(0)
    private long studentId;
    @Min(0)
    private long offerId;
    @Min(0)
    private long companyId;
    private String description;
    @Builder.Default()
    private String companySignature = "";
    @Builder.Default()
    private String gestionnaireSignature = "";
    @Builder.Default()
    private String studentSignature = "";
}
