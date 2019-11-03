package test.maksim.transaction.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.maksim.transaction.ws.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
