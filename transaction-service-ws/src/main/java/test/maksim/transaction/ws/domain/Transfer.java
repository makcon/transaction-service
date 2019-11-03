package test.maksim.transaction.ws.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import test.maksim.transaction.domain.dto.TransferStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;
import static test.maksim.transaction.domain.dto.TransferStatus.IN_PROGRESS;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "status"})
public class Transfer {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne(targetEntity = Account.class, fetch = LAZY)
    @JoinColumn(name = "account_from_id")
    private Account accountFrom;

    @OneToOne(targetEntity = Account.class, fetch = LAZY)
    @JoinColumn(name = "account_to_id")

    private Account accountTo;

    private double requestedAmount;

    private double commission;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    public static Transfer create(Account from, Account to, double amount) {
        var entity = new Transfer();
        entity.setAccountFrom(from);
        entity.setAccountTo(to);
        entity.setRequestedAmount(amount);
        entity.setStatus(IN_PROGRESS);

        return entity;
    }

    public boolean isToSameBank() {
        return Objects.equals(
                accountFrom.getCustomer().getBank(),
                accountTo.getCustomer().getBank()
        );
    }
}
