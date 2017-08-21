# --- !Ups

create table "game" (
  "id" serial not null primary key,
  "player" varchar not null,
  "save" text null,
  "running" boolean not null,
  "createdAt" timestamp not null,
  "lastPlayedAt" timestamp not null
);

# --- !Downs

drop table "game" if exists;
