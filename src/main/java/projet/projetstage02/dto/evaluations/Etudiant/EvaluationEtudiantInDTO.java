package projet.projetstage02.dto.evaluations.Etudiant;

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
public class EvaluationEtudiantInDTO {
    @Min(1)
    private long contractId;
    //Productivite
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String travailEfficace;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String comprendRapidement;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String rythmeSoutenu;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String etablirPriorites;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String respecteEcheances;
    private String commentairesProductivite;
    //Qualite
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String respecteMandatsDemandes;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String attentionAuxDetails;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String doubleCheckTravail;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String occasionsDePerfectionnement;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String bonneAnalyseProblemes;
    private String commentairesQualite;
    //Relations Interpersonnelles
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String planifieTravail;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String contactsFaciles;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String travailEnEquipe;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String adapteCulture;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String accepteCritiques;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String respecteAutres;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String ecouteActiveComprendrePDVautre;
    private String commentairesRelationsInterpersonnelles;
    //Habilites
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String interetMotivation;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String exprimeIdees;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String initiative;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String travailSecuritaire;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String responsableAutonome;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePrononcer")
    private String ponctuel;
    private String commentairesHabilites;
    //Appreciation Generale
    @Pattern(regexp = "depassentBeacoupAttentes|depassentAttentes|repondentAttentes|repondentPartiellementAttentes|repondentPasAttentes")
    @NotBlank
    private String habiletesDemontres;
    private String commentairesAppreciation;
    @Pattern(regexp = "oui|non")
    @NotBlank
    private String discuteAvecStagiaire;
    @Min(0)
    private long heuresEncadrement;
    //Acueillir pour prochain stage
    @Pattern(regexp = "oui|non|peutEtre")
    @NotBlank
    private String acueillirPourProchainStage;
    @NotBlank
    private String formationTechniqueSuffisante;
    @NotBlank
    private String signature;
    @NotBlank
    @Pattern(regexp = "^(19|20[0-9][0-9])-(0[0-9]|1[0-2])-([0-2][0-9]|3[0-1])$")
    private String dateSignature;
}
