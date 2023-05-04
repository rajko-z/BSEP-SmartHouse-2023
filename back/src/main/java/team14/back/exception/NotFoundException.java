package team14.back.exception;

import java.util.function.Supplier;

public class NotFoundException extends AppException {

    public NotFoundException(String message) {
        super(message);
    }

}
