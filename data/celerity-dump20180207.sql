--
-- PostgreSQL database dump
--

-- Dumped from database version 10.6
-- Dumped by pg_dump version 10.5

-- Started on 2019-02-07 03:39:53 UTC

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE celerity;
--
-- TOC entry 2896 (class 1262 OID 20602)
-- Name: celerity; Type: DATABASE; Schema: -; Owner: docker
--

CREATE DATABASE celerity WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8';


ALTER DATABASE celerity OWNER TO docker;

\connect celerity

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 13001)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2899 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 197 (class 1259 OID 22521)
-- Name: settings; Type: TABLE; Schema: public; Owner: docker
--

CREATE TABLE public.settings (
    id bigint NOT NULL,
    language character varying(255),
    traduction character varying(255)
);


ALTER TABLE public.settings OWNER TO docker;

--
-- TOC entry 196 (class 1259 OID 22519)
-- Name: settings_id_seq; Type: SEQUENCE; Schema: public; Owner: docker
--

CREATE SEQUENCE public.settings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.settings_id_seq OWNER TO docker;

--
-- TOC entry 2900 (class 0 OID 0)
-- Dependencies: 196
-- Name: settings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: docker
--

ALTER SEQUENCE public.settings_id_seq OWNED BY public.settings.id;


--
-- TOC entry 199 (class 1259 OID 22532)
-- Name: users; Type: TABLE; Schema: public; Owner: docker
--

CREATE TABLE public.users (
    user_type character varying(31) NOT NULL,
    id bigint NOT NULL,
    creation timestamp without time zone,
    email character varying(255),
    lock timestamp without time zone,
    password character varying(255),
    firstname character varying(255),
    lastname character varying(255),
    picture character varying(255),
    setting_id bigint NOT NULL
);


ALTER TABLE public.users OWNER TO docker;

--
-- TOC entry 198 (class 1259 OID 22530)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: docker
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO docker;

--
-- TOC entry 2901 (class 0 OID 0)
-- Dependencies: 198
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: docker
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 2755 (class 2604 OID 22524)
-- Name: settings id; Type: DEFAULT; Schema: public; Owner: docker
--

ALTER TABLE ONLY public.settings ALTER COLUMN id SET DEFAULT nextval('public.settings_id_seq'::regclass);


--
-- TOC entry 2756 (class 2604 OID 22535)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: docker
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 2888 (class 0 OID 22521)
-- Dependencies: 197
-- Data for Name: settings; Type: TABLE DATA; Schema: public; Owner: docker
--

INSERT INTO public.settings (id, language, traduction) VALUES (2, NULL, NULL);
INSERT INTO public.settings (id, language, traduction) VALUES (1, 'es', 'es');


--
-- TOC entry 2890 (class 0 OID 22532)
-- Dependencies: 199
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: docker
--

INSERT INTO public.users (user_type, id, creation, email, lock, password, firstname, lastname, picture, setting_id) VALUES ('CLIENT', 1, '2019-01-31 23:26:16.782677', 'user@gmail.com', NULL, '17950858528ec9f8fed7b7ea11a703ad2b7b23acc6d65c1b44ffc7b88e02a2b718f9da6f86b1afdbd1de05758cd64f12e88182c1adf63de5c47095b55c2b3e8e', 'First', 'Last', './assets/avatars/avatar.png', 1);


--
-- TOC entry 2902 (class 0 OID 0)
-- Dependencies: 196
-- Name: settings_id_seq; Type: SEQUENCE SET; Schema: public; Owner: docker
--

SELECT pg_catalog.setval('public.settings_id_seq', 1, false);


--
-- TOC entry 2903 (class 0 OID 0)
-- Dependencies: 198
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: docker
--

SELECT pg_catalog.setval('public.users_id_seq', 1, false);


--
-- TOC entry 2758 (class 2606 OID 22529)
-- Name: settings settings_pkey; Type: CONSTRAINT; Schema: public; Owner: docker
--

ALTER TABLE ONLY public.settings
    ADD CONSTRAINT settings_pkey PRIMARY KEY (id);


--
-- TOC entry 2760 (class 2606 OID 22544)
-- Name: users uk_6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: docker
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- TOC entry 2762 (class 2606 OID 22542)
-- Name: users uk_9hnjksuc1u7qt5o1s6xeo279f; Type: CONSTRAINT; Schema: public; Owner: docker
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_9hnjksuc1u7qt5o1s6xeo279f UNIQUE (setting_id);


--
-- TOC entry 2764 (class 2606 OID 22540)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: docker
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 2765 (class 2606 OID 22545)
-- Name: users fkha1494lob0yjxoi9nni7g7itv; Type: FK CONSTRAINT; Schema: public; Owner: docker
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkha1494lob0yjxoi9nni7g7itv FOREIGN KEY (setting_id) REFERENCES public.settings(id);


--
-- TOC entry 2898 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: docker
--

GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2019-02-07 03:39:53 UTC

--
-- PostgreSQL database dump complete
--

