package rendu.georgesvalerian.demo.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExceptionSkeleton {
    private String message;
    private int code;
}
