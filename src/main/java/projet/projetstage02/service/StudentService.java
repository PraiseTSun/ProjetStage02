package projet.projetstage02.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.DTO.*;
import projet.projetstage02.exception.AlreadyExistingPostulation;
import projet.projetstage02.exception.NonExistentEntityException;
import projet.projetstage02.model.Offre;
import projet.projetstage02.model.Postulation;
import projet.projetstage02.model.Student;
import projet.projetstage02.repository.OffreRepository;
import projet.projetstage02.repository.PostulationRepository;
import projet.projetstage02.repository.StudentRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static projet.projetstage02.model.AbstractUser.*;
import static projet.projetstage02.utils.TimeUtil.MILLI_SECOND_DAY;
import static projet.projetstage02.utils.TimeUtil.currentTimestamp;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final OffreRepository offreRepository;
    private final PostulationRepository postulationRepository;
    public void saveStudent(String firstName,
                            String lastName,
                            String email,
                            String password,
                            Department department) {
        StudentDTO dto = StudentDTO.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email.toLowerCase())
                .password(password)
                .department(department.departement)
                .isConfirmed(false)
                .emailConfirmed(false)
                .inscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
                .build();
        saveStudent(dto);
    }

    public long saveStudent(StudentDTO dto) {
        return studentRepository.save(dto.toModel()).getId();
    }

    public boolean isEmailUnique(String email) {
        return studentRepository.findByEmail(email).isEmpty();
    }

    public StudentDTO getStudentById(Long id) throws NonExistentEntityException {
        var studentOpt = studentRepository.findById(id);
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();
        return new StudentDTO(studentOpt.get());
    }

    public StudentDTO getStudentByEmailPassword(String email, String password) throws NonExistentEntityException {
        var studentOpt = studentRepository.findByEmailAndPassword(email.toLowerCase(), password);
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();
        return new StudentDTO(studentOpt.get());
    }

    public StudentDTO uploadCurriculumVitae(PdfDTO dto) throws NonExistentEntityException {
        Student student = getStudentById(dto.getStudentId()).toModel();
        student.setCvToValidate(dto.getPdf());
        saveStudent(new StudentDTO(student));
        return new StudentDTO(student);
    }

    public boolean deleteUnconfirmedStudent(StudentDTO dto) throws NonExistentEntityException {
        Optional<Student> studentOpt = studentRepository.findByEmail(dto.getEmail());
        if(studentOpt.isEmpty()) throw new NonExistentEntityException();
        Student student = studentOpt.get();
        if(currentTimestamp() - student.getInscriptionTimestamp() > MILLI_SECOND_DAY){
             studentRepository.delete(student);
             return true;
        }
        return false;
    }

    public List<OffreDTO> getOffersByStudentDepartment(long id) throws NonExistentEntityException {
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();
        Department department = studentOpt.get().getDepartment();

        List<OffreDTO> offers = new ArrayList<>();
        offreRepository.findAll().stream().
                filter(offre ->
                    offre.isValide()
                    && offre.getDepartment().equals(department)
                )
                .forEach(offre -> {
                    OffreDTO dto = new OffreDTO(offre);
                    dto.setPdf(new byte[0]);
                    offers.add(dto);
                });

        return offers;
    }

    public PdfOutDTO getOfferById(long id) throws NonExistentEntityException {
        Optional<Offre> offerOpt = offreRepository.findById(id);
        if(offerOpt.isEmpty()) throw new NonExistentEntityException();
        Offre offre = offerOpt.get();
        String cv = Arrays.toString(offre.getPdf()).replaceAll("\\s+","");
        return new PdfOutDTO(offre.getId(), cv);
    }

    public PostulOutDTO createPostulation(long studentId, long offerID) throws NonExistentEntityException, AlreadyExistingPostulation {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();

        Optional<Offre> offerOpt = offreRepository.findById(offerID);
        if (offerOpt.isEmpty()) throw new NonExistentEntityException();

        Optional<Postulation> postulationOpt = postulationRepository.findByStudentIdAndOfferId(studentId, offerID);
        if(!postulationOpt.isEmpty()) throw new AlreadyExistingPostulation();

        Student student = studentOpt.get();
        Offre offer = offerOpt.get();

        Postulation postulation = new Postulation(offer.getId(), student.getId());
        postulationRepository.save(postulation);

        return PostulOutDTO.builder()
                .studentId(student.getId())
                .fullName(student.getFirstName() + " " + student.getLastName())
                .offerId(offer.getId())
                .company(offer.getNomDeCompagnie())
                .build();
    }

    public StudentApplysDTO getPostulsOfferId (long studentId) throws NonExistentEntityException {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if(studentOpt.isEmpty()) throw new NonExistentEntityException();

        List<Long> offersId = new ArrayList<>();
        postulationRepository.findByStudentId(studentId)
            .forEach(
                postulation -> offersId.add(postulation.getOfferId())
            );

        return StudentApplysDTO.builder()
                .studentId(studentOpt.get().getId())
                .offersId(offersId)
                .build();
    }
}
