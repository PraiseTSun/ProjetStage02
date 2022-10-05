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
import projet.projetstage02.DTO.*;
import projet.projetstage02.exception.InvalidTokenException;
import projet.projetstage02.exception.NonExistentEntityException;
import projet.projetstage02.exception.NonExistentOfferExeption;
import projet.projetstage02.model.AbstractUser.Department;
import projet.projetstage02.model.Token;
import projet.projetstage02.service.AuthService;
import projet.projetstage02.service.CompanyService;
import projet.projetstage02.service.GestionnaireService;
import projet.projetstage02.service.StudentService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static projet.projetstage02.model.Token.UserTypes.*;
import static projet.projetstage02.utils.TimeUtil.currentTimestamp;

@ExtendWith(MockitoExtension.class)
public class RootControllerTest {

    MockMvc mockMvc;

    @InjectMocks
    RootController rootController;

    @Mock
    StudentService studentService;

    @Mock
    CompanyService companyService;
    @Mock
    AuthService authService;

    @Mock
    GestionnaireService gestionnaireService;

    JacksonTester<StudentDTO> jsonStudentDTO;
    JacksonTester<LoginDTO> jsonLoginDTO;
    JacksonTester<CompanyDTO> jsonCompanyDTO;
    JacksonTester<GestionnaireDTO> jsonGestionnaireDTO;
    JacksonTester<OffreDTO> jsonOffreDTO;
    JacksonTester<TokenDTO> jsonTokenDTO;
    JacksonTester<PdfDTO> jsonPdfDTO;

    StudentDTO bart;
    CompanyDTO duffBeer;
    TokenDTO token;
    LoginDTO login;
    GestionnaireDTO burns;
    OffreDTO duffOffre;
    PdfDTO bartCV;

    // https://thepracticaldeveloper.com/guide-spring-boot-controller-tests/
    @BeforeEach
    void setup() {
        bart = StudentDTO.builder()
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

        duffOffre = OffreDTO.builder()
                .nomDeCompagnie("Duff Beer")
                .department(Department.Transport.departement)
                .position("Delivery Guy")
                .heureParSemaine(40)
                .adresse("654 Duff Street")
                .pdf(new byte[0])
                .build();

        token = TokenDTO.builder()
                .token("9f0b0e68-c177-4504-9087-eea175653ee3")
                .build();

        login = LoginDTO.builder().build();
        bartCV = PdfDTO.builder().pdf(new byte[0]).studentId(1).token(token.getToken()).build();

        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(rootController).build();
    }

    @Test
    void testCreateStudentHappyDay() throws Exception {
        when(studentService.isEmailUnique(anyString())).thenReturn(true);
        when(studentService.saveStudent(any())).thenReturn(1L);
        mockMvc.perform(post("/createStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStudentDTO.write(bart).getJson()))

                .andExpect(status().isCreated());
    }

    @Test
    void testCreateStudentConflict() throws Exception {
        when(studentService.isEmailUnique(anyString())).thenReturn(false);

        mockMvc.perform(post("/createStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStudentDTO.write(bart).getJson()))

                .andExpect(status().isConflict());
    }

    @Test
    void testCreateStudentBadRequest() throws Exception {
        mockMvc.perform(post("/createStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStudentDTO.write(new StudentDTO()).getJson()))

                .andExpect(status().isBadRequest());
    }
    @Test
    void testCreateStudentExistingEmailDeleteOld() throws Exception {
        when(studentService.isEmailUnique(anyString())).thenReturn(false);
        when(studentService.deleteUnconfirmedStudent(any())).thenReturn(true);
        mockMvc.perform(post("/createStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStudentDTO.write(bart).getJson()))

                .andExpect(status().isCreated());
    }

    @Test
    void testCreateStudentExistingEmailDeleteOldNotFound() throws Exception {
        when(studentService.isEmailUnique(anyString())).thenReturn(false);
        when(studentService.deleteUnconfirmedStudent(any())).thenThrow(new NonExistentEntityException());
        mockMvc.perform(post("/createStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStudentDTO.write(bart).getJson()))

                .andExpect(status().isInternalServerError());
    }
    @Test
    void testCreateCompanyHappyDay() throws Exception {
        when(companyService.isEmailUnique(anyString())).thenReturn(true);
        when(companyService.saveCompany(any())).thenReturn(1L);

        mockMvc.perform(post("/createCompany")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCompanyDTO.write(duffBeer).getJson()))

                .andExpect(status().isCreated());
    }

    @Test
    void testCreateCompanyConflict() throws Exception {
        when(companyService.isEmailUnique(anyString())).thenReturn(false);

        mockMvc.perform(post("/createCompany")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCompanyDTO.write(duffBeer).getJson()))

                .andExpect(status().isConflict());
    }
    @Test
    void testCreateCompanyExistingEmailDeleteOld() throws Exception {
        when(companyService.isEmailUnique(anyString())).thenReturn(false);
        when(companyService.deleteUnconfirmedCompany(any())).thenReturn(true);
        mockMvc.perform(post("/createCompany")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCompanyDTO.write(duffBeer).getJson()))

                .andExpect(status().isCreated());
    }

    @Test
    void testCreateCompanyBadRequest() throws Exception {
        mockMvc.perform(post("/createCompany")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCompanyDTO.write(new CompanyDTO()).getJson()))

                .andExpect(status().isBadRequest());
    }
    @Test
    void testCreateCompanyExistingEmailDeleteOldNotFound() throws Exception {
        when(companyService.isEmailUnique(anyString())).thenReturn(false);
        when(companyService.deleteUnconfirmedCompany(any())).thenThrow(new NonExistentEntityException());
        mockMvc.perform(post("/createCompany")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCompanyDTO.write(duffBeer).getJson()))

                .andExpect(status().isInternalServerError());
    }
    @Test
    void testCreateGestionnaireHappyDay() throws Exception {
        burns.setToken(token.getToken());

        when(gestionnaireService.isEmailUnique(anyString())).thenReturn(true);
        when(gestionnaireService.saveGestionnaire(any())).thenReturn(1L);

        mockMvc.perform(post("/createGestionnaire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonGestionnaireDTO.write(burns).getJson()))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateGestionnaireConflict() throws Exception {
        burns.setToken(token.getToken());

        when(gestionnaireService.isEmailUnique(anyString())).thenReturn(false);

        mockMvc.perform(post("/createGestionnaire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonGestionnaireDTO.write(burns).getJson()))

                .andExpect(status().isConflict());
    }

    @Test
    void testCreateGestionnaireBadRequest() throws Exception {

        mockMvc.perform(post("/createGestionnaire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonGestionnaireDTO.write(new GestionnaireDTO()).getJson()))

                .andExpect(status().isBadRequest());
    }
    @Test
    void testCreateGestionnaireExistingEmailDeleteOldNotFound() throws Exception {
        when(gestionnaireService.isEmailUnique(anyString())).thenReturn(false);
        when(gestionnaireService.deleteUnconfirmedGestionnaire(any())).thenThrow(new NonExistentEntityException());
        mockMvc.perform(post("/createGestionnaire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonGestionnaireDTO.write(burns).getJson()))

                .andExpect(status().isInternalServerError());
    }
    @Test
    void testCreateGestionnaireInvalidToken() throws Exception {
        when(authService.getToken(any(),any())).thenThrow(new InvalidTokenException());
        mockMvc.perform(post("/createGestionnaire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonGestionnaireDTO.write(burns).getJson()))

                .andExpect(status().isForbidden());
    }
    @Test
    void testCreateOffreHappyDay() throws Exception {
        duffOffre.setToken(token.getToken());

        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        when(companyService.createOffre(any())).thenReturn(1L);

        mockMvc.perform(post("/createOffre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOffreDTO.write(duffOffre).getJson()))

                .andExpect(status().isCreated());
    }

    @Test
    void testCreateOffreBadRequest() throws Exception {
        mockMvc.perform(post("/createOffre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOffreDTO.write(new OffreDTO()).getJson()))

                .andExpect(status().isBadRequest());
    }
    @Test
    void testCreateOffreInvalidToken() throws Exception {
        duffOffre.setToken(token.getToken());
        when(authService.getToken(any(),any())).thenThrow(new InvalidTokenException());
        mockMvc.perform(post("/createOffre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOffreDTO.write(duffOffre).getJson()))

                .andExpect(status().isForbidden());
    }

    @Test
    void testConfirmStudentEmailHappyDay() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(bart);
        when(studentService.saveStudent(bart)).thenReturn(1L);

        mockMvc.perform(
                        put("/confirmEmail/student/{id}", 1))

                .andExpect(status().isCreated());
    }

    @Test
    void testConfirmStudentEmailNotFound() throws Exception {
        when(studentService.getStudentById(1L)).thenThrow(new NonExistentEntityException());

        mockMvc.perform(
                        put("/confirmEmail/student/{id}", 1))

                .andExpect(status().isNotFound());
    }

    @Test
    void testConfirmStudentEmailExpired() throws Exception {
        bart.setInscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now().minusMonths(1)).getTime());
        when(studentService.getStudentById(1L)).thenReturn(bart);

        mockMvc.perform(
                        put("/confirmEmail/student/{id}", 1))

                .andExpect(status().isBadRequest());
    }

    @Test
    void testConfirmCompanyEmailHappyDay() throws Exception {
        when(companyService.getCompanyById(1L)).thenReturn(duffBeer);
        when(companyService.saveCompany(duffBeer)).thenReturn(1L);

        mockMvc.perform(
                        put("/confirmEmail/company/{id}", 1))

                .andExpect(status().isCreated());
    }

    @Test
    void testConfirmCompanyEmailNotFound() throws Exception {
        when(companyService.getCompanyById(1L)).thenThrow(new NonExistentEntityException());

        mockMvc.perform(
                        put("/confirmEmail/company/{id}", 1))

                .andExpect(status().isNotFound());
    }

    @Test
    void testConfirmCompanyEmailExpired() throws Exception {
        duffBeer.setInscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now().minusMonths(1)).getTime());
        when(companyService.getCompanyById(1L)).thenReturn(duffBeer);

        mockMvc.perform(
                        put("/confirmEmail/company/{id}", 1))

                .andExpect(status().isBadRequest());
    }
    @Test
    void testConfirmGestionnaireEmailHappyDay() throws Exception {
        burns.setInscriptionTimestamp(currentTimestamp());
        when(gestionnaireService.getGestionnaireById(1L)).thenReturn(burns);
        when(gestionnaireService.saveGestionnaire(burns)).thenReturn(1L);

        mockMvc.perform(
                        put("/confirmEmail/gestionaire/{id}", 1))

                .andExpect(status().isCreated());
    }

    @Test
    void testConfirmGestionnaireEmailNotFound() throws Exception {
        when(gestionnaireService.getGestionnaireById(1L)).thenThrow(new NonExistentEntityException());
        mockMvc.perform(
                        put("/confirmEmail/gestionaire/{id}", 1))

                .andExpect(status().isNotFound());
    }

    @Test
    void testConfirmGestionnaireEmailExpired() throws Exception {
        bart.setInscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now().minusMonths(1)).getTime());
        when(gestionnaireService.getGestionnaireById(1L)).thenReturn(burns);

        mockMvc.perform(
                        put("/confirmEmail/gestionaire/{id}", 1))

                .andExpect(status().isBadRequest());
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
    void testLoginStudentHappyDay() throws Exception {
        bart.setEmailConfirmed(true);
        when(authService.getToken(token.getToken(),STUDENT)).thenReturn(Token.builder().userId(1L).build());
        when(studentService.getStudentById(1L)).thenReturn(bart);

        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Bart")));
    }

    @Test
    void testLoginStudentNotEmailConfirmed() throws Exception {
        login.setEmail(bart.getEmail());
        login.setPassword(bart.getPassword());
        bart.setEmailConfirmed(false);
        when(authService.loginIfValid(login,STUDENT)).thenThrow(new InvalidTokenException());
        mockMvc.perform(post("/student/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginDTO.write(login).getJson()))

                .andExpect(status().isForbidden());
        verify(authService, times(1)).loginIfValid(any(),any());
    }

    @Test
    void testLoginStudentNotFound() throws Exception {
        when(authService.getToken(token.getToken(),STUDENT)).thenReturn(Token.builder().userId(1).build());
        when(studentService.getStudentById(anyLong()))
                .thenThrow(new NonExistentEntityException());

        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))

                .andExpect(status().isNotFound());
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
    void testLoginCompanyHappyDay() throws Exception {
        duffBeer.setEmailConfirmed(true);
        when(authService.getToken(token.getToken(),COMPANY)).thenReturn(Token.builder().userId(1L).build());
        when(companyService.getCompanyById(1L)).thenReturn(duffBeer);

        mockMvc.perform(put("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Duff")));
    }

    @Test
    void testLoginCompanyNotEmailConfirmed() throws Exception {
        login.setEmail(duffBeer.getEmail());
        login.setPassword(duffBeer.getPassword());
        duffBeer.setEmailConfirmed(false);
        when(authService.loginIfValid(login,COMPANY)).thenThrow(new InvalidTokenException());
        mockMvc.perform(post("/company/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginDTO.write(login).getJson()))

                .andExpect(status().isForbidden());
        verify(authService, times(1)).loginIfValid(any(),any());
    }

    @Test
    void testLoginCompanyNotFound() throws Exception {
        when(authService.getToken(token.getToken(),COMPANY)).thenReturn(Token.builder().userId(1).build());
        when(companyService.getCompanyById(anyLong()))
                .thenThrow(new NonExistentEntityException());

        mockMvc.perform(put("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))

                .andExpect(status().isNotFound());
    }

    @Test
    void testGetTokenGestionnaireHappyDay() throws Exception {
        login.setEmail(burns.getEmail());
        login.setPassword(burns.getPassword());
        when(authService.loginIfValid(login,GESTIONNAIRE))
                .thenReturn(token.getToken());

        mockMvc.perform(post("/gestionnaire/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginDTO.write(login).getJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", is(token.getToken())));
    }
    @Test
    void testLoginGestionnaireHappyDay() throws Exception {
        burns.setEmailConfirmed(true);
        when(authService.getToken(token.getToken(),GESTIONNAIRE)).thenReturn(Token.builder().userId(1L).build());
        when(gestionnaireService.getGestionnaireById(1L)).thenReturn(burns);

        mockMvc.perform(put("/gestionnaire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Charles")));
    }

    @Test
    void testLoginGestionnaireNotEmailConfirmed() throws Exception {
        login.setEmail(burns.getEmail());
        login.setPassword(burns.getPassword());
        bart.setEmailConfirmed(false);
        when(authService.loginIfValid(login,GESTIONNAIRE)).thenThrow(new InvalidTokenException());
        mockMvc.perform(post("/gestionnaire/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginDTO.write(login).getJson()))

                .andExpect(status().isForbidden());
        verify(authService, times(1)).loginIfValid(any(),any());
    }

    @Test
    void testLoginGestionnaireNotFound() throws Exception {
        when(authService.getToken(token.getToken(),GESTIONNAIRE)).thenReturn(Token.builder().userId(1).build());
        when(gestionnaireService.getGestionnaireById(anyLong()))
                .thenThrow(new NonExistentEntityException());

        mockMvc.perform(put("/gestionnaire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))

                .andExpect(status().isNotFound());
    }
    @Test
    void testUnvalidatedStudentsHappyDay() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        when(gestionnaireService.getUnvalidatedStudents())
                .thenReturn(List.of(bart));

        mockMvc.perform(put("/unvalidatedStudents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("Bart")));
    }
    @Test
    void testUnvalidatedStudentsInvalidToken() throws Exception {
        when(authService.getToken(any(),any())).thenThrow(new InvalidTokenException());

        mockMvc.perform(put("/unvalidatedStudents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUnvalidatedCompaniesHappyDay() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        when(gestionnaireService.getUnvalidatedCompanies())
                .thenReturn(List.of(duffBeer));

        mockMvc.perform(put("/unvalidatedCompanies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("Duff")));
    }
    @Test
    void testUnvalidatedCompaniesInvalidToken() throws Exception {
        when(authService.getToken(any(),any())).thenThrow(new InvalidTokenException());
        mockMvc.perform(put("/unvalidatedCompanies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testValidateStudentHappyDay() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        doAnswer(invocation -> {
            bart.setConfirmed(true);
            return null;
        }).when(gestionnaireService).validateStudent(1L);

        mockMvc.perform(put("/validateStudent/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTokenDTO.write(token).getJson()))

                .andExpect(status().isOk());
    }

    @Test
    void testValidateStudentNotFound() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentEntityException())
                .when(gestionnaireService).validateStudent(1L);

        mockMvc.perform(put("/validateStudent/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }
    @Test
    void testValidateStudentInvalidToken() throws Exception {
        when(authService.getToken(any(),any())).thenThrow(new InvalidTokenException());

        mockMvc.perform(put("/validateStudent/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testValidateCompanyHappyDay() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        doAnswer(invocation -> {
            duffBeer.setConfirmed(true);
            return null;
        }).when(gestionnaireService).validateCompany(1L);

        mockMvc.perform(put("/validateCompany/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    void testValidateCompanyNotFound() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentEntityException())
                .when(gestionnaireService).validateCompany(1L);

        mockMvc.perform(put("/validateCompany/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }
    @Test
    void testValidateCompanyInvalidToken() throws Exception {
        when(authService.getToken(any(),any())).thenThrow(new InvalidTokenException());

        mockMvc.perform(put("/validateCompany/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }
    @Test
    void testRemoveStudentHappyDay() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());

        mockMvc.perform(delete("/removeStudent/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk());
        verify(gestionnaireService, times(1)).removeStudent(1L);
    }

    @Test
    void testRemoveStudentNotFound() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentEntityException())
                .when(gestionnaireService).removeStudent(1L);


        mockMvc.perform(delete("/removeStudent/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }
    @Test
    void testRemoveStudentInvalidToken() throws Exception {
        when(authService.getToken(any(),any())).thenThrow(new InvalidTokenException());

        mockMvc.perform(delete("/removeStudent/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testRemoveCompanyHappyDay() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());

        mockMvc.perform(delete("/removeCompany/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk());
        verify(gestionnaireService, times(1)).removeCompany(1L);
    }

    @Test
    void testRemoveCompanyNotFound() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentEntityException())
                .when(gestionnaireService).removeCompany(1L);


        mockMvc.perform(delete("/removeCompany/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }
    @Test
    void testRemoveCompanyInvalidToken() throws Exception {
        when(authService.getToken(any(),any())).thenThrow(new InvalidTokenException());

        mockMvc.perform(delete("/removeCompany/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUnvalidatedOffers() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        when(gestionnaireService.getNoneValidateOffers())
                .thenReturn(List.of(duffOffre));

        mockMvc.perform(put("/unvalidatedOffers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomDeCompagnie", is("Duff Beer")));
    }
    @Test
    void testUnvalidatedOffersInvalidToken() throws Exception {
        when(authService.getToken(any(),any())).thenThrow(new InvalidTokenException());

        mockMvc.perform(put("/unvalidatedOffers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }


    @Test
    void testValidateOfferSuccess() throws Exception {
        duffOffre.setValide(true);
        when(gestionnaireService.validateOfferById(anyLong())).thenReturn(duffOffre);

        mockMvc.perform(put("/validateOffer/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOffreDTO.write(duffOffre).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomDeCompagnie", is("Duff Beer")))
                .andExpect(jsonPath("$.valide", is(true)));
    }

    @Test
    void testValidationOfferNotFound() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentOfferExeption())
                .when(gestionnaireService).validateOfferById(1L);

        mockMvc.perform(put("/validateOffer/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }
    @Test
    void testValidationOfferInvalidToken() throws Exception {
        when(authService.getToken(any(),any())).thenThrow(new InvalidTokenException());

        mockMvc.perform(put("/validateOffer/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testRemoveOfferSuccess() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());

        mockMvc.perform(delete("/removeOffer/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk());

        verify(gestionnaireService, times(1)).removeOfferById(1L);
    }

    @Test
    void testRemoveOfferNotFound() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentOfferExeption())
                .when(gestionnaireService).removeOfferById(1L);


        mockMvc.perform(delete("/removeOffer/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }
    @Test
    void testRemoveOfferInvalidToken() throws Exception {
        when(authService.getToken(any(),any())).thenThrow(new InvalidTokenException());

        mockMvc.perform(delete("/removeOffer/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }
    @Test
    void testUploadCurriculumVitaeSuccess() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        bart.setCvToValidate(bartCV.getPdf());
        when(studentService.uploadCurriculumVitae(any())).thenReturn(bart);

        mockMvc.perform(put("/uploadStudentCV")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPdfDTO.write(bartCV).getJson()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Bart")))
                .andExpect(jsonPath("$.cvToValidate", is("")));
    }

    @Test
    void testUploadCurriculumVitaeNotFound() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentEntityException())
                .when(studentService).uploadCurriculumVitae(any());

        mockMvc.perform(put("/uploadStudentCV")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPdfDTO.write(bartCV).getJson()));
    }
    @Test
    void testUploadCurriculumVitaeInvalidToken() throws Exception {
        when(authService.getToken(any(),any())).thenThrow(new InvalidTokenException());

        mockMvc.perform(put("/uploadStudentCV")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPdfDTO.write(bartCV).getJson()))
                .andExpect(status().isForbidden());}
    @Test
    void testGetOfferPdfSuccess() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        byte[] pdf = new byte[0];
        when(gestionnaireService.getOffrePdfById(anyLong())).thenReturn(pdf);

        mockMvc.perform(put("/offerPdf/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetOfferPdfNotFound() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentOfferExeption())
                .when(gestionnaireService).getOffrePdfById(1L);

        mockMvc.perform(put("/offerPdf/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }
    @Test
    void testGetOfferPdfInvalidToken() throws Exception {
        when(authService.getToken(any(),any())).thenThrow(new InvalidTokenException());

        mockMvc.perform(put("/offerPdf/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }
    @Test
    void testUnvalidatedCvStudents() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        when(gestionnaireService.getUnvalidatedCVStudents()).thenReturn(List.of(bart));

        mockMvc.perform(put("/unvalidatedCvStudents", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(jsonPath("$[0].firstName", is("Bart")))
                .andExpect(status().isOk());
    }
    @Test
    void testUnvalidatedCvStudentsInvalidToken() throws Exception {
        when(authService.getToken(any(),any())).thenThrow(new InvalidTokenException());

        mockMvc.perform(put("/unvalidatedCvStudents", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }
    @Test
    void testGetStudentCvToValidateSuccess() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        byte[] cv = new byte[0];
        when(gestionnaireService.getStudentCvToValidate(anyLong())).thenReturn(cv);

        mockMvc.perform(put("/studentCv/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(jsonPath("$.pdf", is("")))
                .andExpect(status().isOk());
    }

    @Test
    void testGetStudentCvToValidateNotFound() throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentEntityException()).
                when(gestionnaireService).getStudentCvToValidate(anyLong());

        mockMvc.perform(put("/studentCv/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }
    @Test
    void testGetStudentCvToValidateInvalidToken() throws Exception {
        when(authService.getToken(any(),any())).thenThrow(new InvalidTokenException());

        mockMvc.perform(put("/studentCv/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }
    @Test
    void testValidateStudentCVSuccess () throws Exception {
        when(gestionnaireService.validateStudentCV(anyLong())).thenReturn(bart);

        mockMvc.perform(put("/validateCv/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(bart.getFirstName())));
    }

    @Test
    void testValidateStudentCVNotFound () throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentEntityException()).
                when(gestionnaireService).validateStudentCV(anyLong());

        mockMvc.perform(put("/validateCv/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }
    @Test
    void testValidateStudentCVInvalidToken() throws Exception {
        when(authService.getToken(any(),any())).thenThrow(new InvalidTokenException());

        mockMvc.perform(put("/validateCv/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }
    @Test
    void testRefuseStudentCVSuccess () throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        when(gestionnaireService.removeStudentCvValidation(anyLong())).thenReturn(bart);

        mockMvc.perform(put("/refuseCv/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(bart.getFirstName())));
    }

    @Test
    void testRefuseStudentCVNotFound () throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentEntityException()).
                when(gestionnaireService).removeStudentCvValidation(anyLong());

        mockMvc.perform(put("/refuseCv/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }
    @Test
    void testRefuseStudentCVInvalidToken () throws Exception {
        when(authService.getToken(any(),any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentEntityException()).
                when(gestionnaireService).removeStudentCvValidation(anyLong());

        mockMvc.perform(put("/refuseCv/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }
}
