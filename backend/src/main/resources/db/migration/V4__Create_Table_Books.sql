CREATE TABLE public.books (
	id uuid NOT NULL,
	title varchar(255) NOT NULL,
	isbn varchar(13) NOT NULL,
	pages integer NOT NULL,
	key_words varchar(1000),
	year_publication date NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
	id_publishing_company uuid NOT NULL,
	id_classification_books uuid NOT NULL,
	id_author_books uuid NOT NULL,
	foreign key (id_publishing_company) references publishing_company(id),
	foreign key (id_classification_books) references classification_books(id),
	foreign key (id_author_books) references author_books(id),
	CONSTRAINT books_pk PRIMARY KEY (id),
	CONSTRAINT book_title_unique_title UNIQUE (title)
);