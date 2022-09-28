package projet.projetstage02.exception;

public class NonExistentEntityException extends Exception {
    public NonExistentEntityException() {
        super("User does not exist");
    }
}
