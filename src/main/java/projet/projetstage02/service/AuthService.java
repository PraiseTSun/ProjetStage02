package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.DTO.LoginDTO;
import projet.projetstage02.exception.InvalidTokenException;
import projet.projetstage02.model.*;
import projet.projetstage02.model.Token.UserTypes;
import projet.projetstage02.repository.CompanyRepository;
import projet.projetstage02.repository.GestionnaireRepository;
import projet.projetstage02.repository.StudentRepository;
import projet.projetstage02.repository.TokenRepository;

import java.util.Optional;
import java.util.UUID;

import static projet.projetstage02.model.Token.ONE_HOURS_MS;
import static projet.projetstage02.model.Token.UserTypes.*;
import static projet.projetstage02.utils.TimeUtil.currentTimestamp;

@Service
@AllArgsConstructor
public class AuthService {
    final TokenRepository tokenRepository;

    final GestionnaireRepository gestionnaireRepository;
    final StudentRepository studentRepository;
    final CompanyRepository companyRepository;

    public String loginIfValid(LoginDTO dto, Token.UserTypes userType)
            throws InvalidTokenException {
        if (userType == COMPANY) {
            return loginIfValidCompany(dto);
        } else if (userType == STUDENT) {
            return loginIfValidStudent(dto);
        } else if (userType == GESTIONNAIRE) {
            return loginIfValidGestionnaire(dto);
        }
        throw new InvalidTokenException();
    }

    private String loginIfValidGestionnaire(LoginDTO loginDTO) throws InvalidTokenException {
        Optional<Gestionnaire> gestionnaire = gestionnaireRepository.findByEmailAndPassword(
                loginDTO.getEmail(), loginDTO.getPassword());
        validateOptional(gestionnaire);
        return createToken(gestionnaire.get(), GESTIONNAIRE);
    }

    private String loginIfValidStudent(LoginDTO loginDTO) throws InvalidTokenException {
        Optional<Student> student = studentRepository.findByEmailAndPassword(
                loginDTO.getEmail(), loginDTO.getPassword());
        validateOptional(student);
        return createToken(student.get(), STUDENT);
    }

    private String loginIfValidCompany(LoginDTO loginDTO) throws InvalidTokenException {
        Optional<Company> company = companyRepository.findByEmailAndPassword(loginDTO.getEmail(),
                loginDTO.getPassword());
        validateOptional(company);

        return createToken(company.get(), COMPANY);
    }

    private String createToken(AbstractUser user, UserTypes userType) {
        Token token = Token.builder()
                .token(UUID.randomUUID().toString())
                .userId(user.getId())
                .userType(userType)
                .lastCalledTimeStamp(currentTimestamp())
                .build();
        tokenRepository.save(token);
        return token.getToken();
    }

    private <T extends AbstractUser> void validateOptional(Optional<T> optional) throws InvalidTokenException {
        if (optional.isEmpty() || !optional.get().isEmailConfirmed())
            throw new InvalidTokenException();
    }

    public Token getToken(String tokenId, UserTypes userType) throws InvalidTokenException {
        try {
            Optional<Token> token = tokenRepository.findById(tokenId);
            if (token.isEmpty() || token.get().getUserType() != userType ) {
                throw new InvalidTokenException();
            }
            if(currentTimestamp() - token.get().getLastCalledTimeStamp() > ONE_HOURS_MS){
                deleteToken(tokenId);
                throw new InvalidTokenException();
            }
            Token tokenToReturn = token.get();
            tokenToReturn.setLastCalledTimeStamp(currentTimestamp());
            return tokenToReturn;
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    private void deleteToken(String tokenId) {
        tokenRepository.deleteById(tokenId);
    }
}
