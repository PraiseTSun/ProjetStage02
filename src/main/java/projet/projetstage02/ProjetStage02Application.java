package projet.projetstage02;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import projet.projetstage02.dto.SignatureInDTO;
import projet.projetstage02.dto.contracts.StageContractInDTO;
import projet.projetstage02.dto.contracts.StageContractOutDTO;
import projet.projetstage02.dto.evaluations.Etudiant.EvaluationEtudiantInDTO;
import projet.projetstage02.dto.evaluations.MillieuStage.MillieuStageEvaluationInDTO;
import projet.projetstage02.dto.offres.OffreInDTO;
import projet.projetstage02.dto.pdf.PdfDTO;
import projet.projetstage02.dto.users.CompanyDTO;
import projet.projetstage02.dto.users.GestionnaireDTO;
import projet.projetstage02.dto.users.Students.StudentInDTO;
import projet.projetstage02.dto.users.Students.StudentOutDTO;
import projet.projetstage02.model.AbstractUser;
import projet.projetstage02.model.AbstractUser.Department;
import projet.projetstage02.service.CompanyService;
import projet.projetstage02.service.GestionnaireService;
import projet.projetstage02.service.StudentService;

import java.time.LocalDate;

@SpringBootApplication
@AllArgsConstructor
public class ProjetStage02Application implements CommandLineRunner {

    private final byte[] TEST_PDF = new byte[]{
            37, 80, 68, 70, 45, 49, 46, 55, 10, 10, 49, 32, 48, 32, 111, 98, 106, 32, 32, 37, 32, 101, 110, 116,
            114, 121, 32, 112, 111, 105, 110, 116, 10, 60, 60, 10, 32, 32, 47, 84, 121, 112, 101, 32, 47, 67,
            97, 116, 97, 108, 111, 103, 10, 32, 32, 47, 80, 97, 103, 101, 115, 32, 50, 32, 48, 32, 82, 10, 62,
            62, 10, 101, 110, 100, 111, 98, 106, 10, 10, 50, 32, 48, 32, 111, 98, 106, 10, 60, 60, 10, 32, 32,
            47, 84, 121, 112, 101, 32, 47, 80, 97, 103, 101, 115, 10, 32, 32, 47, 77, 101, 100, 105, 97, 66,
            111, 120, 32, 91, 32, 48, 32, 48, 32, 50, 48, 48, 32, 50, 48, 48, 32, 93, 10, 32, 32, 47, 67, 111,
            117, 110, 116, 32, 49, 10, 32, 32, 47, 75, 105, 100, 115, 32, 91, 32, 51, 32, 48, 32, 82, 32, 93,
            10, 62, 62, 10, 101, 110, 100, 111, 98, 106, 10, 10, 51, 32, 48, 32, 111, 98, 106, 10, 60, 60, 10,
            32, 32, 47, 84, 121, 112, 101, 32, 47, 80, 97, 103, 101, 10, 32, 32, 47, 80, 97, 114, 101, 110, 116,
            32, 50, 32, 48, 32, 82, 10, 32, 32, 47, 82, 101, 115, 111, 117, 114, 99, 101, 115, 32, 60, 60, 10,
            32, 32, 32, 32, 47, 70, 111, 110, 116, 32, 60, 60, 10, 32, 32, 32, 32, 32, 32, 47, 70, 49, 32, 52,
            32, 48, 32, 82, 32, 10, 32, 32, 32, 32, 62, 62, 10, 32, 32, 62, 62, 10, 32, 32, 47, 67, 111, 110,
            116, 101, 110, 116, 115, 32, 53, 32, 48, 32, 82, 10, 62, 62, 10, 101, 110, 100, 111, 98, 106, 10,
            10, 52, 32, 48, 32, 111, 98, 106, 10, 60, 60, 10, 32, 32, 47, 84, 121, 112, 101, 32, 47, 70, 111,
            110, 116, 10, 32, 32, 47, 83, 117, 98, 116, 121, 112, 101, 32, 47, 84, 121, 112, 101, 49, 10, 32,
            32, 47, 66, 97, 115, 101, 70, 111, 110, 116, 32, 47, 84, 105, 109, 101, 115, 45, 82, 111, 109, 97,
            110, 10, 62, 62, 10, 101, 110, 100, 111, 98, 106, 10, 10, 53, 32, 48, 32, 111, 98, 106, 32, 32, 37,
            32, 112, 97, 103, 101, 32, 99, 111, 110, 116, 101, 110, 116, 10, 60, 60, 10, 32, 32, 47, 76, 101,
            110, 103, 116, 104, 32, 52, 52, 10, 62, 62, 10, 115, 116, 114, 101, 97, 109, 10, 66, 84, 10, 55, 48,
            32, 53, 48, 32, 84, 68, 10, 47, 70, 49, 32, 49, 50, 32, 84, 102, 10, 40, 72, 101, 108, 108, 111, 44,
            32, 119, 111, 114, 108, 100, 33, 41, 32, 84, 106, 10, 69, 84, 10, 101, 110, 100, 115, 116, 114, 101,
            97, 109, 10, 101, 110, 100, 111, 98, 106, 10, 10, 120, 114, 101, 102, 10, 48, 32, 54, 10, 48, 48,
            48, 48, 48, 48, 48, 48, 48, 48, 32, 54, 53, 53, 51, 53, 32, 102, 32, 10, 48, 48, 48, 48, 48, 48, 48,
            48, 49, 48, 32, 48, 48, 48, 48, 48, 32, 110, 32, 10, 48, 48, 48, 48, 48, 48, 48, 48, 55, 57, 32, 48,
            48, 48, 48, 48, 32, 110, 32, 10, 48, 48, 48, 48, 48, 48, 48, 49, 55, 51, 32, 48, 48, 48, 48, 48, 32,
            110, 32, 10, 48, 48, 48, 48, 48, 48, 48, 51, 48, 49, 32, 48, 48, 48, 48, 48, 32, 110, 32, 10, 48,
            48, 48, 48, 48, 48, 48, 51, 56, 48, 32, 48, 48, 48, 48, 48, 32, 110, 32, 10, 116, 114, 97, 105, 108,
            101, 114, 10, 60, 60, 10, 32, 32, 47, 83, 105, 122, 101, 32, 54, 10, 32, 32, 47, 82, 111, 111, 116,
            32, 49, 32, 48, 32, 82, 10, 62, 62, 10, 115, 116, 97, 114, 116, 120, 114, 101, 102, 10, 52, 57, 50,
            10, 37, 37, 69, 79, 70
    };
    private StudentService studentService;
    private CompanyService companyService;
    private GestionnaireService gestionnaireService;

    public static void main(String[] args) {
        SpringApplication.run(ProjetStage02Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        studentService.saveStudent("Samir", "Badi", "Samir@gmail.com", "cooldude",
                AbstractUser.Department.Informatique);
        StudentOutDTO student = studentService.getStudentById(1L);
        student.setEmailConfirmed(true);
        studentService.saveStudent(new StudentInDTO(student.toModel()));

        companyService.saveCompany("Bob", "Marley", "Bell", "Bob@bell.com", "bestcompany",
                AbstractUser.Department.Informatique);
        CompanyDTO company = companyService.getCompanyById(2L);
        company.setEmailConfirmed(true);
        companyService.saveCompany(company);

        gestionnaireService.saveGestionnaire("Dave", "Douch", "dave@gmail.ca", "cooldude");
        GestionnaireDTO gestionnaire = gestionnaireService.getGestionnaireById(3L);
        gestionnaire.setEmailConfirmed(true);
        gestionnaireService.saveGestionnaire(gestionnaire);

        studentService.saveStudent("Peter", "Griffin", "peter.griffin@quahog.com", "loislois",
                AbstractUser.Department.Informatique);
        StudentOutDTO student2 = studentService.getStudentByEmailPassword("peter.griffin@quahog.com", "loislois");
        student2.setEmailConfirmed(true);
        studentService.saveStudent(new StudentInDTO(student2.toModel()));

        long offreId = companyService.createOffre(OffreInDTO.builder()
                .adresse("123 Joe Road")
                .department(Department.Informatique.departement)
                .heureParSemaine(40)
                .companyId(company.getId())
                .dateStageDebut("2020-01-01")
                .dateStageFin("2020-01-01")
                .salaire(40)
                .session("Hiver 2023")
                .nomDeCompagnie("Bell")
                .companyId(company.getId())
                .position("Delivery Man")
                .pdf(TEST_PDF)
                .build());
        gestionnaireService.validateOfferById(offreId);

        studentService.uploadCurriculumVitae(PdfDTO.builder()
                .studentId(1L)
                .pdf(TEST_PDF)
                .build());

        studentService.uploadCurriculumVitae(PdfDTO.builder()
                .studentId(student2.getId())
                .pdf(TEST_PDF)
                .build());

        companyService.createOffre(
                OffreInDTO.builder()
                        .id(0L)
                        .nomDeCompagnie("Bell")
                        .companyId(company.getId())
                        .department("Techniques de linformatique")
                        .position("Support TI")
                        .heureParSemaine(35)
                        .salaire(35)
                        .dateStageDebut("2020-01-01")
                        .dateStageFin("2020-01-01")
                        .session("Hiver 2023")
                        .adresse("My Home")
                        .pdf(TEST_PDF)
                        .companyId(company.getId())
                        .token("notoken")
                        .valide(false)
                        .build()
        );
        companyService.createOffre(
                OffreInDTO.builder()
                        .id(0L)
                        .nomDeCompagnie("Bell")
                        .companyId(company.getId())
                        .department("Techniques de linformatique")
                        .position("Support TI")
                        .heureParSemaine(36)
                        .salaire(15)
                        .dateStageDebut("2020-01-01")
                        .dateStageFin("2020-01-01")
                        .session("Hiver 2023")
                        .companyId(company.getId())
                        .adresse("33 My Home")
                        .pdf(TEST_PDF)
                        .token("notoken")
                        .valide(false)
                        .build()
        );
        gestionnaireService.validateStudent(student.getId());
        gestionnaireService.validateStudent(student2.getId());
        gestionnaireService.validateCompany(company.getId());
        gestionnaireService.validateStudentCV(student.getId());
        studentService.createPostulation(student.getId(), offreId);
        studentService.createPostulation(student2.getId(), offreId);
        companyService.saveStudentApplicationAccepted(offreId, student.getId());
        companyService.saveStudentApplicationAccepted(offreId, student2.getId());
        StageContractOutDTO stageContract = gestionnaireService.createStageContract(StageContractInDTO.builder()
                .offerId(offreId)
                .studentId(1L)
                .build());
        gestionnaireService.createStageContract(StageContractInDTO.builder()
                .offerId(offreId)
                .studentId(student2.getId())
                .build());
        gestionnaireService.validateStudentCV(student2.getId());
        System.out.println(studentService.getStudentById(1L));
        System.out.println(companyService.getCompanyById(2L));
        System.out.println(gestionnaireService.getGestionnaireById(3L));
        System.out.println(gestionnaireService.getGestionnaireById(3L));
        System.out.println(studentService.getStudentByEmailPassword("Samir@gmail.com", "cooldude"));
        System.out.println(companyService.getCompanyByEmailPassword("Bob@bell.com", "bestcompany"));
        System.out.println(gestionnaireService.getGestionnaireByEmailPassword("dave@gmail.ca", "cooldude"));
        System.out.println(gestionnaireService.getUnvalidatedOffers());
        System.out.println(gestionnaireService.getUnvalidatedCVStudents());
        companyService.saveStudentApplicationAccepted(offreId, student.getId());

        SignatureInDTO signatureCompany = new SignatureInDTO("", company.getId(), stageContract.getContractId(), "dsfsfdfs");
        companyService.addSignatureToContract(signatureCompany);
        SignatureInDTO signatureStudent = new SignatureInDTO("", student.getId(), stageContract.getContractId(), "studanenn");
        studentService.addSignatureToContract(signatureStudent);
        String signature = "data:image/png;base64,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";

        gestionnaireService.evaluateStage(MillieuStageEvaluationInDTO.builder()
                .climatTravail("plutotEnAccord")
                .commentaires("plutotEnAccord")
                .communicationAvecSuperviser("plutotEnAccord")
                .contractId(stageContract.getContractId())
                .dateSignature("2021-05-01")
                .environementTravail("plutotEnAccord")
                .equipementFourni("plutotEnAccord")
                .heureTotalDeuxiemeMois(23)
                .heureTotalPremierMois(23)
                .heureTotalTroisiemeMois(23)
                .integration("plutotEnAccord")
                .milieuDeStage("plutotEnAccord")
                .tachesAnnonces("plutotEnAccord")
                .volumeDeTravail("plutotEnAccord")
                .tempsReelConsacre("plutotEnAccord")
                .signature(signature)
                .dateSignature(LocalDate.now().toString())
                .build());

        companyService.evaluateStudent(EvaluationEtudiantInDTO.builder()
                .accepteCritiques("plutotEnAccord")
                .acueillirPourProchainStage("oui")
                .adapteCulture("plutotEnAccord")
                .attentionAuxDetails("plutotEnAccord")
                .bonneAnalyseProblemes("plutotEnAccord")
                .commentairesHabilites("commentaire")
                .commentairesProductivite("Un long commentaire qui fait du sens. Il est long. looooooooooooooooooooooong. loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong. assez long.")
                .commentairesQualite("petit commentaire")
                .commentairesRelationsInterpersonnelles("commentaire")
                .commentairesAppreciation("commentaire")
                .comprendRapidement("plutotEnAccord")
                .contactsFaciles("plutotEnAccord")
                .contractId(stageContract.getContractId())
                .dateSignature("2021-05-01")
                .discuteAvecStagiaire("oui")
                .doubleCheckTravail("plutotEnAccord")
                .etablirPriorites("plutotEnAccord")
                .exprimeIdees("plutotEnAccord")
                .ecouteActiveComprendrePDVautre("plutotEnAccord")
                .formationTechniqueSuffisante("plutotEnAccord")
                .habiletesDemontres("repondentAttentes")
                .heuresEncadrement(145)
                .initiative("plutotEnAccord")
                .interetMotivation("plutotEnAccord")
                .occasionsDePerfectionnement("plutotEnAccord")
                .planifieTravail("plutotEnAccord")
                .ponctuel("plutotEnAccord")
                .respecteAutres("plutotEnAccord")
                .respecteEcheances("plutotEnAccord")
                .rythmeSoutenu("plutotEnAccord")
                .responsableAutonome("plutotEnAccord")
                .respecteMandatsDemandes("plutotEnAccord")
                .signature(signature)
                .travailEnEquipe("plutotEnAccord")
                .travailSecuritaire("plutotEnAccord")
                .travailEfficace("plutotEnAccord")
                .dateSignature(LocalDate.now().toString())
                .build());
        gestionnaireService.createEvaluationMillieuStagePDF(stageContract.getContractId());
        gestionnaireService.createEvaluationEtudiantPDF(stageContract.getContractId());
    }
}
