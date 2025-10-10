package com.cinelist.api.infra.exceptions;

import com.cinelist.api.refreshToken.exceptions.CreationTokenException;
import com.cinelist.api.refreshToken.exceptions.InvalidTokenException;
import com.cinelist.api.user.exceptions.EmailAlreadyRegisteredException;
import com.cinelist.api.user.exceptions.UserNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Hidden
public class ApplicationExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    // Auth handlers
    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailAlreadyRegistered(EmailAlreadyRegisteredException exc) {
        var error = new ErrorResponseDTO(HttpStatus.CONFLICT.value(), exc.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        //409 - Conflict: The request was valid but conflicts with an existing user (email already registered)
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFound(UserNotFoundException exc) {
        var error = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), exc.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        //404 - Not Found: No user found with the provided data
    }

    // Token handlers
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidToken(InvalidTokenException exc) {
        var error = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), exc.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        //401 - Not Authorized: Token is invalid
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials(BadCredentialsException ex) {
        var error = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials.", System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        // 401 - Unauthorized: Invalid credentials (wrong password)
    }

    // Spring handlers
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        var error = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), errorMessage, System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        //400 - Bad request:  Validation failed for one or more fields (@Valid)
    }

    // Internal handlers
    @ExceptionHandler(CreationTokenException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenCreationException(CreationTokenException ex) {
        logger.error("Error creating JWT token: ", ex.getCause());
        var error = new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An internal error occurred while processing your request", System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        //500 - Internal Server Error: Failed to create JWT token
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleUnexpectedException(Exception ex) {
        logger.error("An unexpected error occurred: ", ex);
        var error = new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected server error occurred.", System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        //500 - Internal Server Error: Catches any unhandled exceptions
    }
}
