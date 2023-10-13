CREATE TABLE IF NOT EXISTS public.author_books (
	id uuid NOT NULL,
	name varchar(255) NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
	CONSTRAINT author_books_unique_name UNIQUE (name),
	CONSTRAINT author_books_pk PRIMARY KEY (id)
);
