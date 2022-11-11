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
import projet.projetstage02.dto.applications.ApplicationDTO;
import projet.projetstage02.dto.applications.ApplicationListDTO;
import projet.projetstage02.dto.auth.TokenDTO;
import projet.projetstage02.dto.contracts.ContractsDTO;
import projet.projetstage02.dto.contracts.StageContractOutDTO;
import projet.projetstage02.dto.cv.CvStatusDTO;
import projet.projetstage02.dto.interview.InterviewOutDTO;
import projet.projetstage02.dto.interview.InterviewSelectInDTO;
import projet.projetstage02.dto.offres.OffreInDTO;
import projet.projetstage02.dto.offres.OffreOutDTO;
import projet.projetstage02.dto.pdf.PdfDTO;
import projet.projetstage02.dto.pdf.PdfOutDTO;
import projet.projetstage02.dto.users.CompanyDTO;
import projet.projetstage02.dto.users.Students.StudentInDTO;
import projet.projetstage02.dto.users.Students.StudentOutDTO;
import projet.projetstage02.exception.*;
import projet.projetstage02.model.AbstractUser;
import projet.projetstage02.model.Token;
import projet.projetstage02.service.AuthService;
import projet.projetstage02.service.StudentService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static projet.projetstage02.model.Token.UserTypes.STUDENT;
import static projet.projetstage02.utils.ByteConverter.byteToString;

@ExtendWith(MockitoExtension.class)
public class StudentRootControllerTest {
    MockMvc studentMockMvc;

    @InjectMocks
    StudentRootController studentRootController;

    @Mock
    StudentService studentService;
    @Mock
    AuthService authService;
    JacksonTester<StudentInDTO> jsonStudentDTO;
    JacksonTester<TokenDTO> jsonTokenDTO;
    JacksonTester<PdfDTO> jsonPdfDTO;
    JacksonTester<SignatureInDTO> jsonSignatureDTO;
    JacksonTester<InterviewSelectInDTO> jsonInterviewSelectDTO;

    StudentInDTO bart;
    StudentOutDTO bartOut;
    CompanyDTO duffBeer;
    TokenDTO token;
    OffreInDTO duffOffre;
    OffreOutDTO duffOffreOut;
    PdfOutDTO duffOfferOut;
    PdfDTO bartCV;
    ApplicationDTO bartPostulation;
    ApplicationListDTO bartApplys;
    StageContractOutDTO stageContractOutDTO;
    SignatureInDTO signatureInDTO;
    ContractsDTO contractsDTO;

    CvStatusDTO cvStatusDTO;
    InterviewOutDTO interviewOutDTO;
    InterviewSelectInDTO interviewSelectInDTO;
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

        bartCV = PdfDTO.builder().pdf(new byte[0]).studentId(1).token(token.getToken()).build();

        JacksonTester.initFields(this, new ObjectMapper());

        studentMockMvc = MockMvcBuilders.standaloneSetup(studentRootController).build();

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

        cvStatusDTO = CvStatusDTO.builder()
                .status("ACCEPTED")
                .refusalMessage("")
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
    void testCreateStudentHappyDay() throws Exception {
        when(studentService.isStudentInvalid(anyString())).thenReturn(false);
        when(studentService.saveStudent(any())).thenReturn(1L);
        studentMockMvc.perform(post("/createStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStudentDTO.write(bart).getJson()))

                .andExpect(status().isCreated());
    }

    @Test
    void testCreateStudentConflict() throws Exception {
        when(studentService.isStudentInvalid(anyString())).thenReturn(true);

        studentMockMvc.perform(post("/createStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStudentDTO.write(bart).getJson()))

                .andExpect(status().isConflict());
    }

    @Test
    void testCreateStudentBadRequest() throws Exception {
        studentMockMvc.perform(post("/createStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStudentDTO.write(new StudentInDTO()).getJson()))

                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateStudentExistingEmailDeleteOld() throws Exception {
        when(studentService.isStudentInvalid(anyString())).thenReturn(false);
        studentMockMvc.perform(post("/createStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStudentDTO.write(bart).getJson()))

                .andExpect(status().isCreated());
    }

    @Test
    void testCreateStudentExistingEmailDeleteOldNotFound() throws Exception {
        when(studentService.isStudentInvalid(anyString())).thenThrow(new NonExistentEntityException());
        studentMockMvc.perform(post("/createStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStudentDTO.write(bart).getJson()))

                .andExpect(status().isInternalServerError());
    }

    @Test
    void testConfirmStudentEmailHappyDay() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(bartOut);
        when(studentService.saveStudent(any())).thenReturn(1L);

        studentMockMvc.perform(
                        put("/confirmEmail/student/{id}", 1))

                .andExpect(status().isCreated());
    }

    @Test
    void testConfirmStudentEmailNotFound() throws Exception {
        when(studentService.getStudentById(1L)).thenThrow(new NonExistentEntityException());

        studentMockMvc.perform(
                        put("/confirmEmail/student/{id}", 1))

                .andExpect(status().isNotFound());
    }

    @Test
    void testConfirmStudentEmailExpired() throws Exception {
        bartOut.setInscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now().minusMonths(1)).getTime());
        when(studentService.getStudentById(1L)).thenReturn(bartOut);

        studentMockMvc.perform(
                        put("/confirmEmail/student/{id}", 1))

                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginStudentHappyDay() throws Exception {
        bartOut.setEmailConfirmed(true);
        when(authService.getToken(token.getToken(), STUDENT)).thenReturn(Token.builder().userId(1L).build());
        when(studentService.getStudentById(1L)).thenReturn(bartOut);

        studentMockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Bart")));
    }

    @Test
    void testLoginStudentNotFound() throws Exception {
        when(authService.getToken(token.getToken(), STUDENT)).thenReturn(Token.builder().userId(1).build());
        when(studentService.getStudentById(anyLong()))
                .thenThrow(new NonExistentEntityException());

        studentMockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))

                .andExpect(status().isNotFound());
    }

    @Test
    void testUploadCurriculumVitaeSuccess() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        bartOut.setCvToValidate(byteToString(bartCV.getPdf()));
        when(studentService.uploadCurriculumVitae(any())).thenReturn(bartOut);

        studentMockMvc.perform(put("/uploadStudentCV")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPdfDTO.write(bartCV).getJson()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Bart")))
                .andExpect(jsonPath("$.cvToValidate", is("[]")));
    }

    @Test
    void testUploadCurriculumVitaeNotFound() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        doThrow(new NonExistentEntityException())
                .when(studentService).uploadCurriculumVitae(any());

        studentMockMvc.perform(put("/uploadStudentCV")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPdfDTO.write(bartCV).getJson()));
    }

    @Test
    void testUploadCurriculumVitaeInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        studentMockMvc.perform(put("/uploadStudentCV")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPdfDTO.write(bartCV).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetOffersByStudentDepartmentSuccess() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        when(studentService.getOffersByStudentDepartment(anyLong())).thenReturn(List.of(duffOffreOut));

        studentMockMvc.perform(put("/getOffers/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomDeCompagnie", is(duffOffre.getNomDeCompagnie())));
    }

    @Test
    void testGetOffersByStudentDepartmentNotFound() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        when(studentService.getOffersByStudentDepartment(anyLong())).thenThrow(new NonExistentEntityException());

        studentMockMvc.perform(put("/getOffers/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetOffersByStudentDepartmentInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());
        studentMockMvc.perform(put("/getOffers/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetOfferStudentSuccess() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        when(studentService.getOfferPdfById(anyLong())).thenReturn(duffOfferOut);

        studentMockMvc.perform(put("/getOfferStudent/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pdf", is(duffOfferOut.getPdf())));
    }

    @Test
    void testGetOfferStudentNotFound() throws Exception {
        when(authService.getToken(any(), any())).thenReturn(Token.builder().userId(1).build());
        when(studentService.getOfferPdfById(anyLong())).thenThrow(new NonExistentEntityException());

        studentMockMvc.perform(put("/getOfferStudent/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetOfferStudentInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        studentMockMvc.perform(put("/getOfferStudent/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreatePostulationSuccess() throws Exception {
        when(studentService.createPostulation(anyLong(), anyLong())).thenReturn(bartPostulation);
        studentMockMvc.perform(put("/applyToOffer/{studentId}_{offerId}", 2, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName", is(bartPostulation.getFullName())))
                .andExpect(jsonPath("$.company", is(bartPostulation.getCompany())));
    }

    @Test
    void testCreatePostulationNotFound() throws Exception {
        when(studentService.createPostulation(anyLong(), anyLong())).thenThrow(new NonExistentEntityException());

        studentMockMvc.perform(put("/applyToOffer/{studentId}_{offerId}", 2, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreatePostulationConflict() throws Exception {
        when(studentService.createPostulation(anyLong(), anyLong())).thenThrow(new AlreadyExistingPostulation());

        studentMockMvc.perform(put("/applyToOffer/{studentId}_{offerId}", 2, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isConflict());
    }

    @Test
    void testCreatePostulationInvalidToken() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        studentMockMvc.perform(put("/applyToOffer/{studentId}_{offerId}", 2, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetPostulsOfferIdSuccess() throws Exception {
        when(studentService.getPostulsOfferId(anyLong())).thenReturn(bartApplys);

        studentMockMvc.perform(put("/studentApplys/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.offersId.size()", is(1)));
    }

    @Test
    void testGetPostulsOfferIdNotFound() throws Exception {
        when(studentService.getPostulsOfferId(anyLong())).thenThrow(new NonExistentEntityException());

        studentMockMvc.perform(put("/studentApplys/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetPostulsOfferIdTokenInvalid() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        studentMockMvc.perform(put("/studentApplys/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetStatusValidationCVHappyDay() throws Exception {
        when(studentService.getStudentCvStatus(anyLong())).thenReturn(cvStatusDTO);

        studentMockMvc.perform(put("/getStatutValidationCV/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("ACCEPTED")));
    }

    @Test
    void testGetStatusValidationCVForbidden() throws Exception {
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        studentMockMvc.perform(put("/getStatutValidationCV/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetStatusValidationCVNotFound() throws Exception {
        when(studentService.getStudentCvStatus(anyLong())).thenThrow(new NonExistentEntityException());

        studentMockMvc.perform(put("/getStatutValidationCV/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testStudentContractSignatureHappyDay() throws Exception {
        when(studentService.addSignatureToContract(any())).thenReturn(stageContractOutDTO);

        studentMockMvc.perform(put("/studentSignatureContract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSignatureDTO.write(signatureInDTO).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentSignature", is(signatureInDTO.getSignature())));
    }

    @Test
    void testStudentContractSignatureNotFound() throws Exception {
        when(studentService.addSignatureToContract(any())).thenThrow(new NonExistentEntityException());

        studentMockMvc.perform(put("/studentSignatureContract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSignatureDTO.write(signatureInDTO).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testStudentContractSignatureConflict() throws Exception {
        when(studentService.addSignatureToContract(any())).thenThrow(new InvalidOwnershipException());

        studentMockMvc.perform(put("/studentSignatureContract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTokenDTO.write(token).getJson()))
                .andExpect(status().isConflict());
    }

    @Test
    void testStudentSelectDateHappyDay() throws Exception{
        when(studentService.selectInterviewTime(any())).thenReturn(interviewOutDTO);

        studentMockMvc.perform(put("/studentSelectDate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInterviewSelectDTO.write(interviewSelectInDTO).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    void testStudentSelectDateNotFound() throws Exception{
        when(studentService.selectInterviewTime(any())).thenThrow(new NonExistentEntityException());

        studentMockMvc.perform(put("/studentSelectDate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInterviewSelectDTO.write(interviewSelectInDTO).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testStudentSelectDateForbidden() throws Exception{
        when(studentService.selectInterviewTime(any())).thenThrow(new InvalidOwnershipException());

        studentMockMvc.perform(put("/studentSelectDate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInterviewSelectDTO.write(interviewSelectInDTO).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testStudentSelectDateBadRequest() throws Exception{
        when(studentService.selectInterviewTime(any())).thenThrow(new InvalidDateFormatException());

        studentMockMvc.perform(put("/studentSelectDate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInterviewSelectDTO.write(interviewSelectInDTO).getJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testStudentSelectDateInvalidToken() throws Exception{
        when(authService.getToken(any(), any())).thenThrow(new InvalidTokenException());

        studentMockMvc.perform(put("/studentSelectDate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInterviewSelectDTO.write(interviewSelectInDTO).getJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetStudentInterviewsHappyDay() throws Exception{
        when(studentService.getInterviews(anyLong())).thenReturn(new ArrayList<>(){{
            add(new InterviewOutDTO());
            add(new InterviewOutDTO());
            add(new InterviewOutDTO());
        }});

        studentMockMvc.perform(put("/getStudentInterviews/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInterviewSelectDTO.write(interviewSelectInDTO).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(3)));
    }

    @Test
    void testGetStudentInterviewsNotFound() throws Exception{
        when(studentService.getInterviews(anyLong())).thenThrow(new NonExistentEntityException());

        studentMockMvc.perform(put("/getStudentInterviews/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInterviewSelectDTO.write(interviewSelectInDTO).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetStudentInterviewsInvalidToken() throws Exception{
        when(authService.getToken(anyString(), any())).thenThrow(new InvalidTokenException());

        studentMockMvc.perform(put("/getStudentInterviews/{studentId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInterviewSelectDTO.write(interviewSelectInDTO).getJson()))
                .andExpect(status().isForbidden());
    }

}
