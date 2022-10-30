package projet.projetstage02.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projet.projetstage02.DTO.*;
import projet.projetstage02.exception.ExpiredSessionException;
import projet.projetstage02.exception.InvalidStatusException;
import projet.projetstage02.exception.NonExistentEntityException;
import projet.projetstage02.exception.NonExistentOfferExeption;
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
    @InjectMocks
    private GestionnaireService service;

    private Gestionnaire gestionnaireTest;
    private Company companyTest;
    private Student studentTest;
    private Offre offreTest;
    private CvStatus cvStatus;

    @BeforeEach
    void beforeEach() {
        gestionnaireTest = new Gestionnaire(
                "prenom",
                "nom",
                "email@email.com",
                "password");

        companyTest = new Company(
                "prenom",
                "nom",
                "email@email.com",
                "password",
                AbstractUser.Department.Transport,
                "Company Test");

        studentTest = new Student(
                "prenom",
                "nom",
                "email@email.com",
                "password",
                Informatique);

        offreTest = Offre.builder()
                .id(1L)
                .nomDeCompagnie("Company Test")
                .department(Informatique)
                .position("Stagiaire test backend")
                .heureParSemaine(40)
                .salaire(40)
                .session(Offre.currentSession())
                .adresse("69 shitty street")
                .pdf(new byte[0])
                .valide(false)
                .build();
        cvStatus = CvStatus.builder().build();
    }

    @Test
    public void testSaveGestionnaireByParams() {
        // Arrange
        when(gestionnaireRepository.save(any())).thenReturn(gestionnaireTest);

        // Act
        service.saveGestionnaire("Dave", "Chapel", "email", "password");

        // Assert
        verify(gestionnaireRepository, times(1)).save(any());
    }

    @Test
    public void testSaveGestionnaireByDTO() {
        // Arrange
        when(gestionnaireRepository.save(any())).thenReturn(gestionnaireTest);

        // Act
        service.saveGestionnaire(new GestionnaireDTO(gestionnaireTest));

        // Assert
        verify(gestionnaireRepository, times(1)).save(any());
    }

    @Test
    public void testGetGestionnaireByIdSuccess() throws NonExistentEntityException {
        // Arrange
        when(gestionnaireRepository.findById(anyLong())).thenReturn(Optional.of(gestionnaireTest));

        // Act
        GestionnaireDTO dto = service.getGestionnaireById(1L);

        // Assert
        assertThat(dto.toModel()).isEqualTo(gestionnaireTest);
    }

    @Test
    public void testGetGestionnaireByIdNotFound() {
        // Arrange
        when(gestionnaireRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            service.getGestionnaireById(1L);
        } catch (NonExistentEntityException e) {
            // Assert
            return;
        }
        fail("NonExistentUserException not caught");
    }

    @Test
    public void testGetGestionnaireByEmailPasswordSuccess() throws NonExistentEntityException {
        // Arrange
        when(gestionnaireRepository.findByEmailAndPassword(anyString(), anyString()))
                .thenReturn(Optional.of(gestionnaireTest));

        // Act
        GestionnaireDTO dto = service.getGestionnaireByEmailPassword(anyString(), anyString());

        //Assert
        assertThat(dto.getEmail()).isEqualTo(gestionnaireTest.getEmail());
        assertThat(dto.getPassword()).isEqualTo(gestionnaireTest.getPassword());
    }

    @Test
    public void testGetGestionnaireByEmailPasswordNotFound() {
        // Arrange
        when(gestionnaireRepository.findByEmailAndPassword(anyString(), anyString()))
                .thenReturn(Optional.empty());

        // Act
        try {
            service.getGestionnaireByEmailPassword(anyString(), anyString());
        } catch (NonExistentEntityException e) {
            // Assert
            return;
        }
        fail("NonExistentUserException not caught");
    }

    @Test
    public void testValidateCompanySuccess() throws NonExistentEntityException {
        // Arrange
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(companyTest));

        // Act
        service.validateCompany(1L);

        // Assert
        assertThat(companyTest.isConfirm()).isTrue();
    }

    @Test
    public void testValidateCompanyNotFound() {
        // Arrange
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            service.validateCompany(1L);
        } catch (NonExistentEntityException e) {
            return;
        }
        fail("NonExistentUserException not caught");
    }

    @Test
    public void testValidateStudentSuccess() throws NonExistentEntityException {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));

        // Act
        service.validateStudent(1L);

        // Assert
        assertThat(studentTest.isConfirm()).isTrue();
    }

    @Test
    public void testValidateStudentNotFound() {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            service.validateStudent(1L);
        } catch (NonExistentEntityException e) {
            return;
        }

        fail("NonExistentUserException not caught");
    }

    @Test
    public void testRemoveCompanySuccess() throws NonExistentEntityException {
        // Arrange
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(companyTest));
        doNothing().when(companyRepository).delete(any());

        // Act
        service.removeCompany(1L);

        // Assert
        verify(companyRepository).delete(companyTest);
    }

    @Test
    public void testRemoveCompanyNotFound() {
        // Arrange
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            service.removeCompany(1L);
        } catch (NonExistentEntityException e) {
            return;
        }

        fail("NonExistentUserException not caught");
    }

    @Test
    public void testRemoveStudentSuccess() throws NonExistentEntityException {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));
        doNothing().when(studentRepository).delete(any());

        // Act
        service.removeStudent(1L);

        // Assert
        verify(studentRepository).delete(studentTest);
    }

    @Test
    public void testRemoveStudentNotFound() {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            service.removeStudent(1L);
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
        final List<OffreOutDTO> offersDto = service.getUnvalidatedOffers();

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
        final List<OffreOutDTO> offers2022 = service.getValidatedOffers(2022);
        final List<OffreOutDTO> offers2023 = service.getValidatedOffers(2023);
        final List<OffreOutDTO> offers2010 = service.getValidatedOffers(2010);

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
        final List<OffreOutDTO> noneValidateOffers = service.getUnvalidatedOffers();

        // Assert
        assertThat(noneValidateOffers.size()).isEqualTo(2);
    }

    @Test
    public void testValidateOfferByIdSuccess() throws NonExistentOfferExeption, ExpiredSessionException {
        // Arrange
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offreTest));

        // Act
        final OffreOutDTO offreInDTO = service.validateOfferById(1L);

        // Assert
        assertThat(offreInDTO.isValide()).isTrue();
    }

    @Test
    public void testValidateOfferByIdNotFound() throws ExpiredSessionException {
        // Arrange
        when(offreRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            service.validateOfferById(1L);
        } catch (NonExistentOfferExeption e) {
            return;
        }
        fail("NonExistentOfferException not caught");

    }

    @Test
    public void testValidateOfferExpiredOfferException() throws NonExistentOfferExeption {
        // Arrange
        offreTest.setSession("Hiver 2019");
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offreTest));

        // Act
        try {
            service.validateOfferById(1L);
        } catch (ExpiredSessionException e) {
            return;
        }
        fail("ExpiredSessionException not caught");

    }

    @Test
    public void testRemoveOfferByIdSuccess() throws NonExistentOfferExeption {
        // Arrange
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offreTest));
        doNothing().when(offreRepository).delete(any());

        // Act
        service.removeOfferById(1L);

        // Assert
        verify(offreRepository).delete(offreTest);
    }

    @Test
    public void testRemoveOfferByIdNotFound() {
        // Arrange
        when(offreRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            service.removeOfferById(1L);
        } catch (NonExistentOfferExeption e) {
            return;
        }
        fail("NonExistentOfferException not caught");
    }

    @Test
    public void testGetUnvalidatedStudents() {
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
        List<StudentOutDTO> unvalidatedStudents = service.getUnvalidatedStudents();

        // Assert
        assertThat(unvalidatedStudents.size()).isEqualTo(2);
    }

    @Test
    public void testGetUnvalidatedCompanies() {
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
        List<CompanyDTO> unvalidatedCompanies = service.getUnvalidatedCompanies();

        // Assert
        assertThat(unvalidatedCompanies.size()).isEqualTo(2);
    }

    @Test
    void testGetOffreInfoByIdSuccess() throws NonExistentOfferExeption {
        // Arrange
        when(offreRepository.findById(any())).thenReturn(Optional.of(offreTest));

        // Act
        PdfOutDTO pdf = service.getOffrePdfById(1L);

        // Assert
        assertThat(pdf.getPdf()).isEqualTo(Arrays.toString(offreTest.getPdf()).replaceAll("\\s+", ""));
    }

    @Test
    void testGetOffreInfoByIdNotFound() {
        // Arrange
        when(offreRepository.findById(any())).thenReturn(Optional.empty());

        // Act
        try {
            service.getOffrePdfById(1L);
        } catch (NonExistentOfferExeption e) {
            return;
        }
        fail("NonExistentOfferException not caught");
    }

    @Test
    void testInvalidGestionnaireHappyDay() throws NonExistentEntityException {
        // Arrange
        gestionnaireTest.setInscriptionTimestamp(0);
        when(gestionnaireRepository.findByEmail(any())).thenReturn(Optional.of(gestionnaireTest));

        // Act
        service.isGestionnaireInvalid(gestionnaireTest.getEmail());

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
            service.isGestionnaireInvalid(gestionnaireTest.getEmail());
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
            service.isGestionnaireInvalid(gestionnaireTest.getEmail());
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
        List<StudentOutDTO> unvalidatedStudentCV = service.getUnvalidatedCVStudents();
        // Assert
        assertThat(unvalidatedStudentCV.get(0).getEmail()).isEqualTo(studentTest.getEmail());
        assertThat(unvalidatedStudentCV.get(0).getFirstName()).isEqualTo(studentTest.getFirstName());
        assertThat(unvalidatedStudentCV.get(0).getCvToValidate()).isNotEmpty();
        assertThat(unvalidatedStudentCV.size()).isEqualTo(1);
    }

    @Test
    void testValidateStudentCVSuccess() throws NonExistentEntityException, InvalidStatusException {
        // Arrange
        studentTest.setCvToValidate(new byte[0]);
        cvStatus.setStatus("PENDING");
        when(cvStatusRepository.findById(anyLong())).thenReturn(Optional.of(cvStatus));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));

        // Act
        StudentOutDTO studentDTO = service.validateStudentCV(1L);

        // Assert
        assertThat(studentDTO.getFirstName()).isEqualTo(studentTest.getFirstName());
        assertThat(studentDTO.getCv()).isEqualTo("[]");
        assertThat(studentDTO.getCvToValidate()).isEqualTo("[]");
    }

    @Test
    void testValidateStudentCVNotFound() throws InvalidStatusException {
        // Arrange
        cvStatus.setStatus("PENDING");

        // Act
        try {
            service.validateStudentCV(1L);
        } catch (NonExistentEntityException e) {
            return;
        }
        fail("NonExistentUserException not caught");
    }

    @Test
    void testValidateStudentCVInvalidStatus() throws NonExistentEntityException {
        // Arrange
        cvStatus.setStatus("ACCEPTED");
        when(cvStatusRepository.findById(anyLong())).thenReturn(Optional.of(cvStatus));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));

        // Act
        try {
            service.validateStudentCV(1L);
        } catch (InvalidStatusException e) {
            return;
        }
        fail("InvalidStatusException not caught");
    }

    @Test
    void testRemoveStudentCvValidationSuccess() throws NonExistentEntityException, InvalidStatusException {
        // Arrange
        cvStatus.setStatus("PENDING");
        when(cvStatusRepository.findById(anyLong())).thenReturn(Optional.of(cvStatus));
        studentTest.setCvToValidate(new byte[0]);
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));

        // Act
        StudentOutDTO studentDTO = service.removeStudentCvValidation(1L, "Refused");

        // Assert
        assertThat(studentDTO.getEmail()).isEqualTo(studentTest.getEmail());
        assertThat(studentDTO.getCvToValidate()).isEqualTo("[]");
        assertThat(cvStatus.getRefusalMessage()).isEqualTo("Refused");
        assertThat(cvStatus.getStatus()).isEqualTo("REFUSED");
    }

    @Test
    void testRemoveStudentCvValidationNotFound() throws InvalidStatusException {
        // Arrange
        cvStatus.setStatus("PENDING");
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            service.removeStudentCvValidation(1L, "Refused");
        } catch (NonExistentEntityException e) {
            return;
        }
        fail("NonExistentUserException not caught");
    }

    @Test
    void testRemoveStudentCvValidationInvalidStatus() throws NonExistentEntityException {
        // Arrange
        cvStatus.setStatus("ACCEPTED");
        when(cvStatusRepository.findById(anyLong())).thenReturn(Optional.of(cvStatus));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));

        // Act
        try {
            service.removeStudentCvValidation(1L, "Refused");
        } catch (InvalidStatusException e) {
            return;
        }
        fail("InvalidStatusException not caught");
    }

    @Test
    void testGetStudentCvToValidateSuccess() throws NonExistentEntityException {
        // Arrange
        String result = "[72,101,108,108,111,32,87,111,114,100]";
        byte[] stored = HexFormat.of().parseHex("48656c6c6f20576f7264");
        studentTest.setCvToValidate(stored);
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));

        // Act
        PdfOutDTO cv = service.getStudentCvToValidate(1L);

        //
        assertThat(cv.getPdf()).isEqualTo(result);
    }

    @Test
    void testGetStudentCvToValidateNotFound() {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            service.getStudentCvToValidate(1L);
        } catch (NonExistentEntityException e) {
            return;
        }
        fail("NonExistentUserException not caught");
    }
}
