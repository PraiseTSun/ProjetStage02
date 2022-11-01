package projet.projetstage02.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

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
    @Pattern(regexp = "^Hiver (\\d{4})$")
    private String session;
    @Min(0)
    private long studentId;
    @Min(1)
    private long offerId;
    @Min(1)
    private long companyId;
    private String description;
    @Lob
    private String companySignature;
    private LocalDateTime companySignatureDate;
    private String gestionnaireSignature;
    private LocalDateTime gestionnaireSignatureDate;
    private String studentSignature;
    private LocalDateTime studentSignatureDate;
}
