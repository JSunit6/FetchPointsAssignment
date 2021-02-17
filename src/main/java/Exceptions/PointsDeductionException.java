package Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PointsDeductionException extends RuntimeException {
    private static  final long serialVersionId = 1L;

    public PointsDeductionException(String msg) {
        super(msg);
    }
}
