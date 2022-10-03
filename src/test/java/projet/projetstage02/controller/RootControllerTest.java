package projet.projetstage02.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
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
        bartCV = new PdfDTO("1", new byte[0]);

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
    void testCreateCompanyBadRequest() throws Exception {
        mockMvc.perform(post("/createCompany")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCompanyDTO.write(new CompanyDTO()).getJson()))

                .andExpect(status().isBadRequest());
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
    void testUnvalidatedOffers() throws Exception {
        when(gestionnaireService.getNoneValidateOffers())
                .thenReturn(List.of(duffOffre));

        mockMvc.perform(get("/unvalidatedOffers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomDeCompagnie", is("Duff Beer")));
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
        doThrow(new NonExistentOfferExeption())
                .when(gestionnaireService).validateOfferById(1L);

        mockMvc.perform(put("/validateOffer/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRemoveOfferSuccess() throws Exception {

        mockMvc.perform(delete("/removeOffer/{id}", 1))
                .andExpect(status().isOk());

        verify(gestionnaireService, times(1)).removeOfferById(1L);
    }

    @Test
    void testRemoveOfferNotFound() throws Exception {
        doThrow(new NonExistentOfferExeption())
                .when(gestionnaireService).removeOfferById(1L);


        mockMvc.perform(delete("/removeOffer/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUploadCurriculumVitaeSuccess() throws Exception {
        bart.setCv(bartCV.getPdf());
        when(studentService.uploadCurriculumVitae(any())).thenReturn(bart);

        mockMvc.perform(put("/uploadStudentCV")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStudentDTO.write(bart).getJson()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Bart")))
                .andExpect(jsonPath("$.cv", is("")))
                .andExpect(jsonPath("$.cvConfirm", is(false)));
    }

    @Test
    void testUploadCurriculumVitaeNotFound() throws Exception {
        doThrow(new NonExistentUserException())
                .when(studentService).uploadCurriculumVitae(any());

        mockMvc.perform(put("/uploadStudentCV")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPdfDTO.write(bartCV).getJson()));
    }
    @Test
    void testGetOfferPdfSuccess() throws Exception {
        byte[] pdf = new byte[0];
        when(gestionnaireService.getOffrePdfById(anyLong())).thenReturn(pdf);

        mockMvc.perform(get("/offerPdf/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    void testGetOfferPdfNotFound() throws Exception {
        doThrow(new NonExistentOfferExeption())
                .when(gestionnaireService).getOffrePdfById(1L);

        mockMvc.perform(get("/offerPdf/{id}", 1))
                .andExpect(status().isNotFound());
    }
}
