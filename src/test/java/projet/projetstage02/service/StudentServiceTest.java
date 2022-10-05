package projet.projetstage02.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projet.projetstage02.DTO.PdfDTO;
import projet.projetstage02.DTO.StudentDTO;
import projet.projetstage02.exception.NonExistentEntityException;
import projet.projetstage02.model.AbstractUser;
import projet.projetstage02.model.Student;
import projet.projetstage02.repository.StudentRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @InjectMocks
    StudentService studentService;

    @Mock
    StudentRepository studentRepository;

    Student bart;
    PdfDTO bartCv;

    @BeforeEach
    void setup() {
        bart = new Student(
                "Bart",
                "Simpson",
                "bart.simpson@springfield.com",
                "eatMyShorts",
                AbstractUser.Department.Informatique);

        bart.setCv(new byte[0]);

        bartCv = PdfDTO.builder().studentId(1).pdf(new byte[0]).build();
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
}
