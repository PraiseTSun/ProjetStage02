package projet.projetstage02.dto.evaluations.MillieuStage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MillieuStageEvaluationInDTO {
    @Min(1)
    private long contractId;
    @NotBlank
    @Pattern(regexp = "Totalement en accord|Plutôt en accord|Plutôt en désaccord|Totalement en désaccord|Impossible de se prononcer")
    private String tachesAnnonces;
    @NotBlank
    @Pattern(regexp = "Totalement en accord|Plutôt en accord|Plutôt en désaccord|Totalement en désaccord|Impossible de se prononcer")
    private String integration;
    @NotBlank
    @Pattern(regexp = "Totalement en accord|Plutôt en accord|Plutôt en désaccord|Totalement en désaccord|Impossible de se prononcer")
    private String tempsReelConsacre;
    @NotBlank
    @Pattern(regexp = "Totalement en accord|Plutôt en accord|Plutôt en désaccord|Totalement en désaccord|Impossible de se prononcer")
    private String environementTravail;
    @NotBlank
    @Pattern(regexp = "Totalement en accord|Plutôt en accord|Plutôt en désaccord|Totalement en désaccord|Impossible de se prononcer")
    private String climatTravail;
    @NotBlank
    @Pattern(regexp = "Totalement en accord|Plutôt en accord|Plutôt en désaccord|Totalement en désaccord|Impossible de se prononcer")
    private String milieuDeStage;
    @Min(1)
    private long heureTotalPremierMois;
    @Min(1)
    private long heureTotalDeuxiemeMois;
    @Min(1)
    private long heureTotalTroisiemeMois;
    @NotBlank
    @Pattern(regexp = "Totalement en accord|Plutôt en accord|Plutôt en désaccord|Totalement en désaccord|Impossible de se prononcer")
    private String communicationAvecSuperviser;
    @NotBlank
    @Pattern(regexp = "Totalement en accord|Plutôt en accord|Plutôt en désaccord|Totalement en désaccord|Impossible de se prononcer")
    private String equipementFourni;
    @NotBlank
    @Pattern(regexp = "Totalement en accord|Plutôt en accord|Plutôt en désaccord|Totalement en désaccord|Impossible de se prononcer")
    private String volumeDeTravail;
    @NotBlank
    private String commentaires;
    @NotBlank
    private String signature;
    @NotBlank
    private String dateSignature;
}
