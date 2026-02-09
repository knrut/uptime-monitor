create table users (
  id bigserial primary key,
  username varchar(64) not null unique,
  password_hash varchar(100) not null
);

create table target (
  id bigserial primary key,
  user_id bigint not null references users(id) on delete cascade,
  name text not null,
  url  text not null,
  enabled boolean not null default true,
  check_every_sec int not null default 30,
  constraint uq_target_user_url unique (user_id, url)
);

create table check_result (
  id bigserial primary key,
  target_id bigint not null references target(id) on delete cascade,
  status text not null,
  latency_ms int not null,
  error_msg text,
  created_at timestamptz not null default now()
);

create index idx_check_result_target_created
  on check_result(target_id, created_at desc);