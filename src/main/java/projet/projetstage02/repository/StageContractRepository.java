package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.projetstage02.model.StageContract;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface StageContractRepository extends JpaRepository<StageContract, Long> {
    Optional<StageContract> findByStudentIdAndCompanyIdAndOfferId(long student, long companyId, long offerId);

    List<StageContract> findByCompanyId(long companyId);

    List<StageContract> findByStudentId(long studentId);

    Optional<StageContract> findByStudentIdAndOfferId(long student, long offerId);
}
