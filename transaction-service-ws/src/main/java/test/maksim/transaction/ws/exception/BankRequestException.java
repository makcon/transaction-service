package test.maksim.transaction.ws.exception;

import lombok.Getter;

@Getter
public class BankRequestException extends RuntimeException {

    public BankRequestException(String message) {
        super(message);
    }
}
