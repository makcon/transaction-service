package test.maksim.transaction.ws.rest;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import test.maksim.transaction.domain.constants.Endpoints;
import test.maksim.transaction.domain.dto.TransferDTO;
import test.maksim.transaction.domain.dto.TransferRequest;
import test.maksim.transaction.ws.service.TransferService;

import java.util.List;

@RestController
@RequestMapping(Endpoints.V1)
@RequiredArgsConstructor
@Slf4j
@Api
public class TransferController {

    private final TransferService transferService;

    @PostMapping(Endpoints.TRANSFERS)
    public void sendTransfer(@RequestBody TransferRequest request) {
        log.info("Received request: {}", request);
        transferService.sendTransfer(request);
    }

    @GetMapping(Endpoints.TRANSFERS)
    public List<TransferDTO> findTransfers() {
        return transferService.findTransfers();
    }
}
