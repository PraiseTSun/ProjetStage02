package projet.projetstage02.dto.applications;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ApplicationListDTO {
    long studentId;
    List<Long> offersId;
    List<Long> removableOffersId;
}
