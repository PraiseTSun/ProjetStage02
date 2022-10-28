package projet.projetstage02.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    @NotBlank
    private Long studentId;
    @NotBlank
    private Long offerId;
    @NotBlank
    private Long companyId;
    @Pattern(regexp = "^Hiver (\\d{4})$")
    private String session;
    private String description;
    private byte[] companySignature;
    private LocalDateTime companySignatureDate;
    private byte[] studentSignature;
    private LocalDateTime studentSignatureDate;
}
