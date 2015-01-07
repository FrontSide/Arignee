# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table evaluation_result (
  id                        bigint not null,
  ticket_number             bigint,
  result                    text,
  constraint pk_evaluation_result primary key (id))
;

create table hyperlink (
  id                        bigint not null,
  target                    varchar(255),
  text                      varchar(255),
  constraint pk_hyperlink primary key (id))
;

create table web_page (
  id                        bigint not null,
  url                       varchar(255),
  constraint pk_web_page primary key (id))
;

create sequence evaluation_result_seq;

create sequence hyperlink_seq;

create sequence web_page_seq;




# --- !Downs

drop table if exists evaluation_result cascade;

drop table if exists hyperlink cascade;

drop table if exists web_page cascade;

drop sequence if exists evaluation_result_seq;

drop sequence if exists hyperlink_seq;

drop sequence if exists web_page_seq;

