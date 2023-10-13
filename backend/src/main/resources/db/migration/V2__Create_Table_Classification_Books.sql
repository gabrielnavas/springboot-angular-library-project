CREATE TABLE public.classification_books (
	id uuid NOT NULL,
	name varchar(100) NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
	CONSTRAINT classification_books_pk PRIMARY KEY (id),
	CONSTRAINT classification_books_unique_name UNIQUE (name)
);