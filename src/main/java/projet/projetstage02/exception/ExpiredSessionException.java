package projet.projetstage02.exception;

public class ExpiredSessionException extends Exception {
    public ExpiredSessionException() {
        super("This session is expired");
    }

}
