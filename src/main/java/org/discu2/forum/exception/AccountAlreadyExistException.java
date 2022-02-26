package org.discu2.forum.exception;

public class AccountAlreadyExistException extends Exception {

    public AccountAlreadyExistException() {
        super("Account already exist");
    }
}
