package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.DTO.CompanyDTO;
import projet.projetstage02.DTO.OffreDTO;
import projet.projetstage02.exception.NonExistentUserException;
import projet.projetstage02.model.AbstractUser;
import projet.projetstage02.model.Offre;
import projet.projetstage02.repository.CompanyRepository;
import projet.projetstage02.repository.OffreRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final OffreRepository offreRepository;

    public long createOffre(OffreDTO offreDTO){
        Offre offre = new Offre(offreDTO.getNomDeCompagnie(), offreDTO.getDepartment(), offreDTO.getPosition(),
                offreDTO.getHeureParSemaine(), offreDTO.getAdresse(), offreDTO.getPdf());
        return offreRepository.save(offre).getId();
    }
    public List<Offre> findOffre(){
        return offreRepository.findAll();
    }

    public void saveCompany(String firstName, String lastName, String name, String email, String password,
                            AbstractUser.Department department) {
        CompanyDTO dto = new CompanyDTO(
                firstName,
                lastName,
                email.toLowerCase(),
                password,
                false,
                Timestamp.valueOf(LocalDateTime.now()).getTime(),
                false,
                department.departement,
                name);
        saveCompany(dto);
    }

    public long saveCompany(CompanyDTO dto) {
        return companyRepository.save(dto.getClassOrigin()).getId();
    }

    public boolean isEmailUnique(String email) {
        return companyRepository.findByEmail(email).isEmpty();
    }

    public CompanyDTO getCompanyById(Long id) throws NonExistentUserException {
        var companyOpt = companyRepository.findById(id);
        if (companyOpt.isEmpty()) throw new NonExistentUserException();
        return new CompanyDTO(companyOpt.get());
    }

    public CompanyDTO getCompanyByEmailPassword(String email, String password) throws NonExistentUserException {
        var companyOpt = companyRepository.findByEmailAndPassword(email.toLowerCase(), password);
        if (companyOpt.isEmpty()) throw new NonExistentUserException();
        return new CompanyDTO(companyOpt.get());
    }

    public List<CompanyDTO> getUnvalidatedUsers() {
        List<CompanyDTO> unvalidatedCompaniesDTOs = new ArrayList<>();
        companyRepository.findAll().stream()
                .filter(company->
                        !company.isConfirm() && company.isEmailConfirmed()
                )
                .forEach(company ->
                        unvalidatedCompaniesDTOs.add(new CompanyDTO(company)));
        return unvalidatedCompaniesDTOs;
    }
}
