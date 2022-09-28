package projet.projetstage02.service;

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

    @Test
    public void testSaveGestionnaireByParams(){
        // Arrange
        Gestionnaire gestionnaire = new Gestionnaire();
        when(gestionnaireRepository.save(any())).thenReturn(gestionnaire);

        // Act
        service.saveGestionnaire("Dave", "Chapel", "email", "password");

        // Assert
        verify(gestionnaireRepository, times(1)).save(any());
    }

    @Test
    public void testSaveGestionnaireByDTO(){
        // Arrange
        Gestionnaire gestionnaire = new Gestionnaire();
        when(gestionnaireRepository.save(any())).thenReturn(gestionnaire);

        // Act
        service.saveGestionnaire(new GestionnaireDTO(gestionnaire));

        // Assert
        verify(gestionnaireRepository, times(1)).save(any());
    }

    @Test
    public void testIsEmailUnique(){
        // Arrange
        String email = "test@email.ca";
        Optional<Gestionnaire> gestionnaire = Optional.empty();
        when(gestionnaireRepository.findByEmail(anyString())).thenReturn(gestionnaire);

        // Act
        boolean isUnique = service.isEmailUnique(email);

        // Assert
        assertThat(isUnique).isTrue();
    }

    @Test
    public void testGetGestionnaireByIdSucess() throws NonExistentUserException {
        // Arrange
        Gestionnaire gestionnaire = new Gestionnaire();
        when(gestionnaireRepository.findById(anyLong())).thenReturn(Optional.of(gestionnaire));

        // Act
        GestionnaireDTO dto = service.getGestionnaireById(1L);

        // Assert
        assertThat(dto.toModel()).isEqualTo(gestionnaire);
    }

    @Test
    public void testGetGestionnaireByIdFail() throws NonExistentUserException {
        // Arrange
        Gestionnaire gestionnaire = new Gestionnaire();
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
    public void testGetGestionnaireByEmailPassword() throws NonExistentUserException {
        // Arrange
        String email = "test@email.ca";
        String password = "testPassword";
        Gestionnaire gestionnaire = new Gestionnaire();
        gestionnaire.setEmail(email);
        gestionnaire.setPassword(password);
        when(gestionnaireRepository.findByEmailAndPassword(anyString(), anyString()))
                .thenReturn(Optional.of(gestionnaire));

        // Act
        GestionnaireDTO dto = service.getGestionnaireByEmailPassword(anyString(), anyString());

        //Assert
        assertThat(dto.getEmail()).isEqualTo(email);
        assertThat(dto.getPassword()).isEqualTo(password);
    }

    @Test
    public void testValidateCompany() throws NonExistentUserException{
        // Arrange
        Company company = new Company();
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));

        // Act
        service.validateCompany(1L);

        // Assert
        assertThat(company.isConfirm()).isTrue();
    }

    @Test
    public void testValidateStudent() throws NonExistentUserException{
        // Arrange
        Student student = new Student();
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));

        // Act
        service.validateStudent(1L);

        // Assert
        assertThat(student.isConfirm()).isTrue();
    }

    @Test
    public void testRemoveCompany() throws NonExistentUserException {
        // Arrange
        Company company = new Company();
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));
        doNothing().when(companyRepository).delete(any());

        // Act
        service.removeCompany(1L);

        // Assert
        verify(companyRepository).delete(company);
    }

    @Test
    public void testRemoveStudent() throws NonExistentUserException {
        // Arrange
        Student student = new Student();
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).delete(any());

        // Act
        service.removeStudent(1L);

        // Assert
        verify(studentRepository).delete(student);
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
    public void testValidateOfferById() throws NonExistentOfferExeption {
        // Arrange
        Offre offre = new Offre();
        offre.setDepartment(AbstractUser.Department.Informatique);
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offre));

        // Act
        final OffreDTO offreDTO = service.validateOfferById(1L);

        // Assert
        assertThat(offreDTO.isValide()).isTrue();
    }

    @Test
    public void testRemoveOfferById() throws NonExistentOfferExeption {
        // Arrange
        Offre offre = new Offre();
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offre));
        doNothing().when(offreRepository).delete(any());

        // Act
        service.removeOfferById(1L);

        // Assert
        verify(offreRepository).delete(offre);
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
}
