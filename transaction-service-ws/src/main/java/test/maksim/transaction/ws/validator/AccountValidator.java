package test.maksim.transaction.ws.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import test.maksim.transaction.domain.dto.TransferRequest;
import test.maksim.transaction.ws.domain.Account;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class AccountValidator {

    public void validateBeforeTransfer(Account accountFrom, TransferRequest request) {
        if (accountFrom.getCustomer().getId() != request.getCustomerId()) {
            throw new ResponseStatusException(BAD_REQUEST, "Account doesn't belong to the customer");
        }
        if (accountFrom.getBalance() < request.getAmount()) {
            throw new ResponseStatusException(BAD_REQUEST, "You don't have enough balance to process the transfer");
        }
    }
}
