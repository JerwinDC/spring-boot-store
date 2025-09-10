CREATE TABLE `store_api`.`orders` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `customer_id` BIGINT NOT NULL,
  `status` VARCHAR(20) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `total_price` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

ALTER TABLE `orders` ADD CONSTRAINT `orders_users__fk` FOREIGN KEY (`customer_id`) REFERENCES `orders`(`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;


CREATE TABLE `store_api`.`order_items` (`id` BIGINT NOT NULL AUTO_INCREMENT , `order_id` BIGINT NOT NULL , `product_id` BIGINT NOT NULL , `unit_price` DECIMAL(10,2) NOT NULL , `quantity` INT NOT NULL , `total_price` DECIMAL(10,2) NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;

ALTER TABLE `order_items` ADD CONSTRAINT `orders_items_orders_id__fk` FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`) ON DELETE RESTRICT ON UPDATE RESTRICT; ALTER TABLE `order_items` ADD CONSTRAINT `order_items_product__fk` FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
