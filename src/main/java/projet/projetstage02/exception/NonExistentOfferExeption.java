package projet.projetstage02.exception;

public class NonExistentOfferExeption extends Exception{
    public NonExistentOfferExeption() {
        super("Offer does not exist");
    }
}
