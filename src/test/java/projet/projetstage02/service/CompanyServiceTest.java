package projet.projetstage02.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.DTO.OffreDTO;
import projet.projetstage02.exception.NonExistentEntityException;
import projet.projetstage02.model.AbstractUser;
import projet.projetstage02.model.Company;
import projet.projetstage02.model.Offre;
import projet.projetstage02.repository.CompanyRepository;
import projet.projetstage02.repository.OffreRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @InjectMocks
    CompanyService companyService;

    @Mock
    CompanyRepository companyRepository;

    @Mock
    OffreRepository offreRepository;

    Company duffBeer;
    Offre duffBeerOffre;

    @BeforeEach
    void setup() {
        duffBeer = new Company(
                "Duff",
                "Man",
                "duff.beer@springfield.com",
                "bestBeer",
                AbstractUser.Department.Transport,
                "Duff Beer");

        duffBeerOffre = Offre.builder()
                .adresse("653 Duff Street")
                .department(AbstractUser.Department.Transport)
                .heureParSemaine(40)
                .position("Delivery Guy")
                .nomDeCompagnie("Duff beer")
                .pdf(new byte[0])
                .build();
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
    void getCompanyByEmailAndPasswordHappyDayTest() throws NonExistentEntityException{
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
    void isEmailUniqueExistsTest() {
        // Arrange
        when(companyRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(duffBeer));

        // Act
        boolean isEmailUnique = companyService
                .isEmailUnique("duff.beer@springfield.com");

        // Assert
        assertThat(isEmailUnique).isFalse();
    }

    @Test
    void isEmailUniqueNotExistsTest() {
        // Arrange
        when(companyRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        // Act
        boolean isEmailUnique = companyService
                .isEmailUnique("el.barto@springfield.com");

        // Assert
        assertThat(isEmailUnique).isTrue();
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
        when(offreRepository.save(any())).thenReturn(duffBeerOffre);

        // Act
        companyService.createOffre(new OffreDTO(duffBeerOffre));

        // Assert
        verify(offreRepository, times(1)).save(any());
    }
}
