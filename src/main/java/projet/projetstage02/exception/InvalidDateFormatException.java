package projet.projetstage02.exception;

public class InvalidDateFormatException extends Exception{
    public InvalidDateFormatException(){super("The date format isn't compatible with LocalDateTime");}
}
