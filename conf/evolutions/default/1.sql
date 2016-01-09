# --- !Ups

create table "registrations" (
  "id" integer primary key autoincrement,
  "name" varchar not null,
  "email" varchar not null
);

# --- !Downs

drop table "registrations";