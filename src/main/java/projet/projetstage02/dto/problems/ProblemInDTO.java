package projet.projetstage02.dto.problems;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.projetstage02.model.Problem;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProblemInDTO {
    @Pattern(regexp = "Connexion|Inscription|Offres de stage|Contrats|Autre")
    @NotBlank
    private String problemCategory;
    @NotBlank
    private String problemDetails;

    public Problem toModel() {
        return Problem.builder()
                .problemCategory(this.problemCategory)
                .problemDetails(this.problemDetails)
                .resolved(false)
                .build();
    }
}
