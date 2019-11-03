insert into bank (id, name) values (1, 'Sabadell');
insert into bank (id, name) values (2, 'Caixa');
insert into bank (id, name) values (3, 'Santader');

insert into customer (id, name, bank_id) values (11, 'Batman', 1);
insert into customer (id, name, bank_id) values (12, 'Spiderman', 1);
insert into customer (id, name, bank_id) values (13, 'Lady bug', 2);

insert into account (id, customer_id, balance) values (21, 11, 20);
insert into account (id, customer_id, balance) values (22, 11, 210);
insert into account (id, customer_id, balance) values (23, 12, 120);
insert into account (id, customer_id, balance) values (24, 13, 5);

