# --- !Ups

create table "workshops" (
  "id" integer primary key autoincrement,
  "regstart" integer not null,
  "regend" text not null,
  "regmax" text not null,
  "when" text not null,
  "title" text not null,
  "description" text not null
);

alter table "registrations"
  add column "workshop_id" integer default 0

# --- !Downs

drop table "workshops";
