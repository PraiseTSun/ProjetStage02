package projet.projetstage02.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Problem {
    @GeneratedValue
    @Min(1)
    @Id
    private long id;
    @Pattern(regexp = "Connexion|Inscription|Offres de stage|Contrats|Autre")
    @NotBlank
    private String problemCategory;
    @NotBlank
    private String problemDetails;
    private boolean resolved;
}
