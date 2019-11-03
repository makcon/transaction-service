package test.maksim.transaction.ws.domain;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
public class Account {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Integer id;

    @ManyToOne(targetEntity = Customer.class, fetch = LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private double balance;
}
