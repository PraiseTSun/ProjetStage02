package projet.projetstage02.dto.offres;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferAcceptedStudentsDTO {
    private long offerId;
    private List<Long> studentsId;
}
