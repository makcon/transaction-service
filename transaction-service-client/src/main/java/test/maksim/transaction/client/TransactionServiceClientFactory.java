package test.maksim.transaction.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import test.maksim.transaction.client.impl.TransactionServiceClientImpl;

import java.net.http.HttpClient;

public class TransactionServiceClientFactory {

    public TransactionServiceClient defaultClient(String serviceUrl) {
        return TransactionServiceClientImpl.builder()
                .gson(createGson())
                .httpClient(HttpClient.newHttpClient())
                .serviceUrl(serviceUrl)
                .build();
    }

    private Gson createGson() {
        return new GsonBuilder().create();
    }
}
