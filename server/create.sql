create table if not exists account(acc varchar(20), bal DECIMAL(20, 2), PRIMARY KEY(ACC));
insert into account(acc, bal) select * from (
select '40817000000000000001',198
union
select '40817000000000000003',100
) x where not exists(select 1 from account);