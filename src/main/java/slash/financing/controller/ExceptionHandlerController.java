package slash.financing.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import slash.financing.dto.ErrorDto;
import slash.financing.exception.*;

import java.util.Date;

@RestControllerAdvice
public class ExceptionHandlerController {
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorDto> handleIllegalArgument(HttpServletRequest request, IllegalArgumentException e) {
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ErrorDto.builder()
                                                .timestamp(new Date())
                                                .status(HttpStatus.BAD_REQUEST.value())
                                                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                                .path(request.getServletPath())
                                                .message(e.getMessage())
                                                .build());
        }

        @ExceptionHandler({ UserNotFoundException.class, BudgetCategoryNotFoundException.class,
                        VerificationTokenNotFoundException.class })
        public ResponseEntity<?> handleNotFound(HttpServletRequest request, RuntimeException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                ErrorDto.builder()
                                                .timestamp(new Date())
                                                .status(HttpStatus.NOT_FOUND.value())
                                                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                                                .path(request.getServletPath())
                                                .message(e.getMessage())
                                                .build());
        }

        @ExceptionHandler({ UserAlreadyExistsException.class })
        public ResponseEntity<?> handleAlreadyExists(HttpServletRequest request, RuntimeException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                                ErrorDto.builder()
                                                .timestamp(new Date())
                                                .status(HttpStatus.BAD_REQUEST.value())
                                                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                                .path(request.getServletPath())
                                                .message(e.getMessage())
                                                .build());
        }

        @ExceptionHandler({ ExpiredJwtException.class })
        public ResponseEntity<?> handleExpiredToken(HttpServletRequest request, RuntimeException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                                ErrorDto.builder()
                                                .timestamp(new Date())
                                                .status(HttpStatus.UNAUTHORIZED.value())
                                                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                                                .path(request.getServletPath())
                                                .message(e.getMessage())
                                                .build());
        }

        @ExceptionHandler({ BadCredentialsException.class })
        public ResponseEntity<?> handleBadCredentials(HttpServletRequest request, RuntimeException e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                                ErrorDto.builder()
                                                .timestamp(new Date())
                                                .status(HttpStatus.FORBIDDEN.value())
                                                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                                                .path(request.getServletPath())
                                                .message(e.getMessage())
                                                .build());
        }

        @ExceptionHandler({ BudgetCategoryPersonalCountException.class })
        public ResponseEntity<ErrorDto> handleConflict(HttpServletRequest request, RuntimeException e) {
                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(ErrorDto.builder()
                                                .timestamp(new Date())
                                                .status(HttpStatus.CONFLICT.value())
                                                .error(HttpStatus.CONFLICT.getReasonPhrase())
                                                .path(request.getServletPath())
                                                .message(e.getMessage())
                                                .build());
        }

        @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
        public ResponseEntity<ErrorDto> handleMethodNotAllowed(ServletException e, HttpServletRequest request) {
                return ResponseEntity
                                .status(HttpStatus.METHOD_NOT_ALLOWED)
                                .body(ErrorDto.builder()
                                                .timestamp(new Date())
                                                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                                                .error(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())
                                                .path(request.getServletPath())
                                                .message(e.getMessage())
                                                .build());
        }

}