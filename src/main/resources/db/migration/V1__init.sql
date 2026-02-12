create table users (
  id bigserial primary key,
  username varchar(64) not null unique,
  email varchar(255) not null unique,
  password_hash varchar(100) not null,
  email_verified boolean not null default false,
  banned boolean not null default false,
  created_at timestamptz not null default now()
);

create index idx_user_email on users(email);

create table email_verification_code (
  id bigserial primary key,
  user_id bigint not null references users(id) on delete cascade,
  code varchar(10) not null,
  expires_at timestamptz not null,
  created_at timestamptz not null default now()
);

create index idx_verif_user_id on email_verification_code(user_id);
create index idx_verif_expires_at on email_verification_code(expires_at);

create table target (
  id bigserial primary key,
  user_id bigint not null references users(id) on delete cascade,
  name text not null,
  url text not null,
  enabled boolean not null default true,
  check_every_sec int not null default 30,
  constraint uq_target_user_url unique (user_id, url)
);

create index idx_target_user_id on target(user_id);

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
