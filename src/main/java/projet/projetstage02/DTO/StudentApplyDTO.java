package projet.projetstage02.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import projet.projetstage02.model.Postulation;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class StudentApplyDTO {
    long studentId;
    List<Long> offersId;
}
