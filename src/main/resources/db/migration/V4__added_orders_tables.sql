-- orders table
CREATE TABLE orders (
  id BIGINT NOT NULL AUTO_INCREMENT,
  customer_id BIGINT NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  total_price DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

-- correct foreign key
ALTER TABLE orders
ADD CONSTRAINT orders_users__fk
FOREIGN KEY (customer_id) REFERENCES users(id)
ON DELETE RESTRICT ON UPDATE RESTRICT;

-- order_items table
CREATE TABLE order_items (
  id BIGINT NOT NULL AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  unit_price DECIMAL(10,2) NOT NULL,
  quantity INT NOT NULL,
  total_price DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT order_items_orders_fk FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT order_items_product_fk FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB;