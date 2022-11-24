create schema if not exists semes;

set search_path to semes;

drop table if exists orders_product cascade ;
drop table if exists orders cascade;
drop table if exists basket cascade;
drop table if exists promotion_product cascade;
drop table if exists promotion cascade;
drop table if exists event cascade;
drop table if exists category cascade;
drop table if exists products cascade;
drop table if exists users cascade;

create table users(

    id bigserial primary key,
    login varchar(30) unique,
    name varchar(50),
    password varchar(32),
    email varchar(100),
    phone_number varchar(11),
    role varchar(30),
    uuid varchar(36)

);

create table category (

    name varchar(30) unique,
    parent_name varchar(30),
    foreign key (parent_name) references category(name) on update cascade on delete cascade

);

create table products(

    id bigserial primary key,
    name varchar(50),
    path_image varchar(50),
    price int,
    feature text,
    description text,
    category varchar(100),
    count int,
    foreign key (category) references category(name) on update cascade on delete cascade

);

create table event(

    name varchar(30),
    description text,
    date_begin date,
    date_end date

);

create table promotion(

    id bigserial primary key,
    date_begin date,
    date_end date,
    name varchar(30),
    discount int,
    description text

);

create table promotion_product(
    id_promotion bigint,
    id_product bigint,
    foreign key (id_promotion) references promotion(id) on update cascade on delete cascade ,
    foreign key (id_product) references products(id) on update cascade on delete cascade

);

create table basket (
    id_product bigint,
    id_user bigint,
    count int,
    foreign key (id_product) references products(id) on update cascade on delete cascade,
    foreign key (id_user) references users(id) on update cascade on delete cascade
);

create table orders (
    id bigserial primary key,
    id_user bigint,
    number int,
    date date,
    status varchar,
    foreign key (id_user) references users(id) on update cascade on delete cascade
);

create table orders_product (
    id_order bigint,
    id_product bigint,
    count int,
    price int,
    foreign key (id_order) references orders(id) on update cascade on delete cascade,
    foreign key (id_product) references products(id) on update cascade on delete cascade
);


-- insert into category (name, parent_name) values ('Цветы', null),
--                                                 ('Отростки', null),
--                                                 ('Гераньи', 'Цветы'),
--                                                 ('Розы', 'Цветы'),
--                                                 ('Всякие отростки', 'Отростки'),
--                                                 ('Конкретные отростки', 'Отростки');

-- insert into products (name, path_image, price, feature, description, category, count) values ('Герань', '252d4225.jpg', 200, null, 'yes', 'Цветы', 100)

