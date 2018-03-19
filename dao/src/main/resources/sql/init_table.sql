CREATE TABLE IF NOT EXISTS `user`(
  `id` INT UNSIGNED AUTO_INCREMENT,
  `username` VARCHAR(20) NOT NULL,
  `password` VARCHAR(16) NOT NULL,
  `email` VARCHAR(50),
  `date_joined` timestamp default now(),
  `date_update` timestamp default now() ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `redis_memory`(
  `id` INT UNSIGNED AUTO_INCREMENT,
  `task_name` VARCHAR(64) NOT NULL,
  `used_memory` INT UNSIGNED NOT NULL,
  `used_memory_human` FLOAT UNSIGNED NOT NULL,
  `used_memory_rss` INT UNSIGNED NOT NULL,
  `used_memory_peak` INT UNSIGNED NOT NULL,
  `used_memory_peak_human` FLOAT UNSIGNED NOT NULL,
  `used_memory_lua` INT UNSIGNED NOT NULL,
  `used_memory_lua_human` FLOAT UNSIGNED NOT NULL,
  `mem_fragmentation_ratio` FLOAT UNSIGNED NOT NULL,
  `mem_allocator` VARCHAR(32) NOT NULL,
  `date` timestamp default now(),
  PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `redis_cpu`(
  `id` INT UNSIGNED AUTO_INCREMENT,
  `task_name` VARCHAR(64) NOT NULL,
  `used_cpu_sys` FLOAT UNSIGNED NOT NULL,
  `used_cpu_user` FLOAT UNSIGNED NOT NULL,
  `used_cpu_sys_children` FLOAT UNSIGNED NOT NULL,
  `used_cpu_user_children` FLOAT UNSIGNED NOT NULL,
  `date` timestamp default now(),
  PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;