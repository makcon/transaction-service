package test.maksim.transaction.ws.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.server.ResponseStatusException;
import test.maksim.transaction.ws.domain.Account;
import test.maksim.transaction.ws.domain.Transfer;
import test.maksim.transaction.ws.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

class AccountServiceTest {

    private static final int GIVEN_ACCOUNT_ID = 1;

    private AccountService service;

    private final AccountRepository repository = mock(AccountRepository.class);

    @BeforeEach
    void setUp() {
        service = new AccountService(repository);
    }

    @Test
    @DisplayName("should return account when found")
    void findAccount1() {
        var expectedAccount = mock(Account.class);
        when(repository.findById(anyInt())).thenReturn(Optional.of(expectedAccount));

        Account foundAccount = service.findAccount(GIVEN_ACCOUNT_ID);

        assertEquals(Optional.of(expectedAccount), Optional.of(foundAccount));
    }

    @Test
    @DisplayName("should throw an exception when account not found")
    void findAccount2() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.findAccount(GIVEN_ACCOUNT_ID)
        );

        assertEquals(NOT_FOUND, exception.getStatus());
    }

    @Test
    @DisplayName("should update balances")
    void updateBalances() {
        var givenRequestedAmount = 10;
        var givenCommission = 5;
        var givenFromBalance = 15;
        var givenToBalance = 20;
        var givenTransfer = createTransfer(givenRequestedAmount, givenCommission, givenFromBalance, givenToBalance);
        ArgumentCaptor<List<Account>> accountsCapture = ArgumentCaptor.forClass(List.class);

        service.updateBalances(givenTransfer);

        verify(repository).saveAll(accountsCapture.capture());
        assertEquals(givenFromBalance - givenRequestedAmount, accountsCapture.getValue().get(0).getBalance());
        assertEquals(givenToBalance + givenRequestedAmount - givenCommission, accountsCapture.getValue().get(1).getBalance());
    }

    // Util methods

    private Transfer createTransfer(double requestedAmount,
                                    double commission,
                                    double fromBalance,
                                    double toBalance) {
        var accountFrom = new Account();
        accountFrom.setBalance(fromBalance);

        var accountTo = new Account();
        accountTo.setBalance(toBalance);

        var transfer = new Transfer();
        transfer.setRequestedAmount(requestedAmount);
        transfer.setCommission(commission);
        transfer.setAccountFrom(accountFrom);
        transfer.setAccountTo(accountTo);

        return transfer;
    }
}