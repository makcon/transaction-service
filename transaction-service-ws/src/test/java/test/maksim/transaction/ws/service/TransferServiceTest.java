package test.maksim.transaction.ws.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.server.ResponseStatusException;
import test.maksim.transaction.domain.dto.TransferRequest;
import test.maksim.transaction.domain.dto.TransferStatus;
import test.maksim.transaction.ws.domain.Account;
import test.maksim.transaction.ws.domain.Transfer;
import test.maksim.transaction.ws.exception.BankRequestException;
import test.maksim.transaction.ws.handler.TransferHandler;
import test.maksim.transaction.ws.handler.TransferHandlerFactory;
import test.maksim.transaction.ws.repository.TransferRepository;
import test.maksim.transaction.ws.validator.AccountValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FAILED_DEPENDENCY;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

class TransferServiceTest {

    private static final int GIVEN_ACCOUNT_ID_FROM = 1;
    private static final int GIVEN_ACCOUNT_ID_TO = 2;
    private static final double GIVEN_AMOUNT = 20;
    private static final TransferRequest GIVEN_REQUEST = TransferRequest.builder()
            .accountIdFrom(GIVEN_ACCOUNT_ID_FROM)
            .accountIdTo(GIVEN_ACCOUNT_ID_TO)
            .amount(GIVEN_AMOUNT)
            .build();
    private static final Account FOUND_ACCOUNT_FROM = mock(Account.class);
    private static final Account FOUND_ACCOUNT_TO = mock(Account.class);
    private static final TransferHandler DETECTED_TRANSFER_HANDLER = mock(TransferHandler.class);

    private TransferService service;

    private final TransferHandlerFactory transferHandlerFactory = mock(TransferHandlerFactory.class);
    private final AccountValidator accountValidator = mock(AccountValidator.class);
    private final AccountService accountService = mock(AccountService.class);
    private final TransferRepository transferRepository = mock(TransferRepository.class);

    @BeforeEach
    void setUp() {
//        transferHandlerFactory = mock(TransferHandlerFactory.class);
//        accountValidator = mock(AccountValidator.class);
//        accountService = mock(AccountService.class);
//        transferRepository = mock(TransferRepository.class);

        service = new TransferService(
                transferHandlerFactory,
                accountValidator,
                accountService,
                transferRepository
        );
    }

    @Test
    @DisplayName("should throw an exception if account from not found")
    void sendTransfer1() {
        when(accountService.findAccount(GIVEN_ACCOUNT_ID_FROM)).thenThrow(ResponseStatusException.class);

        assertThrows(ResponseStatusException.class, () -> service.sendTransfer(GIVEN_REQUEST));
        verify(accountValidator, never()).validateBeforeTransfer(any(), any());
    }

    @Test
    @DisplayName("should throw an exception if request is invalid")
    void sendTransfer2() {
        when(accountService.findAccount(GIVEN_ACCOUNT_ID_FROM)).thenReturn(FOUND_ACCOUNT_FROM);
        doThrow(ResponseStatusException.class).when(accountValidator).validateBeforeTransfer(any(), any());

        assertThrows(ResponseStatusException.class, () -> service.sendTransfer(GIVEN_REQUEST));
        verify(accountValidator).validateBeforeTransfer(FOUND_ACCOUNT_FROM, GIVEN_REQUEST);
    }

    @Test
    @DisplayName("should throw an exception if account to not found")
    void sendTransfer3() {
        when(accountService.findAccount(GIVEN_ACCOUNT_ID_FROM)).thenReturn(FOUND_ACCOUNT_FROM);
        when(accountService.findAccount(GIVEN_ACCOUNT_ID_TO)).thenThrow(ResponseStatusException.class);

        assertThrows(ResponseStatusException.class, () -> service.sendTransfer(GIVEN_REQUEST));
        verify(transferHandlerFactory, never()).getHandler(any());
    }

    @Test
    @DisplayName("should throw an exception if transfer handler throws validation error")
    void sendTransfer4() {
        when(accountService.findAccount(GIVEN_ACCOUNT_ID_FROM)).thenReturn(FOUND_ACCOUNT_FROM);
        when(accountService.findAccount(GIVEN_ACCOUNT_ID_TO)).thenReturn(FOUND_ACCOUNT_TO);
        when(transferHandlerFactory.getHandler(any())).thenReturn(DETECTED_TRANSFER_HANDLER);
        doThrow(ResponseStatusException.class).when(DETECTED_TRANSFER_HANDLER).validate(any());

        assertThrows(ResponseStatusException.class, () -> service.sendTransfer(GIVEN_REQUEST));
        verify(transferRepository, never()).save(any());
    }

//    @Test
//    @DisplayName("should save transfer with status IN_PROGRESS before handling")
//    void sendTransfer5() {
//        mockAllSuccessActionBeforeHandling();
//        ArgumentCaptor<Transfer> transferCaptor = ArgumentCaptor.forClass(Transfer.class);
//
//        service.sendTransfer(GIVEN_REQUEST);
//
//        verify(transferRepository, times(2)).save(transferCaptor.capture());
//        assertEquals(TransferStatus.IN_PROGRESS, transferCaptor.getAllValues().get(0).getStatus());
//    }

    @Test
    @DisplayName("should save transfer with status SUCCESS when handling is successful")
    void sendTransfer5() {
        mockAllSuccessActionBeforeHandling();
        ArgumentCaptor<Transfer> transferCaptor = ArgumentCaptor.forClass(Transfer.class);

        service.sendTransfer(GIVEN_REQUEST);

        verify(transferRepository, times(2)).save(transferCaptor.capture());
        assertEquals(TransferStatus.SUCCESS, transferCaptor.getAllValues().get(0).getStatus());
    }

    @Test
    @DisplayName("should save transfer with status FAILED and throw an exception when handling is failed")
    void sendTransfer6() {
        mockAllSuccessActionBeforeHandling();
        doThrow(BankRequestException.class).when(DETECTED_TRANSFER_HANDLER).handle(any());
        ArgumentCaptor<Transfer> transferCaptor = ArgumentCaptor.forClass(Transfer.class);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.sendTransfer(GIVEN_REQUEST)
        );

        assertEquals(FAILED_DEPENDENCY, exception.getStatus());
        verify(transferRepository, times(2)).save(transferCaptor.capture());
        assertEquals(TransferStatus.FAILED, transferCaptor.getAllValues().get(0).getStatus());
    }

    @Test
    @DisplayName("should save transfer with status FAILED and throw an exception when unexpected error occurred")
    void sendTransfer7() {
        mockAllSuccessActionBeforeHandling();
        doThrow(RuntimeException.class).when(accountService).updateBalances(any());
        ArgumentCaptor<Transfer> transferCaptor = ArgumentCaptor.forClass(Transfer.class);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.sendTransfer(GIVEN_REQUEST)
        );

        assertEquals(INTERNAL_SERVER_ERROR, exception.getStatus());
        verify(transferRepository, times(2)).save(transferCaptor.capture());
        assertEquals(TransferStatus.FAILED, transferCaptor.getAllValues().get(0).getStatus());
    }

    // Util methods

    private void mockAllSuccessActionBeforeHandling() {
        when(accountService.findAccount(GIVEN_ACCOUNT_ID_FROM)).thenReturn(FOUND_ACCOUNT_FROM);
        when(accountService.findAccount(GIVEN_ACCOUNT_ID_TO)).thenReturn(FOUND_ACCOUNT_TO);
        when(transferHandlerFactory.getHandler(any())).thenReturn(DETECTED_TRANSFER_HANDLER);
        doNothing().when(DETECTED_TRANSFER_HANDLER).validate(any());
        doNothing().when(DETECTED_TRANSFER_HANDLER).handle(any());
    }
}