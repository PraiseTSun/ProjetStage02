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
import projet.projetstage02.dto.auth.TokenDTO;
import projet.projetstage02.dto.contracts.ContractsDTO;
import projet.projetstage02.dto.contracts.StageContractInDTO;
import projet.projetstage02.dto.contracts.StageContractOutDTO;
import projet.projetstage02.dto.cv.CvRefusalDTO;
import projet.projetstage02.dto.evaluations.MillieuStage.MillieuStageEvaluationInDTO;
import projet.projetstage02.dto.evaluations.MillieuStage.MillieuStageEvaluationInfoDTO;
import projet.projetstage02.dto.offres.OffreInDTO;
import projet.projetstage02.dto.offres.OffreOutDTO;
import projet.projetstage02.dto.pdf.PdfOutDTO;
import projet.projetstage02.dto.users.CompanyDTO;
import projet.projetstage02.dto.users.GestionnaireDTO;
import projet.projetstage02.dto.users.Students.StudentInDTO;
import projet.projetstage02.dto.users.Students.StudentOutDTO;
import projet.projetstage02.exception.AlreadyExistingStageContractException;
import projet.projetstage02.exception.InvalidTokenException;
import projet.projetstage02.exception.NonExistentEntityException;
import projet.projetstage02.exception.NonExistentOfferExeption;
import projet.projetstage02.model.AbstractUser;
import projet.projetstage02.model.Token;
import projet.projetstage02.service.AuthService;
import projet.projetstage02.service.GestionnaireService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static projet.projetstage02.model.Token.UserTypes.GESTIONNAIRE;
import static projet.projetstage02.utils.ByteConverter.byteToString;
import static projet.projetstage02.utils.TimeUtil.currentTimestamp;

@ExtendWith(MockitoExtension.class)

public class GestionnaireRootControllerTest {
    MockMvc gestionnaireMockMvc;

    @InjectMocks
    GestionnaireRootController gestionnaireRootController;

    @Mock
    GestionnaireService gestionnaireService;
    @Mock
    AuthService authService;

    JacksonTester<MillieuStageEvaluationInDTO> jsonEvalInDTO;
    JacksonTester<GestionnaireDTO> jsonGestionnaireDTO;
    JacksonTester<OffreInDTO> jsonOffreDTO;
    JacksonTester<TokenDTO> jsonTokenDTO;
    JacksonTester<CvRefusalDTO> jsonCvRefusalDTO;

    StudentInDTO bart;
    StudentOutDTO bartOut;
    CompanyDTO duffBeer;
    TokenDTO token;
    GestionnaireDTO burns;
    OffreInDTO duffOffre;
    OffreOutDTO duffOffreOut;
    CvRefusalDTO cvRefusalDTO;
    StageContractInDTO stageContractInDTO;
    StageContractOutDTO stageContractOutDTO;
    SignatureInDTO signatureInDTO;
    ContractsDTO contractsDTO;

    MillieuStageEvaluationInfoDTO evalInfoDTO;
    MillieuStageEvaluationInDTO evalInDTO;
    // https://thepracticaldeveloper.com/guide-spring-boot-controller-tests/
    @BeforeEach
    void setup() {
        bart = StudentInDTO.builder()
                .firstName("Bart")
                .lastName("Simpson")
                .email("bart.simpson@springfield.com")
                .department(AbstractUser.Department.Transport.departement)
                .password("eatMyShorts")
                .isConfirmed(false)
                .inscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
                .build();
        bartOut = StudentOutDTO.builder()
                .firstName("Bart")
                .lastName("Simpson")
                .email("bart.simpson@springfield.com")
                .department(AbstractUser.Department.Transport.departement)
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
                .department(AbstractUser.Department.Transport.departement)
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
                .department(AbstractUser.Department.Transport.departement)
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
                .department(AbstractUser.Department.Transport.departement)
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

        JacksonTester.initFields(this, new ObjectMapper());

        gestionnaireMockMvc = MockMvcBuilders.standaloneSetup(gestionnaireRootController).build();

        cvRefusalDTO = CvRefusalDTO.builder()
                .refusalReason("refused")
                .token(token.getToken())
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
        doNothing().when(gestionnaireService).saveEvaluateStage(any());
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
}
