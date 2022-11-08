package projet.projetstage02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.projetstage02.model.Interview;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByStudentId(long studentId);

    List<Interview> findByCompanyId(long companyId);
}
