package projet.projetstage02.dto.interview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.projetstage02.model.Interview;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewOutDTO {
    private long interviewId;
    private long companyId;
    private long offerId;
    private long studentId;
    private List<String> companyDateOffers;
    private String studentSelectedDate;

    public InterviewOutDTO(Interview interview){
        interviewId = interview.getId();
        companyId = interview.getCompanyId();
        offerId = interview.getOfferId();
        studentId = interview.getStudentId();
        companyDateOffers = new ArrayList<>();
        studentSelectedDate = interview.getStudentSelectedDate() == null
                ? "" : interview.getStudentSelectedDate().toString();
        interview.getCompanyDateOffers().forEach(localDateTime -> companyDateOffers.add(localDateTime.toString()));
    }
}