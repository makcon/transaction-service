package test.maksim.transaction.ws.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import test.maksim.transaction.ws.domain.Transfer;
import test.maksim.transaction.ws.service.BankRequestService;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
@RequiredArgsConstructor
public class InterBankTransferHandler implements TransferHandler {

    private final BankRequestService bankRequestService;

    @Value("${transfer.commission.eur:5}")
    private double commission;
    @Value("${transfer.limit.eur:1000}")
    private double limit;

    @Override
    public void validate(Transfer transfer) {
        if (transfer.getRequestedAmount() > limit) {
            throw new ResponseStatusException(BAD_REQUEST, "Request amount exceeded the limit: " + limit);
        }
        if (transfer.getRequestedAmount() - commission <= 0) {
            throw new ResponseStatusException(BAD_REQUEST, "Request amount is less or equal the commission: " + commission);
        }
    }

    @Override
    public void handle(Transfer transfer) {
        transfer.setCommission(commission);

        bankRequestService.sendInterTransfer(transfer);
    }
}
