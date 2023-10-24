CREATE TABLE IF NOT EXISTS public.users (
	id uuid NOT NULL,
	first_name varchar(100) NOT NULL,
	last_name varchar(50) NOT NULL,
	street_name varchar(255) NOT NULL,
	street_number varchar(10) NOT NULL,
	telephone varchar(25) NOT NULL,
	borrowing_limit integer NOT NULL,
	reservation_limit integer NOT NULL,
	email varchar(255) NOT NULL,
	role varchar(255) NOT NULL,
	password varchar(255) NOT NULL,
	created_at timestamp NOT NULL,
	updated_at timestamp NOT NULL,
	CONSTRAINT client_pk PRIMARY KEY (id),
	CONSTRAINT user_email_unique UNIQUE (email),
	CONSTRAINT user_role_check CHECK ((
	    (role)::text = ANY
	        ((ARRAY[
	            'CLIENT'::character varying,
	            'ADMIN'::character varying,
	            'LIBRARIAN'::character varying]
            )::text[])
        )
    )
);