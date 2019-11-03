package test.maksim.transaction.ws.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;
import test.maksim.transaction.ws.domain.Transfer;
import test.maksim.transaction.ws.service.BankRequestService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class IntrerBankTransferHandlerTest {

    private static final double COMMISSION = 2;
    private static final double LIMIT = 30;

    private InterBankTransferHandler handler;

    private final BankRequestService bankRequestService = mock(BankRequestService.class);

    @BeforeEach
    void setUp() {
        handler = new InterBankTransferHandler(bankRequestService);

        ReflectionTestUtils.setField(handler, "commission", COMMISSION);
        ReflectionTestUtils.setField(handler, "limit", LIMIT);
    }

    @ParameterizedTest
    @ValueSource(doubles = {LIMIT, LIMIT - 1})
    @DisplayName("should not throw an exception when amount is less or equal limit")
    void validate1(double amount) {
        var givenTransfer = createTransfer(amount);

        handler.validate(givenTransfer);
    }

    @Test
    @DisplayName("should throw an exception when limit exceeded")
    void validate2() {
        var givenTransfer = createTransfer(LIMIT + 1);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> handler.validate(givenTransfer)
        );

        assertEquals(BAD_REQUEST, exception.getStatus());
    }

    @ParameterizedTest
    @ValueSource(doubles = {COMMISSION, COMMISSION - 1})
    @DisplayName("should throw an exception when amount is less or equal the commission")
    void validate3(double amount) {
        var givenTransfer = createTransfer(amount);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> handler.validate(givenTransfer)
        );

        assertEquals(BAD_REQUEST, exception.getStatus());
    }

    @Test
    @DisplayName("should not throw an exception when amount is more the commission")
    void validate4() {
        var givenTransfer = createTransfer(COMMISSION + 1);

        handler.validate(givenTransfer);
    }

    @Test
    @DisplayName("should call bank request service and set commission")
    void handle() {
        var givenTransfer = createTransfer(LIMIT);

        handler.handle(givenTransfer);

        verify(bankRequestService).sendInterTransfer(givenTransfer);
        assertEquals(COMMISSION, givenTransfer.getCommission());
    }

    // Util methods

    private Transfer createTransfer(double amount) {
        var transfer = new Transfer();
        transfer.setRequestedAmount(amount);

        return transfer;
    }
}