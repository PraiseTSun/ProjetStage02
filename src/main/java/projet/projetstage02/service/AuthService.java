package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.DTO.AbstractUserDTO;
import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.DTO.GestionnaireDTO;
import projet.projetstage02.DTO.StudentDTO;
import projet.projetstage02.exception.NonExistentEntityException;
import projet.projetstage02.model.*;
import projet.projetstage02.model.Token.UserTypes;
import projet.projetstage02.repository.CompanyRepository;
import projet.projetstage02.repository.GestionnaireRepository;
import projet.projetstage02.repository.StudentRepository;
import projet.projetstage02.repository.TokenRepository;

import java.util.Optional;
import java.util.UUID;

import static projet.projetstage02.model.Token.UserTypes.*;

@Service
@AllArgsConstructor
public class AuthService {
    final TokenRepository tokenRepository;

    final GestionnaireRepository gestionnaireRepository;
    final StudentRepository studentRepository;
    final CompanyRepository companyRepository;

    public <Y extends AbstractUser, T extends AbstractUserDTO<Y>> String loginIfValid(T dto)
            throws NonExistentEntityException {
        if (dto instanceof CompanyDTO companyDTO) {
            return loginIfValidCompany(companyDTO);
        } else if (dto instanceof StudentDTO studentDTO) {
            return loginIfValidStudent(studentDTO);
        } else if (dto instanceof GestionnaireDTO gestionnaireDTO) {
            return loginIfValidGestionnaire(gestionnaireDTO);
        }
        throw new NonExistentEntityException();
    }

    private String loginIfValidGestionnaire(GestionnaireDTO gestionnaireDTO) throws NonExistentEntityException {
        Optional<Gestionnaire> gestionnaire = gestionnaireRepository.findByEmailAndPassword(
                gestionnaireDTO.getEmail(), gestionnaireDTO.getPassword());
        validateOptional(gestionnaire);
        return createToken(gestionnaire.get(), GESTIONNAIRE);
    }

    private String loginIfValidStudent(StudentDTO studentDTO) throws NonExistentEntityException {
        Optional<Student> student = studentRepository.findByEmailAndPassword(
                studentDTO.getEmail(), studentDTO.getPassword());
        validateOptional(student);
        return createToken(student.get(), STUDENT);
    }

    private String loginIfValidCompany(CompanyDTO companyDTO) throws NonExistentEntityException {
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
                .build();
        tokenRepository.save(token);
        return token.getToken();
    }

    private <T extends AbstractUser> void validateOptional(Optional<T> optional) throws NonExistentEntityException {
        if (optional.isEmpty() || !optional.get().isEmailConfirmed())
            throw new NonExistentEntityException();
    }

    public Token getToken(String tokenId, UserTypes userType) throws NonExistentEntityException {
        try {
            Optional<Token> token = tokenRepository.findById(tokenId);
            if (token.isEmpty() || token.get().getUserType() != userType) {
                throw new NonExistentEntityException();
            }
            return token.get();
        } catch (Exception e) {
            throw new NonExistentEntityException();
        }
    }
}
