package projet.projetstage02.exception;

public class NonExistentEntityException extends Exception {
    public NonExistentEntityException() {
        super("Entity does not exist");
    }
}
