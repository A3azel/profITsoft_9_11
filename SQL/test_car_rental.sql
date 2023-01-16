drop database if exists test_car_rental;

create database if not exists test_car_rental;

USE test_car_rental;

CREATE TABLE driver(
    id int auto_increment primary key,
    first_name varchar(64) not null,
    last_name varchar(64) not null
);

CREATE TABLE car(
    id int auto_increment primary key,
    car_name varchar(256) not null,
    brand varchar(64) not null,
    car_prise decimal(8,2) not null,
    release_date timestamp not null,
    driver_id int not null,
    foreign key(driver_id) references driver(id)
); 