CREATE TABLE IF NOT EXISTS `user`(
  `id` INT UNSIGNED AUTO_INCREMENT,
  `username` VARCHAR(20) NOT NULL,
  `password` VARCHAR(16) NOT NULL,
  `email` VARCHAR(50),
  `date_joined` timestamp default now(),
  `date_update` timestamp default now() ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;