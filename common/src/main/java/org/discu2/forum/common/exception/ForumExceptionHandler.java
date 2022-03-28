package org.discu2.forum.common.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.discu2.forum.common.packet.ErrorMessagePacket;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice(basePackages = { "org.discu2.forum" })
public class ForumExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            AlreadyExistException.class,
            BadPacketFormatException.class,
            JWTDecodeException.class,
            TokenExpiredException.class,
            AuthenticationException.class,
            IllegalFileException.class
    })
    public ResponseEntity<?> handleBadRequestException(IOException e, WebRequest request) {
        var packet = new ErrorMessagePacket(HttpStatus.BAD_REQUEST, e.getMessage());

        return new ResponseEntity<>(packet, new HttpHeaders(), packet.getStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
        var packet = new ErrorMessagePacket(HttpStatus.FORBIDDEN, "Access denied");

        return new ResponseEntity<>(packet, new HttpHeaders(), packet.getStatus());
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<?> handleDataNotFoundException(DataNotFoundException e, WebRequest request) {
        var packet = new ErrorMessagePacket(HttpStatus.NOT_FOUND, e.getMessage());

        return new ResponseEntity<>(packet, new HttpHeaders(), packet.getStatus());
    }
}
