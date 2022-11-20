package projet.projetstage02.exception;

public class CantRemoveApplicationException extends Exception {
    public CantRemoveApplicationException(){super("You can't remove the application because a contract already has been created!");}
}
