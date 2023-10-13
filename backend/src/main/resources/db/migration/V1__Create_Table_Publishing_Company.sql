CREATE TABLE IF NOT EXISTS public.publishing_company (
	id uuid NOT NULL,
	name varchar(255) NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
	CONSTRAINT publishing_company_pk PRIMARY KEY (id),
	CONSTRAINT publishing_company_name_unique UNIQUE (name)
);