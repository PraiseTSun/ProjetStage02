package projet.projetstage02.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projet.projetstage02.DTO.*;
import projet.projetstage02.exception.AlreadyExistingPostulation;
import projet.projetstage02.exception.NonExistentEntityException;
import projet.projetstage02.model.AbstractUser.Department;
import projet.projetstage02.model.Offre;
import projet.projetstage02.model.Postulation;
import projet.projetstage02.model.Student;
import projet.projetstage02.repository.OffreRepository;
import projet.projetstage02.repository.PostulationRepository;
import projet.projetstage02.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @InjectMocks
    StudentService studentService;

    @Mock
    StudentRepository studentRepository;
    @Mock
    OffreRepository offreRepository;
    @Mock
    PostulationRepository postulationRepository;

    Student bart;
    PdfDTO bartCv;
    Offre duffOffer;
    Postulation bartPostulation;

    @BeforeEach
    void setup() {
        bart = new Student(
                "Bart",
                "Simpson",
                "bart.simpson@springfield.com",
                "eatMyShorts",
                Department.Informatique);

        bart.setCv(new byte[0]);

        bartCv = PdfDTO.builder().studentId(1).pdf(new byte[0]).build();

        duffOffer = Offre.builder()
                .id(1L)
                .nomDeCompagnie("Duff")
                .department(Department.Transport)
                .position("Manager")
                .heureParSemaine(69)
                .adresse("Somewhere")
                .valide(true)
                .pdf(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9})
                .build();

        bartPostulation = Postulation.builder()
                .id(3L)
                .studentId(2L)
                .offerId(1L)
                .build();
    }

    @Test
    void getStudentByIdHappyDayTest() throws NonExistentEntityException {
        // Arrange
        when(studentRepository.findById(anyLong()))
                .thenReturn(Optional.of(bart));

        // Act
        StudentDTO studentDTO = studentService.getStudentById(1L);

        // Assert
        assertThat(studentDTO.toModel()).isEqualTo(bart);
    }

    @Test
    void getStudentByIdNonExistentTest() {
        // Arrange
        when(studentRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // Act
        try {
            studentService.getStudentById(1L);
        } catch (NonExistentEntityException e) {

            // Assert
            return;
        }
        fail("NonExistentUserException not caught");
    }

    @Test
    void getStudentByEmailAndPasswordHappyDayTest() throws NonExistentEntityException {
        // Arrange
        when(studentRepository.findByEmailAndPassword(
                "bart.simpson@springfield.com",
                "eatMyShorts"))
                .thenReturn(Optional.of(bart));

        // Act
        StudentDTO studentDTO = studentService
                .getStudentByEmailPassword(
                        "bart.simpson@springfield.com",
                        "eatMyShorts");

        // Assert
        assertThat(studentDTO.toModel()).isEqualTo(bart);
    }

    @Test
    void getStudentByEmailAndPasswordNonExistentTest() {
        // Arrange
        when(studentRepository.findByEmailAndPassword(anyString(), anyString()))
                .thenReturn(Optional.empty());

        // Act
        try {
            studentService.getStudentByEmailPassword(
                    "bart.simpson@springfield.com",
                    "eatMyShorts");
        } catch (NonExistentEntityException e) {

            // Assert
            return;
        }
        fail("NonExistentUserException not caught");
    }

    @Test
    void isEmailUniqueExistsTest() {
        // Arrange
        when(studentRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(bart));

        // Act
        boolean isEmailUnique = studentService
                .isEmailUnique("bart.simpson@springfield.com");

        // Assert
        assertThat(isEmailUnique).isFalse();
    }

    @Test
    void isEmailUniqueNotExistsTest() {
        // Arrange
        when(studentRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        // Act
        boolean isEmailUnique = studentService
                .isEmailUnique("el.barto@springfield.com");

        // Assert
        assertThat(isEmailUnique).isTrue();
    }

    @Test
    void saveStudentMultipleParametersTest() {
        // Arrange
        bart.setId(1L);
        when(studentRepository.save(any())).thenReturn(bart);

        // Act
        studentService.saveStudent(
                bart.getFirstName(),
                bart.getLastName(),
                bart.getEmail(),
                bart.getPassword(),
                bart.getDepartment());

        // Assert
        verify(studentRepository, times(1)).save(any());
    }

    @Test
    void saveStudentDTOTest() {
        // Arrange
        when(studentRepository.save(any()))
                .thenReturn(bart);

        // Act
        studentService.saveStudent(new StudentDTO(bart));

        // Assert
        verify(studentRepository, times(1)).save(any());
    }

    @Test
    void testUploadCurriculumVitaeSuccess() throws NonExistentEntityException {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(bart));
        when(studentRepository.save(any())).thenReturn(bart);

        //Act
        studentService.uploadCurriculumVitae(bartCv);

        // Assert
        verify(studentRepository, times(1)).save(any());
    }

    @Test
    void testUploadCurriculumVitaeNotFound(){
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            studentService.uploadCurriculumVitae(bartCv);
        } catch (NonExistentEntityException e) {
            return;
        }

        fail("NonExistentUserException not caught");
    }
    @Test
    void testDeleteUncofirmedStudentHappyDay() throws NonExistentEntityException {
        // Arrange
        bart.setInscriptionTimestamp(0);
        when(studentRepository.findByEmail(any())).thenReturn(Optional.of(bart));

        // Act
        studentService.deleteUnconfirmedStudent(new StudentDTO(bart));

        // Assert
        verify(studentRepository, times(1)).delete(any());
    }
    @Test
    void testDeleteUncofirmedStudentThrowsException()  {
        // Arrange
        bart.setInscriptionTimestamp(0);
        when(studentRepository.findByEmail(any())).thenReturn(Optional.empty());

        // Act
        try{
            studentService.deleteUnconfirmedStudent(new StudentDTO(bart));
        }catch (NonExistentEntityException e){
            return;
        }
        // Assert
        fail("NonExistentEntityException not thrown");
    }

    @Test
    void testGetOffersByStudentDepartmentSuccess() throws NonExistentEntityException {
        // Arrange
        Department department = Department.Informatique;
        Offre successOffer = Offre.builder().valide(true).department(Department.Informatique).build();
        Offre failOffer1   = Offre.builder().valide(false).department(Department.Informatique).build();
        Offre failOffer2   = Offre.builder().valide(true).department(Department.Transport).build();
        List<Offre> offres = new ArrayList<>(){{
            add(successOffer);
            add(failOffer1);
            add(failOffer2);
        }};
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(bart));
        when(offreRepository.findAll()).thenReturn(offres);

        // Act
        List<OffreDTO> offerDTOs = studentService.getOffersByStudentDepartment(1L);

        // Assert
        assertThat(offerDTOs.size()).isEqualTo(1);
        assertThat(offerDTOs.get(0).isValide()).isEqualTo(true);
        assertThat(offerDTOs.get(0).getDepartment()).isEqualTo(department.departement);
    }

    @Test
    void testGetOffersByStudentDepartmentNotFound() {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            studentService.getOffersByStudentDepartment(1L);
        } catch (NonExistentEntityException e) {
            return;
        }
        fail("NonExistentEntityException not thrown");
    }

    @Test
    void testGetOfferByIdSuccess() throws NonExistentEntityException {
        // Arrange
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(duffOffer));

        // Act
        PdfOutDTO dto = studentService.getOfferById(1L);

        // Assert
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getPdf()).isEqualTo("[1,2,3,4,5,6,7,8,9]");
    }

    @Test
    void testGetOfferByIdNotFound() {
        // Arrange
        when(offreRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            studentService.getOfferById(1L);
        } catch (NonExistentEntityException e) {
            return;
        }

        fail("NonExistentEntityException not thrown");
    }

    @Test
    void testCreatePostulationSuccess() throws Exception {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(bart));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(duffOffer));
        when(postulationRepository.findByStudentIdAndOfferId(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        // Act
        studentService.createPostulation(2L, 1L);

        // Assert
        verify(postulationRepository, times(1)).save(any());
    }

    @Test
    void testCreatePostulationAlreadyExist() {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(bart));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(duffOffer));
        when(postulationRepository.findByStudentIdAndOfferId(anyLong(), anyLong()))
                .thenReturn(Optional.of(bartPostulation));

        // Act
        try {
            studentService.createPostulation(2L,1L);
        } catch (AlreadyExistingPostulation e) {
            return;
        } catch (Exception e) {}

        fail("AlreadyExistingPostulation not thrown");
    }

    @Test
    void testCreatePostulationOfferNotFound() {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(bart));
        when(offreRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            studentService.createPostulation(2L,1L);
        } catch (NonExistentEntityException e) {
            return;
        } catch (Exception e) {}

        fail("NonExistingEntityException not thrown");
    }

    @Test
    void testCreatePostulationStudentNotFound() {
        // Arrange
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        try {
            studentService.createPostulation(2L,1L);
        } catch (NonExistentEntityException e) {
            return;
        } catch (Exception e) {}

        fail("NonExistingEntityException not thrown");
    }
}
