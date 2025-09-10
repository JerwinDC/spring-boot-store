-- carts table
CREATE TABLE IF NOT EXISTS carts (
  id BINARY(16) NOT NULL,
  date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

-- carts_item table
CREATE TABLE IF NOT EXISTS carts_item (
  id BIGINT NOT NULL AUTO_INCREMENT,
  cart_id BINARY(16) NOT NULL,
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  UNIQUE KEY cart_product_unique (cart_id, product_id),
  CONSTRAINT cart_items_carts_fk FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
  CONSTRAINT cart_items_products_fk FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) ENGINE=InnoDB;