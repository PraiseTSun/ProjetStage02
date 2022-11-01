package projet.projetstage02.DTO;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class EvaluationDTO {
    @Min(1)
    long contractId;
    @NotBlank
    String tachesAnnonces;
    @NotBlank
    String integration;
    @NotBlank
    String tempsReelConsacre;
    @NotBlank
    String environementTravail;
    @NotBlank
    String climatTravail;
    @NotBlank
    String milieuDeStage;
    @Min(1)
    long heureTotalPremierMois;
    @Min(1)
    long heureTotalDeuxiemeMois;
    @Min(1)
    long heureTotalTroisiemeMois;
    @NotBlank
    String communicationAvecSuperviser;
    @NotBlank
    String equipementFourni;
    @NotBlank
    String volumeDeTravail;
    @NotBlank
    String commentaires;
    @NotBlank
    String signature;
    @NotBlank
    String dateSignature;
}
