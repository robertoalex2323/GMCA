-- Normalizacion de seguridad: catalogo de roles y relacion FK con usuarios.
-- No modifica migraciones anteriores para evitar checksum mismatch de Flyway.

create table if not exists roles (
    name varchar(60) primary key,
    description varchar(180) not null,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

insert into roles (name, description, created_at, updated_at) values
('ROLE_GERENCIA', 'Dashboard ejecutivo y autorizaciones criticas', now(), now()),
('ROLE_VENTAS', 'Cotizacion, confirmacion de ventas y pagos', now(), now()),
('ROLE_ALMACEN', 'Stock push y preparacion de pedidos sin precios', now(), now()),
('ROLE_PRODUCCION', 'Molienda y produccion programada sin precios', now(), now()),
('ROLE_CONTABILIDAD', 'Validacion financiera, facturas y deuda', now(), now()),
('ROLE_DESPACHO', 'Pizarra de carga y salida en garita', now(), now())
on conflict (name) do update set
    description = excluded.description,
    updated_at = now();

insert into app_users (id, username, password_hash, enabled, created_at, updated_at) values
('00000000-0000-0000-0000-000000000001', 'gerencia', '{noop}admin123', true, now(), now()),
('00000000-0000-0000-0000-000000000002', 'ventas', '{noop}admin123', true, now(), now()),
('00000000-0000-0000-0000-000000000003', 'almacen', '{noop}admin123', true, now(), now()),
('00000000-0000-0000-0000-000000000004', 'produccion', '{noop}admin123', true, now(), now()),
('00000000-0000-0000-0000-000000000005', 'contabilidad', '{noop}admin123', true, now(), now()),
('00000000-0000-0000-0000-000000000006', 'despacho', '{noop}admin123', true, now(), now())
on conflict (username) do update set
    password_hash = excluded.password_hash,
    enabled = true,
    updated_at = now();

insert into app_user_roles (user_id, role)
select u.id, r.role
from (
    values
    ('gerencia', 'ROLE_GERENCIA'),
    ('ventas', 'ROLE_VENTAS'),
    ('almacen', 'ROLE_ALMACEN'),
    ('produccion', 'ROLE_PRODUCCION'),
    ('contabilidad', 'ROLE_CONTABILIDAD'),
    ('despacho', 'ROLE_DESPACHO')
) as r(username, role)
join app_users u on u.username = r.username
on conflict (user_id, role) do nothing;

do $$
begin
    if not exists (
        select 1
        from pg_constraint
        where conname = 'fk_app_user_roles_role'
    ) then
        alter table app_user_roles
        add constraint fk_app_user_roles_role
        foreign key (role) references roles(name)
        on update cascade
        on delete restrict;
    end if;
end $$;

create index if not exists idx_app_user_roles_role on app_user_roles(role);

insert into audit_logs (id, actor, action, entity_name, entity_id, detail, created_at, updated_at) values
('80000000-0000-0000-0000-000000000003', 'system', 'SECURITY_NORMALIZE', 'Database', 'V4', 'Tabla roles y FK app_user_roles.role normalizadas', now(), now())
on conflict (id) do nothing;