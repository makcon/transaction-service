package test.maksim.transaction.client.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Builder;
import test.maksim.transaction.client.TransactionServiceClient;
import test.maksim.transaction.domain.constants.Endpoints;
import test.maksim.transaction.domain.dto.TransferDTO;
import test.maksim.transaction.domain.dto.TransferRequest;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse.BodySubscribers;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.nio.charset.StandardCharsets.UTF_8;

@Builder
public class TransactionServiceClientImpl implements TransactionServiceClient {

    private static final Type TYPE_LIST_TRANSFERS = new TypeToken<List<TransferDTO>>() {}.getType();

    private final String serviceUrl;
    private final HttpClient httpClient;
    private final Gson gson;

    @Override
    public CompletableFuture<Void> sendTransfer(TransferRequest request) {
        var fullUrl = serviceUrl + Endpoints.V1 + Endpoints.TRANSFERS;
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(fullUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(request)))
                .build();

        return httpClient.sendAsync(httpRequest, BodyHandlers.discarding())
                .thenApply(HttpResponse::body);
    }

    @Override
    public CompletableFuture<List<TransferDTO>> findTransfers() {
        var fullUrl = serviceUrl + Endpoints.V1 + Endpoints.TRANSFERS;
        var request = HttpRequest.newBuilder(URI.create(fullUrl))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        return httpClient.sendAsync(request, it -> BodySubscribers.ofString(UTF_8))
                .thenApply(it -> gson.fromJson(it.body(), TYPE_LIST_TRANSFERS));
    }
}
