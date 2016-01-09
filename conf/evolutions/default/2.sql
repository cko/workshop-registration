# --- !Ups

create table "users" ("id" varchar not null primary key, "first_name" varchar not null, "last_name" varchar not null, "email" varchar not null);
create table "logininfo" ("id" integer primary key autoincrement,"providerID" VARCHAR NOT NULL,"providerKey" VARCHAR NOT NULL);
create table "userlogininfo" ("userID" VARCHAR NOT NULL,"loginInfoId" BIGINT NOT NULL);
create table "passwordinfo" ("hasher" VARCHAR NOT NULL,"password" VARCHAR NOT NULL,"salt" VARCHAR,"loginInfoId" BIGINT NOT NULL);

# --- !Downs

drop table "passwordinfo";
drop table "userlogininfo";
drop table "logininfo";
drop table "users";