package test.maksim.transaction.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferDTO {

    private final long id;
    private final int accountFromId;
    private final int accountToId;
    private final double requestedAmount;
    private final double commission;
    private TransferStatus status;
}
