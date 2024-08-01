CREATE DATABASE hotel_db;

USE hotel_db;

-- Table: reservations
CREATE TABLE reservations(
reservation_id INT PRIMARY KEY AUTO_INCREMENT,
guest_name varchar(255) not NULL,
room_number INT NOT NULL,
contact_number varchar(10) NOT NULL,
reservation_date timestamp default current_timestamp
);
select * from reservations;
CREATE TABLE rooms(
room_number INT primary key,
room_type varchar(255) not null,
room_price decimal(10,2) not null
);
select * from rooms;
create table customers(
customer_id int auto_increment primary key,
customer_name varchar(255) not null,
contact_number varchar(20) not null
);
select * from customers;

describe reservations;
describe Room; 
create user 'user'@'localhost' identified by 'mini@5';
grant all privileges on hotel_db.* to 'user'@'localhost';
flush privileges;
select user,host from mysql.user;
show grants for 'user'@'localhost';
select * from Room;