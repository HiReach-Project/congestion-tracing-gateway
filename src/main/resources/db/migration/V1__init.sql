create table company
(
    id         int primary key,
    name       varchar not null,
    access_key varchar not null
);
create sequence company_seq;

create table node
(
    id   int primary key,
    name varchar not null,
    url  varchar not null
);
create sequence node_seq;



