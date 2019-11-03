package test.maksim.transaction.ws.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.maksim.transaction.ws.domain.Transfer;
import test.maksim.transaction.ws.service.BankRequestService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class IntraBankTransferHandlerTest {

    private IntraBankTransferHandler handler;

    private final BankRequestService bankRequestService = mock(BankRequestService.class);

    @BeforeEach
    void setUp() {
        handler = new IntraBankTransferHandler(bankRequestService);
    }

    @Test
    @DisplayName("should call bank request service")
    void handle() {
        var givenTransfer = mock(Transfer.class);

        handler.handle(givenTransfer);

        verify(bankRequestService).sendIntraTransfer(givenTransfer);
    }
}