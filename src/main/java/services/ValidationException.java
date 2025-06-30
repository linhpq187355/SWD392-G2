package services;

import java.util.List;

public class ValidationException extends Exception {
    private final List<String> errorMessages;

    public ValidationException(List<String> errorMessages) {
        super("Validation failed.");
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}