package projet.projetstage02.dto.pdf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PdfOutDTO {
    private long id;
    private String pdf;
}
