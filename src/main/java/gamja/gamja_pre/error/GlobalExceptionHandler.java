package gamja.gamja_pre.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice   // 모든 rest 컨트롤러에서 발생하는 exception 처리
// exception 발생 시 전역으로 처리할 exception handler
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)  // 발생한 NotFoundException에 대해서 처리하는 메서드 작성
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex){
        log.warn("handleNotFoundException",ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex){
        log.warn("handleException",ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.INTER_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}