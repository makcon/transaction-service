package test.maksim.transaction.ws.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
public class Bank {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Integer id;

    private String name;
}
