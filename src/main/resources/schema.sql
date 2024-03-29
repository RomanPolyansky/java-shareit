DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS item_requests;
DROP TABLE IF EXISTS users;


CREATE TABLE IF NOT EXISTS `users`
(
    `id`    INT          NOT NULL AUTO_INCREMENT,
    `name`  varchar(255) NOT NULL,
    `email` varchar(255) NOT NULL UNIQUE,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `item_requests`
(
    `id`           INT          NOT NULL AUTO_INCREMENT,
    `description`  varchar(255) NOT NULL,
    `requester_id` INT          NOT NULL,
    `created`      TIMESTAMP    NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY  (`requester_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `items`
(
    `id`           INT          NOT NULL AUTO_INCREMENT,
    `name`         varchar(255) NOT NULL,
    `description`  varchar(255) NOT NULL,
    `is_available` BOOLEAN      NOT NULL,
    `owner_id`     INT          NOT NULL,
    `request_id`  INT,
    PRIMARY KEY (`id`),
    FOREIGN KEY  (`owner_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY  (`request_id`) REFERENCES `item_requests` (`id`) ON DELETE SET NULL,
    CONSTRAINT name_empty CHECK (`name` NOT LIKE ' ' AND `name` NOT LIKE ''),
    CONSTRAINT items_description_empty CHECK (`description` NOT LIKE ' ' AND `description` NOT LIKE '')
);

CREATE TABLE IF NOT EXISTS `bookings`
(
    `id`         INT          NOT NULL AUTO_INCREMENT,
    `start_date` TIMESTAMP    NOT NULL,
    `end_date`   TIMESTAMP    NOT NULL,
    `booker_id`  INT          NOT NULL,
    `item_id`    INT          NOT NULL,
    `status`     VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`booker_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`item_id`) REFERENCES `items` (`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `comments`
(
    `id`        INT          NOT NULL AUTO_INCREMENT,
    `text`      varchar(255) NOT NULL,
    `author_id` INT          NOT NULL,
    `item_id`   INT          NOT NULL,
    `created`   TIMESTAMP    NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`author_id`) REFERENCES `users` (`id`),
    FOREIGN KEY (`item_id`) REFERENCES `items` (`id`)
);
