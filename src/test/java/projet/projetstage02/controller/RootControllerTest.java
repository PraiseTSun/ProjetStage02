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
import projet.projetstage02.dto.applications.ApplicationAcceptationDTO;
import projet.projetstage02.dto.applications.ApplicationDTO;
import projet.projetstage02.dto.applications.ApplicationListDTO;
import projet.projetstage02.dto.applications.OfferApplicationDTO;
import projet.projetstage02.dto.auth.LoginDTO;
import projet.projetstage02.dto.auth.TokenDTO;
import projet.projetstage02.dto.contracts.ContractsDTO;
import projet.projetstage02.dto.contracts.StageContractInDTO;
import projet.projetstage02.dto.contracts.StageContractOutDTO;
import projet.projetstage02.dto.cv.CvRefusalDTO;
import projet.projetstage02.dto.cv.CvStatusDTO;
import projet.projetstage02.dto.evaluations.MillieuStage.MillieuStageEvaluationInDTO;
import projet.projetstage02.dto.evaluations.MillieuStage.MillieuStageEvaluationInfoDTO;
import projet.projetstage02.dto.interview.CreateInterviewDTO;
import projet.projetstage02.dto.interview.InterviewOutDTO;
import projet.projetstage02.dto.interview.InterviewSelectInDTO;
import projet.projetstage02.dto.offres.OfferAcceptedStudentsDTO;
import projet.projetstage02.dto.offres.OffreInDTO;
import projet.projetstage02.dto.offres.OffreOutDTO;
import projet.projetstage02.dto.pdf.PdfDTO;
import projet.projetstage02.dto.pdf.PdfOutDTO;
import projet.projetstage02.dto.users.CompanyDTO;
import projet.projetstage02.dto.users.GestionnaireDTO;
import projet.projetstage02.dto.users.Students.StudentInDTO;
import projet.projetstage02.dto.users.Students.StudentOutDTO;
import projet.projetstage02.exception.*;
import projet.projetstage02.model.AbstractUser.Department;
import projet.projetstage02.model.Offre;
import projet.projetstage02.model.Token;
import projet.projetstage02.service.AuthService;
import projet.projetstage02.service.CompanyService;
import projet.projetstage02.service.GestionnaireService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static projet.projetstage02.model.Token.UserTypes.*;
import static projet.projetstage02.utils.ByteConverter.byteToString;
import static projet.projetstage02.utils.TimeUtil.currentTimestamp;

@ExtendWith(MockitoExtension.class)
public class RootControllerTest {

    MockMvc mockMvc;
    MockMvc companyMockMvc;
    MockMvc gestionnaireMockMvc;

    @InjectMocks
    RootController rootController;

    @InjectMocks
    CompanyRootController companyRootController;
    @InjectMocks
    GestionnaireRootController gestionnaireRootController;



    @Mock
    CompanyService companyService;
    @Mock
    AuthService authService;
    @Mock
    GestionnaireService gestionnaireService;

    JacksonTester<LoginDTO> jsonLoginDTO;
    JacksonTester<CompanyDTO> jsonCompanyDTO;
    JacksonTester<GestionnaireDTO> jsonGestionnaireDTO;
    JacksonTester<OffreInDTO> jsonOffreDTO;
    JacksonTester<TokenDTO> jsonTokenDTO;
    JacksonTester<CvRefusalDTO> jsonCvRefusalDTO;
    JacksonTester<SignatureInDTO> jsonSignatureDTO;
    JacksonTester<MillieuStageEvaluationInDTO> jsonEvalInDTO;
    JacksonTester<InterviewOutDTO> jsonInterviewOutDTO;

    StudentInDTO bart;
    StudentOutDTO bartOut;
    CompanyDTO duffBeer;
    TokenDTO token;
    LoginDTO login;
    GestionnaireDTO burns;
    OffreInDTO duffOffre;
    OffreOutDTO duffOffreOut;
    PdfOutDTO duffOfferOut;
    PdfDTO bartCV;
    CvRefusalDTO cvRefusalDTO;
    ApplicationDTO bartPostulation;
    ApplicationListDTO bartApplys;
    ApplicationAcceptationDTO applicationDTO;
    OfferAcceptedStudentsDTO acceptedStudentsDTO;
    StageContractInDTO stageContractInDTO;
    StageContractOutDTO stageContractOutDTO;
    SignatureInDTO signatureInDTO;
    ContractsDTO contractsDTO;
    OfferApplicationDTO offerApplicationDTO;

    CvStatusDTO cvStatusDTO;
    MillieuStageEvaluationInfoDTO evalInfoDTO;
    MillieuStageEvaluationInDTO evalInDTO;
    CreateInterviewDTO createInterviewDTO;
    InterviewOutDTO interviewOutDTO;
    InterviewSelectInDTO interviewSelectInDTO;
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
        bartOut = StudentOutDTO.builder()
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
        duffOffreOut = OffreOutDTO.builder()
                .nomDeCompagnie("Duff Beer")
                .department(Department.Transport.departement)
                .position("Delivery Guy")
                .heureParSemaine(40)
                .salaire(40)
                .session("Hiver 2022")
                .adresse("654 Duff Street")
                .pdf(byteToString(new byte[0]))
                .build();

        token = TokenDTO.builder()
                .token("9f0b0e68-c177-4504-9087-eea175653ee3")
                .build();

        login = LoginDTO.builder().build();
        bartCV = PdfDTO.builder().pdf(new byte[0]).studentId(1).token(token.getToken()).build();

        JacksonTester.initFields(this, new ObjectMapper());

        mockMvc = MockMvcBuilders.standaloneSetup(rootController).build();
        companyMockMvc = MockMvcBuilders.standaloneSetup(companyRootController).build();
        gestionnaireMockMvc = MockMvcBuilders.standaloneSetup(gestionnaireRootController).build();

        duffOfferOut = PdfOutDTO.builder()
                .id(1L)
                .pdf("[1,2,3,4,5,6,7,8,9]")
                .build();

        bartPostulation = ApplicationDTO.builder()
                .fullName(bart.getFirstName() + " " + bart.getLastName())
                .company(duffOffre.getNomDeCompagnie())
                .build();

        bartApplys = ApplicationListDTO.builder()
                .studentId(bart.getId())
                .offersId(List.of(duffOffre.getId()))
                .build();

        cvRefusalDTO = CvRefusalDTO.builder()
                .refusalReason("refused")
                .token(token.getToken())
                .build();

        applicationDTO = ApplicationAcceptationDTO.builder()
                .id(3L)
                .studentId(2L)
                .studentName("Simpson Bart")
                .offerId(1L)
                .companyName("Duff Beer")
                .build();

        acceptedStudentsDTO = OfferAcceptedStudentsDTO.builder()
                .offerId(duffOffre.getId())
                .studentsId(new ArrayList<>() {{
                    add(bart.getId());
                }})
                .build();

        offerApplicationDTO = OfferApplicationDTO.builder().applicants(List.of(
                StudentOutDTO.builder().build(),
                StudentOutDTO.builder().build(),
                StudentOutDTO.builder().build()
        )).build();

        cvStatusDTO = CvStatusDTO.builder()
                .status("ACCEPTED")
                .refusalMessage("")
                .build();

        stageContractInDTO = StageContractInDTO.builder()
                .studentId(7L)
                .offerId(8L)
                .build();

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

        evalInfoDTO = new MillieuStageEvaluationInfoDTO(duffBeer.toModel(), duffOffre.toModel(), bart.toModel());

        evalInDTO = MillieuStageEvaluationInDTO.builder()
                .climatTravail("Plutôt en accord")
                .commentaires("Plutôt en accord")
                .communicationAvecSuperviser("Plutôt en accord")
                .contractId(1L)
                .dateSignature("2021-05-01")
                .environementTravail("Plutôt en accord")
                .equipementFourni("Plutôt en accord")
                .heureTotalDeuxiemeMois(23)
                .heureTotalPremierMois(23)
                .heureTotalTroisiemeMois(23)
                .integration("Plutôt en accord")
                .milieuDeStage("Plutôt en accord")
                .tachesAnnonces("Plutôt en accord")
                .volumeDeTravail("Plutôt en accord")
                .tempsReelConsacre("Plutôt en accord")
                .signature(byteToString(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}))
                .build();

        createInterviewDTO = CreateInterviewDTO.builder()
                .token("token")
                .companyId(duffBeer.getId())
                .offerId(duffOffreOut.getId())
                .studentId(bartOut.getId())
                .companyDateOffers(new ArrayList<>(){{
                    add("2022-11-28T12:30:00");
                    add("2022-11-29T12:30:00");
                    add("2022-11-30T12:30:00");
                }})
                .build();

        interviewOutDTO = InterviewOutDTO.builder()
                .interviewId(10L)
                .companyId(duffBeer.getId())
                .offerId(duffOffreOut.getId())
                .studentId(bartOut.getId())
                .companyDateOffers(new ArrayList<>(){{
                    add("2022-11-28T12:30:00");
                    add("2022-11-29T12:30:00");
                    add("2022-11-30T12:30:00");
                }})
                .studentSelectedDate("")
                .build();

        interviewSelectInDTO = InterviewSelectInDTO.builder()
                .token("token")
                .interviewId(interviewOutDTO.getInterviewId())
                .studentId(bart.getId())
                .selectedDate("2022-11-29T12:30")
                .build();
    }

    @Test
    void testCreateCompanyHappyDay() throws Exception {
        when(companyService.isCompanyInvalid(anyString())).thenReturn(false);
        when(companyService.saveCompany(any())).thenReturn(1L);

        companyMockMvc.perform(post("/createCompany")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCompanyDTO.write(duffBeer).getJson()))

                .andExpect(status().isCreated());
    }

    @Test
    void testCreateCompanyConflict() throws Exception {
        when(companyService.isCompanyInvalid(anyString())).thenReturn(true);

        companyMockMvc.perform(post("/createCompany")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCompanyDTO.write(duffBeer).getJson()))

                .andExpect(status().isConflict());
    }

    @Test
    void testCreateCompanyExistingEmailDeleteOld() throws Exception {
        when(companyService.isCompanyInvalid(anyString())).thenReturn(false);
        companyMockMvc.perform(post("/createCompany")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCompanyDTO.write(duffBeer).getJson()))

                .andExpect(status().isCreated());
    }

    @Test
    void testCreateCompanyBadRequest() throws Exception {
        companyMockMvc.perform(post("/createCompany")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCompanyDTO.write(new CompanyDTO()).getJson()))

                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCompanyExistingEmailDeleteOldNotFound() throws Exception {
        when(companyService.isCompanyInvalid(anyString())).thenThrow(new NonExistentEntityException());
        companyMockMvc.perform(post("/createCompany")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCompanyDTO.write(duffBeer).getJson()))

                .andExpect(status().isInternalServerError());
    }

    @Test
    void testCreateGestionnaireHappyDay() throws Exception {
        burns.setToken(token.getToken());

        when(gestionnaireService.isGestionnaireInvalid(anyString())).thenReturn(false);
        when(gestionnaireService.saveGestionnaire(any())).thenReturn(1L);

        gestionnaireMockMvc.perform(post("/createGestionnaire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonGestionnaireDTO.write(burns).getJson()))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateGestionnaireConflict() throws Exception {
        burns.setToken(token.getToken());

        when(gestionnaireService.isGestionnaireInvalid(anyString())).thenReturn(true);

        gestionnaireMockMvc.perform(post("/createGestionnaire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonGestionnaireDTO.write(burns).getJson()))

                .andExpect(status().isConflict());
    }

    @Test
    void testCreateGestionnaireBadRequest() throws Exception {

        gestionnaireMockMvc.perform(post("/createGestionnaire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonGestionnaireDTO.write(new GestionnaireDTO()).getJson()))

                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateGestionnaireExistingEmailDeleteOldNotFound() throws Exception {
        when(gestionnaireService.isGestionnaireInvalid(anyString())).thenThrow(new NonExistentEntityException());
        gestionnaireMockMvc.perform(post("/createGestionnaire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonGestionnaireDTO.write(burns).getJson()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testCreateGestionnaireInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());
        gestionnaireMockMvc.perform(post("/createGestionnaire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonGestionnaireDTO.write(burns).getJson()))

                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateOffreHappyDay() throws Exception {
        duffOffre.setToken(token.getToken());

        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        when(companyService.createOffre(any())).thenReturn(1L);

        companyMockMvc.perform(post("/createOffre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOffreDTO.write(duffOffre).getJson()))

                .andExpect(status().isCreated());
    }

    @Test
    void testCreateOffreBadRequest() throws Exception {
        companyMockMvc.perform(post("/createOffre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOffreDTO.write(new OffreInDTO()).getJson()))

                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateOffreInvalidToken() throws Exception {
        duffOffre.setToken(token.getToken());
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());
        companyMockMvc.perform(post("/createOffre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOffreDTO.write(duffOffre).getJson()))

                .andExpect(status().isForbidden());
    }


    @Test
    void testConfirmCompanyEmailHappyDay() throws Exception {
        when(companyService.getCompanyById(1L)).thenReturn(duffBeer);
        when(companyService.saveCompany(duffBeer)).thenReturn(1L);

        companyMockMvc.perform(
                        put("/confirmEmail/company/{id}", 1))

                .andExpect(status().isCreated());
    }

    @Test
    void testConfirmCompanyEmailNotFound() throws Exception {
        when(companyService.getCompanyById(1L)).thenThrow(new NonExistentEntityException());

        companyMockMvc.perform(
                        put("/confirmEmail/company/{id}", 1))

                .andExpect(status().isNotFound());
    }

    @Test
    void testConfirmCompanyEmailExpired() throws Exception {
        duffBeer.setInscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now().minusMonths(1)).getTime());
        when(companyService.getCompanyById(1L)).thenReturn(duffBeer);

        companyMockMvc.perform(
                        put("/confirmEmail/company/{id}", 1))

                .andExpect(status().isBadRequest());
    }

    @Test
    void testConfirmGestionnaireEmailHappyDay() throws Exception {
        burns.setInscriptionTimestamp(currentTimestamp());
        when(gestionnaireService.getGestionnaireById(1L)).thenReturn(burns);
        when(gestionnaireService.saveGestionnaire(burns)).thenReturn(1L);

        gestionnaireMockMvc.perform(
                        put("/confirmEmail/gestionaire/{id}", 1))

                .andExpect(status().isCreated());
    }

    @Test
    void testConfirmGestionnaireEmailNotFound() throws Exception {
        when(gestionnaireService.getGestionnaireById(1L)).thenThrow(new NonExistentEntityException());
        gestionnaireMockMvc.perform(
                        put("/confirmEmail/gestionaire/{id}", 1))

                .andExpect(status().isNotFound());
    }

    @Test
    void testConfirmGestionnaireEmailExpired() throws Exception {
        bart.setInscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now().minusMonths(1)).getTime());
        when(gestionnaireService.getGestionnaireById(1L)).thenReturn(burns);

        gestionnaireMockMvc.perform(
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
    void testLoginCompanyHappyDay() throws Exception {
        duffBeer.setEmailConfirmed(true);
        when(authService.getToken(token.getToken(), COMPANY)).thenReturn(Token.builder().userId(1L).build());
        when(companyService.getCompanyById(1L)).thenReturn(duffBeer);

        companyMockMvc.perform(put("/company")
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
        when(authService.loginIfValid(login, COMPANY)).thenThrow(new InvalidTokenException());
        mockMvc.perform(post("/company/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginDTO.write(login).getJson()))

                .andExpect(status().isForbidden());
        verify(authService, times(1)).loginIfValid(any(), any());
    }

    @Test
    void testLoginCompanyNotFound() throws Exception {
        when(authService.getToken(token.getToken(), COMPANY)).thenReturn(Token.builder().userId(1).build());
        when(companyService.getCompanyById(anyLong()))
                .thenThrow(new NonExistentEntityException());

        companyMockMvc.perform(put("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))

                .andExpect(status().isNotFound());
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
    void testLoginGestionnaireHappyDay() throws Exception {
        burns.setEmailConfirmed(true);
        when(authService.getToken(token.getToken(), GESTIONNAIRE))
                .thenReturn(Token.builder().userId(1L).build());
        when(gestionnaireService.getGestionnaireById(1L)).thenReturn(burns);

        gestionnaireMockMvc.perform(put("/gestionnaire")
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
        when(authService.loginIfValid(login, GESTIONNAIRE)).thenThrow(new InvalidTokenException());
        mockMvc.perform(post("/gestionnaire/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginDTO.write(login).getJson()))

                .andExpect(status().isForbidden());
        verify(authService, times(1)).loginIfValid(any(), any());
    }

    @Test
    void testLoginGestionnaireNotFound() throws Exception {
        when(authService.getToken(token.getToken(), GESTIONNAIRE)).thenReturn(Token.builder().userId(1).build());
        when(gestionnaireService.getGestionnaireById(anyLong()))
                .thenThrow(new NonExistentEntityException());

        gestionnaireMockMvc.perform(put("/gestionnaire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))

                .andExpect(status().isNotFound());
    }

    @Test
    void testUnvalidatedStudentsHappyDay() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        when(gestionnaireService.getUnvalidatedStudents())
                .thenReturn(List.of(bartOut));

        gestionnaireMockMvc.perform(put("/unvalidatedStudents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("Bart")));
    }

    @Test
    void testUnvalidatedStudentsInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        gestionnaireMockMvc.perform(put("/unvalidatedStudents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUnvalidatedCompaniesHappyDay() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        when(gestionnaireService.getUnvalidatedCompanies())
                .thenReturn(List.of(duffBeer));

        gestionnaireMockMvc.perform(put("/unvalidatedCompanies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("Duff")));
    }

    @Test
    void testUnvalidatedCompaniesInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());
        gestionnaireMockMvc.perform(put("/unvalidatedCompanies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testValidateStudentHappyDay() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        doAnswer(invocation -> {
            bart.setConfirmed(true);
            return null;
        }).when(gestionnaireService).validateStudent(1L);

        gestionnaireMockMvc.perform(put("/validateStudent/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))

                .andExpect(status().isOk());
    }

    @Test
    void testValidateStudentNotFound() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentEntityException())
                .when(gestionnaireService).validateStudent(1L);

        gestionnaireMockMvc.perform(put("/validateStudent/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testValidateStudentInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        gestionnaireMockMvc.perform(put("/validateStudent/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testValidateCompanyHappyDay() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        doAnswer(invocation -> {
            duffBeer.setConfirmed(true);
            return null;
        }).when(gestionnaireService).validateCompany(1L);

        gestionnaireMockMvc.perform(put("/validateCompany/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    void testValidateCompanyNotFound() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentEntityException())
                .when(gestionnaireService).validateCompany(1L);

        gestionnaireMockMvc.perform(put("/validateCompany/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testValidateCompanyInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        gestionnaireMockMvc.perform(put("/validateCompany/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testRemoveStudentHappyDay() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());

        gestionnaireMockMvc.perform(delete("/removeStudent/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk());
        verify(gestionnaireService, times(1)).removeStudent(1L);
    }

    @Test
    void testRemoveStudentNotFound() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentEntityException())
                .when(gestionnaireService).removeStudent(1L);


        gestionnaireMockMvc.perform(delete("/removeStudent/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRemoveStudentInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        gestionnaireMockMvc.perform(delete("/removeStudent/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testRemoveCompanyHappyDay() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());

        gestionnaireMockMvc.perform(delete("/removeCompany/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk());
        verify(gestionnaireService, times(1)).removeCompany(1L);
    }

    @Test
    void testRemoveCompanyNotFound() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentEntityException())
                .when(gestionnaireService).removeCompany(1L);


        gestionnaireMockMvc.perform(delete("/removeCompany/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRemoveCompanyInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        gestionnaireMockMvc.perform(delete("/removeCompany/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUnvalidatedOffers() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        when(gestionnaireService.getUnvalidatedOffers())
                .thenReturn(List.of(duffOffreOut));

        gestionnaireMockMvc.perform(put("/unvalidatedOffers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomDeCompagnie", is("Duff Beer")));
    }

    @Test
    void testUnvalidatedOffersInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        gestionnaireMockMvc.perform(put("/unvalidatedOffers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testValidatedOffersInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        gestionnaireMockMvc.perform(put("/validatedOffers/2022")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testValidatedOffersHappyDay() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        when(gestionnaireService.getValidatedOffers(anyInt()))
                .thenReturn(List.of(duffOffreOut));

        gestionnaireMockMvc.perform(put("/validatedOffers/2022")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomDeCompagnie", is("Duff Beer")));
    }

    @Test
    void testValidateOfferSuccess() throws Exception {
        duffOffreOut.setValide(true);
        when(gestionnaireService.validateOfferById(anyLong())).thenReturn(duffOffreOut);

        gestionnaireMockMvc.perform(put("/validateOffer/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOffreDTO.write(duffOffre).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomDeCompagnie", is("Duff Beer")))
                .andExpect(jsonPath("$.valide", is(true)));
    }

    @Test
    void testValidationOfferNotFound() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentOfferExeption())
                .when(gestionnaireService).validateOfferById(1L);

        gestionnaireMockMvc.perform(put("/validateOffer/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testValidationOfferInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        gestionnaireMockMvc.perform(put("/validateOffer/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testRemoveOfferSuccess() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());

        gestionnaireMockMvc.perform(delete("/removeOffer/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk());

        verify(gestionnaireService, times(1)).removeOfferById(1L);
    }

    @Test
    void testRemoveOfferNotFound() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentOfferExeption())
                .when(gestionnaireService).removeOfferById(1L);


        gestionnaireMockMvc.perform(delete("/removeOffer/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRemoveOfferInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        gestionnaireMockMvc.perform(delete("/removeOffer/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }



    @Test
    void testGetOfferPdfSuccess() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        PdfOutDTO pdf = PdfOutDTO.builder().pdf("").build();
        when(gestionnaireService.getOffrePdfById(anyLong())).thenReturn(pdf);

        gestionnaireMockMvc.perform(put("/offerPdf/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetOfferPdfNotFound() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentOfferExeption())
                .when(gestionnaireService).getOffrePdfById(1L);

        gestionnaireMockMvc.perform(put("/offerPdf/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetOfferPdfInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        gestionnaireMockMvc.perform(put("/offerPdf/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUnvalidatedCvStudents() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        when(gestionnaireService.getUnvalidatedCVStudents()).thenReturn(List.of(bartOut));

        gestionnaireMockMvc.perform(put("/unvalidatedCvStudents", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(jsonPath("$[0].firstName", is("Bart")))
                .andExpect(status().isOk());
    }

    @Test
    void testUnvalidatedCvStudentsInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        gestionnaireMockMvc.perform(put("/unvalidatedCvStudents", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetStudentCvToValidateSuccess() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        PdfOutDTO cv = new PdfOutDTO(1L, "[96,17,69]");
        when(gestionnaireService.getStudentCvToValidate(anyLong())).thenReturn(cv);

        gestionnaireMockMvc.perform(put("/studentCv/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(jsonPath("$.pdf", is("[96,17,69]")))
                .andExpect(status().isOk());
    }

    @Test
    void testGetStudentCvToValidateNotFound() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentEntityException()).
                when(gestionnaireService).getStudentCvToValidate(anyLong());

        gestionnaireMockMvc.perform(put("/studentCv/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetStudentCvToValidateInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        gestionnaireMockMvc.perform(put("/studentCv/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testValidateStudentCVSuccess() throws Exception {
        when(gestionnaireService.validateStudentCV(anyLong())).thenReturn(bartOut);

        gestionnaireMockMvc.perform(put("/validateCv/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(bart.getFirstName())));
    }

    @Test
    void testValidateStudentCVNotFound() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentEntityException()).
                when(gestionnaireService).validateStudentCV(anyLong());

        gestionnaireMockMvc.perform(put("/validateCv/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testValidateStudentCVInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        gestionnaireMockMvc.perform(put("/validateCv/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testRefuseStudentCVSuccess() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        when(gestionnaireService.removeStudentCvValidation(anyLong(), anyString())).thenReturn(bartOut);

        gestionnaireMockMvc.perform(put("/refuseCv/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCvRefusalDTO.write(cvRefusalDTO).getJson()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(bart.getFirstName())));
    }

    @Test
    void testRefuseStudentCVNotFound() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentEntityException()).
                when(gestionnaireService).removeStudentCvValidation(anyLong(), any());

        gestionnaireMockMvc.perform(put("/refuseCv/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCvRefusalDTO.write(cvRefusalDTO).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRefuseStudentCVInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentEntityException()).
                when(gestionnaireService).removeStudentCvValidation(anyLong(), any());

        gestionnaireMockMvc.perform(put("/refuseCv/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCvRefusalDTO.write(cvRefusalDTO).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSaveStudentAcceptationHappyDay() throws Exception {
        when(companyService.saveStudentApplicationAccepted(anyLong(), anyLong())).thenReturn(applicationDTO);

        companyMockMvc.perform(put("/studentAcceptation/{offerId}_{studentId}", 1, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentName", is(applicationDTO.getStudentName())))
                .andExpect(jsonPath("$.companyName", is(applicationDTO.getCompanyName())));
    }

    @Test
    void testSaveStudentAcceptationStudentNotFound() throws Exception {
        when(companyService.saveStudentApplicationAccepted(anyLong(), anyLong())).thenThrow(new NonExistentEntityException());

        companyMockMvc.perform(put("/studentAcceptation/{offerId}_{studentId}", 1, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSaveStudentAcceptationOfferNotFound() throws Exception {
        when(companyService.saveStudentApplicationAccepted(anyLong(), anyLong())).thenThrow(new NonExistentOfferExeption());

        companyMockMvc.perform(put("/studentAcceptation/{offerId}_{studentId}", 1, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSaveStudentAcceptationConflict() throws Exception {
        when(companyService.saveStudentApplicationAccepted(anyLong(), anyLong())).thenThrow(new AlreadyExistingAcceptationException());

        companyMockMvc.perform(put("/studentAcceptation/{offerId}_{studentId}", 1, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isConflict());
    }

    @Test
    void testSaveStudentAcceptationTokenInvalid() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        companyMockMvc.perform(put("/studentAcceptation/{offerId}_{studentId}", 1, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetAcceptedStudentsForOfferHappyDay() throws Exception {
        when(companyService.getAcceptedStudentsForOffer(anyLong())).thenReturn(acceptedStudentsDTO);

        companyMockMvc.perform(put("/getAcceptedStudentsForOffer/{offerId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentsId.size()", is(1)));
    }

    @Test
    void testGetAcceptedStudentsForOfferNotFound() throws Exception {
        when(companyService.getAcceptedStudentsForOffer(anyLong())).thenThrow(new NonExistentOfferExeption());

        companyMockMvc.perform(put("/getAcceptedStudentsForOffer/{offerId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAcceptedStudentsForOfferTokenInvalid() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        companyMockMvc.perform(put("/getAcceptedStudentsForOffer/{offerId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetApplicationsForOfferHappyDay() throws Exception {
        when(companyService.getStudentsForOffer(anyLong())).thenReturn(offerApplicationDTO);

        companyMockMvc.perform(put("/offer/{id}/applications", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicants.length()", is(3)));
    }

    @Test
    void testGetApplicationsForOfferNotFound() throws Exception {
        when(companyService.getStudentsForOffer(anyLong())).thenThrow(new NonExistentOfferExeption());

        companyMockMvc.perform(put("/offer/{id}/applications", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetApplicationsForOfferForbidden() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        companyMockMvc.perform(put("/offer/{id}/applications", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }


    @Test
    void testGetCompanyOffersHappyDay() throws Exception {
        when(companyService.getValidatedOffers(anyLong())).thenReturn(List.of(duffOffreOut));

        companyMockMvc.perform(put("/company/validatedOffers/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    void testGetCompanyOffersForbidden() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        companyMockMvc.perform(put("/company/validatedOffers/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetStudentCvToValidateCompanyHappyDay() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        PdfOutDTO cv = new PdfOutDTO(1L, "[96,17,69]");
        when(companyService.getStudentCv(anyLong())).thenReturn(cv);

        companyMockMvc.perform(put("/company/studentCv/{studentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(jsonPath("$.pdf", is("[96,17,69]")))
                .andExpect(status().isOk());
    }

    @Test
    void testGetStudentCvToValidateCompanyNotFound() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentEntityException()).
                when(companyService).getStudentCv(anyLong());

        companyMockMvc.perform(put("/company/studentCv/{studentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetStudentCvToValidateCompanyInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        companyMockMvc.perform(put("/company/studentCv/{studentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }


    @Test
    void testCreateStageContactHappyDay() throws Exception {
        when(gestionnaireService.createStageContract(any())).thenReturn(stageContractOutDTO);

        gestionnaireMockMvc.perform(post("/createStageContract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contractId", is((int) stageContractOutDTO.getContractId())));
    }

    @Test
    void testCreateStageContactUserNotFound() throws Exception {
        when(gestionnaireService.createStageContract(any())).thenThrow(new NonExistentEntityException());

        gestionnaireMockMvc.perform(post("/createStageContract", stageContractInDTO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateStageContactOfferNotFound() throws Exception {
        when(gestionnaireService.createStageContract(any())).thenThrow(new NonExistentOfferExeption());

        gestionnaireMockMvc.perform(post("/createStageContract", stageContractInDTO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateStageContactConflict() throws Exception {
        when(gestionnaireService.createStageContract(any())).thenThrow(new AlreadyExistingStageContractException());

        gestionnaireMockMvc.perform(post("/createStageContract", stageContractInDTO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isConflict());
    }

    @Test
    void testCreateStageContactInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        gestionnaireMockMvc.perform(post("/createStageContract", stageContractInDTO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCompanyContractSignatureHappyDay() throws Exception {
        when(companyService.addSignatureToContract(any())).thenReturn(stageContractOutDTO);

        companyMockMvc.perform(put("/companySignatureContract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSignatureDTO.write(signatureInDTO).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companySignature", is(signatureInDTO.getSignature())));
    }

    @Test
    void testCompanyContractSignatureNotFound() throws Exception {
        when(companyService.addSignatureToContract(any())).thenThrow(new NonExistentEntityException());

        companyMockMvc.perform(put("/companySignatureContract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSignatureDTO.write(signatureInDTO).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCompanyContractSignatureInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        companyMockMvc.perform(put("/companySignatureContract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSignatureDTO.write(signatureInDTO).getJson()))
                .andExpect(status().isForbidden());
    }


    @Test
    void testGetUnvalidatedAcceptationsInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        gestionnaireMockMvc.perform(put("/contractsToCreate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetEvaluationInfoHappyDay() throws Exception {
        when(gestionnaireService.getMillieuEvaluationInfoForContract(anyLong())).thenReturn(evalInfoDTO);

        gestionnaireMockMvc.perform(put("/evaluateStage/{contractId}/getInfo", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomCompagnie", is(duffBeer.getCompanyName())));
    }

    @Test
    void testGetEvaluationInfoNotFound() throws Exception {
        when(gestionnaireService.getMillieuEvaluationInfoForContract(anyLong())).thenThrow(new NonExistentEntityException());

        gestionnaireMockMvc.perform(put("/evaluateStage/{contractId}/getInfo", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEvaluateStageHappyDay() throws Exception {
        doNothing().when(gestionnaireService).evaluateStage(any());
        gestionnaireMockMvc.perform(post("/evaluateStage/{token}", token.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEvalInDTO.write(evalInDTO).getJson()))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetAllContractsHappyDay() throws Exception {
        when(gestionnaireService.getContracts()).thenReturn(contractsDTO);

        gestionnaireMockMvc.perform(put("/getContracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contracts.size()", is(2)));
    }

    @Test
    void testGetAllContractsEmpty() throws Exception {
        contractsDTO.setContracts(new ArrayList<>());
        when(gestionnaireService.getContracts()).thenReturn(contractsDTO);

        gestionnaireMockMvc.perform(put("/getContracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contracts.size()", is(0)));
    }

    @Test
    void testGetCompanyContractsHappyDay() throws Exception {
        when(companyService.getContracts(anyLong(), anyString())).thenReturn(contractsDTO);

        companyMockMvc.perform(put("/companyContracts/{companyId}_{session}", 1, Offre.currentSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    void testGetCompanyContractsNotFound() throws Exception {
        when(companyService.getContracts(anyLong(), anyString())).thenThrow(new NonExistentEntityException());

        companyMockMvc.perform(put("/companyContracts/{companyId}_{session}", 1, Offre.currentSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCompanyContractsTokenInvalid() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        companyMockMvc.perform(put("/companyContracts/{companyId}_{session}", 1, Offre.currentSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateInterviewHappyDay() throws Exception{
        when(companyService.createInterview(any())).thenReturn(interviewOutDTO);

        companyMockMvc.perform(post("/createInterview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInterviewOutDTO.write(interviewOutDTO).getJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentSelectedDate", is("")))
                .andExpect(jsonPath("$.companyDateOffers.size()", is(3)));
    }

    @Test
    void testCreateInterviewNotFound() throws Exception {
        when(companyService.createInterview(any())).thenThrow(new NonExistentEntityException());

        companyMockMvc.perform(post("/createInterview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInterviewOutDTO.write(interviewOutDTO).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateInterviewOwnershipForbidden() throws Exception {
        when(companyService.createInterview(any())).thenThrow(new InvalidOwnershipException());

        companyMockMvc.perform(post("/createInterview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInterviewOutDTO.write(interviewOutDTO).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateInterviewBadRequest() throws Exception {
        when(companyService.createInterview(any())).thenThrow(new InvalidDateFormatException());

        companyMockMvc.perform(post("/createInterview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInterviewOutDTO.write(interviewOutDTO).getJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateInterviewInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        companyMockMvc.perform(post("/createInterview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInterviewOutDTO.write(interviewOutDTO).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetCompanyInterviewsHappyDay() throws Exception {
        when(companyService.getInterviews(anyLong())).thenReturn(new ArrayList<>(){{
            add(new InterviewOutDTO());
            add(new InterviewOutDTO());
            add(new InterviewOutDTO());
        }});

        companyMockMvc.perform(put("/getCompanyInterviews/{companyId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(3)));
    }

    @Test
    void testGetCompanyInterviewsNotFound() throws Exception {
        when(companyService.getInterviews(anyLong())).thenThrow(new NonExistentEntityException());

        companyMockMvc.perform(put("/getCompanyInterviews/{companyId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCompanyInterviewsInvalidTokken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        companyMockMvc.perform(put("/getCompanyInterviews/{companyId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

}
