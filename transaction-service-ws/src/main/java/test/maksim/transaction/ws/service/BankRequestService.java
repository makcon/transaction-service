package test.maksim.transaction.ws.service;

import org.springframework.stereotype.Service;
import test.maksim.transaction.ws.domain.Transfer;
import test.maksim.transaction.ws.exception.BankRequestException;

@Service
public class BankRequestService {

    public void sendInterTransfer(Transfer transfer) {
        // emulate 30% chance of failure
        if(Math.random() <= 0.3){
            throw new BankRequestException("Failed to process transfer");
        }
    }

    public void sendIntraTransfer(Transfer transfer) {
        // always true
    }
}
