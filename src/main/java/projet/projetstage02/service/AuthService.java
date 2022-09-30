package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.DTO.AbstractUserDTO;
import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.DTO.GestionnaireDTO;
import projet.projetstage02.DTO.StudentDTO;
import projet.projetstage02.exception.InvalidTokenException;
import projet.projetstage02.exception.NonExistentEntityException;
import projet.projetstage02.model.*;
import projet.projetstage02.model.Token.UserTypes;
import projet.projetstage02.repository.CompanyRepository;
import projet.projetstage02.repository.GestionnaireRepository;
import projet.projetstage02.repository.StudentRepository;
import projet.projetstage02.repository.TokenRepository;

import java.time.LocalDateTime;
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

    public <Y extends AbstractUser, T extends AbstractUserDTO<Y>> String loginIfValid(T dto)
            throws InvalidTokenException {
        if (dto instanceof CompanyDTO companyDTO) {
            return loginIfValidCompany(companyDTO);
        } else if (dto instanceof StudentDTO studentDTO) {
            return loginIfValidStudent(studentDTO);
        } else if (dto instanceof GestionnaireDTO gestionnaireDTO) {
            return loginIfValidGestionnaire(gestionnaireDTO);
        }
        throw new InvalidTokenException();
    }

    private String loginIfValidGestionnaire(GestionnaireDTO gestionnaireDTO) throws InvalidTokenException {
        Optional<Gestionnaire> gestionnaire = gestionnaireRepository.findByEmailAndPassword(
                gestionnaireDTO.getEmail(), gestionnaireDTO.getPassword());
        validateOptional(gestionnaire);
        return createToken(gestionnaire.get(), GESTIONNAIRE);
    }

    private String loginIfValidStudent(StudentDTO studentDTO) throws InvalidTokenException {
        Optional<Student> student = studentRepository.findByEmailAndPassword(
                studentDTO.getEmail(), studentDTO.getPassword());
        validateOptional(student);
        return createToken(student.get(), STUDENT);
    }

    private String loginIfValidCompany(CompanyDTO companyDTO) throws InvalidTokenException {
        Optional<Company> company = companyRepository.findByEmailAndPassword(companyDTO.getEmail(),
                companyDTO.getPassword());
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
