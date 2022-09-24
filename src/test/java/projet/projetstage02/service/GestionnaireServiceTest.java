package projet.projetstage02.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projet.projetstage02.DTO.OffreValidateDTO;
import projet.projetstage02.model.Offre;
import projet.projetstage02.repository.OffreRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GestionnaireServiceTest {

    @Mock
    private OffreRepository offreRepository;
    @InjectMocks
    private GestionnaireService service;

    @Test
    public void offerNotValidated(){
        // Arrange
        List<OffreValidateDTO> offres = new ArrayList<>();
        offres.add(new OffreValidateDTO());

//        when(offreRepository.findAllByValide(false)).thenReturn(offres);

        // Act
        final List<OffreValidateDTO> noneValidateOffers = service.getNoneValidateOffers();

        // Assert
        assertThat(noneValidateOffers.size()).isEqualTo(3);
    }
}
