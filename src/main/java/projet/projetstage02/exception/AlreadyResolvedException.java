package projet.projetstage02.exception;

public class AlreadyResolvedException extends Exception {
    public AlreadyResolvedException() {
        super("This problem is already resolved");
    }
}
