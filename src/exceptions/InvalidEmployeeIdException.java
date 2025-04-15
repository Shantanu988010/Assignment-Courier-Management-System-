package exceptions;

public class InvalidEmployeeIdException extends Exception {
    public InvalidEmployeeIdException(String message) {
        super(message);
    }
}