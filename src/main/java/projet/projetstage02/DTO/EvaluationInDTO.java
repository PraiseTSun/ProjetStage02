package projet.projetstage02.DTO;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class EvaluationInDTO {
    @Min(1)
    private long contractId;
    @NotBlank
    private String tachesAnnonces;
    @NotBlank
    private String integration;
    @NotBlank
    private String tempsReelConsacre;
    @NotBlank
    private String environementTravail;
    @NotBlank
    private String climatTravail;
    @NotBlank
    private String milieuDeStage;
    @Min(1)
    private long heureTotalPremierMois;
    @Min(1)
    private long heureTotalDeuxiemeMois;
    @Min(1)
    private long heureTotalTroisiemeMois;
    @NotBlank
    private String communicationAvecSuperviser;
    @NotBlank
    private String equipementFourni;
    @NotBlank
    private String volumeDeTravail;
    @NotBlank
    private String commentaires;
    @NotBlank
    private String signature;
    @NotBlank
    private String dateSignature;
}
