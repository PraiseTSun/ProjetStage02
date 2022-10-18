package projet.projetstage02.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Postulation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Min(1)
    private Long offerId;

    @Min(1)
    private Long studentId;

    public Postulation(Long offerId, Long studentId) {
        this.offerId = offerId;
        this.studentId = studentId;
    }
}
