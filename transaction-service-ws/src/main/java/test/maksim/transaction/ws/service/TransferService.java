package test.maksim.transaction.ws.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import test.maksim.transaction.domain.dto.TransferDTO;
import test.maksim.transaction.domain.dto.TransferRequest;
import test.maksim.transaction.ws.exception.BankRequestException;
import test.maksim.transaction.ws.domain.Transfer;
import test.maksim.transaction.ws.handler.TransferHandler;
import test.maksim.transaction.ws.handler.TransferHandlerFactory;
import test.maksim.transaction.ws.repository.TransferRepository;
import test.maksim.transaction.ws.validator.AccountValidator;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.FAILED_DEPENDENCY;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static test.maksim.transaction.domain.dto.TransferStatus.FAILED;
import static test.maksim.transaction.domain.dto.TransferStatus.SUCCESS;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final TransferHandlerFactory transferHandlerFactory;
    private final AccountValidator accountValidator;
    private final AccountService accountService;
    private final TransferRepository transferRepository;

    public void sendTransfer(TransferRequest request) {
        var accountFrom = accountService.findAccount(request.getAccountIdFrom());

        accountValidator.validateBeforeTransfer(accountFrom, request);

        var accountTo = accountService.findAccount(request.getAccountIdTo());
        var transfer = Transfer.create(accountFrom, accountTo, request.getAmount());

        TransferHandler handler = transferHandlerFactory.getHandler(transfer);
        handler.validate(transfer);

        log.debug("Creating transfer: {}", transfer);
        transferRepository.save(transfer);

        doSend(handler, transfer);
    }

    private void doSend(TransferHandler handler, Transfer transfer) {
        transfer.setStatus(FAILED);

        try {
            log.debug("Handling transfer: {}", transfer);
            handler.handle(transfer);

            log.debug("Updating balance for transfer: {}", transfer);
            accountService.updateBalances(transfer);

            transfer.setStatus(SUCCESS);
        } catch (BankRequestException e) {
            log.error("Failed to process transfer: {}", transfer, e);
            throw new ResponseStatusException(FAILED_DEPENDENCY, "Failed to process transfer: " + transfer.getId(), e);
        } catch (RuntimeException e) {
            log.error("An internal error occurred for transfer: {}", transfer, e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Failed to process transfer: " + transfer.getId(), e);
        } finally {
            log.debug("Updating transfer: {}", transfer);
            transferRepository.save(transfer);
        }
    }

    public List<TransferDTO> findTransfers() {
        return transferRepository.findAll().stream()
                .map(it -> TransferDTO.builder()
                        .id(it.getId())
                        .accountFromId(it.getAccountFrom().getId())
                        .accountToId(it.getAccountTo().getId())
                        .requestedAmount(it.getRequestedAmount())
                        .commission(it.getCommission())
                        .status(it.getStatus())
                        .build())
                .collect(toList());
    }
}
