package projet.projetstage02.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projet.projetstage02.DTO.LoginDTO;
import projet.projetstage02.DTO.TokenDTO;
import projet.projetstage02.exception.InvalidTokenException;
import projet.projetstage02.model.*;
import projet.projetstage02.repository.CompanyRepository;
import projet.projetstage02.repository.GestionnaireRepository;
import projet.projetstage02.repository.StudentRepository;
import projet.projetstage02.repository.TokenRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static projet.projetstage02.model.Token.UserTypes.*;
import static projet.projetstage02.utils.TimeUtil.currentTimestamp;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private GestionnaireRepository gestionnaireRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private TokenRepository tokenRepository;
    @InjectMocks
    private AuthService service;

    LoginDTO studentLoginDTO;
    LoginDTO companyLoginDTO;
    LoginDTO gestionnaireLoginDTO;
    private Gestionnaire gestionnaireTest;
    private Company companyTest;
    private Student studentTest;
    TokenDTO tokenDTO;
    @BeforeEach
    void setup(){
        gestionnaireTest = new Gestionnaire(
                "prenom",
                "nom",
                "gestionnaire@email.com",
                "password3"
        );

        companyTest = new Company(
                "prenom",
                "nom",
                "company@email.com",
                "password2",
                AbstractUser.Department.Transport,
                "Company Test"
        );

        studentTest = new Student(
                "prenom",
                "nom",
                "student@email.com",
                "password1",
                AbstractUser.Department.Informatique
        );
        studentLoginDTO = LoginDTO.builder()
                .email("student@email.com")
                .password("password1")
                .build();
        companyLoginDTO = LoginDTO.builder()
                .email("company@email.com")
                .password("password2")
                .build();
        gestionnaireLoginDTO = LoginDTO.builder()
                .email("gestionnaire@email.com")
                .password("password3")
                .build();

        tokenDTO = TokenDTO.builder()
                .token("9f0b0e68-c177-4504-9087-eea175653ee3")
                .build();
    }

    @Test
    void testLoginStudentHappyDay() throws InvalidTokenException {
        //Arrange
        studentTest.setEmailConfirmed(true);
        studentTest.setConfirm(true);
        when(studentRepository.findByEmailAndPassword(any(),any()))
                .thenReturn(Optional.of(studentTest));
        //Act
        String token = service.loginIfValid(studentLoginDTO,STUDENT);
        //Assert
        assertThat(token).isNotNull();
    }
    @Test
    void testLoginStudentNotFound() {
        //Arrange
        studentTest.setEmailConfirmed(true);
        when(studentRepository.findByEmailAndPassword(any(),any()))
                .thenReturn(Optional.empty());
        //Act
        try {
            service.loginIfValid(studentLoginDTO,STUDENT);
        }catch (InvalidTokenException e){
        //Assert
            return;
        };
        fail();
    }
    @Test
    void testLoginStudentEmailUnconfirmed(){
        //Arrange
        studentTest.setConfirm(true);
        when(studentRepository.findByEmailAndPassword(any(),any()))
                .thenReturn(Optional.of(studentTest));
        //Act
        try {
            service.loginIfValid(studentLoginDTO,STUDENT);
        }catch (InvalidTokenException e){
            //Assert
            return;
        };
        fail();
    }
    @Test
    void testLoginStudentUnconfirmed(){
        //Arrange
        studentTest.setEmailConfirmed(true);
        when(studentRepository.findByEmailAndPassword(any(),any()))
                .thenReturn(Optional.of(studentTest));
        //Act
        try {
            service.loginIfValid(studentLoginDTO,STUDENT);
        }catch (InvalidTokenException e){
            //Assert
            return;
        };
        fail();
    }
    @Test
    void testLoginCompanyHappyDay() throws InvalidTokenException {
        //Arrange
        companyTest.setEmailConfirmed(true);
        companyTest.setConfirm(true);
        when(companyRepository.findByEmailAndPassword(any(),any()))
                .thenReturn(Optional.of(companyTest));
        //Act
        String token = service.loginIfValid(companyLoginDTO,COMPANY);
        //Assert
        assertThat(token).isNotNull();
    }
    @Test
    void testLoginCompanyNotFound() {
        //Arrange
        companyTest.setEmailConfirmed(true);
        //Act
        try {
            service.loginIfValid(companyLoginDTO,COMPANY);
        }catch (InvalidTokenException e){
            //Assert
            return;
        };
        fail();
    }
    @Test
    void testLoginCompanyEmailUnconfirmed() {
        //Arrange
        companyTest.setConfirm(true);
        when(companyRepository.findByEmailAndPassword(any(),any()))
                .thenReturn(Optional.of(companyTest));
        //Act
        try {
            service.loginIfValid(companyLoginDTO,COMPANY);
        }catch (InvalidTokenException e){
            //Assert
            return;
        };
        fail();
    }
    @Test
    void testLoginCompanyUnconfirmed() {
        //Arrange
        companyTest.setEmailConfirmed(true);
        when(companyRepository.findByEmailAndPassword(any(),any()))
                .thenReturn(Optional.of(companyTest));
        //Act
        try {
            service.loginIfValid(companyLoginDTO,COMPANY);
        }catch (InvalidTokenException e){
            //Assert
            return;
        };
        fail();
    }
    @Test
    void testLoginGestionnaireHappyDay() throws InvalidTokenException {
        //Arrange
        gestionnaireTest.setEmailConfirmed(true);
        when(gestionnaireRepository.findByEmailAndPassword(any(),any()))
                .thenReturn(Optional.of(gestionnaireTest));
        //Act
        String token = service.loginIfValid(gestionnaireLoginDTO,GESTIONNAIRE);
        //Assert
        assertThat(token).isNotNull();
    }
    @Test
    void testLoginGestionnaireNotFound() {
        //Arrange
        gestionnaireTest.setEmailConfirmed(true);
        when(gestionnaireRepository.findByEmailAndPassword(any(),any()))
                .thenReturn(Optional.empty());
        //Act
        try {
            service.loginIfValid(gestionnaireLoginDTO,GESTIONNAIRE);
        }catch (InvalidTokenException e){
            //Assert
            return;
        };
        fail();
    }
    @Test
    void testLoginGestionnaireEmailUnconfirmed() {
        //Arrange
        when(gestionnaireRepository.findByEmailAndPassword(any(),any()))
                .thenReturn(Optional.of(gestionnaireTest));
        //Act
        try {
            service.loginIfValid(gestionnaireLoginDTO,GESTIONNAIRE);
        }catch (InvalidTokenException e){
            //Assert
            return;
        };
        fail();
    }
    @Test
    void testGetTokenStudentHappyDay() throws InvalidTokenException {
        //Arrange
        when(tokenRepository.findById(any())).thenReturn(
                Optional.of(Token.builder().token(tokenDTO.getToken())
                        .userType(STUDENT)
                        .lastCalledTimeStamp(currentTimestamp()).build()));
        //Act
        Token token = service.getToken(tokenDTO.getToken(), STUDENT);
        //Assert
        assertThat(token.getToken()).isEqualTo(tokenDTO.getToken());
    }
    @Test
    void getTokenCompanyHappyDay() throws InvalidTokenException {

        //Arrange
        when(tokenRepository.findById(any())).thenReturn(
                Optional.of(Token.builder().token(tokenDTO.getToken())
                        .userType(COMPANY)
                        .lastCalledTimeStamp(currentTimestamp()).build()));
        //Act
        Token token = service.getToken(tokenDTO.getToken(), COMPANY);
        //Assert
        assertThat(token.getToken()).isEqualTo(tokenDTO.getToken());
    }
    @Test
    void getTokenGestionnaireHappyDay() throws InvalidTokenException {

        //Arrange
        when(tokenRepository.findById(any())).thenReturn(
                Optional.of(Token.builder().token(tokenDTO.getToken())
                        .userType(GESTIONNAIRE)
                        .lastCalledTimeStamp(currentTimestamp()).build()));
        //Act
        Token token = service.getToken(tokenDTO.getToken(), GESTIONNAIRE);
        //Assert
        assertThat(token.getToken()).isEqualTo(tokenDTO.getToken());
    }
    @Test
    void getTokenStudentExpired()  {
        //Arrange
        when(tokenRepository.findById(any())).thenReturn(
                Optional.of(Token.builder().token(tokenDTO.getToken())
                        .userType(STUDENT)
                        .build()));
        //Act
        try{
            service.getToken(tokenDTO.getToken(), STUDENT);
        }catch (InvalidTokenException e){
        //Assert
            return;
        }
        fail();
    }

    @Test
    void getTokenCompanyExpired(){
        //Arrange
        when(tokenRepository.findById(any())).thenReturn(
                Optional.of(Token.builder().token(tokenDTO.getToken())
                        .userType(COMPANY)
                        .build()));
        //Act
        try{
            service.getToken(tokenDTO.getToken(), COMPANY);
        }catch (InvalidTokenException e){
            //Assert
            return;
        }
        fail();
    }
    @Test
    void getTokenGestionnaireExpired(){
        //Arrange
        when(tokenRepository.findById(any())).thenReturn(
                Optional.of(Token.builder().token(tokenDTO.getToken())
                        .userType(GESTIONNAIRE)
                        .build()));
        //Act
        try{
            service.getToken(tokenDTO.getToken(), GESTIONNAIRE);
        }catch (InvalidTokenException e){
            //Assert
            return;
        }
        fail();
    }
    @Test
    void getTokenStudentWrongUser(){
        //Arrange
        when(tokenRepository.findById(any())).thenReturn(
                Optional.of(Token.builder().token(tokenDTO.getToken())
                        .lastCalledTimeStamp(currentTimestamp())
                        .userType(GESTIONNAIRE)
                        .build()));
        //Act
        try{
            service.getToken(tokenDTO.getToken(), STUDENT);
        }catch (InvalidTokenException e){
            //Assert
            return;
        }
        fail();
    }
    @Test
    void getTokenCompanyWrongUser(){
        //Arrange
        when(tokenRepository.findById(any())).thenReturn(
                Optional.of(Token.builder().token(tokenDTO.getToken())
                        .lastCalledTimeStamp(currentTimestamp())
                        .userType(GESTIONNAIRE)
                        .build()));
        //Act
        try{
            service.getToken(tokenDTO.getToken(), COMPANY);
        }catch (InvalidTokenException e){
            //Assert
            return;
        }
        fail();
    }
    @Test
    void getTokenGestionnaireWrongUser(){
        //Arrange
        when(tokenRepository.findById(any())).thenReturn(
                Optional.of(Token.builder().token(tokenDTO.getToken())
                        .lastCalledTimeStamp(currentTimestamp())
                        .userType(STUDENT)
                        .build()));
        //Act
        try{
            service.getToken(tokenDTO.getToken(), GESTIONNAIRE);
        }catch (InvalidTokenException e){
            //Assert
            return;
        }
        fail();
    }
    @Test
    void getTokenStudentNotFound(){
        //Arrange
        when(tokenRepository.findById(any())).thenReturn(
                Optional.empty());
        //Act
        try{
            service.getToken(tokenDTO.getToken(), STUDENT);
        }catch (InvalidTokenException e){
            //Assert
            return;
        }
        fail();
    }
    @Test
    void getTokenCompanyNotFound(){
        //Arrange
        when(tokenRepository.findById(any())).thenReturn(
                Optional.empty());
        //Act
        try{
            service.getToken(tokenDTO.getToken(), COMPANY);
        }catch (InvalidTokenException e){
            //Assert
            return;
        }
        fail();
    }
    @Test
    void testGetTokenGestionnaireNotFound(){
        //Arrange
        when(tokenRepository.findById(any())).thenReturn(
                Optional.empty());
        //Act
        try{
            service.getToken(tokenDTO.getToken(), GESTIONNAIRE);
        }catch (InvalidTokenException e){
            //Assert
            return;
        }
        fail();
    }

}
