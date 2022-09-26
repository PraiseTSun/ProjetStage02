package projet.projetstage02.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projet.projetstage02.DTO.OffreDTO;
import projet.projetstage02.DTO.OffreValidateDTO;
import projet.projetstage02.model.Offre;
import projet.projetstage02.repository.CompanyRepository;
import projet.projetstage02.repository.GestionnaireRepository;
import projet.projetstage02.repository.OffreRepository;
import projet.projetstage02.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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

//    @InjectMocks
    private GestionnaireService service;

    @BeforeEach
    void beforeEach(){
        service = new GestionnaireService(
                gestionnaireRepository,
                companyRepository,
                studentRepository,
                offreRepository
        );
    }

    @Test
    public void offreNotValidated(){
        // Arrange
        List<Offre> offres = new ArrayList<>();
        offres.add(new Offre());
        offres.add(new Offre());
        offres.add(new Offre());

        Offre offre = new Offre();
        offre.setValide(true);
        offres.add(offre);

        when(offreRepository.findAll()).thenReturn(offres);

        // Act
        final List<OffreValidateDTO> noneValidateOffers = service.getNoneValidateOffers();

        // Assert
        assertThat(noneValidateOffers.size()).isEqualTo(3);
    }

    @Test
    public void validateOfferById(){
        // Arrange
        Offre offre = new Offre();
        when(offreRepository.findById(anyLong())).thenReturn(Optional.of(offre));

        // Act
        final OffreDTO offreDTO = service.valideOfferById(1L);

        // Assert
        assertThat(offreDTO.isValide()).isTrue();
    }
}
