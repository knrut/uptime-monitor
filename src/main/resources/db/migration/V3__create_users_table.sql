create table if not exists users (
  id bigserial primary key,
  username varchar(64) not null unique,
  password_hash varchar(100) not null
);
