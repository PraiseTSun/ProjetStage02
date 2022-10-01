package projet.projetstage02.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projet.projetstage02.DTO.*;
import projet.projetstage02.exception.NonExistentOfferExeption;
import projet.projetstage02.exception.NonExistentUserException;
import projet.projetstage02.model.*;
import projet.projetstage02.repository.CompanyRepository;
import projet.projetstage02.repository.GestionnaireRepository;
import projet.projetstage02.repository.OffreRepository;
import projet.projetstage02.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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

    @InjectMocks
    private GestionnaireService service;

    private Gestionnaire gestionnaireTest;
    private Company companyTest;
    private Student studentTest;
    private Offre offreTest;

    @BeforeEach
    void beforeEach(){
        gestionnaireTest = new Gestionnaire(
                "prenom",
                "nom",
                "email",
                "password"
        );

        companyTest = new Company(
                "prenom",
                "nom",
                "email",
                "password",
                AbstractUser.Department.Transport,
                "Company Test"
        );

        studentTest = new Student(
                "prenom",
                "nom",
                "email",
                "password",
                AbstractUser.Department.Informatique
        );

        offreTest = new Offre(
                1L,
                "Company Test",
                AbstractUser.Department.Informatique,
                "Stagiaire test backend",
                40,
                "69 shitty street",
                false,
                new byte[0]
        );
    }

    @Test
    public void testSaveGestionnaireByParams(){
        // Arrange
        when(gestionnaireRepository.save(any())).thenReturn(gestionnaireTest);

        // Act
        service.saveGestionnaire("Dave", "Chapel", "email", "password");

        // Assert
        verify(gestionnaireRepository, times(1)).save(any());
    }

    @Test
    public void testSaveGestionnaireByDTO(){
        // Arrange
        when(gestionnaireRepository.save(any())).thenReturn(gestionnaireTest);

        // Act
        service.saveGestionnaire(new GestionnaireDTO(gestionnaireTest));

        // Assert
        verify(gestionnaireRepository, times(1)).save(any());
    }

    @Test
    public void testIsEmailUnique(){
        // Arrange
        String email = "test@email.ca";
        when(gestionnaireRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act
        boolean isUnique = service.isEmailUnique(email);

        // Assert
        assertThat(isUnique).isTrue();
    }

    @Test
    public void testGetGestionnaireByIdSuccess() throws NonExistentUserException {
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
        } catch (NonExistentUserException e) {
            // Assert
            return;
        }
        fail("NonExistentUserException not caught");
    }

    @Test
    public void testGetGestionnaireByEmailPasswordSuccess() throws NonExistentUserException {
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
        } catch (NonExistentUserException e) {
            // Assert
            return;
        }
        fail("NonExistentUserException not caught");
    }

    @Test
    public void testValidateCompanySuccess() throws NonExistentUserException{
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
        } catch (NonExistentUserException e) {
            return;
        }
        fail("NonExistentUserException not caught");
    }

    @Test
    public void testValidateStudentSuccess() throws NonExistentUserException{
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentTest));

        // Act
        service.validateStudent(1L);

        // Assert
        assertThat(studentTest.isConfirm()).isTrue();
    }

    @Test
    public void testValidateStudentNotFound(){
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            service.validateStudent(1L);
        } catch (NonExistentUserException e) {
            return;
        }

        fail("NonExistentUserException not caught");
    }

    @Test
    public void testRemoveCompanySuccess() throws NonExistentUserException {
        // Arrange
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(companyTest));
        doNothing().when(companyRepository).delete(any());

        // Act
        service.removeCompany(1L);

        // Assert
        verify(companyRepository).delete(companyTest);
    }

    @Test
    public void testRemoveCompanyNotFound(){
        // Arrange
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            service.removeCompany(1L);
        } catch (NonExistentUserException e) {
            return;
        }

        fail("NonExistentUserException not caught");
    }

    @Test
    public void testRemoveStudentSuccess() throws NonExistentUserException {
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
        } catch (NonExistentUserException e) {
            return;
        }
        fail("NonExistentUserException not caught");
    }


    @Test
    public void testOffreNotValidated(){
        // Arrange
        List<Offre> offres = new ArrayList<>();
        Offre offre = new Offre();
        offre.setDepartment(AbstractUser.Department.Informatique);
        offres.add(offre);
        offre = new Offre();
        offre.setDepartment(AbstractUser.Department.Informatique);
        offres.add(offre);
        offre = new Offre();
        offre.setDepartment(AbstractUser.Department.Informatique);
        offres.add(offre);

        offre = new Offre();
        offre.setDepartment(AbstractUser.Department.Informatique);
        offre.setValide(true);
        offres.add(offre);

        when(offreRepository.findAll()).thenReturn(offres);

        // Act
        final List<OffreDTO> noneValidateOffers = service.getNoneValidateOffers();

        // Assert
        assertThat(noneValidateOffers.size()).isEqualTo(3);
    }

    @Test
    public void testValidateOfferByIdSuccess() throws NonExistentOfferExeption {
        // Arrange
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offreTest));

        // Act
        final OffreDTO offreDTO = service.validateOfferById(1L);

        // Assert
        assertThat(offreDTO.isValide()).isTrue();
    }

    @Test
    public void testValidateOfferByIdNotFound(){
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
    public void testGetUnvalidatedStudents(){
        // Arrange
        List<Student> students = new ArrayList<>();

        Student student = new Student();
        student.setDepartment(AbstractUser.Department.Informatique);
        students.add(student);

        student = new Student();
        student.setDepartment(AbstractUser.Department.Informatique);
        student.setEmailConfirmed(true);
        students.add(student);

        student = new Student();
        student.setDepartment(AbstractUser.Department.Informatique);
        student.setEmailConfirmed(true);
        students.add(student);

        student = new Student();
        student.setDepartment(AbstractUser.Department.Informatique);
        student.setEmailConfirmed(true);
        student.setConfirm(true);
        students.add(student);

        when(studentRepository.findAll()).thenReturn(students);

        // Act
        List<StudentDTO> unvalidatedStudents = service.getUnvalidatedStudents();

        // Assert
        assertThat(unvalidatedStudents.size()).isEqualTo(2);
    }

    @Test
    public void testGetUnvalidatedCompanies(){
        // Arrange
        List<Company> companies = new ArrayList<>();

        Company company = new Company();
        company.setDepartment(AbstractUser.Department.Informatique);
        companies.add(company);

        company = new Company();
        company.setDepartment(AbstractUser.Department.Informatique);
        company.setEmailConfirmed(true);
        companies.add(company);

        company = new Company();
        company.setDepartment(AbstractUser.Department.Informatique);
        company.setEmailConfirmed(true);
        companies.add(company);

        company = new Company();
        company.setDepartment(AbstractUser.Department.Informatique);
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
        byte[] pdf = service.getOffrePdfById(1L);

        // Assert
        assertThat(pdf).isEqualTo(offreTest.getPdf());
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
}
