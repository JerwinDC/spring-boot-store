-- First, drop the wrong foreign key constraint
ALTER TABLE `orders` DROP FOREIGN KEY `orders_users__fk`;

-- Then, create the correct one pointing to users(id)
ALTER TABLE `orders`
ADD CONSTRAINT `orders_users__fk`
FOREIGN KEY (`customer_id`) REFERENCES `users`(`id`)
ON DELETE RESTRICT ON UPDATE RESTRICT;