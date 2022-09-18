package projet.projetstage02.service;

import org.springframework.stereotype.Component;
import projet.projetstage02.DTO.OffreDTO;
import projet.projetstage02.modele.Offre;
import projet.projetstage02.repository.OffreRepository;

import java.util.List;

@Component
public class OffreService {
    private OffreRepository offreRepository;

    public OffreService(OffreRepository offreRepository) {
        this.offreRepository = offreRepository;
    }

    public long createOffre(OffreDTO offreDTO){

        Offre offre = new Offre(offreDTO.getNomDeCompagie(), offreDTO.getDepartment(), offreDTO.getPosition(),
                offreDTO.getHeureParSemaine(), offreDTO.getAdresse(), offreDTO.getPdf());
        return offreRepository.save(offre).getId();

    }
    public List<Offre> findOffre(){
        return offreRepository.findAll();
    }
}
