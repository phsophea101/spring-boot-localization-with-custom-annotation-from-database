package com.sample.spring.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


//@RestControllerAdvice
@Slf4j
public class RestExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> responseException(Exception ex) {
//        log.error("Exception {}", ex.getMessage(), ex);
//        return new ResponseEntity<>(new ResponseVOBuilder<>().fail().error(new ErrorVo(BizErrorCode.E0001.getValue(), BizErrorCode.E0001.getDescription(), ex.getLocalizedMessage())).build(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(BizException.class)
//    public ResponseEntity<?> responseBizException(BizException ex) {
//        log.error("BizException {}", ex.getMessage(), ex);
//        return new ResponseEntity<>(new ResponseVOBuilder<>().fail().error(new ErrorVo(ex.getError().getValue(), ex.getError().getDescription())).build(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

//    @ExceptionHandler(NullPointerException.class)
//    public ResponseEntity<?> responseNullPointerException(NullPointerException ex) {
//        log.error("NullPointerException {}", "Null pointer exception error.", ex);
//        return new ResponseEntity<>(new ResponseVOBuilder<>().fail().error(new ErrorVo(BizErrorCode.E0001.getValue(), ex.getLocalizedMessage())).build(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ResponseEntity<Object> handleValidationNoHandlerFoundException(NoHandlerFoundException ex) {
//        log.error("NoHandlerFoundException {}", "No Handler Found Exception.", ex);
//        return new ResponseEntity<>(new ResponseVOBuilder<>().fail().error(new ErrorVo(BizErrorCode.E0001.getValue(), ex.getLocalizedMessage())).build(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<Object> handleValidationHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
//        log.error("HttpMessageNotReadableException {}", "Http Message Not Readable Exception.", ex);
//        return new ResponseEntity<>(new ResponseVOBuilder<>().fail().error(new ErrorVo(BizErrorCode.E0001.getValue(), ex.getLocalizedMessage())).build(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    public ResponseEntity<Object> handleValidationHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
//        log.error("BindException {}", "Bind Exception.", ex);
//        return new ResponseEntity<>(new ResponseVOBuilder<>().fail().error(new ErrorVo(BizErrorCode.E0001.getValue(), ex.getLocalizedMessage())).build(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    public ResponseEntity<Object> handleValidationMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
//        log.error("BindException {}", "Bind Exception.", ex);
//        return new ResponseEntity<>(new ResponseVOBuilder<>().fail().error(new ErrorVo(BizErrorCode.E0001.getValue(), ex.getLocalizedMessage())).build(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(WebExchangeBindException.class)
//    public ResponseEntity<Object> handleValidationWebExchangeBindException(WebExchangeBindException ex) {
//        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
//        ObjectError objectError = allErrors.get(NumberUtils.INTEGER_ZERO);
//        log.error("WebExchangeBindException {}", "Web Exchange Bind Exception.", ex);
//        return new ResponseEntity<>(new ResponseVOBuilder<>().fail().error(new ErrorVo(BizErrorCode.E0001.getValue(), objectError.getDefaultMessage())).build(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleValidationMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
//        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
//        ObjectError objectError = allErrors.get(NumberUtils.INTEGER_ZERO);
//        log.error("MethodArgumentNotValidException {}", "Method Argument Not Valid Exception.", ex);
//        return new ResponseEntity<>(new ResponseVOBuilder<>().fail().error(new ErrorVo(BizErrorCode.E0001.getValue(), objectError.getDefaultMessage())).build(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(BindException.class)
//    public ResponseEntity<Object> handleValidationBindException(BindException ex) {
//        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
//        ObjectError objectError = allErrors.get(NumberUtils.INTEGER_ZERO);
//        log.error("BindException {}", "Bind Exception.", ex);
//        return new ResponseEntity<>(new ResponseVOBuilder<>().fail().error(new ErrorVo(BizErrorCode.E0001.getValue(), objectError.getDefaultMessage())).build(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}
