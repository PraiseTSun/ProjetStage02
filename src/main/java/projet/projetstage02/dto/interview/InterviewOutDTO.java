package projet.projetstage02.dto.interview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewOutDTO {
    private long interviewId;
    private long companyId;
    private long studentId;
    private List<String> companyDateOffers;
    private String studentSelectedDate;
}