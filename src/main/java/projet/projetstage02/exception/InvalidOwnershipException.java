package projet.projetstage02.exception;

public class InvalidOwnershipException extends Exception{
    public InvalidOwnershipException(){super("You do not own the entity needed in this operation");}
}
