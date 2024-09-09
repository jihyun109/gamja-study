package gamja.gamja_pre.error;

import lombok.Getter;
import java.lang.RuntimeException;

@Getter
public class NotFoundException extends RuntimeException {
    private ErrorCode errorCode;
    public NotFoundException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
