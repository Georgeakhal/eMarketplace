CREATE TABLE IF NOT EXISTS public.post (
    id VARCHAR NOT NULL,
    name VARCHAR,
    price INTEGER,
    description VARCHAR,
    submission_time DATE,
    photo_url VARCHAR,
    CONSTRAINT post_pkey PRIMARY KEY (id)
);
