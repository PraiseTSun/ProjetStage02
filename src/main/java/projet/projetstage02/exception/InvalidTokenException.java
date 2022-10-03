package projet.projetstage02.exception;

public class InvalidTokenException extends Exception {
    public InvalidTokenException() {
        super("Token is invalid");
    }
}
