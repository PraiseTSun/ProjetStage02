package projet.projetstage02.exception;

public class NonExistentUserException extends Exception {
    public NonExistentUserException() {
        super("User does not exist");
    }
}
