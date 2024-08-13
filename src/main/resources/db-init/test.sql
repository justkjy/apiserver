create table coffee_order
(
    id           bigint auto_increment
        primary key,
    created_date datetime(6)  not null,
    updated_date datetime(6)  not null,
    coffee_name  varchar(255) null,
    description  varchar(255) null,
    order_count  int          null,
    order_name   varchar(255) null
);

