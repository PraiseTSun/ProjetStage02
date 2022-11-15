package projet.projetstage02.service;

import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import projet.projetstage02.dto.SignatureInDTO;
import projet.projetstage02.dto.contracts.ContractsDTO;
import projet.projetstage02.dto.contracts.StageContractInDTO;
import projet.projetstage02.dto.contracts.StageContractOutDTO;
import projet.projetstage02.dto.evaluations.EvaluationInfoDTO;
import projet.projetstage02.dto.evaluations.MillieuStage.MillieuStageEvaluationInDTO;
import projet.projetstage02.dto.offres.OffreOutDTO;
import projet.projetstage02.dto.pdf.PdfOutDTO;
import projet.projetstage02.dto.users.CompanyDTO;
import projet.projetstage02.dto.users.GestionnaireDTO;
import projet.projetstage02.dto.users.Students.StudentOutDTO;
import projet.projetstage02.exception.*;
import projet.projetstage02.model.*;
import projet.projetstage02.repository.*;
import projet.projetstage02.utils.PDFCreationUtil;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static projet.projetstage02.utils.ByteConverter.byteToString;
import static projet.projetstage02.utils.TimeUtil.*;

@Service
@AllArgsConstructor
public class GestionnaireService {
    private final GestionnaireRepository gestionnaireRepository;
    private final CompanyRepository companyRepository;
    private final StudentRepository studentRepository;
    private final CvStatusRepository cvStatusRepository;
    private final OffreRepository offreRepository;
    private final StageContractRepository stageContractRepository;
    private final ApplicationAcceptationRepository applicationAcceptationRepository;
    private final EvaluationMillieuStageRepository evaluationMillieuStageRepository;
    private final EvaluationEtudiantRepository evaluationEtudiantRepository;
    private final EvaluationMillieuStagePDFRepository evaluationMillieuStagePDFRepository;
    private final EvaluationEtudiantPDFRepository evaluationEtudiantPDFRepository;

    public long saveGestionnaire(String firstname, String lastname, String email, String password) {
        GestionnaireDTO dto = GestionnaireDTO.builder()
                .firstName(firstname)
                .lastName(lastname)
                .email(email.toLowerCase())
                .password(password)
                .isConfirmed(true)
                .inscriptionTimestamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
                .build();
        return saveGestionnaire(dto);
    }

    public long saveGestionnaire(GestionnaireDTO dto) {
        return gestionnaireRepository.save(dto.toModel()).getId();
    }

    private boolean isEmailUnique(String email) {
        return gestionnaireRepository.findByEmail(email).isEmpty();
    }

    public GestionnaireDTO getGestionnaireById(Long id) throws NonExistentEntityException {
        var gestionnaireOpt = gestionnaireRepository.findById(id);
        if (gestionnaireOpt.isEmpty()) throw new NonExistentEntityException();
        return new GestionnaireDTO(gestionnaireOpt.get());
    }

    public GestionnaireDTO getGestionnaireByEmailPassword(String email, String password) throws NonExistentEntityException {
        var gestionnaireOpt = gestionnaireRepository.findByEmailAndPassword(email.toLowerCase(), password);
        if (gestionnaireOpt.isEmpty()) throw new NonExistentEntityException();
        return new GestionnaireDTO(gestionnaireOpt.get());
    }

    public void validateCompany(Long id) throws NonExistentEntityException {
        // Throws NonExistentUserException
        Company company = getCompany(id);
        company.setConfirm(true);
        companyRepository.save(company);
    }

    public void validateStudent(Long id) throws NonExistentEntityException {
        // Throws NonExistentUserException
        Student student = getStudent(id);
        student.setConfirm(true);
        studentRepository.save(student);
    }

    public void removeCompany(long id) throws NonExistentEntityException {
        // Throws NonExistentUserException
        Company company = getCompany(id);
        companyRepository.delete(company);
    }

    public void removeStudent(long id) throws NonExistentEntityException {
        // Throws NonExistentUserException
        Student student = getStudent(id);
        studentRepository.delete(student);
    }

    private Company getCompany(long id) throws NonExistentEntityException {
        Optional<Company> companyOptional = companyRepository.findById(id);
        if (companyOptional.isEmpty()) throw new NonExistentEntityException();
        return companyOptional.get();
    }

    private Student getStudent(long id) throws NonExistentEntityException {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isEmpty()) throw new NonExistentEntityException();
        return studentOptional.get();
    }

    private Offre getOffer(long id) throws NonExistentOfferExeption {
        Optional<Offre> offreOpt = offreRepository.findById(id);
        if (offreOpt.isEmpty()) throw new NonExistentOfferExeption();
        return offreOpt.get();
    }

    public List<OffreOutDTO> getUnvalidatedOffers() {
        List<OffreOutDTO> offres = new ArrayList<>();
        offreRepository.findAll().stream().
                filter(offre ->
                        !offre.isValide() && isRightSession(offre.getSession(), getNextYear()))
                .forEach(offre ->
                        offres.add(new OffreOutDTO(offre)));
        offres.forEach(offre -> offre.setPdf(byteToString(new byte[0])));
        return offres;
    }

    public List<OffreOutDTO> getValidatedOffers(int year) {
        List<OffreOutDTO> offres = new ArrayList<>();
        offreRepository.findAll().stream().
                filter(offre ->
                        offre.isValide() && isRightSession(offre.getSession(), year))
                .forEach(offre ->
                        offres.add(new OffreOutDTO(offre)));
        offres.forEach(offre -> offre.setPdf(byteToString(new byte[0])));
        return offres;
    }


    public OffreOutDTO validateOfferById(Long id) throws NonExistentOfferExeption, ExpiredSessionException {

        Offre offre = getOffer(id);
        if (!isRightSession(offre.getSession(), getNextYear())) {
            throw new ExpiredSessionException();
        }
        offre.setValide(true);
        offreRepository.save(offre);

        return new OffreOutDTO(offre);
    }

    public void removeOfferById(long id) throws NonExistentOfferExeption {
        offreRepository.delete(getOffer(id));
    }

    public List<StudentOutDTO> getUnvalidatedStudents() {
        List<StudentOutDTO> unvalidatedStudentDTOs = new ArrayList<>();
        studentRepository.findAll().stream()
                .filter(student ->
                        student.isEmailConfirmed() && !student.isConfirm()
                )
                .forEach(student ->
                        unvalidatedStudentDTOs.add(new StudentOutDTO(student)));
        return unvalidatedStudentDTOs;
    }

    public List<CompanyDTO> getUnvalidatedCompanies() {
        List<CompanyDTO> unvalidatedCompaniesDTOs = new ArrayList<>();
        companyRepository.findAll().stream()
                .filter(company ->
                        !company.isConfirm() && company.isEmailConfirmed()
                )
                .forEach(company ->
                        unvalidatedCompaniesDTOs.add(new CompanyDTO(company)));
        return unvalidatedCompaniesDTOs;
    }

    public PdfOutDTO getOffrePdfById(long id) throws NonExistentOfferExeption {
        @NotNull byte[] offer = getOffer(id).getPdf();
        String offreString = byteToString(offer);
        return PdfOutDTO.builder().pdf(offreString).build();
    }

    public List<StudentOutDTO> getUnvalidatedCVStudents() {
        List<StudentOutDTO> studentDTOS = new ArrayList<>();

        studentRepository.findAll().stream()
                .filter(student ->
                        student.getCvToValidate() != null &&
                                student.getCvToValidate().length > 0
                                && student.isConfirm()
                )
                .forEach(student -> {
                    StudentOutDTO dto = new StudentOutDTO(student);
                    dto.setPassword("");
                    dto.setCv("");

                    studentDTOS.add(dto);
                });

        return studentDTOS;
    }

    public StudentOutDTO validateStudentCV(long id) throws NonExistentEntityException, InvalidStatusException {
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();
        Student student = studentOpt.get();
        Optional<CvStatus> cvStatusOpt = cvStatusRepository.findById(student.getId());
        if (cvStatusOpt.isEmpty()) {
            throw new NonExistentEntityException();
        }
        CvStatus cvStatus = cvStatusOpt.get();
        if (!cvStatus.getStatus().equals("PENDING")) {
            throw new InvalidStatusException();
        }
        cvStatus.setStatus("ACCEPTED");
        student.setCv(student.getCvToValidate());
        cvStatus.setRefusalMessage("");
        student.setCvToValidate(new byte[0]);
        studentRepository.save(student);
        cvStatusRepository.save(cvStatus);
        return new StudentOutDTO(student);
    }

    public StudentOutDTO removeStudentCvValidation(long id, String refusalReason) throws NonExistentEntityException, InvalidStatusException {
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();
        Optional<CvStatus> cvStatusOpt = cvStatusRepository.findById(id);
        if (cvStatusOpt.isEmpty()) {
            throw new NonExistentEntityException();
        }
        CvStatus cvStatus = cvStatusOpt.get();
        if (!cvStatus.getStatus().equals("PENDING")) {
            throw new InvalidStatusException();
        }
        cvStatus.setStatus("REFUSED");
        cvStatus.setRefusalMessage(refusalReason);
        Student student = studentOpt.get();
        student.setCvToValidate(new byte[0]);
        studentRepository.save(student);
        return new StudentOutDTO(student);
    }

    public PdfOutDTO getStudentCvToValidate(long studentId) throws NonExistentEntityException {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();
        byte[] cv = studentOpt.get().getCvToValidate();
        String cvConvert = byteToString(cv);
        return new PdfOutDTO(studentOpt.get().getId(), cvConvert);
    }

    private boolean deleteUnconfirmedGestionnaire(String email) throws NonExistentEntityException {
        Optional<Gestionnaire> studentOpt = gestionnaireRepository.findByEmail(email);
        if (studentOpt.isEmpty())
            throw new NonExistentEntityException();
        Gestionnaire gestionnaire = studentOpt.get();
        if (currentTimestamp() - gestionnaire.getInscriptionTimestamp() > MILLI_SECOND_DAY) {
            gestionnaireRepository.delete(gestionnaire);
            return true;
        }
        return false;
    }

    public boolean isGestionnaireInvalid(String email) throws NonExistentEntityException {
        return !isEmailUnique(email)
                && !deleteUnconfirmedGestionnaire(email);
    }

    public StageContractOutDTO createStageContract(StageContractInDTO contract)
            throws NonExistentEntityException, NonExistentOfferExeption, AlreadyExistingStageContractException {
        Optional<Student> studentOpt = studentRepository.findById(contract.getStudentId());
        if (studentOpt.isEmpty()) throw new NonExistentEntityException();
        Student student = studentOpt.get();

        Optional<Offre> offerOpt = offreRepository.findById(contract.getOfferId());
        if (offerOpt.isEmpty()) throw new NonExistentOfferExeption();
        Offre offer = offerOpt.get();

        Optional<Company> companyOpt = companyRepository.findById(offer.getIdCompagnie());
        if (companyOpt.isEmpty()) throw new NonExistentEntityException();
        Company company = companyOpt.get();

        Optional<StageContract> stageContractOpt
                = stageContractRepository.findByStudentIdAndCompanyIdAndOfferId(student.getId(), company.getId(), offer.getId());
        if (stageContractOpt.isPresent()) throw new AlreadyExistingStageContractException();

        StageContract stageContract = StageContract.builder()
                .studentId(student.getId())
                .offerId(offer.getId())
                .companyId(company.getId())
                .session(offer.getSession())
                .description(getContractDescription(student, offer, company))
                .build();

        Optional<ApplicationAcceptation> application
                = applicationAcceptationRepository.findByOfferIdAndStudentId(offer.getId(), student.getId());
        if (application.isEmpty()) throw new NonExistentEntityException();
        applicationAcceptationRepository.delete(application.get());
        stageContract = stageContractRepository.save(stageContract);
        return new StageContractOutDTO(stageContract);
    }

    private String getContractDescription(Student student, Offre offer, Company company) {
        return company.getCompanyName() + " a accepté " + student.getFirstName() + " "
                + student.getLastName() + " Pour le role de " + offer.getPosition() + " pour la session "
                + offer.getSession() + ".";
    }

    public ContractsDTO getContractsToCreate() {
        ContractsDTO contractsDTO = new ContractsDTO();

        List<ApplicationAcceptation> all = applicationAcceptationRepository.findAll();
        all
                .forEach(application -> {

                    Optional<Offre> offerOpt = offreRepository.findById(application.getOfferId());
                    if (offerOpt.isEmpty()) return;
                    Offre offer = offerOpt.get();
                    Optional<Company> companyOpt = companyRepository.findById(offer.getIdCompagnie());
                    if (companyOpt.isEmpty()) return;
                    Optional<Student> studentOpt = studentRepository.findById(application.getStudentId());
                    if (studentOpt.isEmpty()) return;

                    Student student = studentOpt.get();
                    Company company = companyOpt.get();
                    Offre offre = offerOpt.get();

                    StageContractOutDTO stageContractOutDTO = StageContractOutDTO.builder()
                            .companyId(company.getId())
                            .offerId(offre.getId())
                            .description(getContractDescription(student, offre, company))
                            .companyName(company.getCompanyName())
                            .position(offre.getPosition())
                            .employFullName(company.getFirstName() + " " + company.getLastName())
                            .studentId(student.getId())
                            .studentFullName(student.getFirstName() + " " + student.getLastName())
                            .build();
                    contractsDTO.add(stageContractOutDTO);
                });
        return contractsDTO;
    }


    public EvaluationInfoDTO getEvaluationInfoForContract(long contractId) throws NonExistentOfferExeption, NonExistentEntityException {
        Optional<StageContract> optional = stageContractRepository.findById(contractId);
        if (optional.isEmpty()) {
            throw new NonExistentEntityException();
        }
        StageContract stageContract = optional.get();
        Optional<Offre> offreOptional = offreRepository.findById(stageContract.getOfferId());
        if (offreOptional.isEmpty()) {
            throw new NonExistentOfferExeption();
        }
        Offre offre = offreOptional.get();
        Optional<Student> optionalStudent = studentRepository.findById(stageContract.getStudentId());
        if (optionalStudent.isEmpty()) {
            throw new NonExistentEntityException();
        }
        Student student = optionalStudent.get();
        Optional<Company> optionalCompany = companyRepository.findById(stageContract.getCompanyId());
        if (optionalCompany.isEmpty()) {
            throw new NonExistentEntityException();
        }
        Company company = optionalCompany.get();
        return new EvaluationInfoDTO(company, offre, student);
    }

    public long evaluateStage(MillieuStageEvaluationInDTO millieuStageEvaluationInDTO) {
        EvaluationMillieuStage evaluationMillieuStage = new EvaluationMillieuStage(millieuStageEvaluationInDTO);
        EvaluationMillieuStage save = evaluationMillieuStageRepository.save(evaluationMillieuStage);
        return save.getId();
    }

    public ContractsDTO getContractsToEvaluateMillieuStage() {
        List<StageContractOutDTO> contracts = new ArrayList<>();
        stageContractRepository.findAll().stream().filter(
                stageContract -> evaluationMillieuStageRepository.findByContractId(stageContract.getId()).isEmpty()
        ).forEach(stageContract -> {
            Optional<Company> companyOptional = companyRepository.findById(stageContract.getCompanyId());
            Optional<Student> studentOptional = studentRepository.findById(stageContract.getStudentId());
            Optional<Offre> offreOptional = offreRepository.findById(stageContract.getOfferId());
            StageContractOutDTO stageContractOutDTO = new StageContractOutDTO(stageContract);
            if (offreOptional.isEmpty()) {
                return;
            }

            if (companyOptional.isEmpty()) {
                return;
            }


            if (studentOptional.isEmpty()) {
                return;
            }

            Company company = companyOptional.get();
            stageContractOutDTO.setEmployFullName(company.getFirstName() + " " + company.getLastName());
            stageContractOutDTO.setCompanyName(company.getCompanyName());

            Offre offre = offreOptional.get();
            stageContractOutDTO.setPosition(offre.getPosition());

            Student student = studentOptional.get();
            stageContractOutDTO.setStudentFullName(student.getFirstName() + " " + student.getLastName());

            contracts.add(stageContractOutDTO);
        });
        return ContractsDTO.builder().contracts(contracts).build();
    }

    public void createEvaluationMillieuStagePDF(long contractId) throws NonExistentEntityException, NonExistentOfferExeption, DocumentException, EmptySignatureException {
        Optional<EvaluationMillieuStage> optional = evaluationMillieuStageRepository.findByContractId(contractId);
        if (optional.isEmpty()) {
            throw new NonExistentEntityException();
        }
        EvaluationMillieuStage evaluationMillieuStage = optional.get();
        EvaluationInfoDTO evaluationInfoDTO =
                getEvaluationInfoForContract(contractId);

        evaluationMillieuStagePDFRepository.save(EvaluationPDF.builder()
                .pdf(PDFCreationUtil.createPDFFromMap("Évaluation du millieu stage",
                        evaluationMillieuStageToMap(evaluationInfoDTO, evaluationMillieuStage)))
                .contractId(contractId)
                .build());

    }

    private Map<String, Map<String, String>> evaluationMillieuStageToMap(EvaluationInfoDTO evaluationInfoDTO,
                                                                         EvaluationMillieuStage evaluationMillieuStage) throws EmptySignatureException {
        Map<String, Map<String, String>> map = new LinkedHashMap<>();
        Map<String, String> companyInfo = new LinkedHashMap<>();
        Map<String, String> studentInfo = new LinkedHashMap<>();
        Map<String, String> offerInfo = new LinkedHashMap<>();
        Map<String, String> evaluationParagraph = new LinkedHashMap<>();
        Map<String, String> commentaires = new LinkedHashMap<>();
        Map<String, String> signPara = new LinkedHashMap<>();

        studentInfo.put("Nom de l'étudiant", evaluationInfoDTO.getNomEtudiant());
        studentInfo.put("Prénom de l'étudiant", evaluationInfoDTO.getPrenomEtudiant());
        studentInfo.put("Email de l'étudiant", evaluationInfoDTO.getEmailEtudiant());

        companyInfo.put("Nom de la compagnie", evaluationInfoDTO.getNomCompagnie());
        companyInfo.put("Nom de la personne contact", evaluationInfoDTO.getNomContact());
        companyInfo.put("Prénom de la personne contact", evaluationInfoDTO.getPrenomContact());
        companyInfo.put("Adresse de la compagnie", evaluationInfoDTO.getAdresse());
        companyInfo.put("Département", evaluationInfoDTO.getDepartement());
        companyInfo.put("Email de la compagnie", evaluationInfoDTO.getEmailCompagnie());

        offerInfo.put("Titre du poste", evaluationInfoDTO.getPoste());
        offerInfo.put("Session", evaluationInfoDTO.getSession());
        offerInfo.put("Date de début", evaluationInfoDTO.getDateStageDebut());
        offerInfo.put("Date de fin", evaluationInfoDTO.getDateStageFin());
        offerInfo.put("Nombre d'heures par semaine", evaluationInfoDTO.getHeureParSemaine() + " heures");
        offerInfo.put("Salaire", evaluationInfoDTO.getSalaire() + " $/h");


        evaluationParagraph.put("Les taches confiées sont celles annoncées dans l'entente de stage",
                fieldToText(evaluationMillieuStage.getTachesAnnonces()));
        evaluationParagraph.put("Des mesures d'accueil facilitent l'intégration du nouveau stagiaire",
                fieldToText(evaluationMillieuStage.getIntegration()));
        evaluationParagraph.put("Le temps réel consacré à l'encadrement du stagiaire est suffisant",
                fieldToText(evaluationMillieuStage.getTempsReelConsacre()));
        evaluationParagraph.put("Nombre d'heures pour le premier mois",
                evaluationMillieuStage.getHeureTotalPremierMois() + " heures");
        evaluationParagraph.put("Nombre d'heures pour le deuxième mois",
                evaluationMillieuStage.getHeureTotalDeuxiemeMois() + " heures");
        evaluationParagraph.put("Nombre d'heures pour le troisième mois",
                evaluationMillieuStage.getHeureTotalTroisiemeMois() + " heures");
        evaluationParagraph.put("L'environnement de travail respecte les normes de sécurité",
                fieldToText(evaluationMillieuStage.getEnvironementTravail()));
        evaluationParagraph.put("Le climat de travail est agréable",
                fieldToText(evaluationMillieuStage.getClimatTravail()));
        evaluationParagraph.put("Le millieu de stage est accessible par transport en commun",
                fieldToText(evaluationMillieuStage.getMilieuDeStage()));
        evaluationParagraph.put("La communication avec le superviseur est efficace",
                fieldToText(evaluationMillieuStage.getCommunicationAvecSuperviser()));
        evaluationParagraph.put("L'équipement de travail est adéquat",
                fieldToText(evaluationMillieuStage.getEquipementFourni()));
        evaluationParagraph.put("Le volume de travail est raisonnable",
                fieldToText(evaluationMillieuStage.getVolumeDeTravail()));

        commentaires.put("", evaluationMillieuStage.getCommentaires());

        signPara.put("Signé le", evaluationMillieuStage.getDateSignature().toString());
        map.put("Information sur la compagnie", companyInfo);
        map.put("Information sur l'étudiant", studentInfo);
        map.put("Information sur l'offre de stage", offerInfo);
        map.put("Évaluation", evaluationParagraph);
        if (evaluationMillieuStage.getCommentaires() != null && !evaluationMillieuStage.getCommentaires().isEmpty()) {
            map.put("Commentaires", commentaires);
        }
        map.put("Signature", signPara);
        map.put("_signature_", getSignature(evaluationMillieuStage.getSignature()));
        return map;
    }

    private Map<String, String> getSignature(String signature) throws EmptySignatureException {
        String[] split = signature.split(",");
        if (split.length != 2) {
            throw new EmptySignatureException();
        }
        signature = split[1];
        if (signature.length() < 2) throw new EmptySignatureException();
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedByteArray = decoder.decode(signature);
        String bytes = byteToString(decodedByteArray);
        Map<String, String> map = new LinkedHashMap<>();
        map.put("signature", bytes);
        return map;
    }

    public PdfOutDTO getEvaluationMillieuStagePDF(long id) throws NonExistentEntityException {
        Optional<EvaluationPDF> optional = evaluationMillieuStagePDFRepository.findById(id);
        if (optional.isEmpty()) throw new NonExistentEntityException();
        EvaluationPDF evaluationPDF = optional.get();
        return new PdfOutDTO(evaluationPDF.getContractId(), evaluationPDF.getPdf());
    }

    public List<StageContractOutDTO> getEvaluationMillieuStage() {
        List<StageContract> stageContracts = stageContractRepository.findAll();
        return stageContracts.stream().filter(stageContract -> {
            Optional<EvaluationMillieuStage> opt = evaluationMillieuStageRepository.findByContractId(stageContract.getId());
            return opt.isPresent();
        }).map(StageContractOutDTO::new).toList();
    }

    private String fieldToText(String original) {
        return switch (original) {
            case "totalementEnAccord" -> "Totalement en accord";
            case "plutotEnAccord" -> "Plutôt en accord";
            case "plutotEnDesaccord" -> "Plutôt en désaccord";
            case "totalementEnDesaccord" -> "Totalement en désaccord";
            case "impossibleDeSePrononcer" -> "Impossible de se prononcer";
            case "oui" -> "Oui";
            case "non" -> "Non";
            case "peutEtre" -> "Peut-être";
            case "depassentBeacoupAttentes" -> "Dépassent beaucoup les attentes";
            case "depassentAttentes" -> "Dépassent les attentes";
            case "repondentAttentes" -> "Répondent aux attentes";
            case "repondentPartiellementAttentes" -> "Répondent partiellement aux attentes";
            case "repondentPasAttentes" -> "Ne répondent pas aux attentes";
            default -> original;
        };
    }

    public List<StageContractOutDTO> getContractsToSigne() {
        List<StageContractOutDTO> contractsDTO = new ArrayList<>();

        stageContractRepository.findAll()
                .stream()
                .filter(stageContract ->
                        !stageContract.getCompanySignature().isBlank()
                                && !stageContract.getStudentSignature().isBlank()
                                && stageContract.getGestionnaireSignature().isBlank()
                )
                .forEach(stageContract -> contractsDTO.add(new StageContractOutDTO(stageContract)));

        return contractsDTO;
    }

    public List<StageContractOutDTO> getEvaluatedContractsEtudiants() {
        return stageContractRepository.findAll().stream().filter(
                stageContract -> evaluationEtudiantRepository.findByContractId(stageContract.getId()).isPresent()
        ).map(StageContractOutDTO::new).toList();
    }

    public PdfOutDTO getEvaluationPDFEtudiant(long contractID) throws NonExistentEntityException {
        Optional<EvaluationPDF> optional = evaluationEtudiantPDFRepository.findById(contractID);
        if (optional.isEmpty()) throw new NonExistentEntityException();
        EvaluationPDF evaluationPDF = optional.get();
        return PdfOutDTO.builder().id(contractID).pdf(evaluationPDF.getPdf()).build();
    }

    public void createEvaluationEtudiantPDF(long contractId)
            throws NonExistentEntityException, NonExistentOfferExeption, DocumentException, EmptySignatureException {
        Optional<EvaluationEtudiant> optional = evaluationEtudiantRepository.findByContractId(contractId);
        if (optional.isEmpty()) {
            throw new NonExistentEntityException();
        }
        Optional<StageContract> opt = stageContractRepository.findById(contractId);
        if (opt.isEmpty()) throw new NonExistentEntityException();
        EvaluationEtudiant evaluationMillieuStage = optional.get();
        evaluationMillieuStagePDFRepository.save(EvaluationPDF.builder()
                .pdf(PDFCreationUtil.createPDFFromMap("Évaluation de l'étudiant",
                        evaluationEtudiantToMap(evaluationMillieuStage, getEvaluationInfoForContract(contractId))))
                .contractId(contractId)
                .build());

    }

    private Map<String, Map<String, String>> evaluationEtudiantToMap(EvaluationEtudiant evaluation, EvaluationInfoDTO infoDTO)
            throws EmptySignatureException {
        Map<String, Map<String, String>> map = new LinkedHashMap<>();
        Map<String, String> studentInfo = new LinkedHashMap<>();
        Map<String, String> companyInfo = new LinkedHashMap<>();
        Map<String, String> offerInfo = new LinkedHashMap<>();
        Map<String, String> productivite = new LinkedHashMap<>();
        Map<String, String> qualiteDuTravail = new LinkedHashMap<>();
        Map<String, String> qualiteRelationInterpesonnelles = new LinkedHashMap<>();
        Map<String, String> habiletesPersonnelles = new LinkedHashMap<>();
        Map<String, String> appreciationGlobale = new LinkedHashMap<>();
        Map<String, String> prochainStage = new LinkedHashMap<>();
        Map<String, String> signPara = new LinkedHashMap<>();

        studentInfo.put("Nom de l'étudiant", infoDTO.getNomEtudiant());
        studentInfo.put("Prénom de l'étudiant", infoDTO.getPrenomEtudiant());
        studentInfo.put("Email de l'étudiant", infoDTO.getEmailEtudiant());

        companyInfo.put("Nom de la compagnie", infoDTO.getNomCompagnie());
        companyInfo.put("Nom de la personne contact", infoDTO.getNomContact());
        companyInfo.put("Prénom de la personne contact", infoDTO.getPrenomContact());
        companyInfo.put("Adresse de la compagnie", infoDTO.getAdresse());
        companyInfo.put("Département", infoDTO.getDepartement());
        companyInfo.put("Email de la compagnie", infoDTO.getEmailCompagnie());

        offerInfo.put("Titre du poste", infoDTO.getPoste());
        offerInfo.put("Session", infoDTO.getSession());
        offerInfo.put("Date de début", infoDTO.getDateStageDebut());
        offerInfo.put("Date de fin", infoDTO.getDateStageFin());
        offerInfo.put("Nombre d'heures par semaine", infoDTO.getHeureParSemaine() + " heures");
        offerInfo.put("Salaire", infoDTO.getSalaire() + " $/h");

        productivite.put("Planifier son travail efficacement", fieldToText(evaluation.getTravailEfficace()));
        productivite.put("Comprendre rapidement les directives", fieldToText(evaluation.getComprendRapidement()));
        productivite.put("Rythme de travail soutenu", fieldToText(evaluation.getRythmeSoutenu()));
        productivite.put("Établir ses prioritées", fieldToText(evaluation.getEtablirPriorites()));
        productivite.put("Respect des echeances", fieldToText(evaluation.getRespecteEcheances()));
        productivite.put("Commentaires", fieldToText(evaluation.getCommentairesProductivite()));

        qualiteDuTravail.put("Respect des mandats demandés", fieldToText(evaluation.getRespecteMandatsDemandes()));
        qualiteDuTravail.put("Attention aux détails", fieldToText(evaluation.getAttentionAuxDetails()));
        qualiteDuTravail.put("Revérifier son travail", fieldToText(evaluation.getDoubleCheckTravail()));
        qualiteDuTravail.put("Rechercher des occasions pour se perfectionner", fieldToText(evaluation.getOccasionsDePerfectionnement()));
        qualiteDuTravail.put("Bonne analyse des problèmes rencontrés", fieldToText(evaluation.getBonneAnalyseProblemes()));
        qualiteDuTravail.put("Commentaires", evaluation.getCommentairesQualite());

        qualiteRelationInterpesonnelles.put("Planifie son travail efficacement", fieldToText(evaluation.getPlanifieTravail()));
        qualiteRelationInterpesonnelles.put("Etabli facilement des contacts avec les gens", fieldToText(evaluation.getContactsFaciles()));
        qualiteRelationInterpesonnelles.put("Contribue au travail d'équipe", fieldToText(evaluation.getTravailEnEquipe()));
        qualiteRelationInterpesonnelles.put("S'adapte facilement a la culture d'entreprise", fieldToText(evaluation.getAdapteCulture()));
        qualiteRelationInterpesonnelles.put("Accepte les critiques constructives", fieldToText(evaluation.getAccepteCritiques()));
        qualiteRelationInterpesonnelles.put("Respecte les autres", fieldToText(evaluation.getRespecteAutres()));
        qualiteRelationInterpesonnelles.put("Essaie de comprendre le point de vue des autres", fieldToText(evaluation.getEcouteActiveComprendrePDVautre()));
        qualiteRelationInterpesonnelles.put("Commentaires", evaluation.getCommentairesRelationsInterpersonnelles());


        habiletesPersonnelles.put("Démontre de l'interet de la motivation", fieldToText(evaluation.getInteretMotivation()));
        habiletesPersonnelles.put("Exprime clairement ses idées", fieldToText(evaluation.getExprimeIdees()));
        habiletesPersonnelles.put("Fait preuve d'initiative", fieldToText(evaluation.getInitiative()));
        habiletesPersonnelles.put("Travail de façon sécuritaire", fieldToText(evaluation.getTravailSecuritaire()));
        habiletesPersonnelles.put("Bon sens des responsabilitées et d'autonomie", fieldToText(evaluation.getResponsableAutonome()));
        habiletesPersonnelles.put("Ponctuel", fieldToText(evaluation.getPonctuel()));
        habiletesPersonnelles.put("Commentaires", evaluation.getCommentairesHabilites());

        appreciationGlobale.put("Habilitées démontrées", fieldToText(evaluation.getHabiletesDemontres()));
        appreciationGlobale.put("Commentaires", evaluation.getCommentairesAppreciation());
        appreciationGlobale.put("Evaluation discutée avec le stagiaire", fieldToText(evaluation.getDiscuteAvecStagiaire()));
        appreciationGlobale.put("Heures d'encadrement par semaine", evaluation.getHeuresEncadrement() + " heures");

        prochainStage.put("Souhaite un autre stage avec cet etudiant", fieldToText(evaluation.getAcueillirPourProchainStage()));
        prochainStage.put("Formation suffisante?", evaluation.getFormationTechniqueSuffisante());

        signPara.put("Signé le", evaluation.getDateSignature().toString());

        map.put("Information sur la compagnie", companyInfo);
        map.put("Information sur l'étudiant", studentInfo);
        map.put("Information sur l'offre", offerInfo);
        map.put("Productivité", productivite);
        map.put("Qualité du travail", qualiteDuTravail);
        map.put("Qualité des relations interpersonnelles", qualiteRelationInterpesonnelles);
        map.put("Habiletées personnelles", habiletesPersonnelles);
        map.put("Appréciation globale du stagiaire", appreciationGlobale);
        map.put("Prochain stage", prochainStage);
        map.put("Signature", signPara);
        map.put("_signature_", getSignature(evaluation.getSignature()));
        return map;
    }

    public StageContractOutDTO contractSignature(SignatureInDTO signature)
            throws NonExistentEntityException, NotReadyToBeSignedException {
        Optional<Gestionnaire> gestionnaireOpt = gestionnaireRepository.findById(signature.getUserId());
        if (gestionnaireOpt.isEmpty()) throw new NonExistentEntityException();

        Optional<StageContract> contractOpt = stageContractRepository.findById(signature.getContractId());
        if (contractOpt.isEmpty()) throw new NonExistentEntityException();
        StageContract contract = contractOpt.get();

        if (contract.getCompanySignature().isBlank() || contract.getStudentSignature().isBlank())
            throw new NotReadyToBeSignedException();

        contract.setGestionnaireSignature(signature.getSignature());
        contract.setGestionnaireSignatureDate(LocalDateTime.now());
        stageContractRepository.save(contract);

        return new StageContractOutDTO(contract);
    }
}
