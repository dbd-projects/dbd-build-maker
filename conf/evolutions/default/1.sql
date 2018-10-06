# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table addons (
  id                            bigint auto_increment not null,
  type                          integer not null,
  name                          varchar(255) not null,
  description                   varchar(255),
  constraint ck_addons_type check ( type in (0,1)),
  constraint uq_addons_name unique (name),
  constraint pk_addons primary key (id)
);

create table characters (
  id                            bigint auto_increment not null,
  type                          integer not null,
  name                          varchar(255) not null,
  description                   varchar(255),
  constraint ck_characters_type check ( type in (0,1)),
  constraint uq_characters_name unique (name),
  constraint pk_characters primary key (id)
);

create table items (
  id                            bigint auto_increment not null,
  type                          integer not null,
  name                          varchar(255) not null,
  description                   varchar(255),
  constraint ck_items_type check ( type in (0,1)),
  constraint uq_items_name unique (name),
  constraint pk_items primary key (id)
);

create table perks (
  id                            bigint auto_increment not null,
  type                          integer not null,
  name                          varchar(255) not null,
  description                   varchar(255),
  constraint ck_perks_type check ( type in (0,1)),
  constraint uq_perks_name unique (name),
  constraint pk_perks primary key (id)
);


# --- !Downs

drop table if exists addons;

drop table if exists characters;

drop table if exists items;

drop table if exists perks;

