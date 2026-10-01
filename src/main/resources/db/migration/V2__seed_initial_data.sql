-- Datos iniciales GMCA para pruebas funcionales.
-- Este script es idempotente: puede ejecutarse sin duplicar registros clave.

insert into products (id, sku, name, type, base_price, created_at, updated_at) values
('10000000-0000-0000-0000-000000000001', 'ARZ-NIR-49KG', 'Arroz Nir 49 kg', 'FINISHED_RICE', 168.00, now(), now()),
('10000000-0000-0000-0000-000000000002', 'ARZ-AEJ-49KG', 'Arroz Añejo 49 kg', 'FINISHED_RICE', 182.00, now(), now()),
('10000000-0000-0000-0000-000000000003', 'ARZ-SUP-49KG', 'Arroz Superior 49 kg', 'FINISHED_RICE', 176.00, now(), now()),
('10000000-0000-0000-0000-000000000004', 'SUB-POLVILLO', 'Polvillo de arroz', 'BYPRODUCT', 38.00, now(), now()),
('10000000-0000-0000-0000-000000000005', 'SUB-ÑELEN', 'Ñelen', 'BYPRODUCT', 42.00, now(), now()),
('10000000-0000-0000-0000-000000000006', 'SUB-CASCARILLA', 'Cascarilla de arroz', 'BYPRODUCT', 15.00, now(), now())
on conflict (sku) do update set
    name = excluded.name,
    type = excluded.type,
    base_price = excluded.base_price,
    updated_at = now();

insert into clients (id, business_name, document_number, credit_limit, current_debt, active, created_at, updated_at) values
('20000000-0000-0000-0000-000000000001', 'Comercial San Martin SAC', '20600000001', 50000.00, 6720.00, true, now(), now()),
('20000000-0000-0000-0000-000000000002', 'Distribuidora El Molino EIRL', '20600000002', 35000.00, 0.00, true, now(), now()),
('20000000-0000-0000-0000-000000000003', 'Mercado Mayorista Norte', '20600000003', 75000.00, 0.00, true, now(), now()),
('20000000-0000-0000-0000-000000000004', 'Abarrotes La Familia', '10400000004', 12000.00, 0.00, true, now(), now())
on conflict (document_number) do update set
    business_name = excluded.business_name,
    credit_limit = excluded.credit_limit,
    active = excluded.active,
    updated_at = now();

insert into vehicles (id, plate, driver_name, active, created_at, updated_at) values
('30000000-0000-0000-0000-000000000001', 'B8R-921', 'Carlos Mendoza Ruiz', true, now(), now()),
('30000000-0000-0000-0000-000000000002', 'F2D-614', 'Luis Herrera Campos', true, now(), now()),
('30000000-0000-0000-0000-000000000003', 'T7K-338', 'Marco Salazar Rojas', true, now(), now()),
('30000000-0000-0000-0000-000000000004', 'V9P-204', 'Jorge Alva Torres', true, now(), now())
on conflict (plate) do update set
    driver_name = excluded.driver_name,
    active = excluded.active,
    updated_at = now();

insert into stock_pushes (id, product_id, stock_date, physical_stock, created_at, updated_at) values
('40000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', current_date, 850.000, now(), now()),
('40000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000002', current_date, 420.000, now(), now()),
('40000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000003', current_date, 600.000, now(), now()),
('40000000-0000-0000-0000-000000000004', '10000000-0000-0000-0000-000000000004', current_date, 120.000, now(), now()),
('40000000-0000-0000-0000-000000000005', '10000000-0000-0000-0000-000000000005', current_date, 80.000, now(), now()),
('40000000-0000-0000-0000-000000000006', '10000000-0000-0000-0000-000000000006', current_date, 300.000, now(), now())
on conflict (id) do nothing;

insert into production_plans (id, product_id, production_date, quantity, approved_extra, created_at, updated_at) values
('50000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', current_date, 250.000, false, now(), now()),
('50000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000002', current_date, 180.000, false, now(), now()),
('50000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000003', current_date + interval '1 day', 220.000, true, now(), now())
on conflict (id) do nothing;

insert into sales (id, client_id, product_id, quantity, unit_price, status, price_exception_authorized, credit_exception_authorized, created_at, updated_at) values
('60000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', 40.000, 168.00, 'CONFIRMED', false, false, now(), now()),
('60000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000002', 25.000, 182.00, 'CONFIRMED', false, false, now(), now())
on conflict (id) do nothing;

insert into dispatch_orders (id, sale_id, vehicle_id, status, created_at, updated_at) values
('70000000-0000-0000-0000-000000000001', '60000000-0000-0000-0000-000000000001', '30000000-0000-0000-0000-000000000001', 'LOADED', now(), now()),
('70000000-0000-0000-0000-000000000002', '60000000-0000-0000-0000-000000000002', '30000000-0000-0000-0000-000000000002', 'PREPARING', now(), now())
on conflict (id) do nothing;

insert into audit_logs (id, actor, action, entity_name, entity_id, detail, created_at, updated_at) values
('80000000-0000-0000-0000-000000000001', 'system', 'SEED', 'Database', 'V2', 'Carga inicial de datos GMCA', now(), now())
on conflict (id) do nothing;