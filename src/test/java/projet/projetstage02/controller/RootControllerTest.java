package projet.projetstage02.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import projet.projetstage02.DTO.OffreValidateDTO;
import projet.projetstage02.service.GestionnaireService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RootControllerTest {
    @MockBean
    private GestionnaireService gestionnaireService;

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    @DisplayName("Get /validateOffers")
//    void testGetOfferToValidate() throws Exception {
//        // Setup mocked service
//        List<OffreValidateDTO> dtos = new ArrayList<>();
//        dtos.add(new OffreValidateDTO("1", "Bell", "Techniques de linformatique","Stagiaire"));
//        dtos.add(new OffreValidateDTO("2", "Amazon", "Techniques de linformatique","Stagiaire"));
//        dtos.add(new OffreValidateDTO("3", "RBC", "Techniques de linformatique","Stagiaire"));
//
//        when(gestionnaireService.getNoneValidateOffers()).thenReturn(dtos);
//
//        // Execute the GET request
//        mockMvc.perform(get("/validateOffers"))
//                // Validate the response code and content type
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//
//                // Validate the headers
//                .andExpect(header().string(HttpHeaders.LOCATION, "/validateOffers"))
//
//                //
//                .
//        ;
//    }
}
