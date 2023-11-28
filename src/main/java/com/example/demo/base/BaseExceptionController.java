package com.example.demo.base;

import com.example.demo.exception.InternalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@RestControllerAdvice
@Log4j2
@RequiredArgsConstructor
public class BaseExceptionController {

    public static String getTrace(Exception e) {
        try {
            return String.format("%s:%s", e.getStackTrace()[0].getClassName(), e.getStackTrace()[0].getLineNumber());
        } catch (Exception ignored) {
        }
        return ":";
    }

    @ExceptionHandler({InternalException.class})
    public ResponseEntity<?> handleBusinessException(InternalException e) {
        log.error("Business Error: {}, trace: {}", e.getMessage(), getTrace(e));
        return ResponseEntity.status(e.getType()).body(new ResponseBase<>(e.getCode(), e.getMessage()));

    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleException(Exception e) {
        log.error("", e);
        return new ResponseEntity<>(new ResponseBase<>(CommonResponseCode.COMMON_ERROR.getCode(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BindException.class
    })
    public ResponseEntity<?> handleArgumentInvalidException(BindException e) {
        Map<String, List<String>> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, List.of(Optional.ofNullable(errorMessage).orElse("")));
        });

        ResponseBase<?> responseBase = new ResponseBase<>(errors);
        responseBase.setCode(CommonResponseCode.INVALID_PARAM.getCode());
        responseBase.setMessage(CommonResponseCode.INVALID_PARAM.getMessage());

        return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
        return new ResponseEntity<>(
                new ResponseBase<>(
                        CommonResponseCode.ACCESS_DENIED.getCode(),
                        CommonResponseCode.ACCESS_DENIED.getMessage()),
                HttpStatus.FORBIDDEN);
    }

}