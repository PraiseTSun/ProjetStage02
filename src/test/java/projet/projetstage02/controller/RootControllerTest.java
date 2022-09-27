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
import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.DTO.GestionnaireDTO;
import projet.projetstage02.DTO.OffreDTO;
import projet.projetstage02.DTO.StudentDTO;
import projet.projetstage02.exception.NonExistentUserException;
import projet.projetstage02.model.AbstractUser.Department;
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
    GestionnaireService gestionnaireService;

    JacksonTester<StudentDTO> jsonStudentDTO;
    JacksonTester<CompanyDTO> jsonCompanyDTO;
    JacksonTester<GestionnaireDTO> jsonGestionnaireDTO;
    JacksonTester<OffreDTO> jsonOffreDTO;

    StudentDTO bart;
    CompanyDTO duffBeer;
    GestionnaireDTO burns;
    OffreDTO duffOffre;

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

        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(rootController).build();
    }

    @Test
    void createStudentHappyDayTest() throws Exception {
        when(studentService.isEmailUnique(anyString())).thenReturn(true);
        when(studentService.saveStudent(any())).thenReturn(1L);

        mockMvc.perform(post("/createStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStudentDTO.write(bart).getJson()))

                .andExpect(status().isCreated());
    }

    @Test
    void createStudentConflictTest() throws Exception {
        when(studentService.isEmailUnique(anyString())).thenReturn(false);

        mockMvc.perform(post("/createStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStudentDTO.write(bart).getJson()))

                .andExpect(status().isConflict());
    }

    @Test
    void createStudentBadRequestTest() throws Exception {
        mockMvc.perform(post("/createStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStudentDTO.write(new StudentDTO()).getJson()))

                .andExpect(status().isBadRequest());
    }

    @Test
    void createCompanyHappyDayTest() throws Exception {
        when(companyService.isEmailUnique(anyString())).thenReturn(true);
        when(companyService.saveCompany(any())).thenReturn(1L);

        mockMvc.perform(post("/createCompany")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCompanyDTO.write(duffBeer).getJson()))

                .andExpect(status().isCreated());
    }

    @Test
    void createCompanyConflictTest() throws Exception {
        when(companyService.isEmailUnique(anyString())).thenReturn(false);

        mockMvc.perform(post("/createCompany")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCompanyDTO.write(duffBeer).getJson()))

                .andExpect(status().isConflict());
    }

    @Test
    void createCompanyBadRequestTest() throws Exception {
        mockMvc.perform(post("/createCompany")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCompanyDTO.write(new CompanyDTO()).getJson()))

                .andExpect(status().isBadRequest());
    }

    @Test
    void createGestionnaireHappyDayTest() throws Exception {
        when(gestionnaireService.isEmailUnique(anyString())).thenReturn(true);
        when(gestionnaireService.saveGestionnaire(any())).thenReturn(1L);

        mockMvc.perform(post("/createGestionnaire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonGestionnaireDTO.write(burns).getJson()))

                .andExpect(status().isCreated());
    }

    @Test
    void createGestionnaireConflictTest() throws Exception {
        when(gestionnaireService.isEmailUnique(anyString())).thenReturn(false);

        mockMvc.perform(post("/createGestionnaire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonGestionnaireDTO.write(burns).getJson()))

                .andExpect(status().isConflict());
    }

    @Test
    void createGestionnaireBadRequestTest() throws Exception {
        mockMvc.perform(post("/createGestionnaire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonGestionnaireDTO.write(new GestionnaireDTO()).getJson()))

                .andExpect(status().isBadRequest());
    }

    @Test
    void createOffreHappyDayTest() throws Exception {
        when(companyService.createOffre(any())).thenReturn(1L);

        mockMvc.perform(post("/createOffre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOffreDTO.write(duffOffre).getJson()))

                .andExpect(status().isCreated());
    }

    @Test
    void createOffreBadRequestTest() throws Exception {
        mockMvc.perform(post("/createOffre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOffreDTO.write(new OffreDTO()).getJson()))

                .andExpect(status().isBadRequest());
    }

    @Test
    void confirmStudentEmailHappyDayTest() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(bart);
        when(studentService.saveStudent(bart)).thenReturn(1L);

        mockMvc.perform(
                        put("/confirmEmail/student/{id}", 1))

                .andExpect(status().isCreated());
    }

    @Test
    void confirmStudentEmailNotFoundTest() throws Exception {
        when(studentService.getStudentById(1L)).thenThrow(new NonExistentUserException());

        mockMvc.perform(
                        put("/confirmEmail/student/{id}", 1))

                .andExpect(status().isNotFound());
    }

    @Test
    void confirmStudentEmailExpiredTest() throws Exception {
        bart.setInscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now().minusMonths(1)).getTime());
        when(studentService.getStudentById(1L)).thenReturn(bart);

        mockMvc.perform(
                        put("/confirmEmail/student/{id}", 1))

                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void confirmCompanyEmailHappyDayTest() throws Exception {
        when(companyService.getCompanyById(1L)).thenReturn(duffBeer);
        when(companyService.saveCompany(duffBeer)).thenReturn(1L);

        mockMvc.perform(
                        put("/confirmEmail/company/{id}", 1))

                .andExpect(status().isCreated());
    }

    @Test
    void confirmCompanyEmailNotFoundTest() throws Exception {
        when(companyService.getCompanyById(1L)).thenThrow(new NonExistentUserException());

        mockMvc.perform(
                        put("/confirmEmail/company/{id}", 1))

                .andExpect(status().isNotFound());
    }

    @Test
    void confirmCompanyEmailExpiredTest() throws Exception {
        duffBeer.setInscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now().minusMonths(1)).getTime());
        when(companyService.getCompanyById(1L)).thenReturn(duffBeer);

        mockMvc.perform(
                        put("/confirmEmail/company/{id}", 1))

                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void loginStudentHappyDayTest() throws Exception {
        bart.setEmailConfirmed(true);
        when(studentService.getStudentByEmailPassword(
                "bart.simpson@springfield.com",
                "eatMyShorts"))
                .thenReturn(bart);

        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStudentDTO.write(bart).getJson()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Bart")));
    }

    @Test
    void loginStudentNotEmailConfirmedTest() throws Exception {
        when(studentService.getStudentByEmailPassword(
                "bart.simpson@springfield.com",
                "eatMyShorts"))
                .thenReturn(bart);

        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStudentDTO.write(bart).getJson()))

                .andExpect(status().isNotFound());
    }

    @Test
    void loginStudentNotFoundTest() throws Exception {
        when(studentService.getStudentByEmailPassword(
                "bart.simpson@springfield.com",
                "eatMyShorts"))
                .thenThrow(new NonExistentUserException());

        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStudentDTO.write(bart).getJson()))

                .andExpect(status().isNotFound());
    }

    @Test
    void loginCompanyHappyDayTest() throws Exception {
        duffBeer.setEmailConfirmed(true);
        when(companyService.getCompanyByEmailPassword(
                "duff.beer@springfield.com",
                "bestBeer"))
                .thenReturn(duffBeer);

        mockMvc.perform(put("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCompanyDTO.write(duffBeer).getJson()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Duff")));
    }

    @Test
    void loginCompanyNotEmailConfirmedTest() throws Exception {
        when(companyService.getCompanyByEmailPassword(
                "duff.beer@springfield.com",
                "bestBeer"))
                .thenReturn(duffBeer);

        mockMvc.perform(put("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCompanyDTO.write(duffBeer).getJson()))

                .andExpect(status().isNotFound());
    }

    @Test
    void loginCompanyNotFoundTest() throws Exception {
        when(companyService.getCompanyByEmailPassword(
                "duff.beer@springfield.com",
                "bestBeer"))
                .thenThrow(new NonExistentUserException());

        mockMvc.perform(put("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCompanyDTO.write(duffBeer).getJson()))

                .andExpect(status().isNotFound());
    }

    @Test
    void loginGestionnaireHappyDayTest() throws Exception {
        burns.setEmailConfirmed(true);
        when(gestionnaireService.getGestionnaireByEmailPassword(
                "charles.burns@springfield.com",
                "excellent"))
                .thenReturn(burns);

        mockMvc.perform(put("/gestionnaire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonGestionnaireDTO.write(burns).getJson()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Charles")));
    }

    @Test
    void loginGestionnaireNotEmailConfirmedTest() throws Exception {
        when(gestionnaireService.getGestionnaireByEmailPassword(
                "charles.burns@springfield.com",
                "excellent"))
                .thenReturn(burns);

        mockMvc.perform(put("/gestionnaire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonGestionnaireDTO.write(burns).getJson()))

                .andExpect(status().isNotFound());
    }

    @Test
    void loginGestionnaireNotFoundTest() throws Exception {
        when(gestionnaireService.getGestionnaireByEmailPassword(
                "charles.burns@springfield.com",
                "excellent"))
                .thenThrow(new NonExistentUserException());

        mockMvc.perform(put("/gestionnaire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonGestionnaireDTO.write(burns).getJson()))

                .andExpect(status().isNotFound());
    }

    @Test
    void unvalidatedStudentsHappyDayTest() throws Exception {
        when(gestionnaireService.getUnvalidatedStudents())
                .thenReturn(List.of(bart));

        mockMvc.perform(get("/unvalidatedStudents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("Bart")));
    }

    @Test
    void unvalidatedCompaniesHappyDayTest() throws Exception {
        when(gestionnaireService.getUnvalidatedCompanies())
                .thenReturn(List.of(duffBeer));

        mockMvc.perform(get("/unvalidatedCompanies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("Duff")));
    }

    @Test
    void validateStudentHappyDayTest() throws Exception {
        doAnswer(invocation -> {
            bart.setConfirmed(true);
            return null;
        }).when(gestionnaireService).validateStudent(1L);

        mockMvc.perform(put("/validateStudent/{id}", 1))

                .andExpect(status().isOk());
    }

    @Test
    void validateStudentNotFoundTest() throws Exception {
        doThrow(new NonExistentUserException())
                .when(gestionnaireService).validateStudent(1L);

        mockMvc.perform(put("/validateStudent/{id}", 1))

                .andExpect(status().isNotFound());
    }

    @Test
    void validateCompanyHappyDayTest() throws Exception {
        doAnswer(invocation -> {
            duffBeer.setConfirmed(true);
            return null;
        }).when(gestionnaireService).validateCompany(1L);

        mockMvc.perform(put("/validateCompany/{id}", 1))

                .andExpect(status().isOk());
    }

    @Test
    void validateCompanyNotFoundTest() throws Exception {
        doThrow(new NonExistentUserException())
                .when(gestionnaireService).validateCompany(1L);

        mockMvc.perform(put("/validateCompany/{id}", 1))

                .andExpect(status().isNotFound());
    }

    @Test
    void removeStudentHappyDayTest() throws Exception {

        mockMvc.perform(delete("/removeStudent/{id}", 1))

                .andExpect(status().isOk());
        verify(gestionnaireService, times(1)).removeStudent(1L);
    }

    @Test
    void removeStudentNotFoundTest() throws Exception {
        doThrow(new NonExistentUserException())
                .when(gestionnaireService).removeStudent(1L);


        mockMvc.perform(delete("/removeStudent/{id}", 1))

                .andExpect(status().isNotFound());
    }

    @Test
    void removeCompanyHappyDayTest() throws Exception {

        mockMvc.perform(delete("/removeCompany/{id}", 1))

                .andExpect(status().isOk());
        verify(gestionnaireService, times(1)).removeCompany(1L);
    }

    @Test
    void removeCompanyNotFoundTest() throws Exception {
        doThrow(new NonExistentUserException())
                .when(gestionnaireService).removeCompany(1L);


        mockMvc.perform(delete("/removeCompany/{id}", 1))

                .andExpect(status().isNotFound());
    }
}
