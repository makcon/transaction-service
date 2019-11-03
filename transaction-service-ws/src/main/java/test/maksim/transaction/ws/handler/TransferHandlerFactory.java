package test.maksim.transaction.ws.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import test.maksim.transaction.ws.domain.Transfer;

@Component
@RequiredArgsConstructor
public class TransferHandlerFactory {

    private final IntraBankTransferHandler intraBank;
    private final InterBankTransferHandler interBank;

    public TransferHandler getHandler(Transfer transfer) {
        return transfer.isToSameBank() ? intraBank : interBank;
    }
}
