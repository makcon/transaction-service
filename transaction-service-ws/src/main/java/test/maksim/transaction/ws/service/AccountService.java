package test.maksim.transaction.ws.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import test.maksim.transaction.ws.domain.Account;
import test.maksim.transaction.ws.domain.Transfer;
import test.maksim.transaction.ws.repository.AccountRepository;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository repository;

    public Account findAccount(int accountId) {
        return repository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Account not found for id: " + accountId));
    }

    public void updateBalances(Transfer transfer) {
        var from = transfer.getAccountFrom();
        from.setBalance(from.getBalance() - transfer.getRequestedAmount());

        var to = transfer.getAccountTo();
        to.setBalance(to.getBalance() + transfer.getRequestedAmount() - transfer.getCommission());

        log.debug("Saving accounts from: {}, to: {}", from, to);
        repository.saveAll(List.of(from, to));
    }
}
