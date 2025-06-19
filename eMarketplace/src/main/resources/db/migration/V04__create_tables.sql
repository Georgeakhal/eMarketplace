CREATE TABLE IF NOT EXISTS public.post (
    id UUID NOT NULL,
    username VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    birthday DATE,
    CONSTRAINT post_pkey PRIMARY KEY (id)
);

