CREATE TABLE IF NOT EXISTS `people_books` (
  `people_id` bigint(20) NOT NULL,
  `book_id` int(10) NOT NULL,
  PRIMARY KEY (`people_id`, `book_id`),
  FOREIGN KEY (`people_id`) REFERENCES `people`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`book_id`) REFERENCES `books`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;