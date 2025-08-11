INSERT INTO categories (id, name, description, is_deleted) VALUES (1, 'Fantasy', 'Fantasy category', false);
INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted) VALUES (1, 'The Hobbit', 'J.R.R. Tolkien', '978-0345339683', 15.99, 'A fantasy novel', 'hobbit.jpg', false);
INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);