package projet.projetstage02.exception;

public class NonExistentDepartementException extends Exception{
    public NonExistentDepartementException() {
        super("Offer does not exist");
    }
}
