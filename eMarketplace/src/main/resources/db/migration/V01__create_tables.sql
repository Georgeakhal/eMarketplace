CREATE TABLE IF NOT EXISTS public."Post"
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    name character varying COLLATE pg_catalog."default",
    price integer,
    description character varying COLLATE pg_catalog."default",
    "submittionTime" date,
    "photoUrl" character varying COLLATE pg_catalog."default",
    CONSTRAINT "Post_pkey" PRIMARY KEY (id)
);
