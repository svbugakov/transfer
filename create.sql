create table if not exists account(acc varchar(20), bal DECIMAL(20, 2));
insert into account(acc, bal) select * from (
select '40817000000000000001',198
) x where not exists(select 1 from account);