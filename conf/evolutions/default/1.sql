# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table evaluation_result (
  id                        bigint not null,
  web_page_id               integer not null,
  cre_date                  timestamp,
  ticket_number             varchar(255),
  result                    text,
  constraint uq_evaluation_result_ticket_numb unique (ticket_number),
  constraint pk_evaluation_result primary key (id))
;

create table hyperlink (
  id                        bigint not null,
  parent_page_id            integer,
  target                    varchar(255),
  text                      varchar(255),
  constraint pk_hyperlink primary key (id))
;

create table web_page (
  id                        integer not null,
  url                       varchar(255),
  constraint uq_web_page_url unique (url),
  constraint pk_web_page primary key (id))
;

create sequence evaluation_result_seq;

create sequence hyperlink_seq;

create sequence web_page_seq;

alter table evaluation_result add constraint fk_evaluation_result_web_page_1 foreign key (web_page_id) references web_page (id);
create index ix_evaluation_result_web_page_1 on evaluation_result (web_page_id);
alter table hyperlink add constraint fk_hyperlink_parentPage_2 foreign key (parent_page_id) references web_page (id);
create index ix_hyperlink_parentPage_2 on hyperlink (parent_page_id);



# --- !Downs

drop table if exists evaluation_result cascade;

drop table if exists hyperlink cascade;

drop table if exists web_page cascade;

drop sequence if exists evaluation_result_seq;

drop sequence if exists hyperlink_seq;

drop sequence if exists web_page_seq;

