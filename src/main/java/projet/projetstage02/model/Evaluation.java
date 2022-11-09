package projet.projetstage02.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.projetstage02.dto.evaluations.MillieuStage.MillieuStageEvaluationInDTO;

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
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String tachesAnnonces;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String integration;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String tempsReelConsacre;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String environementTravail;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String climatTravail;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String milieuDeStage;
    @Min(1)
    private long heureTotalPremierMois;
    @Min(1)
    private long heureTotalDeuxiemeMois;
    @Min(1)
    private long heureTotalTroisiemeMois;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String communicationAvecSuperviser;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String equipementFourni;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String volumeDeTravail;
    @NotBlank
    private String commentaires;
    @NotBlank
    private String signature;
    @NotBlank
    private String dateSignature;

    public Evaluation(MillieuStageEvaluationInDTO millieuStageEvaluationInDTO) {
        contractId = millieuStageEvaluationInDTO.getContractId();
        tachesAnnonces = millieuStageEvaluationInDTO.getTachesAnnonces();
        integration = millieuStageEvaluationInDTO.getIntegration();
        tempsReelConsacre = millieuStageEvaluationInDTO.getTempsReelConsacre();
        environementTravail = millieuStageEvaluationInDTO.getEnvironementTravail();
        climatTravail = millieuStageEvaluationInDTO.getClimatTravail();
        milieuDeStage = millieuStageEvaluationInDTO.getMilieuDeStage();
        heureTotalPremierMois = millieuStageEvaluationInDTO.getHeureTotalPremierMois();
        heureTotalDeuxiemeMois = millieuStageEvaluationInDTO.getHeureTotalDeuxiemeMois();
        heureTotalTroisiemeMois = millieuStageEvaluationInDTO.getHeureTotalTroisiemeMois();
        communicationAvecSuperviser = millieuStageEvaluationInDTO.getCommunicationAvecSuperviser();
        equipementFourni = millieuStageEvaluationInDTO.getEquipementFourni();
        volumeDeTravail = millieuStageEvaluationInDTO.getVolumeDeTravail();
        commentaires = millieuStageEvaluationInDTO.getCommentaires();
        signature = millieuStageEvaluationInDTO.getSignature();
        dateSignature = millieuStageEvaluationInDTO.getDateSignature();
    }
}
