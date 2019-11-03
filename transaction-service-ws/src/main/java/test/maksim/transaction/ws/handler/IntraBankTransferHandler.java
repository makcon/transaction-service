package test.maksim.transaction.ws.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import test.maksim.transaction.ws.domain.Transfer;
import test.maksim.transaction.ws.service.BankRequestService;

@Component
@RequiredArgsConstructor
public class IntraBankTransferHandler implements TransferHandler {

    private final BankRequestService bankRequestService;

    @Override
    public void validate(Transfer transfer) {
        // always valid
    }

    @Override
    public void handle(Transfer transfer) {
        bankRequestService.sendIntraTransfer(transfer);
    }
}
