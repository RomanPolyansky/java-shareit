CREATE TABLE IF NOT EXISTS `users`
(
    `id`    INT          NOT NULL AUTO_INCREMENT,
    `name`  varchar(255) NOT NULL,
    `email` varchar(255) NOT NULL UNIQUE,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `items`
(
    `id`           INT          NOT NULL AUTO_INCREMENT,
    `name`         varchar(255) NOT NULL,
    `description`  varchar(255) NOT NULL,
    `is_available` BOOLEAN      NOT NULL,
    `owner_id`     INT          NOT NULL,
    `request_id`   INT          NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `bookings`
(
    `id`         INT          NOT NULL AUTO_INCREMENT,
    `start_date` TIMESTAMP    NOT NULL,
    `end_date`   TIMESTAMP    NOT NULL,
    `booker_id`  INT          NOT NULL,
    `item_id`    INT          NOT NULL,
    `status`     VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `requests`
(
    `id`           INT          NOT NULL AUTO_INCREMENT,
    `description`  varchar(255) NOT NULL,
    `requestor_id` INT          NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `comments`
(
    `id`        INT          NOT NULL AUTO_INCREMENT,
    `text`      varchar(255) NOT NULL,
    `author_id` INT          NOT NULL,
    `item_id`   INT          NOT NULL,
    PRIMARY KEY (`id`)
);

ALTER TABLE `items`
    ADD CONSTRAINT IF NOT EXISTS `items_fk0` FOREIGN KEY  (`owner_id`) REFERENCES `users` (`id`);

ALTER TABLE `items`
    ADD CONSTRAINT IF NOT EXISTS `items_fk1` FOREIGN KEY (`request_id`) REFERENCES `requests` (`id`);

ALTER TABLE `bookings`
    ADD CONSTRAINT IF NOT EXISTS `bookings_fk0` FOREIGN KEY (`booker_id`) REFERENCES `users` (`id`);

ALTER TABLE `bookings`
    ADD CONSTRAINT IF NOT EXISTS `bookings_fk1` FOREIGN KEY (`item_id`) REFERENCES `items` (`id`);

ALTER TABLE `requests`
    ADD CONSTRAINT IF NOT EXISTS `requests_fk0` FOREIGN KEY (`requestor_id`) REFERENCES `users` (`id`);

ALTER TABLE `comments`
    ADD CONSTRAINT IF NOT EXISTS `comments_fk0` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`);

ALTER TABLE `comments`
    ADD CONSTRAINT IF NOT EXISTS `comments_fk1` FOREIGN KEY (`item_id`) REFERENCES `items` (`id`);





