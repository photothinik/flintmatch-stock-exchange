create table order_transaction (
        id serial primary key,
        trader_id integer not null,
        order_type char(1) not null,
        stock_symbol varchar(25) not null,
        quantity integer not null,
        fulfilled char(1) not null default 'N',
        confirmed char(1) not null default 'N'
)


create table trader (

        id serial primary key,
        name varchar(255) not null

)