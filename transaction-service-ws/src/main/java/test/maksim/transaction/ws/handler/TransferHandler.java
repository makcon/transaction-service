package test.maksim.transaction.ws.handler;

import test.maksim.transaction.ws.domain.Transfer;

public interface TransferHandler {

    void validate(Transfer transfer);

    void handle(Transfer transfer);
}
