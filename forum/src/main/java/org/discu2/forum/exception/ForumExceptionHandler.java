package org.discu2.forum.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.discu2.forum.packet.ErrorMessagePacket;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ForumExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<?> handleAccountAlreadyExist(AlreadyExistException e, WebRequest request) {
        var packet = new ErrorMessagePacket(HttpStatus.BAD_REQUEST, e.getMessage());

        return new ResponseEntity<>(packet, new HttpHeaders(), packet.getStatus());
    }

    @ExceptionHandler(BadPacketFormatException.class)
    public ResponseEntity<?> handleBadPacketFormatException(BadPacketFormatException e, WebRequest request) {
        var packet = new ErrorMessagePacket(HttpStatus.BAD_REQUEST, e.getMessage());

        return new ResponseEntity<>(packet, new HttpHeaders(), packet.getStatus());
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<?> handleJWTDecodeException(JWTDecodeException e, WebRequest request) {
        var packet = new ErrorMessagePacket(HttpStatus.BAD_REQUEST, e.getMessage());

        return new ResponseEntity<>(packet, new HttpHeaders(), packet.getStatus());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> handleTokenExpiredException(TokenExpiredException e, WebRequest request) {
        var packet = new ErrorMessagePacket(HttpStatus.BAD_REQUEST, e.getMessage());

        return new ResponseEntity<>(packet, new HttpHeaders(), packet.getStatus());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException e, WebRequest request) {
        var packet = new ErrorMessagePacket(HttpStatus.BAD_REQUEST, e.getMessage());

        return new ResponseEntity<>(packet, new HttpHeaders(), packet.getStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
        var packet = new ErrorMessagePacket(HttpStatus.FORBIDDEN, "Access denied");

        return new ResponseEntity<>(packet, new HttpHeaders(), packet.getStatus());
    }
}
