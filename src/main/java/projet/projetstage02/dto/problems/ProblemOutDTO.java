package projet.projetstage02.dto.problems;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.projetstage02.model.Problem;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemOutDTO {
    private long id;
    @Pattern(regexp = "Connexion|Inscription|Offres de stage|Contrats|Autre")
    private String problemCategory;
    private String problemDetails;
    private boolean resolved;

    public ProblemOutDTO(Problem problem) {
        this.id = problem.getId();
        this.problemCategory = problem.getProblemCategory();
        this.problemDetails = problem.getProblemDetails();
        this.resolved = problem.isResolved();
    }
}
