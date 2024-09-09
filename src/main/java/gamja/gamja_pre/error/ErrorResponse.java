package gamja.gamja_pre.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// exception 발생시 응답하는 에러 정보 클래스
public class ErrorResponse {
    private int status;
    private String message;
    private String code;

    public ErrorResponse(ErrorCode errorCode){
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
        this.code = errorCode.getErrorCode();
    }
}
