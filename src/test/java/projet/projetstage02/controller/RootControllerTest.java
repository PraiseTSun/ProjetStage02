package projet.projetstage02.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import projet.projetstage02.dto.SignatureInDTO;
import projet.projetstage02.dto.auth.LoginDTO;
import projet.projetstage02.dto.auth.TokenDTO;
import projet.projetstage02.dto.contracts.ContractsDTO;
import projet.projetstage02.dto.contracts.StageContractOutDTO;
import projet.projetstage02.dto.offres.OffreInDTO;
import projet.projetstage02.dto.users.CompanyDTO;
import projet.projetstage02.dto.users.GestionnaireDTO;
import projet.projetstage02.dto.users.Students.StudentInDTO;
import projet.projetstage02.exception.InvalidTokenException;
import projet.projetstage02.model.AbstractUser.Department;
import projet.projetstage02.service.AuthService;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static projet.projetstage02.model.Token.UserTypes.*;

@ExtendWith(MockitoExtension.class)
public class RootControllerTest {
    MockMvc mockMvc;

    @InjectMocks
    RootController rootController;

    @Mock
    AuthService authService;

    JacksonTester<LoginDTO> jsonLoginDTO;


    StudentInDTO bart;
    CompanyDTO duffBeer;
    TokenDTO token;
    LoginDTO login;
    GestionnaireDTO burns;
    OffreInDTO duffOffre;
    StageContractOutDTO stageContractOutDTO;
    SignatureInDTO signatureInDTO;
    ContractsDTO contractsDTO;

    // https://thepracticaldeveloper.com/guide-spring-boot-controller-tests/
    @BeforeEach
    void setup() {
        bart = StudentInDTO.builder()
                .firstName("Bart")
                .lastName("Simpson")
                .email("bart.simpson@springfield.com")
                .department(Department.Transport.departement)
                .password("eatMyShorts")
                .isConfirmed(false)
                .inscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
                .build();

        duffBeer = CompanyDTO.builder()
                .firstName("Duff")
                .lastName("Man")
                .isConfirmed(false)
                .email("duff.beer@springfield.com")
                .password("bestBeer")
                .department(Department.Transport.departement)
                .companyName("Duff Beer")
                .inscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
                .build();

        burns = GestionnaireDTO.builder()
                .firstName("Charles")
                .lastName("Burns")
                .isConfirmed(true)
                .email("charles.burns@springfield.com")
                .password("excellent")
                .build();

        duffOffre = OffreInDTO.builder()
                .nomDeCompagnie("Duff Beer")
                .department(Department.Transport.departement)
                .position("Delivery Guy")
                .heureParSemaine(40)
                .salaire(40)
                .dateStageDebut("2020-01-01")
                .dateStageFin("2020-01-01")
                .session("Hiver 2022")
                .adresse("654 Duff Street")
                .pdf(new byte[0])
                .build();

        token = TokenDTO.builder()
                .token("9f0b0e68-c177-4504-9087-eea175653ee3")
                .build();

        login = LoginDTO.builder().build();

        JacksonTester.initFields(this, new ObjectMapper());

        mockMvc = MockMvcBuilders.standaloneSetup(rootController).build();

        signatureInDTO = SignatureInDTO.builder()
                .token(token.getToken())
                .contractId(10L)
                .userId(11L)
                .signature("")
                .build();

        stageContractOutDTO = StageContractOutDTO.builder()
                .contractId(9L)
                .studentId(7L)
                .offerId(8L)
                .companyId(6L)
                .description("description")
                .companySignature(signatureInDTO.getSignature())
                .studentSignature(signatureInDTO.getSignature())
                .build();

        contractsDTO = new ContractsDTO();
        contractsDTO.add(stageContractOutDTO);
        contractsDTO.add(stageContractOutDTO);
    }

    @Test
    void testGetTokenStudentHappyDay() throws Exception {
        login.setEmail(bart.getEmail());
        login.setPassword(bart.getPassword());
        when(authService.loginIfValid(login, STUDENT))
                .thenReturn(token.getToken());

        mockMvc.perform(post("/student/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginDTO.write(login).getJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", is(token.getToken())));
    }


    @Test
    void testLoginStudentNotEmailConfirmed() throws Exception {
        login.setEmail(bart.getEmail());
        login.setPassword(bart.getPassword());
        bart.setEmailConfirmed(false);
        when(authService.loginIfValid(login, STUDENT)).thenThrow(new InvalidTokenException());
        mockMvc.perform(post("/student/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginDTO.write(login).getJson()))

                .andExpect(status().isForbidden());
        verify(authService, times(1)).loginIfValid(any(), any());
    }

    @Test
    void testGetTokenCompanyHappyDay() throws Exception {
        login.setEmail(duffBeer.getEmail());
        login.setPassword(duffBeer.getPassword());
        when(authService.loginIfValid(login, COMPANY))
                .thenReturn(token.getToken());

        mockMvc.perform(post("/company/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginDTO.write(login).getJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", is(token.getToken())));
    }

    @Test
    void testLoginCompanyNotEmailConfirmed() throws Exception {
        login.setEmail(duffBeer.getEmail());
        login.setPassword(duffBeer.getPassword());
        duffBeer.setEmailConfirmed(false);
        when(authService.loginIfValid(login, COMPANY)).thenThrow(new InvalidTokenException());
        mockMvc.perform(post("/company/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginDTO.write(login).getJson()))

                .andExpect(status().isForbidden());
        verify(authService, times(1)).loginIfValid(any(), any());
    }

    @Test
    void testGetTokenGestionnaireHappyDay() throws Exception {
        login.setEmail(burns.getEmail());
        login.setPassword(burns.getPassword());
        when(authService.loginIfValid(login, GESTIONNAIRE))
                .thenReturn(token.getToken());

        mockMvc.perform(post("/gestionnaire/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginDTO.write(login).getJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", is(token.getToken())));
    }

    @Test
    void testLoginGestionnaireNotEmailConfirmed() throws Exception {
        login.setEmail(burns.getEmail());
        login.setPassword(burns.getPassword());
        bart.setEmailConfirmed(false);
        when(authService.loginIfValid(login, GESTIONNAIRE)).thenThrow(new InvalidTokenException());
        mockMvc.perform(post("/gestionnaire/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginDTO.write(login).getJson()))

                .andExpect(status().isForbidden());
        verify(authService, times(1)).loginIfValid(any(), any());
    }
}