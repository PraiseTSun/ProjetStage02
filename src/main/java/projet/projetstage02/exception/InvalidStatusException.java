package projet.projetstage02.exception;

public class InvalidStatusException extends Exception {

    public InvalidStatusException() {
        super("The cv status is invalid");
    }
}
