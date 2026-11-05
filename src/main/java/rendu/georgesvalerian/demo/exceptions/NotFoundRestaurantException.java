package rendu.georgesvalerian.demo.exceptions;

import lombok.experimental.StandardException;

@StandardException
public class NotFoundRestaurantException extends RuntimeException {

    public NotFoundRestaurantException(String message) {
        super(message);
    }
}
