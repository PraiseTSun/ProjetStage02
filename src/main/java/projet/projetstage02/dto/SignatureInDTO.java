package projet.projetstage02.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignatureInDTO {
    private String token;
    private long userId;
    private long contractId;
    private String signature;
}
