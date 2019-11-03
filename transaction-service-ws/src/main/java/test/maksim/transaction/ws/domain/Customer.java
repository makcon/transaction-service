package test.maksim.transaction.ws.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
public class Customer {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Integer id;

    private String name;

    @ManyToOne(targetEntity = Bank.class, fetch = LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;
}
