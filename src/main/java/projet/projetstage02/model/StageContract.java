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
    private Long id;
    @Min(1)
    private Long studentId;
    @Min(1)
    private Long offerId;
    @Min(1)
    private Long companyId;
    @Pattern(regexp = "^Hiver (\\d{4})$")
    private String session;
    private String description;
    @Lob
    private String companySignature;
    private LocalDateTime companySignatureDate;
    @Lob
    private String studentSignature;
    private LocalDateTime studentSignatureDate;
}
