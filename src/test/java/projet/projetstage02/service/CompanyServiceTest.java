package projet.projetstage02.service;

import com.jayway.jsonpath.internal.path.PathCompiler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projet.projetstage02.dto.SignatureInDTO;
import projet.projetstage02.dto.applications.ApplicationAcceptationDTO;
import projet.projetstage02.dto.applications.OfferApplicationDTO;
import projet.projetstage02.dto.contracts.ContractsDTO;
import projet.projetstage02.dto.contracts.StageContractOutDTO;
import projet.projetstage02.dto.evaluations.Etudiant.EvaluationEtudiantInDTO;
import projet.projetstage02.dto.interview.CreateInterviewDTO;
import projet.projetstage02.dto.interview.InterviewOutDTO;
import projet.projetstage02.dto.notification.CompanyNotificationDTO;
import projet.projetstage02.dto.offres.OfferAcceptedStudentsDTO;
import projet.projetstage02.dto.offres.OffreInDTO;
import projet.projetstage02.dto.offres.OffreOutDTO;
import projet.projetstage02.dto.pdf.PdfOutDTO;
import projet.projetstage02.dto.users.CompanyDTO;
import projet.projetstage02.exception.*;
import projet.projetstage02.model.*;
import projet.projetstage02.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static projet.projetstage02.utils.ByteConverter.byteToString;
import static projet.projetstage02.utils.TimeUtil.currentTimestamp;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @InjectMocks
    CompanyService companyService;

    @Mock
    CompanyRepository companyRepository;

    @Mock
    OffreRepository offreRepository;

    @Mock
    StudentRepository studentRepository;
    @Mock
    StageContractRepository stageContractRepository;
    @Mock
    ApplicationAcceptationRepository applicationAcceptationRepository;
    @Mock
    ApplicationRepository applicationRepository;
    @Mock
    InterviewRepository interviewRepository;
    @Mock
    EvaluationEtudiantRepository evaluationEtudiantRepository;

    Company duffBeer;
    OffreInDTO duffBeerOffreInDTO;
    Student bart;
    Offre duffBeerOffer;
    ApplicationAcceptation applicationAcceptation;
    StageContract stageContract;
    SignatureInDTO signatureInDTO;
    CreateInterviewDTO createInterviewDTO;
    Interview interview;
    OfferApplicationDTO offerApplicationDTO;
    EvaluationEtudiantInDTO evaluationEtudiantInDTO;

    @BeforeEach
    void setup() {
        duffBeer = new Company(
                "Duff",
                "Man",
                "duff.beer@springfield.com",
                "bestBeer",
                AbstractUser.Department.Transport,
                "Duff Beer");
        duffBeer.setId(4L);

        duffBeerOffreInDTO = OffreInDTO.builder()
                .adresse("653 Duff Street")
                .department(AbstractUser.Department.Transport.departement)
                .heureParSemaine(40)
                .session("Hiver 2023")
                .position("Delivery Guy")
                .nomDeCompagnie("Duff beer")
                .pdf(new byte[0])
                .build();

        bart = new Student(
                "Bart",
                "Simpson",
                "bart.simpson@springfield.com",
                "eatMyShorts",
                AbstractUser.Department.Informatique);
        bart.setCv(new byte[0]);
        bart.setId(2L);

        duffBeerOffer = Offre.builder()
                .id(1L)
                .idCompagnie(duffBeer.getId())
                .nomDeCompagnie("Duff")
                .department(AbstractUser.Department.Transport)
                .position("Manager")
                .heureParSemaine(69)
                .adresse("Somewhere")
                .valide(true)
                .pdf(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9})
                .session("Hiver 2023")
                .build();

        applicationAcceptation = ApplicationAcceptation.builder()
                .id(3L)
                .studentId(bart.getId())
                .studentName(bart.getLastName() + " " + bart.getFirstName())
                .offerId(duffBeerOffer.getId())
                .companyName(duffBeerOffer.getNomDeCompagnie())
                .build();

        stageContract = StageContract.builder()
                .id(5L)
                .studentId(bart.getId())
                .offerId(duffBeerOffer.getId())
                .companyId(duffBeer.getId())
                .description("Do a better job than Homer Simpson")
                .companySignature("Winner")
                .build();

        signatureInDTO = SignatureInDTO.builder()
                .userId(duffBeer.getId())
                .contractId(stageContract.getId())
                .signature("Winner")
                .build();

        offerApplicationDTO = OfferApplicationDTO.builder().build();

        createInterviewDTO = CreateInterviewDTO.builder()
                .companyId(duffBeer.getId())
                .offerId(duffBeerOffer.getId())
                .studentId(bart.getId())
                .companyDateOffers(new ArrayList<>() {{
                    add("2022-11-28T12:30:00");
                    add("2022-11-29T12:30:00");
                    add("2022-11-30T12:30:00");
                }})
                .build();

        interview = Interview.builder()
                .id(7L)
                .companyId(duffBeer.getId())
                .offerId(duffBeerOffer.getId())
                .studentId(bart.getId())
                .companyDateOffers(new ArrayList<>() {{
                    add(LocalDateTime.parse("2022-11-28T12:30:00"));
                    add(LocalDateTime.parse("2022-11-29T12:30:00"));
                    add(LocalDateTime.parse("2022-11-30T12:30:00"));
                }})
                .studentSelectedDate(null)
                .build();
        evaluationEtudiantInDTO = EvaluationEtudiantInDTO.builder()
                .accepteCritiques("plutotEnAccord")
                .acueillirPourProchainStage("oui")
                .adapteCulture("plutotEnAccord")
                .attentionAuxDetails("plutotEnAccord")
                .bonneAnalyseProblemes("plutotEnAccord")
                .commentairesHabilites("plutotEnAccord")
                .commentairesProductivite("plutotEnAccord")
                .commentairesAppreciation("plutotEnAccord")
                .commentairesQualite("plutotEnAccord")
                .comprendRapidement("plutotEnAccord")
                .contactsFaciles("plutotEnAccord")
                .commentairesRelationsInterpersonnelles("plutotEnAccord")
                .contractId(1L)
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
                .signature(byteToString(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}))
                .travailEnEquipe("plutotEnAccord")
                .travailSecuritaire("plutotEnAccord")
                .travailEfficace("plutotEnAccord")
                .build();
    }

    @Test
    void createOffreTest() {
        // Arrange
        when(offreRepository.save(any())).thenReturn(duffBeerOffreInDTO.toModel());

        // Act
        companyService.createOffre(duffBeerOffreInDTO);

        // Assert
        verify(offreRepository, times(1)).save(any());
    }

    @Test
    void saveCompanyDTOTest() {
        // Arrange
        when(companyRepository.save(any()))
                .thenReturn(duffBeer);

        // Act
        companyService.saveCompany(new CompanyDTO(duffBeer));

        // Assert
        verify(companyRepository, times(1)).save(any());
    }

    @Test
    void testIsCompanyInvalidEmailUniqueHappyDay() throws NonExistentEntityException {
        // Arrange
        duffBeer.setInscriptionTimestamp(0);
        when(companyRepository.findByEmail(any())).thenReturn(Optional.empty());

        // Act
        boolean isUnique = companyService.isCompanyInvalid(duffBeer.getEmail());

        // Assert
        assertThat(isUnique).isFalse();
    }

    @Test
    void testIsCompanyInvalidDeleteUnconfirmedHappyDay() throws NonExistentEntityException {
        // Arrange
        duffBeer.setInscriptionTimestamp(0);
        when(companyRepository.findByEmail(any())).thenReturn(Optional.of(duffBeer));

        // Act
        boolean isUnique = companyService.isCompanyInvalid(duffBeer.getEmail());

        // Assert
        verify(companyRepository, times(1)).delete(any());
        assertThat(isUnique).isFalse();
    }

    @Test
    void testIsCompanyInvalidForbidden() throws NonExistentEntityException {
        // Arrange
        duffBeer.setInscriptionTimestamp(currentTimestamp());
        when(companyRepository.findByEmail(any())).thenReturn(Optional.empty());
        // Assert
        assertThat(companyService.isCompanyInvalid(duffBeer.getEmail())).isFalse();
    }

    @Test
    void testIsCompanyInvalidDeleteUnconfirmedNotFound() {
        // Arrange
        duffBeer.setInscriptionTimestamp(0);
        when(companyRepository.findByEmail(any())).thenReturn(Optional.of(duffBeer), Optional.empty());

        // Act
        try {
            companyService.isCompanyInvalid(duffBeer.getEmail());
        } catch (NonExistentEntityException e) {
            return;
        }
        // Assert
        fail("NonExistentEntityException not thrown");
    }

    @Test
    void testSaveStudentApplicationAcceptedHappyDay() throws Exception {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(bart));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(duffBeerOffer));
        when(applicationAcceptationRepository.findByOfferIdAndStudentId(anyLong(), anyLong()))
                .thenReturn(Optional.empty(), Optional.of(applicationAcceptation));

        // Act
        ApplicationAcceptationDTO dto = companyService.saveStudentApplicationAccepted(1L, 2L);

        // Assert
        verify(applicationAcceptationRepository, times(1)).save(any());
        assertThat(dto.getId()).isEqualTo(applicationAcceptation.getId());
        assertThat(dto.getStudentId()).isEqualTo(applicationAcceptation.getStudentId());
        assertThat(dto.getStudentName()).isEqualTo(applicationAcceptation.getStudentName());
        assertThat(dto.getOfferId()).isEqualTo(applicationAcceptation.getOfferId());
        assertThat(dto.getCompanyName()).isEqualTo(applicationAcceptation.getCompanyName());
    }

    @Test
    void testSaveStudentApplicationAcceptedStudentNotFound() {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            companyService.saveStudentApplicationAccepted(1L, 2L);
        } catch (NonExistentEntityException e) {
            return;
        } catch (Exception ignored) {
        }
        // Assert
        fail("NonExistentEntityException not thrown");
    }

    @Test
    void testSaveStudentApplicationAcceptedOfferNotFound() {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(bart));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            companyService.saveStudentApplicationAccepted(1L, 2L);
        } catch (NonExistentEntityException e) {
            return;
        } catch (AlreadyExistingAcceptationException | NonExistentOfferExeption ignored) {
        }
        // Assert
        fail("NonExistentOfferException not thrown");
    }

    @Test
    void testSaveStudentApplicationAcceptedConflict() {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(bart));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(duffBeerOffer));
        when(applicationAcceptationRepository.findByOfferIdAndStudentId(anyLong(), anyLong()))
                .thenReturn(Optional.of(applicationAcceptation));

        // Act
        try {
            companyService.saveStudentApplicationAccepted(1L, 2L);
        } catch (AlreadyExistingAcceptationException e) {
            return;
        } catch (Exception ignored) {
        }
        // Assert
        fail("AlreadyExistingAcceptationException not thrown");
    }

    @Test
    void testGetAcceptedStudentForOfferHappyDay() throws NonExistentEntityException {
        // Arrange
        List<ApplicationAcceptation> applications = new ArrayList<>() {{
            add(ApplicationAcceptation.builder().offerId(duffBeerOffer.getId()).studentId(bart.getId()).build());
            add(ApplicationAcceptation.builder().offerId(0L).studentId(bart.getId()).build());
        }};
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(duffBeerOffer));
        when(applicationAcceptationRepository.findAll()).thenReturn(applications);

        // Act
        OfferAcceptedStudentsDTO dto = companyService.getAcceptedStudentsForOffer(1L);

        // Assert
        assertThat(dto.getOfferId()).isEqualTo(duffBeerOffer.getId());
        assertThat(dto.getStudentsId().size()).isEqualTo(1);
    }

    @Test
    void testGetAcceptedStudentForOfferNotFound() {
        // Arrange
        when(offreRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            companyService.getAcceptedStudentsForOffer(1L);
        } catch (NonExistentEntityException e) {
            return;
        }
        fail("NonExistentEntityException not thrown");
    }

    @Test
    void testAddSignatureToContractHappyDay() throws Exception {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(duffBeer));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));

        StageContractOutDTO dto = companyService.addSignatureToContract(signatureInDTO);

        assertThat(dto.getCompanyId()).isEqualTo(duffBeer.getId());
        assertThat(dto.getContractId()).isEqualTo(stageContract.getId());
        assertThat(dto.getCompanySignature()).isEqualTo(signatureInDTO.getSignature());
    }

    @Test
    void testAddSignatureToContractOwnershipConflict() {
        duffBeer.setId(99L);
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(duffBeer));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));

        try {
            companyService.addSignatureToContract(signatureInDTO);
        } catch (InvalidOwnershipException e) {
            return;
        } catch (Exception ignored) {
        }

        fail("Fail to catch the InvalidOwnershipException!");
    }

    @Test
    void testAddSignatureToContractCompanyNotFound() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            companyService.addSignatureToContract(signatureInDTO);
        } catch (NonExistentEntityException e) {
            return;
        } catch (Exception ignored) {
        }

        fail("Fail to catch the NonExistentEntityException!");
    }

    @Test
    void testAddSignatureToContractNotFound() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(duffBeer));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            companyService.addSignatureToContract(signatureInDTO);
        } catch (NonExistentEntityException e) {
            return;
        } catch (Exception ignored) {
        }

        fail("Fail to catch the NonExistentEntityException!");
    }

    @Test
    void testGetApplicantsForOfferHappyDay() throws NonExistentOfferExeption, NonExistentEntityException {
        // Arrange
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(duffBeer));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(duffBeerOffer));
        when(stageContractRepository.findByStudentIdAndCompanyIdAndOfferId(anyLong(), anyLong(), anyLong()))
                .thenReturn(Optional.of(stageContract), Optional.empty());
        when(applicationRepository.findByOfferId(anyLong())).thenReturn(new ArrayList<>() {{
            add(Application.builder().studentId(1L).build());
            add(Application.builder().studentId(2L).build());
            add(Application.builder().studentId(3L).build());
            add(Application.builder().studentId(4L).build());
            add(Application.builder().studentId(5L).build());
        }});
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(bart));

        // Act
        OfferApplicationDTO studentsForOffer = companyService.getStudentsForOffer(1L);

        // Assert
        assertThat(studentsForOffer.getApplicants().size()).isEqualTo(4);
    }

    @Test
    void testGetApplicantsForOfferNotFull() throws NonExistentOfferExeption, NonExistentEntityException {
        // Arrange
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(duffBeer));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(duffBeerOffer));
        when(applicationRepository.findByOfferId(anyLong())).thenReturn(new ArrayList<>() {{
            add(Application.builder().studentId(1L).build());
            add(Application.builder().build());
            add(Application.builder().studentId(3L).build());
            add(Application.builder().build());
            add(Application.builder().studentId(5L).build());
        }});
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(bart));

        // Act
        OfferApplicationDTO studentsForOffer = companyService.getStudentsForOffer(1L);

        // Assert
        assertThat(studentsForOffer.getApplicants().size()).isEqualTo(3);
    }

    @Test
    void testGetApplicantsForOfferNonExistentOffer() {
        // Arrange
        when(offreRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            companyService.getStudentsForOffer(1L);
        } catch (NonExistentEntityException e) {
            return;
        }

        fail("NonExistingEntityException not thrown");
    }

    @Test
    void testGetApplicantsForOfferNotFound() throws NonExistentOfferExeption {
        // Arrange
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(duffBeerOffer));

        // Act
        try {
            companyService.getStudentsForOffer(1L);
        } catch (NonExistentEntityException e) {
            return;
        }

        fail("NonExistentEntityExeption not thrown");
    }

    @Test
    void testGetStudentCvToValidateHappyDay() throws NonExistentEntityException {
        // Arrange
        String result = "[72,101,108,108,111,32,87,111,114,100]";
        byte[] stored = HexFormat.of().parseHex("48656c6c6f20576f7264");
        bart.setCv(stored);
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(bart));

        // Act
        PdfOutDTO cv = companyService.getStudentCv(1L);

        //
        Assertions.assertThat(cv.getPdf()).isEqualTo(result);
    }

    @Test
    void testGetStudentCvToValidateNotFound() {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            companyService.getStudentCv(1L);
        } catch (NonExistentEntityException e) {
            return;
        }
        PathCompiler.fail("NonExistentUserException not caught");
    }

    @Test
    void testGetOffersForCompanyHappyDay() {
        // Arrange
        when(offreRepository.findAllByIdCompagnie(anyLong())).thenReturn(List.of(duffBeerOffer));

        // Act
        List<OffreOutDTO> validatedOffers = companyService.getValidatedOffers(1L);

        // Assert
        assertThat(validatedOffers.size()).isEqualTo(1);
    }

    @Test
    void testGetOffersForCompanyEmpty() {
        // Arrange
        when(offreRepository.findAllByIdCompagnie(anyLong())).thenReturn(new ArrayList<>());

        // Act
        List<OffreOutDTO> validatedOffers = companyService.getValidatedOffers(1L);

        // Assert
        assertThat(validatedOffers.size()).isEqualTo(0);
    }

    @Test
    void testGetContractHappyDay() throws NonExistentEntityException {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(duffBeer));
        StageContract contractValid = StageContract.builder()
                .id(1L).studentId(1L).offerId(1L).companyId(duffBeer.getId()).companySignature("")
                .session(Offre.currentSession()).description("").companySignatureDate(LocalDateTime.now()).build();
        StageContract contractInvalid1 = StageContract.builder()
                .id(1L).studentId(1L).offerId(1L).companyId(duffBeer.getId()).companySignature("").session("Hiver 2000")
                .description("").companySignatureDate(LocalDateTime.now()).build();
        StageContract contractInvalid2 = StageContract.builder()
                .id(1L).studentId(1L).offerId(1L).companyId(duffBeer.getId()).companySignature("").session("Hiver 1997")
                .description("").companySignatureDate(LocalDateTime.now()).build();
        when(stageContractRepository.findByCompanyId(anyLong())).thenReturn(
                new ArrayList<>() {{
                    add(contractValid);
                    add(contractInvalid1);
                    add(contractValid);
                    add(contractInvalid2);
                }}
        );

        ContractsDTO contracts = companyService.getContracts(duffBeer.getId(), Offre.currentSession());

        assertThat(contracts.size()).isEqualTo(2);
    }

    @Test
    void testGetContractsNotFound() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            companyService.getContracts(1L, "");
        } catch (NonExistentEntityException e) {
            return;
        }
        fail("Fail to catch the NonExistentEntityException");
    }

    @Test
    void testCreateInterviewHappyDay()
            throws InvalidOwnershipException, InvalidDateFormatException, NonExistentEntityException {
        // Arrange
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(duffBeer));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(bart));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(duffBeerOffer));
        when(interviewRepository.save(any())).thenReturn(interview);

        // Act
        InterviewOutDTO dto = companyService.createInterview(createInterviewDTO);

        // Assert
        verify(interviewRepository, times(1)).save(any());
        assertThat(dto.getCompanyDateOffers().size()).isEqualTo(3);
        assertThat(dto.getStudentSelectedDate()).isEqualTo("");
        assertThat(dto.getCompanyDateOffers().get(0)).isEqualTo(interview.getCompanyDateOffers().get(0).toString());
        assertThat(dto.getCompanyDateOffers().get(1)).isEqualTo(interview.getCompanyDateOffers().get(1).toString());
        assertThat(dto.getCompanyDateOffers().get(2)).isEqualTo(interview.getCompanyDateOffers().get(2).toString());
    }

    @Test
    void testCreateInterviewOfferNotFound() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(duffBeer));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(bart));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            companyService.createInterview(createInterviewDTO);
        } catch (NonExistentEntityException e) {
            return;
        } catch (InvalidDateFormatException | InvalidOwnershipException ignored) {
        }

        fail("Fail to catch the NonExistentEntityException");
    }

    @Test
    void testCreateInterviewStudentNotFound() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(duffBeer));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            companyService.createInterview(createInterviewDTO);
        } catch (NonExistentEntityException e) {
            return;
        } catch (InvalidDateFormatException | InvalidOwnershipException ignored) {
        }

        fail("Fail to catch the NonExistentEntityException");
    }

    @Test
    void testCreateInterviewCompanyNotFound() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            companyService.createInterview(createInterviewDTO);
        } catch (NonExistentEntityException e) {
            return;
        } catch (InvalidDateFormatException | InvalidOwnershipException ignored) {
        }

        fail("Fail to catch the NonExistentEntityException");
    }

    @Test
    void testCreateInterviewConflict() {
        // Arrange
        createInterviewDTO.getCompanyDateOffers().add("test");
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(duffBeer));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(bart));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(duffBeerOffer));

        // Act
        try {
            companyService.createInterview(createInterviewDTO);
        } catch (InvalidDateFormatException e) {
            return;
        } catch (InvalidOwnershipException | NonExistentEntityException ignored) {
        }

        fail("Fail to catch the InvalidDateFormatException");
    }

    @Test
    void testCreateInterviewForbiden() {
        // Arrange
        duffBeer.setId(0L);
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(duffBeer));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(bart));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(duffBeerOffer));

        // Act
        try {
            companyService.createInterview(createInterviewDTO);
        } catch (InvalidOwnershipException e) {
            return;
        } catch (InvalidDateFormatException | NonExistentEntityException ignored) {
        }

        fail("Fail to catch the InvalidOwnershipException");
    }

    @Test
    void testGetInterviewsHappyDay() throws NonExistentEntityException {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(duffBeer));
        when(interviewRepository.findByCompanyId(anyLong())).thenReturn(
                new ArrayList<>() {{
                    add(interview);
                    add(interview);
                    add(interview);
                }}
        );

        List<InterviewOutDTO> interviews = companyService.getInterviews(1L);

        assertThat(interviews.size()).isEqualTo(3);
        assertThat(interviews.get(0).getStudentId()).isEqualTo(bart.getId());
        assertThat(interviews.get(0).getCompanyId()).isEqualTo(duffBeer.getId());
    }

    @Test
    void testGetInterviewsNotFound() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            companyService.getInterviews(1L);
        } catch (NonExistentEntityException e) {
            return;
        }

        fail("Fail to catch the NonExistentEntityException");
    }

    @Test
    void testEvaluateEtudiantHappyDay() {
        companyService.evaluateStudent(evaluationEtudiantInDTO);
        verify(evaluationEtudiantRepository, times(1)).save(any());
    }

    @Test
    void testGetEvaluatedStudentsContractsHappyDay() {
        when(stageContractRepository.findByCompanyId(anyLong())).thenReturn(new ArrayList<>() {{
            add(stageContract);
            add(stageContract);
            add(stageContract);
        }});
        when(evaluationEtudiantRepository.findByContractId(anyLong()))
                .thenReturn(Optional.of(new EvaluationEtudiant(evaluationEtudiantInDTO))
                );

        List<Long> evaluations = companyService.getEvaluatedStudentsContracts(1L);

        assertThat(evaluations.size()).isEqualTo(3);
    }

    @Test
    void testGetEvaluatedStudentsContractsEmptyNoContracts() {
        when(stageContractRepository.findByCompanyId(anyLong())).thenReturn(new ArrayList<>());

        List<Long> evaluations = companyService.getEvaluatedStudentsContracts(1L);

        assertThat(evaluations.size()).isEqualTo(0);
    }

    @Test
    void testGetEvaluatedStudentsContractsEmptyNoEvaluation() {
        when(stageContractRepository.findByCompanyId(anyLong())).thenReturn(new ArrayList<>() {{
            add(stageContract);
            add(stageContract);
            add(stageContract);
        }});
        when(evaluationEtudiantRepository.findByContractId(anyLong())).thenReturn(Optional.empty());

        List<Long> evaluations = companyService.getEvaluatedStudentsContracts(1L);

        assertThat(evaluations.size()).isEqualTo(0);
    }

    @Test
    void testGetNotificationHappyDay() throws NonExistentEntityException {
        List<Offre> offers = new ArrayList<>(){{
            add(Offre.builder().id(1L).build());
            add(Offre.builder().id(2L).build());
            add(Offre.builder().id(3L).build());
        }};
        List<Application> applicationFirstOffer = new ArrayList<>(){{
            add(Application.builder().studentId(4L).build());
            add(Application.builder().studentId(5L).build());
            add(Application.builder().studentId(6L).build());
        }};
        List<Application> applicationSecondOffer = new ArrayList<>(){{
            add(Application.builder().studentId(7L).build());
            add(Application.builder().studentId(8L).build());
            add(Application.builder().studentId(9L).build());
            add(Application.builder().studentId(10L).build());
        }};
        List<StageContract> contracts = new ArrayList<>(){{
            add(StageContract.builder().companySignature("").build());
            add(StageContract.builder().companySignature("Not a answer").build());
            add(StageContract.builder().companySignature("").build());
        }};


        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(duffBeer));

        when(offreRepository.findAllByIdCompagnie(anyLong())).thenReturn(offers);
        when(applicationRepository.findByOfferId(anyLong()))
                .thenReturn(applicationFirstOffer, applicationSecondOffer, new ArrayList<>());
        when(applicationAcceptationRepository.findByOfferIdAndStudentId(anyLong(), anyLong())).thenReturn(
                Optional.empty(),
                Optional.of(new ApplicationAcceptation()),
                Optional.empty(),
                Optional.empty(),
                Optional.of(new ApplicationAcceptation()),
                Optional.of(new ApplicationAcceptation()),
                Optional.empty());

        when(stageContractRepository.findByCompanyId(anyLong())).thenReturn(contracts);

        CompanyNotificationDTO notification = companyService.getNotification(1L);

        assertThat(notification.getNbOffers()).isEqualTo(4);
        assertThat(notification.getNbContracts()).isEqualTo(2);
    }

    @Test
    void testGetNotificationNotFound(){
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            companyService.getNotification(1L);
        } catch (NonExistentEntityException e) {
            return;
        }

        fail("Fail to catch NonExistentEntityException");
    }
}
