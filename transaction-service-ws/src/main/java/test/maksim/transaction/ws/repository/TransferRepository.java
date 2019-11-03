package test.maksim.transaction.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.maksim.transaction.ws.domain.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
