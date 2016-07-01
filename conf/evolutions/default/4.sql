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

# --- !Downs

drop table "workshops";
