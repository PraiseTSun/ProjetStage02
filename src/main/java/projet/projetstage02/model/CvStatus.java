package projet.projetstage02.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CvStatus {

    @Id
    Long studentId;
    @Pattern(regexp = ("NOTHING|PENDING|ACCEPTED|REFUSED"))
    @NotBlank
    String status;
    @Builder.Default
    String refusalMessage = "";
}
