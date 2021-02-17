package Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TransactionCannotBeAddedException extends  RuntimeException{
    private static  final long serialVersionId = 1L;

    public TransactionCannotBeAddedException(String msg) {
        super(msg);
    }
}
