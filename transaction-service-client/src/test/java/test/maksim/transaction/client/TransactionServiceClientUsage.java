package test.maksim.transaction.client;

import test.maksim.transaction.domain.dto.TransferRequest;

public class TransactionServiceClientUsage {

    public static void main(String[] args) {
        TransactionServiceClientFactory factory = new TransactionServiceClientFactory();
        TransactionServiceClient client = factory.defaultClient("http://localhost:8080");

        sentTransactions(client);
        findTransfers(client);
    }

    private static void sentTransactions(TransactionServiceClient client) {
        var request = TransferRequest.builder()
                .customerId(11)
                .accountIdFrom(22)
                .accountIdTo(24)
                .amount(6)
                .build();
        client.sendTransfer(request).join();
    }

    private static void findTransfers(TransactionServiceClient client) {
        System.out.println(client.findTransfers().join());
    }
}