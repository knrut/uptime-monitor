create table if not exists target (
  id bigserial primary key,
  name text not null,
  url  text not null unique,
  enabled boolean not null default true,
  check_every_sec int not null default 30
);

create table if not exists check_result (
  id bigserial primary key,
  target_id bigint not null references target(id),
  status text not null,                 -- 'UP' / 'DOWN'
  latency_ms int not null,
  error_msg text,
  created_at timestamptz not null default now()
);

create index if not exists idx_check_result_target_created
  on check_result(target_id, created_at desc);

