package projet.projetstage02.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
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
import projet.projetstage02.dto.applications.OfferApplicationDTO;
import projet.projetstage02.dto.auth.TokenDTO;
import projet.projetstage02.dto.contracts.ContractsDTO;
import projet.projetstage02.dto.contracts.StageContractOutDTO;
import projet.projetstage02.dto.evaluations.Etudiant.EvaluationEtudiantInDTO;
import projet.projetstage02.dto.interview.InterviewOutDTO;
import projet.projetstage02.dto.offres.OfferAcceptedStudentsDTO;
import projet.projetstage02.dto.offres.OffreInDTO;
import projet.projetstage02.dto.offres.OffreOutDTO;
import projet.projetstage02.dto.pdf.PdfOutDTO;
import projet.projetstage02.dto.users.CompanyDTO;
import projet.projetstage02.dto.users.Students.StudentInDTO;
import projet.projetstage02.dto.users.Students.StudentOutDTO;
import projet.projetstage02.exception.*;
import projet.projetstage02.model.AbstractUser;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static projet.projetstage02.model.Token.UserTypes.COMPANY;
import static projet.projetstage02.utils.ByteConverter.byteToString;

@ExtendWith(MockitoExtension.class)

public class CompanyRootControllerTest {
    MockMvc companyMockMvc;

    @InjectMocks
    CompanyRootController companyRootController;

    @Mock
    CompanyService companyService;
    @Mock
    GestionnaireService gestionnaireService;
    @Mock
    AuthService authService;

    JacksonTester<CompanyDTO> jsonCompanyDTO;
    JacksonTester<OffreInDTO> jsonOffreDTO;
    JacksonTester<TokenDTO> jsonTokenDTO;
    JacksonTester<SignatureInDTO> jsonSignatureDTO;
    JacksonTester<InterviewOutDTO> jsonInterviewOutDTO;
    JacksonTester<EvaluationEtudiantInDTO> jsonEvalEtudiantInDTO;

    StudentInDTO bart;
    StudentOutDTO bartOut;
    CompanyDTO duffBeer;
    TokenDTO token;
    OffreInDTO duffOffre;
    OffreOutDTO duffOffreOut;
    ApplicationAcceptationDTO applicationDTO;
    OfferAcceptedStudentsDTO acceptedStudentsDTO;
    StageContractOutDTO stageContractOutDTO;
    SignatureInDTO signatureInDTO;
    ContractsDTO contractsDTO;
    OfferApplicationDTO offerApplicationDTO;
    InterviewOutDTO interviewOutDTO;
    EvaluationEtudiantInDTO evaluationEtudiantInDTO;
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

        companyMockMvc = MockMvcBuilders.standaloneSetup(companyRootController).build();

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

        evaluationEtudiantInDTO = EvaluationEtudiantInDTO.builder()
                .accepteCritiques("plutotEnAccord")
                .acueillirPourProchainStage("oui")
                .adapteCulture("plutotEnAccord")
                .attentionAuxDetails("plutotEnAccord")
                .bonneAnalyseProblemes("plutotEnAccord")
                .commentairesHabilites("plutotEnAccord")
                .commentairesProductivite("plutotEnAccord")
                .commentairesQualite("plutotEnAccord")
                .commentairesAppreciation("plutotEnAccord")
                .comprendRapidement("plutotEnAccord")
                .contactsFaciles("plutotEnAccord")
                .commentairesRelationsInterpersonnelles("plutotEnAccord")
                .contractId(1L)
                .dateSignature("2021-05-01")
                .discuteAvecStagiaire("oui")
                .doubleCheckTravail("plutotEnAccord")
                .etablirPriorites("plutotEnAccord")
                .exprimeIdees("plutotEnAccord")
                .ecouteActiveComprendrePDVautre("plutotEnAccord")
                .formationTechniqueSuffisante("plutotEnAccord")
                .habiletesDemontres("repondentAttentes")
                .heuresEncadrement(145)
                .initiative("plutotEnAccord")
                .interetMotivation("plutotEnAccord")
                .occasionsDePerfectionnement("plutotEnAccord")
                .planifieTravail("plutotEnAccord")
                .ponctuel("plutotEnAccord")
                .respecteAutres("plutotEnAccord")
                .respecteEcheances("plutotEnAccord")
                .rythmeSoutenu("plutotEnAccord")
                .responsableAutonome("plutotEnAccord")
                .respecteMandatsDemandes("plutotEnAccord")
                .signature(byteToString(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}))
                .travailEnEquipe("plutotEnAccord")
                .travailSecuritaire("plutotEnAccord")
                .travailEfficace("plutotEnAccord")
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
        when(companyService.getCompanyByIdDTO(1L)).thenReturn(duffBeer);
        when(companyService.saveCompany(duffBeer)).thenReturn(1L);

        companyMockMvc.perform(
                        put("/confirmEmail/company/{id}", 1))

                .andExpect(status().isCreated());
    }

    @Test
    void testConfirmCompanyEmailNotFound() throws Exception {
        when(companyService.getCompanyByIdDTO(1L)).thenThrow(new NonExistentEntityException());

        companyMockMvc.perform(
                        put("/confirmEmail/company/{id}", 1))

                .andExpect(status().isNotFound());
    }

    @Test
    void testConfirmCompanyEmailExpired() throws Exception {
        duffBeer.setInscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now().minusMonths(1)).getTime());
        when(companyService.getCompanyByIdDTO(1L)).thenReturn(duffBeer);

        companyMockMvc.perform(
                        put("/confirmEmail/company/{id}", 1))

                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginCompanyHappyDay() throws Exception {
        duffBeer.setEmailConfirmed(true);
        when(authService.getToken(token.getToken(), COMPANY)).thenReturn(Token.builder().userId(1L).build());
        when(companyService.getCompanyByIdDTO(1L)).thenReturn(duffBeer);

        companyMockMvc.perform(put("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Duff")));
    }

    @Test
    void testLoginCompanyNotFound() throws Exception {
        when(authService.getToken(token.getToken(), COMPANY)).thenReturn(Token.builder().userId(1).build());
        when(companyService.getCompanyByIdDTO(anyLong())).thenThrow(new NonExistentEntityException());

        companyMockMvc.perform(put("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))

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
        when(companyService.getAcceptedStudentsForOffer(anyLong())).thenThrow(new NonExistentEntityException());

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
        when(companyService.getStudentsForOffer(anyLong())).thenThrow(new NonExistentEntityException());

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
    void testGetCompanyInterviewsInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        companyMockMvc.perform(put("/getCompanyInterviews/{companyId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testEvaluateStudentHappyDay() throws Exception {
        doNothing().when(companyService).evaluateStudent(any());
        companyMockMvc.perform(post("/evaluateStudent/{token}", token.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEvalEtudiantInDTO.write(evaluationEtudiantInDTO).getJson()))
                .andExpect(status().isCreated());
    }

    @Test
    void testEvaluateStudentInvalidToken() throws Exception {
        when(authService.getToken(anyString(), any())).thenThrow(new InvalidTokenException());

        companyMockMvc.perform(post("/evaluateStudent/{token}", token.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEvalEtudiantInDTO.write(evaluationEtudiantInDTO).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testEvaluateStudentNotFound() throws Exception {
        doThrow(new NonExistentEntityException()).when(gestionnaireService).createEvaluationEtudiantPDF(anyLong());

        companyMockMvc.perform(post("/evaluateStudent/{token}", token.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEvalEtudiantInDTO.write(evaluationEtudiantInDTO).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEvaluateStudentInternalServerError() throws Exception {
        doThrow(new DocumentException()).when(gestionnaireService).createEvaluationEtudiantPDF(anyLong());

        companyMockMvc.perform(post("/evaluateStudent/{token}", token.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEvalEtudiantInDTO.write(evaluationEtudiantInDTO).getJson()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testEvaluateStudentBadRequest() throws Exception {
        doThrow(new EmptySignatureException()).when(gestionnaireService).createEvaluationEtudiantPDF(anyLong());

        companyMockMvc.perform(post("/evaluateStudent/{token}", token.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEvalEtudiantInDTO.write(evaluationEtudiantInDTO).getJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetEvaluatedStudentContratsHappyDay() throws Exception {
        when(companyService.getEvaluatedStudentsContracts(anyLong())).thenReturn(new ArrayList<>() {{
            add(stageContractOutDTO.getContractId());
            add(stageContractOutDTO.getContractId());
            add(stageContractOutDTO.getContractId());
            add(stageContractOutDTO.getContractId());
        }});

        companyMockMvc.perform(put("/getEvaluatedStudentsContracts/{companyId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(4)));

    }

    @Test
    void testGetEvaluatedStudentContratsInvalidToken() throws Exception {
        when(authService.getToken(anyString(), any())).thenThrow(new InvalidTokenException());

        companyMockMvc.perform(put("/getEvaluatedStudentsContracts/{companyId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }
}
