package gamja.gamja_pre.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice   // 모든 rest 컨트롤러에서 발생하는 exception 처리
// exception 발생 시 전역으로 처리할 exception handler
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)  // 발생한 NotFoundException에 대해서 처리하는 메서드 작성
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        log.warn("handleNotFoundException", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)    // 유효성 검사 실패시 발생하는 예외
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("Validation error occurred for request: {}", ex.getParameter().getParameterName(), ex);    // 유효성 검사 오류가 발생한 매개변수의 이름

        // 유효성 검사에서 실패한 필드와 에러 메시지 추출
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        Map<String, String> errorMap = new HashMap<>();

        // 필드 이름과 에러 메시지를 Map에 담기
        for (FieldError fieldError : fieldErrors) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        // ErrorResponse 생성 및 필드별 오류 설정
        ErrorResponse response = new ErrorResponse(ErrorCode.Method_Argument_Not_Valid);
        response.setFieldErrors(errorMap);  // 필드별 오류를 추가

        // 기본 예외 처리 (위에서 처리되지 않은 경우)
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        log.warn("handleNoResourceFoundException", ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.Method_Argument_Not_Valid);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("handleIllegalArgumentException", ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.Method_Argument_Not_Valid);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.warn("handleException", ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.INTER_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}