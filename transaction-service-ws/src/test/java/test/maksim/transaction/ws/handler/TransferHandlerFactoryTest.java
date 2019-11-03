package test.maksim.transaction.ws.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.maksim.transaction.ws.domain.Account;
import test.maksim.transaction.ws.domain.Bank;
import test.maksim.transaction.ws.domain.Customer;
import test.maksim.transaction.ws.domain.Transfer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class TransferHandlerFactoryTest {

    private TransferHandlerFactory factory;

    private final IntraBankTransferHandler intraBank = mock(IntraBankTransferHandler.class);
    private final InterBankTransferHandler interBank = mock(InterBankTransferHandler.class);

    @BeforeEach
    void setUp() {
        factory = new TransferHandlerFactory(intraBank, interBank);
    }

    @Test
    @DisplayName("should return proper handler")
    void getHandler() {
        assertEquals(intraBank, factory.getHandler(createTransfer(true)));
        assertEquals(interBank, factory.getHandler(createTransfer(false)));
    }

    // Util methods

    private Transfer createTransfer(boolean isSameBank) {
        var transfer = new Transfer();

        var accountFrom = new Account();
        var customer1 = new Customer();
        var bank1 = mock(Bank.class);
        customer1.setBank(bank1);
        accountFrom.setCustomer(customer1);
        transfer.setAccountFrom(accountFrom);

        var accountTo = new Account();
        var customer2 = new Customer();
        var bank2 = isSameBank ? bank1 : mock(Bank.class);
        customer2.setBank(bank2);
        accountTo.setCustomer(customer2);
        transfer.setAccountTo(accountTo);

        return transfer;
    }
}