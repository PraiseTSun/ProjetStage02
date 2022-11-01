package projet.projetstage02.service;

import com.jayway.jsonpath.internal.path.PathCompiler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projet.projetstage02.DTO.*;
import projet.projetstage02.exception.AlreadyExistingAcceptationException;
import projet.projetstage02.exception.InvalidOwnershipException;
import projet.projetstage02.exception.NonExistentEntityException;
import projet.projetstage02.exception.NonExistentOfferExeption;
import projet.projetstage02.model.*;
import projet.projetstage02.repository.*;

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

    Company duffBeer;
    OffreInDTO duffBeerOffreInDTO;
    Student bart;
    Offre duffBeerOffer;
    ApplicationAcceptation applicationAcceptation;
    StageContract stageContract;
    SignatureInDTO signatureInDTO;

    OfferApplicationDTO offerApplicationDTO;

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
                .companySignature(new byte[0])
                .build();

        signatureInDTO = SignatureInDTO.builder()
                .userId(duffBeer.getId())
                .contractId(stageContract.getId())
                .signature(new byte[]{0,1,2,3,4,5,6,7,8,9})
                .build();

        offerApplicationDTO = OfferApplicationDTO.builder().build();
    }

    @Test
    void getCompanyByIdHappyDayTest() throws NonExistentEntityException {
        // Arrange
        when(companyRepository.findById(anyLong()))
                .thenReturn(Optional.of(duffBeer));

        // Act
        CompanyDTO companyDTO = companyService.getCompanyById(1L);

        // Assert
        assertThat(companyDTO.toModel()).isEqualTo(duffBeer);
    }

    @Test
    void getCompanyByIdNonExistentTest() {
        // Arrange
        when(companyRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // Act
        try {
            companyService.getCompanyById(1L);
        } catch (NonExistentEntityException e) {

            // Assert
            return;
        }
        fail("NonExistentUserException not caught");
    }

    @Test
    void getCompanyByEmailAndPasswordHappyDayTest() throws NonExistentEntityException {
        // Arrange
        when(companyRepository.findByEmailAndPassword(
                "duff.beer@springfield.com",
                "bestBeer"))
                .thenReturn(Optional.of(duffBeer));

        // Act
        CompanyDTO companyDTO = companyService
                .getCompanyByEmailPassword(
                        "duff.beer@springfield.com",
                        "bestBeer");

        // Assert
        assertThat(companyDTO.toModel()).isEqualTo(duffBeer);
    }

    @Test
    void getCompanyByEmailAndPasswordNonExistentTest() {
        // Arrange
        when(companyRepository.findByEmailAndPassword(anyString(), anyString()))
                .thenReturn(Optional.empty());

        // Act
        try {
            companyService.getCompanyByEmailPassword(
                    "duff.beer@springfield.com",
                    "bestBeer");
        } catch (NonExistentEntityException e) {

            // Assert
            return;
        }
        fail("NonExistentUserException not caught");
    }

    @Test
    void saveCompanyMultipleParametersTest() {
        // Arrange
        duffBeer.setId(1L);
        when(companyRepository.save(any()))
                .thenReturn(duffBeer);

        // Act
        companyService.saveCompany(
                duffBeer.getFirstName(),
                duffBeer.getLastName(),
                duffBeer.getCompanyName(),
                duffBeer.getEmail(),
                duffBeer.getPassword(),
                duffBeer.getDepartment());

        // Assert
        verify(companyRepository, times(1)).save(any());
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
    void createOffreTest() {
        // Arrange
        when(offreRepository.save(any())).thenReturn(duffBeerOffreInDTO.toModel());

        // Act
        companyService.createOffre(duffBeerOffreInDTO);

        // Assert
        verify(offreRepository, times(1)).save(any());
    }

    @Test
    void testInvalidCompanyHappyDay() throws NonExistentEntityException {
        // Arrange
        duffBeer.setInscriptionTimestamp(0);
        when(companyRepository.findByEmail(any())).thenReturn(Optional.of(duffBeer));

        // Act
        companyService.isCompanyInvalid(duffBeer.getEmail());

        // Assert
        verify(companyRepository, times(1)).delete(any());
    }

    @Test
    void testInvalidCompanyReturnFalse() throws NonExistentEntityException {
        // Arrange
        duffBeer.setInscriptionTimestamp(currentTimestamp());
        when(companyRepository.findByEmail(any())).thenReturn(Optional.empty());
        // Assert
        assertThat(companyService.isCompanyInvalid(duffBeer.getEmail())).isFalse();
    }

    @Test
    void testInvalidCompanyThrowsException() {
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
        } catch (Exception e) {
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
        } catch (NonExistentOfferExeption e) {
            return;
        } catch (Exception e) {
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
        } catch (Exception e) {
        }
        // Assert
        fail("AlreadyExistingAcceptationException not thrown");
    }

    @Test
    void testGetAcceptedStudentForOfferHappyDay() throws NonExistentOfferExeption {
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
        } catch (NonExistentOfferExeption e) {
            return;
        }
        fail("NonExistentOfferException not thrown");
    }

    @Test
    void testAddSignatureToContractHappyDay() throws Exception{
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(duffBeer));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));

        StageContractOutDTO dto = companyService.addSignatureToContract(signatureInDTO);

        assertThat(dto.getCompanyId()).isEqualTo(duffBeer.getId());
        assertThat(dto.getId()).isEqualTo(stageContract.getId());
        assertThat(dto.getCompanySignature()).isEqualTo(byteToString(signatureInDTO.getSignature()));
    }

    @Test
    void testAddSignatureToContractOwnershipConflict(){
        duffBeer.setId(99L);
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(duffBeer));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.of(stageContract));

        try {
            companyService.addSignatureToContract(signatureInDTO);
        } catch (InvalidOwnershipException e) {
            return;
        } catch (Exception e) {}

        fail("Fail to catch the InvalidOwnershipException!");
    }

    @Test
    void testAddSignatureToContractCompanyNotFound(){
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            companyService.addSignatureToContract(signatureInDTO);
        } catch (NonExistentEntityException e) {
            return;
        } catch (Exception e) {}

        fail("Fail to catch the NonExistentEntityException!");
    }

    @Test
    void testAddSignatureToContractNotFound(){
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(duffBeer));
        when(stageContractRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            companyService.addSignatureToContract(signatureInDTO);
        } catch (NonExistentEntityException e) {
            return;
        } catch (Exception e) {}

        fail("Fail to catch the NonExistentEntityException!");
    }

    @Test
    void testGetApplicantsForOfferHappyDay() throws NonExistentOfferExeption {
        // Arrange
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(duffBeerOffer));
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
        assertThat(studentsForOffer.getApplicants().size()).isEqualTo(5);
    }

    @Test
    void testGetApplicantsForOfferNotFull() throws NonExistentOfferExeption {
        // Arrange
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
        } catch (NonExistentOfferExeption e) {
            return;
        }

        fail("NonExistentOfferExeption not thrown");
    }

    @Test
    void testGetApplicantsForOfferEmpty() throws NonExistentOfferExeption {
        // Arrange
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(duffBeerOffer));
        when(applicationRepository.findByOfferId(anyLong())).thenReturn(new ArrayList<>());

        // Act
        OfferApplicationDTO studentsForOffer = companyService.getStudentsForOffer(1L);

        // Assert
        assertThat(studentsForOffer.getApplicants().size()).isEqualTo(0);
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
    void testGetStudentCvToValidateSuccess() throws NonExistentEntityException {
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
}
