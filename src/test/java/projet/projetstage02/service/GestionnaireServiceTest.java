package projet.projetstage02.service;

import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projet.projetstage02.dto.SignatureInDTO;
import projet.projetstage02.dto.contracts.ContractsDTO;
import projet.projetstage02.dto.contracts.StageContractInDTO;
import projet.projetstage02.dto.contracts.StageContractOutDTO;
import projet.projetstage02.dto.evaluations.Etudiant.EvaluationEtudiantInDTO;
import projet.projetstage02.dto.evaluations.EvaluationInfoDTO;
import projet.projetstage02.dto.evaluations.MillieuStage.MillieuStageEvaluationInDTO;
import projet.projetstage02.dto.offres.OffreOutDTO;
import projet.projetstage02.dto.pdf.PdfOutDTO;
import projet.projetstage02.dto.problems.ProblemOutDTO;
import projet.projetstage02.dto.users.CompanyDTO;
import projet.projetstage02.dto.users.GestionnaireDTO;
import projet.projetstage02.dto.users.Students.StudentOutDTO;
import projet.projetstage02.exception.*;
import projet.projetstage02.model.*;
import projet.projetstage02.repository.*;

import java.util.*;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static projet.projetstage02.model.AbstractUser.Department.Informatique;
import static projet.projetstage02.utils.TimeUtil.currentTimestamp;

@ExtendWith(MockitoExtension.class)
public class GestionnaireServiceTest {
    @InjectMocks
    private GestionnaireService gestionnaireService;
    @Mock
    private GestionnaireRepository gestionnaireRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private OffreRepository offreRepository;
    @Mock
    private CvStatusRepository cvStatusRepository;
    @Mock
    private StageContractRepository stageContractRepository;
    @Mock
    private ApplicationAcceptationRepository applicationAcceptationRepository;
    @Mock
    private EvaluationMillieuStageRepository evaluationMillieuStageRepository;
    @Mock
    private EvaluationMillieuStagePDFRepository evaluationMillieuStagePDFRepository;

    @Mock
    private EvaluationEtudiantRepository evaluationEtudiantRepository;
    @Mock
    private EvaluationEtudiantPDFRepository evaluationEtudiantPDFRepository;
    @Mock
    private ProblemsRepository problemsRepository;

    private Gestionnaire gestionnaireTest;
    private Company companyTest;
    private Student studentTest;
    private Offre offerTest;
    private CvStatus cvStatus;
    private StageContract stageContract;
    private ApplicationAcceptation applicationAcceptationTest;
    private Problem problemTest;
    private MillieuStageEvaluationInDTO millieuStageEvaluationInDTO;

    private StageContractInDTO stageContractInDTO;
    private EvaluationEtudiantInDTO evaluationEtudiantInDTO;

    @BeforeEach
    void beforeEach() {
        String signature = "data:image/png;base64,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";

        problemTest = new Problem();
        gestionnaireTest = new Gestionnaire(
                "prenom",
                "nom",
                "email@email.com",
                "password");
        gestionnaireTest.setId(1L);

        companyTest = new Company(
                "prenom",
                "nom",
                "email@email.com",
                "password",
                AbstractUser.Department.Transport,
                "Company Test");
        companyTest.setId(2L);

        studentTest = new Student(
                "prenom",
                "nom",
                "email@email.com",
                "password",
                Informatique);
        studentTest.setId(3L);

        offerTest = Offre.builder()
                .id(4L)
                .nomDeCompagnie("Company Test")
                .department(Informatique)
                .position("Stagiaire test backend")
                .heureParSemaine(40)
                .salaire(40)
                .dateStageDebut("2020-01-01")
                .dateStageFin("01-01-2021")
                .session(Offre.currentSession())
                .adresse("69 shitty street")
                .pdf(new byte[0])
                .valide(false)
                .build();
        cvStatus = CvStatus.builder().build();

        stageContract = StageContract.builder()
                .id(5L)
                .studentId(studentTest.getId())
                .offerId(offerTest.getId())
                .companyId(companyTest.getId())
                .session(offerTest.getSession())
                .description("description")
                .build();

        stageContractInDTO = StageContractInDTO.builder()
                .studentId(studentTest.getId())
                .offerId(offerTest.getId())
                .build();

        applicationAcceptationTest = ApplicationAcceptation.builder()
                .id(6L)
                .studentId(studentTest.getId())
                .studentName(studentTest.getFirstName() + " " + studentTest.getLastName())
                .offerId(offerTest.getId())
                .companyName(companyTest.getCompanyName())
                .build();

        millieuStageEvaluationInDTO = MillieuStageEvaluationInDTO.builder()
                .climatTravail("plutotEnAccord")
                .commentaires("plutotEnAccord")
                .communicationAvecSuperviser("plutotEnAccord")
                .contractId(1L)
                .dateSignature("2021-05-01")
                .environementTravail("plutotEnAccord")
                .equipementFourni("plutotEnAccord")
                .heureTotalDeuxiemeMois(23)
                .heureTotalPremierMois(23)
                .heureTotalTroisiemeMois(23)
                .integration("plutotEnAccord")
                .milieuDeStage("plutotEnDesccord")
                .tachesAnnonces("plutotEnAccord")
                .volumeDeTravail("plutotEnAccord")
                .tempsReelConsacre("plutotEnAccord")
                .signature(signature)
                .build();

        evaluationEtudiantInDTO = EvaluationEtudiantInDTO.builder()
                .accepteCritiques("plutotEnAccord")
                .acueillirPourProchainStage("peutEtre")
                .adapteCulture("plutotEnAccord")
                .attentionAuxDetails("plutotEnAccord")
                .bonneAnalyseProblemes("plutotEnAccord")
                .commentairesHabilites("plutotEnAccord")
                .commentairesProductivite("plutotEnAccord")
                .commentairesQualite("plutotEnAccord")
                .commentairesAppreciation("plutotEnAccord")
                .comprendRapidement("plutotEnAccord")
                .contactsFaciles("plutotEnAccord")
                .commentairesRelationsInterpersonnelles("plutotEnAccord")
                .contractId(1L)
                .dateSignature("2021-05-01")
                .discuteAvecStagiaire("oui")
                .doubleCheckTravail("plutotEnAccord")
                .etablirPriorites("plutotEnDesaccord")
                .exprimeIdees("impossibleDeSePrononcer")
                .ecouteActiveComprendrePDVautre("totalementEnDesaccord")
                .formationTechniqueSuffisante("totalementEnAccord")
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
                .build();
    }

    @Test
    public void testSaveGestionnaireHappyDay() {
        // Arrange
        when(gestionnaireRepository.save(any())).thenReturn(gestionnaireTest);

        // Act
        long id = gestionnaireService.saveGestionnaire(new GestionnaireDTO(gestionnaireTest));

        // Assert
        verify(gestionnaireRepository, times(1)).save(any());
        assertThat(id).isEqualTo(gestionnaireTest.getId());
    }

    @Test
    public void testGetGestionnaireByIdHappyDay() throws Exception {
        // Arrange
        when(gestionnaireRepository.findById(anyLong())).thenReturn(Optional.of(gestionnaireTest));

        // Act
        GestionnaireDTO dto = gestionnaireService.getGestionnaireById(1L);

        // Assert
        assertThat(dto.toModel()).isEqualTo(gestionnaireTest);
    }

    @Test
    public void testGetGestionnaireByIdNotFound() {
        // Arrange
        when(gestionnaireRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            gestionnaireService.getGestionnaireById(1L);
        } catch (NonExistentEntityException e) {
            // Assert
            return;
        }
        fail("NonExistentUserException not caught");
    }

    @Test
    public void testValidateCompanySuccess() throws Exception {
        // Arrange
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(companyTest));

        // Act
        gestionnaireService.validateCompany(1L);

        // Assert
        assertThat(companyTest.isConfirm()).isTrue();
    }

    @Test
    public void testValidateCompanyNotFound() {
        // Arrange
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            gestionnaireService.validateCompany(1L);
        } catch (NonExistentEntityException e) {
            return;
        }
        fail("NonExistentUserException not caught");
    }

    @Test
    public void testValidateStudentSuccess() throws Exception {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));

        // Act
        gestionnaireService.validateStudent(1L);

        // Assert
        assertThat(studentTest.isConfirm()).isTrue();
    }

    @Test
    public void testValidateStudentNotFound() {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            gestionnaireService.validateStudent(1L);
        } catch (NonExistentEntityException e) {
            return;
        }

        fail("NonExistentUserException not caught");
    }

    @Test
    public void testRemoveCompanySuccess() throws Exception {
        // Arrange
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(companyTest));
        doNothing().when(companyRepository).delete(any());

        // Act
        gestionnaireService.removeCompany(1L);

        // Assert
        verify(companyRepository).delete(companyTest);
    }

    @Test
    public void testRemoveCompanyNotFound() {
        // Arrange
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            gestionnaireService.removeCompany(1L);
        } catch (NonExistentEntityException e) {
            return;
        }

        fail("NonExistentUserException not caught");
    }

    @Test
    public void testRemoveStudentSuccess() throws Exception {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));
        doNothing().when(studentRepository).delete(any());

        // Act
        gestionnaireService.removeStudent(1L);

        // Assert
        verify(studentRepository).delete(studentTest);
    }

    @Test
    public void testRemoveStudentNotFound() {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            gestionnaireService.removeStudent(1L);
        } catch (NonExistentEntityException e) {
            return;
        }
        fail("NonExistentUserException not caught");
    }

    @Test
    public void testGetUnvalidatedOffersHappyDay() {
        List<Offre> offers = List.of(
                Offre.builder().session("Hiver 2010").department(Informatique).build(),
                Offre.builder().session("Hiver 2010").department(Informatique).build(),
                Offre.builder().session("Hiver 2023").department(Informatique).build(),
                Offre.builder().session("Hiver 2023").department(Informatique).build()
        );
        when(offreRepository.findAll()).thenReturn(offers);
        final List<OffreOutDTO> offersDto = gestionnaireService.getUnvalidatedOffers();

        assertThat(offersDto).hasSize(2);
    }

    @Test
    public void testGetValidatedOffersDifferentYearsHappyDay() {
        List<Offre> offers = List.of(
                Offre.builder().session("Hiver 2010").valide(true).department(Informatique).build(),
                Offre.builder().session("Hiver 2010").valide(true).department(Informatique).build(),
                Offre.builder().session("Hiver 2022").department(Informatique).build(),
                Offre.builder().session("Hiver 2022").valide(true).department(Informatique).build(),
                Offre.builder().session("Hiver 2023").valide(true).department(Informatique).build()
        );
        when(offreRepository.findAll()).thenReturn(offers);

        final List<OffreOutDTO> offers2022 = gestionnaireService.getValidatedOffers(2022);
        final List<OffreOutDTO> offers2023 = gestionnaireService.getValidatedOffers(2023);
        final List<OffreOutDTO> offers2010 = gestionnaireService.getValidatedOffers(2010);

        assertThat(offers2022).hasSize(1);
        assertThat(offers2023).hasSize(1);
        assertThat(offers2010).hasSize(2);
    }

    @Test
    public void testOffreNotValidated() {
        // Arrange
        List<Offre> offres = new ArrayList<>();
        Offre offre = new Offre();
        offre.setDepartment(Informatique);
        offre.setSession("Hiver 2022");
        offres.add(offre);
        offre = new Offre();
        offre.setDepartment(Informatique);
        offre.setSession("Hiver 2023");
        offres.add(offre);
        offre = new Offre();
        offre.setDepartment(Informatique);
        offre.setSession("Hiver 2023");
        offres.add(offre);

        offre = new Offre();
        offre.setDepartment(Informatique);
        offre.setValide(true);
        offres.add(offre);

        when(offreRepository.findAll()).thenReturn(offres);

        // Act
        final List<OffreOutDTO> noneValidateOffers = gestionnaireService.getUnvalidatedOffers();

        // Assert
        assertThat(noneValidateOffers.size()).isEqualTo(2);
    }

    @Test
    public void testValidateOfferByIdSuccess() throws Exception {
        // Arrange
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offerTest));

        // Act
        final OffreOutDTO offreInDTO = gestionnaireService.validateOfferById(1L);

        // Assert
        assertThat(offreInDTO.isValide()).isTrue();
    }

    @Test
    public void testValidateOfferByIdNotFound() throws Exception {
        // Arrange
        when(offreRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            gestionnaireService.validateOfferById(1L);
        } catch (NonExistentOfferExeption e) {
            return;
        }
        fail("NonExistentOfferException not caught");

    }

    @Test
    public void testValidateOfferExpiredOfferException() throws NonExistentOfferExeption {
        // Arrange
        offerTest.setSession("Hiver 2019");
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offerTest));

        // Act
        try {
            gestionnaireService.validateOfferById(1L);
        } catch (ExpiredSessionException e) {
            return;
        }
        fail("ExpiredSessionException not caught");

    }

    @Test
    public void testRemoveOfferByIdSuccess() throws NonExistentOfferExeption {
        // Arrange
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offerTest));
        doNothing().when(offreRepository).delete(any());

        // Act
        gestionnaireService.removeOfferById(1L);

        // Assert
        verify(offreRepository).delete(offerTest);
    }

    @Test
    public void testRemoveOfferByIdNotFound() {
        // Arrange
        when(offreRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            gestionnaireService.removeOfferById(1L);
        } catch (NonExistentOfferExeption e) {
            return;
        }
        fail("NonExistentOfferException not caught");
    }

    @Test
    public void testGetUnvalidatedStudentsHappyDay() {
        // Arrange
        List<Student> students = new ArrayList<>();

        Student student = new Student();
        student.setDepartment(Informatique);
        students.add(student);

        student = new Student();
        student.setDepartment(Informatique);
        student.setEmailConfirmed(true);
        students.add(student);

        student = new Student();
        student.setDepartment(Informatique);
        student.setEmailConfirmed(true);
        students.add(student);

        student = new Student();
        student.setDepartment(Informatique);
        student.setEmailConfirmed(true);
        student.setConfirm(true);
        students.add(student);

        when(studentRepository.findAll()).thenReturn(students);

        // Act
        List<StudentOutDTO> unvalidatedStudents = gestionnaireService.getUnvalidatedStudents();

        // Assert
        assertThat(unvalidatedStudents.size()).isEqualTo(2);
    }

    @Test
    public void testGetUnvalidatedCompaniesHappyDay() {
        // Arrange
        List<Company> companies = new ArrayList<>();

        Company company = new Company();
        company.setDepartment(Informatique);
        companies.add(company);

        company = new Company();
        company.setDepartment(Informatique);
        company.setEmailConfirmed(true);
        companies.add(company);

        company = new Company();
        company.setDepartment(Informatique);
        company.setEmailConfirmed(true);
        companies.add(company);

        company = new Company();
        company.setDepartment(Informatique);
        company.setEmailConfirmed(true);
        company.setConfirm(true);
        companies.add(company);

        when(companyRepository.findAll()).thenReturn(companies);

        // Act
        List<CompanyDTO> unvalidatedCompanies = gestionnaireService.getUnvalidatedCompanies();

        // Assert
        assertThat(unvalidatedCompanies.size()).isEqualTo(2);
    }

    @Test
    void testGetOffrePdfByIdHappyDay() throws NonExistentOfferExeption {
        // Arrange
        when(offreRepository.findById(any())).thenReturn(Optional.of(offerTest));

        // Act
        PdfOutDTO pdf = gestionnaireService.getOffrePdfById(1L);

        // Assert
        assertThat(pdf.getPdf()).isEqualTo(Arrays.toString(offerTest.getPdf()).replaceAll("\\s+", ""));
    }

    @Test
    void testGetOffrePdfByIdNotFound() {
        // Arrange
        when(offreRepository.findById(any())).thenReturn(Optional.empty());

        // Act
        try {
            gestionnaireService.getOffrePdfById(1L);
        } catch (NonExistentOfferExeption e) {
            return;
        }
        fail("NonExistentOfferException not caught");
    }

    @Test
    void testInvalidGestionnaireHappyDay() throws Exception {
        // Arrange
        gestionnaireTest.setInscriptionTimestamp(0);
        when(gestionnaireRepository.findByEmail(any())).thenReturn(Optional.of(gestionnaireTest));

        // Act
        gestionnaireService.isGestionnaireInvalid(gestionnaireTest.getEmail());

        // Assert
        verify(gestionnaireRepository, times(1)).delete(any());
    }

    @Test
    void testInvalidGestionnaireThrowsException() {
        // Arrange
        gestionnaireTest.setInscriptionTimestamp(0);
        when(gestionnaireRepository.findByEmail(any())).thenReturn(Optional.of(gestionnaireTest), Optional.empty());

        // Act
        try {
            gestionnaireService.isGestionnaireInvalid(gestionnaireTest.getEmail());
        } catch (NonExistentEntityException e) {
            return;
        }
        // Assert
        fail("NonExistentEntityException not thrown");
    }

    @Test
    void testInvalidGestionnaireReturnFalse() {
        // Arrange
        gestionnaireTest.setInscriptionTimestamp(currentTimestamp());
        when(gestionnaireRepository.findByEmail(any())).thenReturn(Optional.of(gestionnaireTest), Optional.empty());

        // Act
        try {
            gestionnaireService.isGestionnaireInvalid(gestionnaireTest.getEmail());
        } catch (NonExistentEntityException e) {
            return;
        }
        // Assert
        fail("NonExistentEntityException not thrown");
    }

    @Test
    void testGetUnvalidatedStudentCV() {
        // Arrange
        List<Student> students = new ArrayList<>();
        students.add(studentTest);
        studentTest.setCvToValidate(new byte[]{1, 2, 3});
        studentTest.setConfirm(true);

        Student studentCvValidated = new Student();
        studentCvValidated.setConfirm(true);

        students.add(studentCvValidated);
        when(studentRepository.findAll()).thenReturn(students);

        // Act
        List<StudentOutDTO> unvalidatedStudentCV = gestionnaireService.getUnvalidatedCVStudents();
        // Assert
        assertThat(unvalidatedStudentCV.get(0).getEmail()).isEqualTo(studentTest.getEmail());
        assertThat(unvalidatedStudentCV.get(0).getFirstName()).isEqualTo(studentTest.getFirstName());
        assertThat(unvalidatedStudentCV.get(0).getCvToValidate()).isNotEmpty();
        assertThat(unvalidatedStudentCV.size()).isEqualTo(1);
    }

    @Test
    void testValidateStudentCVHappyDay() throws Exception {
        // Arrange
        studentTest.setCvToValidate(new byte[0]);
        cvStatus.setStatus("PENDING");
        when(cvStatusRepository.findById(anyLong())).thenReturn(Optional.of(cvStatus));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));

        // Act
        StudentOutDTO studentDTO = gestionnaireService.validateStudentCV(1L);

        // Assert
        assertThat(studentDTO.getFirstName()).isEqualTo(studentTest.getFirstName());
        assertThat(studentDTO.getCv()).isEqualTo("[]");
        assertThat(studentDTO.getCvToValidate()).isEqualTo("[]");
    }

    @Test
    void testValidateStudentCVNotFound() throws Exception {
        // Arrange
        cvStatus.setStatus("PENDING");

        // Act
        try {
            gestionnaireService.validateStudentCV(1L);
        } catch (NonExistentEntityException e) {
            return;
        }
        fail("NonExistentUserException not caught");
    }

    @Test
    void testValidateStudentCVInvalidStatus() throws Exception {
        // Arrange
        cvStatus.setStatus("ACCEPTED");
        when(cvStatusRepository.findById(anyLong())).thenReturn(Optional.of(cvStatus));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));

        // Act
        try {
            gestionnaireService.validateStudentCV(1L);
        } catch (InvalidStatusException e) {
            return;
        }
        fail("InvalidStatusException not caught");
    }

    @Test
    void testRemoveStudentCvValidationSuccess() throws Exception {
        // Arrange
        cvStatus.setStatus("PENDING");
        when(cvStatusRepository.findById(anyLong())).thenReturn(Optional.of(cvStatus));
        studentTest.setCvToValidate(new byte[0]);
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));

        // Act
        StudentOutDTO studentDTO = gestionnaireService.removeStudentCvValidation(1L, "Refused");

        // Assert
        assertThat(studentDTO.getEmail()).isEqualTo(studentTest.getEmail());
        assertThat(studentDTO.getCvToValidate()).isEqualTo("[]");
        assertThat(cvStatus.getRefusalMessage()).isEqualTo("Refused");
        assertThat(cvStatus.getStatus()).isEqualTo("REFUSED");
    }

    @Test
    void testRemoveStudentCvValidationNotFound() throws Exception {
        // Arrange
        cvStatus.setStatus("PENDING");
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            gestionnaireService.removeStudentCvValidation(1L, "Refused");
        } catch (NonExistentEntityException e) {
            return;
        }
        fail("NonExistentUserException not caught");
    }

    @Test
    void testRemoveStudentCvValidationInvalidStatus() throws Exception {
        // Arrange
        cvStatus.setStatus("ACCEPTED");
        when(cvStatusRepository.findById(anyLong())).thenReturn(Optional.of(cvStatus));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));

        // Act
        try {
            gestionnaireService.removeStudentCvValidation(1L, "Refused");
        } catch (InvalidStatusException e) {
            return;
        }
        fail("InvalidStatusException not caught");
    }

    @Test
    void testGetStudentCvToValidateHappyDay() throws Exception {
        // Arrange
        String result = "[72,101,108,108,111,32,87,111,114,100]";
        byte[] stored = HexFormat.of().parseHex("48656c6c6f20576f7264");
        studentTest.setCvToValidate(stored);
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));

        // Act
        PdfOutDTO cv = gestionnaireService.getStudentCvToValidate(1L);

        //
        assertThat(cv.getPdf()).isEqualTo(result);
    }

    @Test
    void testGetStudentCvToValidateNotFound() {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            gestionnaireService.getStudentCvToValidate(1L);
        } catch (NonExistentEntityException e) {
            return;
        }
        fail("NonExistentUserException not caught");
    }

    @Test
    void testCreateStageContractHappyDay() throws Exception {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offerTest));
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(companyTest));
        when(applicationAcceptationRepository.findByOfferIdAndStudentId(anyLong(), anyLong()))
                .thenReturn(Optional.of(applicationAcceptationTest));
        when(stageContractRepository.findByStudentIdAndCompanyIdAndOfferId(anyLong(), anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        when(stageContractRepository.save(any())).thenReturn(stageContract);
        // Act
        StageContractOutDTO dto = gestionnaireService.createStageContract(stageContractInDTO);

        // Assert
        verify(stageContractRepository, times(1)).save(any());
        verify(applicationAcceptationRepository, times(1)).delete(any());
        assertThat(dto.getStudentId()).isEqualTo(studentTest.getId());
        assertThat(dto.getOfferId()).isEqualTo(offerTest.getId());
        assertThat(dto.getCompanyId()).isEqualTo(companyTest.getId());
        assertThat(dto.getSession()).isEqualTo(Offre.currentSession());
    }

    @Test
    void testCreateStageContractConflict() {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offerTest));
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(companyTest));
        when(stageContractRepository.findByStudentIdAndCompanyIdAndOfferId(anyLong(), anyLong(), anyLong()))
                .thenReturn(Optional.of(stageContract));

        // Act
        try {
            gestionnaireService.createStageContract(stageContractInDTO);
        } catch (AlreadyExistingStageContractException e) {
            return;
        } catch (Exception ignored) {
        }
        fail("Failed to catch the error AlreadyExistingStageContractException!");
    }

    @Test
    void testCreateStageContractCompanyNotFound() {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offerTest));
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            gestionnaireService.createStageContract(stageContractInDTO);
        } catch (NonExistentEntityException e) {
            return;
        } catch (Exception ignored) {
        }
        fail("Failed to catch the error NonExistentEntityException!");
    }

    @Test
    void testCreateStageContractOfferNotFound() {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            gestionnaireService.createStageContract(stageContractInDTO);
        } catch (NonExistentOfferExeption e) {
            return;
        } catch (Exception ignored) {
        }
        fail("Failed to catch the error NonExistentOfferExeption!");
    }

    @Test
    void testCreateStageContractStudentNotFound() {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            gestionnaireService.createStageContract(stageContractInDTO);
        } catch (NonExistentEntityException e) {
            return;
        } catch (Exception ignored) {
        }
        fail("Failed to catch the error NonExistentEntityException!");
    }

    @Test
    void testGetUnvalidatedAcceptationHappyDay() {
        when(applicationAcceptationRepository.findAll()).thenReturn(new ArrayList<>() {{
            add(applicationAcceptationTest);
            add(applicationAcceptationTest);
            add(applicationAcceptationTest);
        }});
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offerTest));
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(companyTest));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));

        ContractsDTO dto = gestionnaireService.getContractsToCreate();

        assertThat(dto.size()).isEqualTo(3);
    }


    @Test
    void testGetMillieuEvaluationInfoForContractHappyDay() throws NonExistentOfferExeption, NonExistentEntityException {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offerTest));
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(companyTest));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));

        EvaluationInfoDTO dto = gestionnaireService.getEvaluationInfoForContract(1L);

        assertThat(dto.getAdresse()).isEqualTo(offerTest.getAdresse());
        assertThat(dto.getNomCompagnie()).isEqualTo(companyTest.getCompanyName());
        assertThat(dto.getPrenomEtudiant()).isEqualTo(studentTest.getFirstName());
        assertThat(dto.getNomEtudiant()).isEqualTo(studentTest.getLastName());
        assertThat(dto.getSalaire()).isEqualTo(offerTest.getSalaire());
        assertThat(dto.getPoste()).isEqualTo(offerTest.getPosition());
        assertThat(dto.getAdresse()).isEqualTo(offerTest.getAdresse());
        assertThat(dto.getDateStageDebut()).isEqualTo(offerTest.getDateStageDebut());
        assertThat(dto.getDateStageFin()).isEqualTo(offerTest.getDateStageFin());
        assertThat(dto.getEmailCompagnie()).isEqualTo(companyTest.getEmail());
        assertThat(dto.getEmailEtudiant()).isEqualTo(studentTest.getEmail());
        assertThat(dto.getDepartement()).isEqualTo(offerTest.getDepartment().departement);
        assertThat(dto.getHeureParSemaine()).isEqualTo(offerTest.getHeureParSemaine());
        assertThat(dto.getPrenomContact()).isEqualTo(companyTest.getFirstName());
        assertThat(dto.getNomContact()).isEqualTo(companyTest.getLastName());
        assertThat(dto.getSession()).isEqualTo(offerTest.getSession());
    }

    @Test
    void testGetMillieuEvaluationInfoForContractNotFound() throws NonExistentOfferExeption {
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            gestionnaireService.getEvaluationInfoForContract(1L);
        } catch (NonExistentEntityException e) {
            return;
        }
        fail("NonExistentEntityException not thrown");
    }

    @Test
    void testgetMillieuEvaluationInfoForContractOfferNotFound() throws NonExistentEntityException {
        when(offreRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));

        try {
            gestionnaireService.getEvaluationInfoForContract(1L);
        } catch (NonExistentOfferExeption e) {
            return;
        }
        fail("NonExistentEntityException not thrown");
    }

    @Test
    void testGetMillieuEvaluationInfoForContractStudentNotFound() throws NonExistentOfferExeption {
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offerTest));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            gestionnaireService.getEvaluationInfoForContract(1L);
        } catch (NonExistentEntityException e) {
            return;
        }
        fail("NonExistentEntityException not thrown");
    }

    @Test
    void testGetMillieuEvaluationInfoForContractCompanyNotFound() throws NonExistentOfferExeption {
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offerTest));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            gestionnaireService.getEvaluationInfoForContract(1L);
        } catch (NonExistentEntityException e) {
            return;
        }
        fail("NonExistentEntityException not thrown");
    }

    @Test
    void testEvaluateStageHappyDay() {
        when(evaluationMillieuStageRepository.save(any())).thenReturn(EvaluationMillieuStage.builder().id(1L).build());
        gestionnaireService.evaluateStage(millieuStageEvaluationInDTO);
        verify(evaluationMillieuStageRepository, times(1)).save(any());
    }

    @Test
    void testGetContractsHappyDay() {
        StageContractOutDTO expected = new StageContractOutDTO(stageContract);
        expected.setStudentFullName(studentTest.getFirstName() + " " + studentTest.getLastName());
        expected.setEmployFullName(companyTest.getFirstName() + " " + companyTest.getLastName());
        expected.setPosition(offerTest.getPosition());
        expected.setCompanyName(companyTest.getCompanyName());
        expected.setGestionnaireSignature("test");
        stageContract.setGestionnaireSignature("test");
        when(evaluationMillieuStageRepository.findByContractId(anyLong())).thenReturn(Optional.empty());
        when(stageContractRepository.findAll()).thenReturn(List.of(stageContract, stageContract, stageContract));
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(companyTest));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offerTest));
        ContractsDTO dto = gestionnaireService.getContractsToEvaluateMillieuStage();


        assertThat(dto.size()).isEqualTo(3);
        assertThat(dto.getContracts().get(0)).isEqualTo(expected);
    }

    @Test
    void testGetContractsEmpty() {
        when(stageContractRepository.findAll()).thenReturn(new ArrayList<>());

        ContractsDTO dto = gestionnaireService.getContractsToEvaluateMillieuStage();

        assertThat(dto.size()).isEqualTo(0);
    }

    @Test
    void testCreateEvaluationStagePDFHappyDay() throws NonExistentOfferExeption, NonExistentEntityException, DocumentException, EmptySignatureException {
        when(evaluationMillieuStageRepository.findByContractId(anyLong())).thenReturn(
                Optional.of(new EvaluationMillieuStage(millieuStageEvaluationInDTO)));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offerTest));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(companyTest));
        gestionnaireService.createEvaluationMillieuStagePDF(1L);

        verify(evaluationMillieuStageRepository, times(1)).findByContractId(anyLong());
        verify(evaluationMillieuStagePDFRepository, times(1)).save(any());
    }

    @Test
    void testCreateEvaluationStageNonExistentException() {
        when(evaluationMillieuStageRepository.findByContractId(anyLong()))
                .thenReturn(Optional.empty());
        try {
            gestionnaireService.createEvaluationMillieuStagePDF(1L);
        } catch (NonExistentEntityException e) {
            return;
        } catch (Exception ignored) {
        }
        fail("NonExistentEntityException not thrown");
    }

    @Test
    void testCreateEvaluationStagePDFNonExistentOfferException() {
        when(evaluationMillieuStageRepository.findByContractId(anyLong()))
                .thenReturn(Optional.of(new EvaluationMillieuStage(millieuStageEvaluationInDTO)));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            gestionnaireService.createEvaluationMillieuStagePDF(1L);
        } catch (NonExistentOfferExeption e) {
            return;
        } catch (Exception ignored) {
        }
        fail("NonExistentOfferExeption not thrown");
    }

    @Test
    void testCreateEvaluationStagePDFNonExistentStudentException() {
        when(evaluationMillieuStageRepository.findByContractId(anyLong()))
                .thenReturn(Optional.of(new EvaluationMillieuStage(millieuStageEvaluationInDTO)));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offerTest));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            gestionnaireService.createEvaluationMillieuStagePDF(1L);
        } catch (NonExistentEntityException e) {
            return;
        } catch (Exception ignored) {
        }
        fail("NonExistentEntityException not thrown");
    }

    @Test
    void testCreateEvaluationStagePDFNonExistentCompanyException() {
        when(evaluationMillieuStageRepository.findByContractId(anyLong()))
                .thenReturn(Optional.of(new EvaluationMillieuStage(millieuStageEvaluationInDTO)));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offerTest));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            gestionnaireService.createEvaluationMillieuStagePDF(1L);
        } catch (NonExistentEntityException e) {
            return;
        } catch (Exception ignored) {
        }
        fail("NonExistentEntityException not thrown");
    }

    @Test
    void testCreateEvaluationStagePDFEmptySignatureException() throws NonExistentOfferExeption, NonExistentEntityException, DocumentException {
        millieuStageEvaluationInDTO.setSignature("");

        when(evaluationMillieuStageRepository.findByContractId(anyLong()))
                .thenReturn(Optional.of(new EvaluationMillieuStage(millieuStageEvaluationInDTO)));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offerTest));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(companyTest));
        try {
            gestionnaireService.createEvaluationMillieuStagePDF(1L);
        } catch (EmptySignatureException e) {
            return;
        }
        fail("EmptySignatureException not thrown");
    }

    @Test
    void testGetEvaluationStagePDFHappyDay() throws NonExistentEntityException {
        when(evaluationMillieuStagePDFRepository.findByContractId(anyLong())).thenReturn(
                Optional.of(EvaluationMilieuDeStagePDF.builder().pdf("TestPDF").build()));
        gestionnaireService.getEvaluationMillieuStagePDF(1L);

        verify(evaluationMillieuStagePDFRepository, times(1)).findByContractId(anyLong());
    }

    @Test
    void testGetEvaluationStagePDFNonExistentException() {
        when(evaluationMillieuStagePDFRepository.findByContractId(anyLong())).thenReturn(Optional.empty());
        try {
            gestionnaireService.getEvaluationMillieuStagePDF(1L);
        } catch (NonExistentEntityException e) {
            return;
        } catch (Exception ignored) {
        }
        fail("NonExistentEntityException not thrown");
    }

    @Test
    void testGetEvaluatedContractsMillieuStageHappyDay() {
        when(evaluationMillieuStageRepository.findByContractId(anyLong()))
                .thenReturn(Optional.of(new EvaluationMillieuStage(millieuStageEvaluationInDTO)), Optional.empty());
        when(stageContractRepository.findAll()).thenReturn(List.of(stageContract, stageContract));

        List<StageContractOutDTO> evaluatedContractsMillieuStage = gestionnaireService.getEvaluationMillieuStage();

        verify(evaluationMillieuStageRepository, times(2)).findByContractId(anyLong());
        verify(stageContractRepository, times(1)).findAll();
        assertThat(evaluatedContractsMillieuStage).hasSize(1);
    }

    @Test
    void testGetEvaluatedContractsMillieuStageEmpty() {
        when(evaluationMillieuStageRepository.findByContractId(anyLong()))
                .thenReturn(Optional.empty());
        when(stageContractRepository.findAll()).thenReturn(List.of(stageContract, stageContract));

        List<StageContractOutDTO> evaluatedContractsMillieuStage = gestionnaireService.getEvaluationMillieuStage();

        verify(evaluationMillieuStageRepository, times(2)).findByContractId(anyLong());
        verify(stageContractRepository, times(1)).findAll();
        assertThat(evaluatedContractsMillieuStage).hasSize(0);
    }

    @Test
    void testGetEvaluatedStudentsContractsHappyDay() {
        when(evaluationEtudiantRepository.findByContractId(anyLong()))
                .thenReturn(Optional.of(new EvaluationEtudiant(evaluationEtudiantInDTO)), Optional.empty());
        when(stageContractRepository.findAll())
                .thenReturn(List.of(stageContract, stageContract));

        List<StageContractOutDTO> evaluatedContractsMillieuStage = gestionnaireService.getEvaluatedContractsEtudiants();

        verify(evaluationEtudiantRepository, times(2)).findByContractId(anyLong());
        verify(stageContractRepository, times(1)).findAll();
        assertThat(evaluatedContractsMillieuStage).hasSize(1);
    }

    @Test
    void testGetEvaluatedStudentsContractsEmpty() {
        when(evaluationEtudiantRepository.findByContractId(anyLong()))
                .thenReturn(Optional.empty());
        when(stageContractRepository.findAll())
                .thenReturn(List.of(stageContract, stageContract));

        List<StageContractOutDTO> evaluatedContractsMillieuStage = gestionnaireService.getEvaluatedContractsEtudiants();

        verify(evaluationEtudiantRepository, times(2)).findByContractId(anyLong());
        verify(stageContractRepository, times(1)).findAll();
        assertThat(evaluatedContractsMillieuStage).hasSize(0);
    }

    @Test
    void testGetEvaluationPDFEtudiantHappyDay() throws NonExistentEntityException {
        when(evaluationEtudiantPDFRepository.findByContractId(anyLong())).thenReturn(
                Optional.of(EvaluationEtudiantPDF.builder().pdf("TestPDF").build()));
        gestionnaireService.getEvaluationPDFEtudiant(1L);

        verify(evaluationEtudiantPDFRepository, times(1)).findByContractId(anyLong());
    }

    @Test
    void testGetEvaluationPDFEtudiantNonExistentException() {
        when(evaluationEtudiantPDFRepository.findByContractId(anyLong())).thenReturn(Optional.empty());
        try {
            gestionnaireService.getEvaluationPDFEtudiant(1L);
        } catch (NonExistentEntityException e) {
            return;
        } catch (Exception ignored) {
        }
        fail("NonExistentEntityException not thrown");
    }

    @Test
    void testCreateEvaluationEtudiantPDFHappyDay() throws Exception {
        when(evaluationEtudiantRepository.findByContractId(anyLong()))
                .thenReturn(Optional.of(new EvaluationEtudiant(evaluationEtudiantInDTO)));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offerTest));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(companyTest));
        gestionnaireService.createEvaluationEtudiantPDF(1L);

        verify(evaluationEtudiantRepository, times(1)).findByContractId(anyLong());
        verify(stageContractRepository, times(2)).findById(anyLong());
        verify(offreRepository, times(1)).findById(anyLong());
        verify(studentRepository, times(1)).findById(anyLong());
        verify(companyRepository, times(1)).findById(anyLong());
    }

    @Test
    void testCreateEvaluationEtudiantNonExistentException() {
        when(evaluationEtudiantRepository.findByContractId(anyLong()))
                .thenReturn(Optional.empty());
        try {
            gestionnaireService.createEvaluationEtudiantPDF(1L);
        } catch (NonExistentEntityException e) {
            return;
        } catch (Exception ignored) {
        }
        fail("NonExistentEntityException not thrown");
    }

    @Test
    void testCreateEvaluationEtudiantPDFNonExistentOfferException() {
        when(evaluationEtudiantRepository.findByContractId(anyLong()))
                .thenReturn(Optional.of(new EvaluationEtudiant(evaluationEtudiantInDTO)));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            gestionnaireService.createEvaluationEtudiantPDF(1L);
        } catch (NonExistentOfferExeption e) {
            return;
        } catch (Exception ignored) {
        }
        fail("NonExistentOfferExeption not thrown");
    }

    @Test
    void testCreateEvaluationEtudiantPDFNonExistentStudentException() {
        when(evaluationEtudiantRepository.findByContractId(anyLong()))
                .thenReturn(Optional.of(new EvaluationEtudiant(evaluationEtudiantInDTO)));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offerTest));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            gestionnaireService.createEvaluationEtudiantPDF(1L);
        } catch (NonExistentEntityException e) {
            return;
        } catch (Exception ignored) {
        }
        fail("NonExistentEntityException not thrown");
    }

    @Test
    void testCreateEvaluationEtudiantPDFNonExistentCompanyException() {
        when(evaluationEtudiantRepository.findByContractId(anyLong()))
                .thenReturn(Optional.of(new EvaluationEtudiant(evaluationEtudiantInDTO)));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offerTest));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            gestionnaireService.createEvaluationEtudiantPDF(1L);
        } catch (NonExistentEntityException e) {
            return;
        } catch (Exception ignored) {
        }
        fail("NonExistentEntityException not thrown");
    }

    @Test
    void testCreateEvaluationEtudiantPDFEmptySignatureException() throws NonExistentOfferExeption, NonExistentEntityException, DocumentException {
        evaluationEtudiantInDTO.setSignature("");

        when(evaluationEtudiantRepository.findByContractId(anyLong()))
                .thenReturn(Optional.of(new EvaluationEtudiant(evaluationEtudiantInDTO)));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offerTest));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(companyTest));
        try {
            gestionnaireService.createEvaluationEtudiantPDF(1L);
        } catch (EmptySignatureException e) {
            return;
        }
        fail("EmptySignatureException not thrown");
    }


    @Test
    void testGetContractsToSigneHappyDay() {
        List<StageContract> contracts = new ArrayList<>() {{
            add(StageContract.builder().id(0L).studentId(0L).offerId(0L).companyId(0L).session("").description("")
                    .companySignature("").studentSignature("").gestionnaireSignature("").build());
            add(StageContract.builder().id(0L).studentId(0L).offerId(0L).companyId(0L).session("").description("")
                    .companySignature("Test").studentSignature("").gestionnaireSignature("").build());
            add(StageContract.builder().id(0L).studentId(0L).offerId(0L).companyId(0L).session("").description("")
                    .companySignature("").studentSignature("Test").gestionnaireSignature("").build());
            add(StageContract.builder().id(0L).studentId(0L).offerId(0L).companyId(0L).session("").description("")
                    .companySignature("Test").studentSignature("Test").gestionnaireSignature("").build());
            add(StageContract.builder().id(0L).studentId(0L).offerId(0L).companyId(0L).session("").description("")
                    .companySignature("Test").studentSignature("Test").gestionnaireSignature("").build());
            add(StageContract.builder().id(0L).studentId(0L).offerId(0L).companyId(0L).session("").description("")
                    .companySignature("Test").studentSignature("Test").gestionnaireSignature("Test").build());
        }};
        when(stageContractRepository.findAll()).thenReturn(contracts);
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offerTest));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(companyTest));

        List<StageContractOutDTO> contractsDTO = gestionnaireService.getContractsToSigne();

        assertThat(contractsDTO.size()).isEqualTo(2);
    }

    @Test
    void testContractSignatureHappyDay()
            throws NonExistentEntityException, NotReadyToBeSignedException {
        stageContract.setCompanySignature("done");
        stageContract.setStudentSignature("done");
        when(gestionnaireRepository.findById(anyLong())).thenReturn(Optional.of(gestionnaireTest));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));

        StageContractOutDTO dto = gestionnaireService.contractSignature(SignatureInDTO.builder()
                .token("token")
                .userId(0L)
                .contractId(0L)
                .signature("IAmSoOverIt")
                .build());

        assertThat(dto.getGestionnaireSignature()).isEqualTo("IAmSoOverIt");
    }

    @Test
    void testContractSignatureCompanyMissing() {
        stageContract.setCompanySignature("");
        stageContract.setStudentSignature("done");
        when(gestionnaireRepository.findById(anyLong())).thenReturn(Optional.of(gestionnaireTest));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));

        try {
            gestionnaireService.contractSignature(SignatureInDTO.builder()
                    .token("token")
                    .userId(0L)
                    .contractId(0L)
                    .signature("IAmSoOverIt")
                    .build());
        } catch (NotReadyToBeSignedException e) {
            return;
        } catch (NonExistentEntityException e) {
        }

        fail("Fail to catch the NotReadyToBeSigneException");
    }

    @Test
    void testContractSignatureStudentMissing() {
        stageContract.setCompanySignature("done");
        stageContract.setStudentSignature("");
        when(gestionnaireRepository.findById(anyLong())).thenReturn(Optional.of(gestionnaireTest));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));

        try {
            gestionnaireService.contractSignature(SignatureInDTO.builder()
                    .token("token")
                    .userId(0L)
                    .contractId(0L)
                    .signature("IAmSoOverIt")
                    .build());
        } catch (NotReadyToBeSignedException e) {
            return;
        } catch (NonExistentEntityException e) {
        }

        fail("Fail to catch the NotReadyToBeSigneException");
    }

    @Test
    void testContractSignatureContractNotFound() {
        when(gestionnaireRepository.findById(anyLong())).thenReturn(Optional.of(gestionnaireTest));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            gestionnaireService.contractSignature(SignatureInDTO.builder()
                    .token("token")
                    .userId(0L)
                    .contractId(0L)
                    .signature("IAmSoOverIt")
                    .build());
        } catch (NonExistentEntityException e) {
            return;
        } catch (NotReadyToBeSignedException e) {
        }

        fail("Fail to catch the NonExistentEntityException");
    }

    @Test
    void testContractSignatureGestionnaireNotFound() {
        when(gestionnaireRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            gestionnaireService.contractSignature(SignatureInDTO.builder()
                    .token("token")
                    .userId(0L)
                    .contractId(0L)
                    .signature("IAmSoOverIt")
                    .build());
        } catch (NonExistentEntityException e) {
            return;
        } catch (NotReadyToBeSignedException e) {
        }

        fail("Fail to catch the NonExistentEntityException");
    }

    @Test
    public void testResolveProblemHappyDay() throws Exception {
        when(problemsRepository.findById(anyLong())).thenReturn(Optional.of(problemTest));
        gestionnaireService.resolveProblem(1L);
        assertThat(problemTest.isResolved()).isTrue();
        verify(problemsRepository, times(1)).save(any());
    }

    @Test
    public void testResolveProblemProblemNotFound() throws Exception {
        when(problemsRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            gestionnaireService.resolveProblem(1L);
        } catch (NonExistentEntityException e) {
            return;
        }
        fail("Fail to catch the NonExistentEntityException");
    }

    @Test
    public void testGetUnresolvedProblemsHappyDay() {
        List<Problem> problems = new ArrayList<>() {{
            add(Problem.builder().id(0L).problemDetails("Test").resolved(false).build());
            add(Problem.builder().id(0L).problemDetails("Test").resolved(false).build());
            add(Problem.builder().id(0L).problemDetails("Test").resolved(true).build());
            add(Problem.builder().id(0L).problemDetails("Test").resolved(false).build());
            add(Problem.builder().id(0L).problemDetails("Test").resolved(true).build());
        }};
        when(problemsRepository.findAll()).thenReturn(problems);
        List<ProblemOutDTO> problemsDTO = gestionnaireService.getUnresolvedProblems();
        assertThat(problemsDTO.size()).isEqualTo(3);
    }

    @Test
    public void testGetUnresolvedProblemsNoUnresolvedProblems() {
        List<Problem> problems = new ArrayList<>() {{
            add(Problem.builder().id(0L).problemDetails("Test").resolved(true).build());
            add(Problem.builder().id(0L).problemDetails("Test").resolved(true).build());
            add(Problem.builder().id(0L).problemDetails("Test").resolved(true).build());
            add(Problem.builder().id(0L).problemDetails("Test").resolved(true).build());
            add(Problem.builder().id(0L).problemDetails("Test").resolved(true).build());
        }};
        when(problemsRepository.findAll()).thenReturn(problems);
        List<ProblemOutDTO> problemsDTO = gestionnaireService.getUnresolvedProblems();
        assertThat(problemsDTO.size()).isEqualTo(0);
    }
}
