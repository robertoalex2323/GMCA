-- Correcciones de auditoria sobre datos semilla.
-- Se mantiene en una migracion nueva para no alterar checksums de Flyway ya aplicados.

update products
set name = 'Arroz Anejo 49 kg', updated_at = now()
where id = '10000000-0000-0000-0000-000000000002';

update products
set sku = 'SUB-NELEN', name = 'Nelen', updated_at = now()
where id = '10000000-0000-0000-0000-000000000005';

update clients c
set current_debt = coalesce(s.total_debt, 0), updated_at = now()
from (
    select client_id, sum(quantity * unit_price) as total_debt
    from sales
    where status = 'CONFIRMED'
    group by client_id
) s
where c.id = s.client_id
  and c.id in (
      '20000000-0000-0000-0000-000000000001',
      '20000000-0000-0000-0000-000000000002',
      '20000000-0000-0000-0000-000000000003',
      '20000000-0000-0000-0000-000000000004'
  );

update clients
set current_debt = 0, updated_at = now()
where id in (
    '20000000-0000-0000-0000-000000000003',
    '20000000-0000-0000-0000-000000000004'
)
and not exists (
    select 1
    from sales
    where sales.client_id = clients.id
      and sales.status = 'CONFIRMED'
);

insert into audit_logs (id, actor, action, entity_name, entity_id, detail, created_at, updated_at) values
('80000000-0000-0000-0000-000000000002', 'system', 'SEED_FIX', 'Database', 'V3', 'Correccion de nombres y deuda inicial', now(), now())
on conflict (id) do nothing;