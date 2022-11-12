package projet.projetstage02.exception;

public class EmptySignatureException extends Exception {
    public EmptySignatureException() {
        super("Signature is empty");
    }
}
