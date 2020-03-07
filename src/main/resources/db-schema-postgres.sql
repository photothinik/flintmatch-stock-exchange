create table stock_order (
        id serial primary key,
        trader_id integer not null,
        order_type char(1) not null,
        stock_symbol varchar(25) not null,
        total_quantity integer not null,
        fulfilled char(1) not null default 'N'
)

create table stock_order_transaction (
        seller_id integer not null,
        buyer_id integer not null,
        quantity integer not null,
        reserved char(1) not null default 'N',
        committed char(1) not null default 'N',
        primary key (seller_id, buyer_id)
)


create table stock_trader (

        id serial primary key,
        name varchar(255) not null

)