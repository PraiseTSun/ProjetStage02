package projet.projetstage02.dto.interview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewSelectInDTO {
    private String token;
    private long interviewId;
    private long studentId;
    private String selectedDate;
}
