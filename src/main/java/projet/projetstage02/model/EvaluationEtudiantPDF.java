package projet.projetstage02.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationEtudiantPDF extends EvaluationPDF {
    @Id
    @GeneratedValue
    private long id;
}

