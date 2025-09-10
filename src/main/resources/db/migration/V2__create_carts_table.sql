-- carts table
CREATE TABLE carts (
  id BINARY(16) NOT NULL,
  date_created DATE NOT NULL DEFAULT CURRENT_DATE,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

-- carts_item table
CREATE TABLE carts_item (
  id BIGINT NOT NULL AUTO_INCREMENT,
  cart_id BINARY(16) NOT NULL,
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL DEFAULT 1,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

-- foreign key to carts
ALTER TABLE carts_item
ADD CONSTRAINT cart_items_carts__fk
FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE;

-- foreign key to products
ALTER TABLE carts_item
ADD CONSTRAINT cart_items_products__fk
FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE;

-- unique constraint
ALTER TABLE carts_item
ADD CONSTRAINT cart_items_cart_product_unique
UNIQUE (cart_id, product_id);
