package org.discu2.forum.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.discu2.forum.packet.ErrorMessagePacket;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ForumExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AccountAlreadyExistException.class})
    public ResponseEntity<?> handleAccountAlreadyExist(AccountAlreadyExistException e, WebRequest request) {
        var packet = new ErrorMessagePacket(HttpStatus.BAD_REQUEST, e.getMessage());

        return new ResponseEntity<>(packet, new HttpHeaders(), packet.getStatus());
    }

    @ExceptionHandler({BadPacketFormatException.class})
    public ResponseEntity<?> handleBadPacketFormatException(BadPacketFormatException e, WebRequest request) {
        var packet = new ErrorMessagePacket(HttpStatus.BAD_REQUEST, e.getMessage());

        return new ResponseEntity<>(packet, new HttpHeaders(), packet.getStatus());
    }

    @ExceptionHandler({JWTDecodeException.class})
    public ResponseEntity<?> handleJWTDecodeException(JWTDecodeException e, WebRequest request) {
        var packet = new ErrorMessagePacket(HttpStatus.BAD_REQUEST, e.getMessage());

        return new ResponseEntity<>(packet, new HttpHeaders(), packet.getStatus());
    }

    @ExceptionHandler({TokenExpiredException.class})
    public ResponseEntity<?> handleTokenExpiredException(TokenExpiredException e, WebRequest request) {
        var packet = new ErrorMessagePacket(HttpStatus.BAD_REQUEST, e.getMessage());

        return new ResponseEntity<>(packet, new HttpHeaders(), packet.getStatus());
    }
}
