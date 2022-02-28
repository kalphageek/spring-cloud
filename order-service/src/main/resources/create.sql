CREATE TABLE orders (
  id BIGINT AUTO_INCREMENT NOT NULL,
   product_id VARCHAR(255) NOT NULL,
   qty INT NOT NULL default 0,
   unit_price INT NOT NULL default 0,
   total_amount INT NOT NULL default 0,
   order_id VARCHAR(255) NOT NULL,
   user_id VARCHAR(255) NOT NULL,
   created_at datetime NOT NULL default now(),
   CONSTRAINT pk_orders PRIMARY KEY (id)
);