create table app_users (
    id uuid primary key,
    username varchar(120) not null unique,
    password_hash varchar(255) not null,
    enabled boolean not null default true,
    created_at timestamptz not null,
    updated_at timestamptz not null
);

create table app_user_roles (
    user_id uuid not null references app_users(id) on delete cascade,
    role varchar(60) not null,
    primary key (user_id, role)
);

create table audit_logs (
    id uuid primary key,
    actor varchar(120) not null,
    action varchar(80) not null,
    entity_name varchar(120) not null,
    entity_id varchar(120) not null,
    detail text,
    created_at timestamptz not null,
    updated_at timestamptz not null
);

create table clients (
    id uuid primary key,
    business_name varchar(180) not null,
    document_number varchar(40) not null unique,
    credit_limit numeric(14,2) not null default 0,
    current_debt numeric(14,2) not null default 0,
    active boolean not null default true,
    created_at timestamptz not null,
    updated_at timestamptz not null
);

create table vehicles (
    id uuid primary key,
    plate varchar(30) not null unique,
    driver_name varchar(160),
    active boolean not null default true,
    created_at timestamptz not null,
    updated_at timestamptz not null
);

create table products (
    id uuid primary key,
    sku varchar(60) not null unique,
    name varchar(180) not null,
    type varchar(40) not null,
    base_price numeric(14,2) not null default 0,
    created_at timestamptz not null,
    updated_at timestamptz not null
);

create table stock_pushes (
    id uuid primary key,
    product_id uuid not null references products(id),
    stock_date date not null,
    physical_stock numeric(14,3) not null default 0,
    created_at timestamptz not null,
    updated_at timestamptz not null
);

create table production_plans (
    id uuid primary key,
    product_id uuid not null references products(id),
    production_date date not null,
    quantity numeric(14,3) not null,
    approved_extra boolean not null default false,
    created_at timestamptz not null,
    updated_at timestamptz not null
);

create table sales (
    id uuid primary key,
    client_id uuid not null references clients(id),
    product_id uuid not null references products(id),
    quantity numeric(14,3) not null,
    unit_price numeric(14,2) not null,
    status varchar(30) not null,
    price_exception_authorized boolean not null default false,
    credit_exception_authorized boolean not null default false,
    created_at timestamptz not null,
    updated_at timestamptz not null
);

create table dispatch_orders (
    id uuid primary key,
    sale_id uuid not null references sales(id),
    vehicle_id uuid not null references vehicles(id),
    status varchar(40) not null,
    created_at timestamptz not null,
    updated_at timestamptz not null
);

insert into app_users (id, username, password_hash, enabled, created_at, updated_at) values
('00000000-0000-0000-0000-000000000001', 'gerencia', '{noop}admin123', true, now(), now()),
('00000000-0000-0000-0000-000000000002', 'ventas', '{noop}admin123', true, now(), now()),
('00000000-0000-0000-0000-000000000003', 'almacen', '{noop}admin123', true, now(), now()),
('00000000-0000-0000-0000-000000000004', 'produccion', '{noop}admin123', true, now(), now()),
('00000000-0000-0000-0000-000000000005', 'contabilidad', '{noop}admin123', true, now(), now()),
('00000000-0000-0000-0000-000000000006', 'despacho', '{noop}admin123', true, now(), now());

insert into app_user_roles (user_id, role) values
('00000000-0000-0000-0000-000000000001', 'ROLE_GERENCIA'),
('00000000-0000-0000-0000-000000000002', 'ROLE_VENTAS'),
('00000000-0000-0000-0000-000000000003', 'ROLE_ALMACEN'),
('00000000-0000-0000-0000-000000000004', 'ROLE_PRODUCCION'),
('00000000-0000-0000-0000-000000000005', 'ROLE_CONTABILIDAD'),
('00000000-0000-0000-0000-000000000006', 'ROLE_DESPACHO');
