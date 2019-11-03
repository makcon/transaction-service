package test.maksim.transaction.client;

import test.maksim.transaction.domain.dto.TransferDTO;
import test.maksim.transaction.domain.dto.TransferRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TransactionServiceClient {

    CompletableFuture<Void> sendTransfer(TransferRequest request);

    CompletableFuture<List<TransferDTO>> findTransfers();
}
