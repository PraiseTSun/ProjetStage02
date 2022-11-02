package projet.projetstage02.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.projetstage02.DTO.EvaluationInDTO;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Evaluation {
    @Id
    @Min(1)
    private long id;
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

    public Evaluation(EvaluationInDTO evaluationInDTO) {
        contractId = evaluationInDTO.getContractId();
        tachesAnnonces = evaluationInDTO.getTachesAnnonces();
        integration = evaluationInDTO.getIntegration();
        tempsReelConsacre = evaluationInDTO.getTempsReelConsacre();
        environementTravail = evaluationInDTO.getEnvironementTravail();
        climatTravail = evaluationInDTO.getClimatTravail();
        milieuDeStage = evaluationInDTO.getMilieuDeStage();
        heureTotalPremierMois = evaluationInDTO.getHeureTotalPremierMois();
        heureTotalDeuxiemeMois = evaluationInDTO.getHeureTotalDeuxiemeMois();
        heureTotalTroisiemeMois = evaluationInDTO.getHeureTotalTroisiemeMois();
        communicationAvecSuperviser = evaluationInDTO.getCommunicationAvecSuperviser();
        equipementFourni = evaluationInDTO.getEquipementFourni();
        volumeDeTravail = evaluationInDTO.getVolumeDeTravail();
        commentaires = evaluationInDTO.getCommentaires();
        signature = evaluationInDTO.getSignature();
        dateSignature = evaluationInDTO.getDateSignature();
    }
}
