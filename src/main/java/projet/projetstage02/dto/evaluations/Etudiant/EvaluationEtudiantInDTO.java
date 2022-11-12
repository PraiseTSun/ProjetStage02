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
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String travailEfficace;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String comprendRapidement;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String rythmeSoutenu;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String etablirPriorites;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String respecteEcheances;
    @NotBlank
    private String commentairesProductivite;
    //Qualite
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String respecteMandatsDemandes;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String attentionAuxDetails;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String doubleCheckTravail;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String occasionsDePerfectionnement;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String bonneAnalyseProblemes;
    @NotBlank
    private String commentairesQualite;
    //Relations Interpersonnelles
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String planifieTravail;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String contactsFaciles;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String travailEnEquipe;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String adapteCulture;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String accepteCritiques;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String respecteAutres;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String ecouteActiveComprendrePDVautre;
    @NotBlank
    private String commentairesRelationsInterpersonnelles;
    //Habilites
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String interetMotivation;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String exprimeIdees;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String initiative;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String travailSecuritaire;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String responsableAutonome;
    @NotBlank
    @Pattern(regexp = "totalementEnAccord|plutotEnAccord|plutotEnDesaccord|totalementEnDesaccord|impossibleDeSePronnoncer")
    private String ponctuel;
    @NotBlank
    private String commentairesHabilites;
    //Appreciation Generale
    @Pattern(regexp = "depassentBeacoupAttentes|depassentAttentes|repondentAttentes|repondentPartiellementAttentes|repondentPasAttentes")
    @NotBlank
    private String habiletesDemontres;
    @NotBlank
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
