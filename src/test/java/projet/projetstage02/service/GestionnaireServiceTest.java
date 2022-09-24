package projet.projetstage02.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

public class GestionnaireServiceTest {

    @InjectMocks
    private GestionnaireService gestionnaireService;

    @Test
    public void offerToValidate(){
        // Arrange

        // Act
        gestionnaireService.getNoneValidateOffers();
        // Assert
    }
}
