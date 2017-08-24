create grain filters version '1.0';

create table aFilter (
  id int identity not null primary key,
  date datetime,
  number1 int,
  number2 int,
  noIndexA int
);

create index idxDateNumb1Numb2 on aFilter (date, number1, number2);

create table bFilter (
  id int identity not null primary key,
  created datetime default GETDATE(),
  numb1 int,
  numb2 int,
  noIndexB int
);

create index idxCreatedNumb1Numb2 on bFilter (created, numb1, numb2);

create table cFilter (
  id int not null primary key
);

create table dFilter (
  id int not null primary key
);


create table eFilter (
  id int not null,
  number int not null,
  str varchar(2) not null,
  CONSTRAINT Pk_filters_e PRIMARY KEY (id, number, str)
);

create table fFilter (
  id int not null,
  numb int not null,
  CONSTRAINT Pk_filters_d PRIMARY KEY (id, numb)
);
