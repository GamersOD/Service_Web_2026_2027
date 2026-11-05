package rendu.georgesvalerian.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomRestaurantException {

    //gestionnaire d'exception basique, afficher un message et un code
    @ExceptionHandler(NotFoundRestaurantException.class)
    public ResponseEntity<ExceptionSkeleton> gestionRestaurantException(NotFoundRestaurantException e){
        ExceptionSkeleton skeleton = new ExceptionSkeleton();
        skeleton.setMessage(e.getMessage());
        skeleton.setCode(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(skeleton, HttpStatus.NOT_FOUND);
    }
}
