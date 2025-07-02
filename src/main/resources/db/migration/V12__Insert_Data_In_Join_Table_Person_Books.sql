INSERT INTO people_books (people_id, book_id)
SELECT 
    p.id AS people_id, 
    b.id AS book_id
FROM 
    (SELECT id FROM people WHERE id <= 12) p
CROSS JOIN
    (SELECT id FROM books ORDER BY RAND() LIMIT 20) b;

INSERT INTO people_books (people_id, book_id)
SELECT
    p.id AS people_id,
    b.id AS book_id
FROM
    (SELECT id FROM people WHERE id > 12) p
CROSS JOIN
    (SELECT id FROM books ORDER BY RAND() LIMIT 3) b;