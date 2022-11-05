package projet.projetstage02.dto.interview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInterviewDTO {
    private String token;
    private long companyId;
    private long offerId;
    private long studentId;
    private List<String> companyDateOffers;
}