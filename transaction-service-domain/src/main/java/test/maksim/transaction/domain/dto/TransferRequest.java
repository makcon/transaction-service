package test.maksim.transaction.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferRequest {

    private final int customerId;
    private final int accountIdFrom;
    private final int accountIdTo;
    private final double amount;
}
