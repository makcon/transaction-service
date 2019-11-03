package test.maksim.transaction.ws.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.web.server.ResponseStatusException;
import test.maksim.transaction.domain.dto.TransferRequest;
import test.maksim.transaction.ws.domain.Account;
import test.maksim.transaction.ws.domain.Customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class AccountValidatorTest {

    private static final int GIVEN_CUSTOMER_ID = 1;
    private static final int GIVEN_AMOUNT = 20;

    private final AccountValidator validator = new AccountValidator();

    @Test
    @DisplayName("should not throw an exception when customer id matches")
    void validateBeforeTransfer1() {
        var givenAccountFrom = createAccountFrom(GIVEN_AMOUNT);
        var givenRequest = createRequest(GIVEN_CUSTOMER_ID, GIVEN_AMOUNT);

        validator.validateBeforeTransfer(givenAccountFrom, givenRequest);
    }

    @Test
    @DisplayName("should throw an exception when customer id doesn't match")
    void validateBeforeTransfer2() {
        var givenAccountFrom = createAccountFrom(GIVEN_AMOUNT);
        var givenRequest = createRequest(GIVEN_CUSTOMER_ID + 1, GIVEN_AMOUNT);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> validator.validateBeforeTransfer(givenAccountFrom, givenRequest)
        );

        assertEquals(BAD_REQUEST, exception.getStatus());
    }

    @Test
    @DisplayName("should throw an exception when account's balance less than requested amount")
    void validateBeforeTransfer3() {
        var givenAccountFrom = createAccountFrom(GIVEN_AMOUNT - 1);
        var givenRequest = createRequest(GIVEN_CUSTOMER_ID, GIVEN_AMOUNT);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> validator.validateBeforeTransfer(givenAccountFrom, givenRequest)
        );

        assertEquals(BAD_REQUEST, exception.getStatus());
    }

    @ParameterizedTest
    @ValueSource(doubles = {GIVEN_AMOUNT, GIVEN_AMOUNT + 1})
    @DisplayName("should not throw an exception when account's balance more or equal requested amount")
    void validateBeforeTransfer4(double balance) {
        var givenAccountFrom = createAccountFrom(balance);
        var givenRequest = createRequest(GIVEN_CUSTOMER_ID, GIVEN_AMOUNT);

        validator.validateBeforeTransfer(givenAccountFrom, givenRequest);
    }

    // Util mehods

    private Account createAccountFrom(double balance) {
        var customer = new Customer();
        customer.setId(GIVEN_CUSTOMER_ID);

        var account = new Account();
        account.setCustomer(customer);
        account.setBalance(balance);

        return account;
    }

    private TransferRequest createRequest(int customerId, double amount) {
        return TransferRequest.builder()
                .customerId(customerId)
                .amount(amount)
                .build();
    }
}