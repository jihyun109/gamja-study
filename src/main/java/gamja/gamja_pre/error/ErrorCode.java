package gamja.gamja_pre.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 에러코드 정리
@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND(404, "COMMON-ERR-404", "ID not found"),
    INTER_SERVER_ERROR(500, "COMMON-ERR-500", "Internal server error"),
    Method_Argument_Not_Valid(400, "COMMON-ERR-400", "Invalid method argument");


    private int status;
    private String errorCode;
    private String message;
}
