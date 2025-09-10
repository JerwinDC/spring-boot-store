CREATE TABLE `store_api`.`carts` (
  `id` BINARY(16) NOT NULL,
  `date_created` DATE NOT NULL DEFAULT (curdate()),
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

CREATE TABLE `store_api`.`carts_item` (`id` BIGINT NOT NULL AUTO_INCREMENT , `cart_id` BINARY(16) NOT NULL , `product_id` BIGINT NOT NULL , `quantity` INT NOT NULL DEFAULT '1' , PRIMARY KEY (`id`)) ENGINE = InnoDB;

ALTER TABLE `carts_item` ADD CONSTRAINT `cart_items_carts__fk` FOREIGN KEY (`cart_id`) REFERENCES `carts`(`id`) ON DELETE CASCADE ON UPDATE NO ACTION; ALTER TABLE `carts_item` ADD CONSTRAINT `cart_items_products__fk` FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

ALTER TABLE `store_api`.`carts_item`
ADD CONSTRAINT cart_items_cart_product_unique UNIQUE (`cart_id`, `product_id`);