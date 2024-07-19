--
-- PostgreSQL database dump
--

-- Dumped from database version 15.7
-- Dumped by pg_dump version 16.3

-- Started on 2024-07-03 09:13:13

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 220 (class 1259 OID 16681)
-- Name: category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.category_id_seq OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 221 (class 1259 OID 16682)
-- Name: category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.category (
    category_id integer DEFAULT nextval('public.category_id_seq'::regclass) NOT NULL,
    category_name character varying(30) NOT NULL
);


ALTER TABLE public.category OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 16603)
-- Name: departments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.departments (
    department_id integer NOT NULL,
    department_name character varying(50) NOT NULL,
    manager_id integer,
    status_aktif integer DEFAULT 1 NOT NULL
);


ALTER TABLE public.departments OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 16578)
-- Name: tbl_employee_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tbl_employee_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tbl_employee_id_seq OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16608)
-- Name: employees; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.employees (
    employee_id integer DEFAULT nextval('public.tbl_employee_id_seq'::regclass) NOT NULL,
    nip character varying(10) NOT NULL,
    nama character varying(50) NOT NULL,
    email character varying(50) NOT NULL,
    department_id integer,
    manager_id integer,
    status_aktif integer DEFAULT 1 NOT NULL,
    ismanager boolean DEFAULT false,
    password character varying(30) DEFAULT '12345678'::character varying
);


ALTER TABLE public.employees OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 16724)
-- Name: history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.history_id_seq OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16688)
-- Name: stock_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.stock_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.stock_id_seq OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16712)
-- Name: stock; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.stock (
    stock_id integer DEFAULT nextval('public.stock_id_seq'::regclass) NOT NULL,
    description character varying(50) NOT NULL,
    category integer,
    buying_price integer NOT NULL,
    selling_price integer,
    quantity integer NOT NULL,
    status_aktif integer DEFAULT 1 NOT NULL
);


ALTER TABLE public.stock OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16660)
-- Name: students_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.students_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.students_id_seq OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16664)
-- Name: students; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.students (
    student_id integer DEFAULT nextval('public.students_id_seq'::regclass) NOT NULL,
    nrp character varying(10) NOT NULL,
    nama character varying(50) NOT NULL,
    email character varying(50) NOT NULL,
    department_id integer,
    manager_id integer,
    status_aktif integer DEFAULT 1 NOT NULL
);


ALTER TABLE public.students OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16628)
-- Name: tbl_students_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tbl_students_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tbl_students_id_seq OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 16845)
-- Name: transactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transactions (
    transaction_id integer NOT NULL,
    transaction_date date DEFAULT CURRENT_DATE NOT NULL,
    employee_id integer,
    stock_id integer,
    total_jual integer NOT NULL,
    profit integer,
    quantity integer NOT NULL,
    student_id integer
);


ALTER TABLE public.transactions OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 16844)
-- Name: transactions_transaction_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.transactions_transaction_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.transactions_transaction_id_seq OWNER TO postgres;

--
-- TOC entry 3392 (class 0 OID 0)
-- Dependencies: 225
-- Name: transactions_transaction_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.transactions_transaction_id_seq OWNED BY public.transactions.transaction_id;


--
-- TOC entry 3209 (class 2604 OID 16848)
-- Name: transactions transaction_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions ALTER COLUMN transaction_id SET DEFAULT nextval('public.transactions_transaction_id_seq'::regclass);


--
-- TOC entry 3381 (class 0 OID 16682)
-- Dependencies: 221
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.category (category_id, category_name) FROM stdin;
1	equipment
2	snack
3	small school supplies
4	large school supplies
\.


--
-- TOC entry 3375 (class 0 OID 16603)
-- Dependencies: 215
-- Data for Name: departments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.departments (department_id, department_name, manager_id, status_aktif) FROM stdin;
30	sales	65	1
40	finance	\N	1
10	BPH	63	1
20	admin	64	1
\.


--
-- TOC entry 3376 (class 0 OID 16608)
-- Dependencies: 216
-- Data for Name: employees; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.employees (employee_id, nip, nama, email, department_id, manager_id, status_aktif, ismanager, password) FROM stdin;
65	c14230127	Gregorius	gregnovantino@gmail.com	30	\N	1	t	12345678
66	c14230102	Valerio Reynaldo	reynaldoworking@gmail.com	10	63	1	f	12345678
68	c14230095	Hans Anthony	hansant@yahoo.com	30	65	1	f	12345678
67	c14230101	Evan Jeremia	evanjeremia@outlook.com	20	64	1	f	12345678
69	c14230082	Reyhan Amando	reyhanthehouseseller@gmail.com	30	65	1	f	12345678
64	c14230099	Marcel Hans	marcelhans11@gmail.com	20	\N	1	t	marcel123
72	c12345678	joselyn 	joselin@gmail.com	10	63	0	f	12345678
71	c14230157	Tiffany Auberta	tiffa@gmail.com	\N	\N	1	f	12345678
70	c14230085	Bryan 	bryanchrist@gmail.com	40	\N	1	f	12345678
63	c14230096	Joseph Evan	josephevan303@gmail.com	10	\N	1	t	easytime
\.


--
-- TOC entry 3383 (class 0 OID 16712)
-- Dependencies: 223
-- Data for Name: stock; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.stock (stock_id, description, category, buying_price, selling_price, quantity, status_aktif) FROM stdin;
8	Laptop	1	2000000	\N	30	1
21	fasfjlads	1	11	111	11	0
10	ndz	1	211	112	2121	0
2	aaerg	3	1000	200	50	0
12	Yupi	2	1000	2000	86	1
6	Bag	4	75000	125000	27	1
4	Spidol	3	2000	3500	88	1
22	bottle	1	10000	15000	18	1
9	Tango	2	2000	3500	42	1
3	Pulpen	3	2500	4000	251	1
23	bengbeng	2	2000	3000	46	1
7	Oreo	2	5000	7500	33	1
\.


--
-- TOC entry 3379 (class 0 OID 16664)
-- Dependencies: 219
-- Data for Name: students; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.students (student_id, nrp, nama, email, department_id, manager_id, status_aktif) FROM stdin;
17	d14230067	Owen Handojo	owenukp@outlook.com	20	64	1
18	d14230001	Dicky Sanjaya	dickyiscool@gmail.com	20	64	1
19	d14230077	Kevin Tanaka	kevintanaka@gmail.com	30	65	1
20	d14230202	Terry Clement	terryeagle@outlook.com	40	\N	1
\.


--
-- TOC entry 3386 (class 0 OID 16845)
-- Dependencies: 226
-- Data for Name: transactions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.transactions (transaction_id, transaction_date, employee_id, stock_id, total_jual, profit, quantity, student_id) FROM stdin;
24	2024-07-02	68	4	7000	3000	2	\N
25	2024-07-02	\N	7	37500	12500	5	19
26	2024-07-02	68	6	250000	100000	2	\N
27	2024-07-02	68	4	10500	4500	3	\N
28	2024-07-02	\N	3	8000	3000	2	19
29	2024-07-02	69	7	7500	2500	1	\N
30	2024-07-02	69	22	15000	5000	1	\N
31	2024-07-02	68	9	7000	3000	2	\N
32	2024-07-02	69	23	6000	2000	2	\N
33	2024-07-02	69	7	75000	25000	10	\N
34	2024-07-02	\N	12	20000	10000	10	19
35	2024-07-02	\N	6	125000	50000	1	19
36	2024-07-02	\N	4	7000	3000	2	19
37	2024-07-02	68	22	15000	5000	1	\N
38	2024-07-02	69	9	17500	7500	5	\N
39	2024-07-02	68	3	8000	3000	2	\N
40	2024-07-02	68	23	6000	2000	2	\N
41	2024-07-03	69	7	7500	2500	1	\N
\.


--
-- TOC entry 3393 (class 0 OID 0)
-- Dependencies: 220
-- Name: category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.category_id_seq', 4, true);


--
-- TOC entry 3394 (class 0 OID 0)
-- Dependencies: 224
-- Name: history_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.history_id_seq', 1, false);


--
-- TOC entry 3395 (class 0 OID 0)
-- Dependencies: 222
-- Name: stock_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.stock_id_seq', 23, true);


--
-- TOC entry 3396 (class 0 OID 0)
-- Dependencies: 218
-- Name: students_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.students_id_seq', 20, true);


--
-- TOC entry 3397 (class 0 OID 0)
-- Dependencies: 214
-- Name: tbl_employee_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tbl_employee_id_seq', 72, true);


--
-- TOC entry 3398 (class 0 OID 0)
-- Dependencies: 217
-- Name: tbl_students_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tbl_students_id_seq', 1, false);


--
-- TOC entry 3399 (class 0 OID 0)
-- Dependencies: 225
-- Name: transactions_transaction_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.transactions_transaction_id_seq', 41, true);


--
-- TOC entry 3218 (class 2606 OID 16687)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (category_id);


--
-- TOC entry 3212 (class 2606 OID 16607)
-- Name: departments departments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.departments
    ADD CONSTRAINT departments_pkey PRIMARY KEY (department_id);


--
-- TOC entry 3214 (class 2606 OID 16614)
-- Name: employees employees_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employees
    ADD CONSTRAINT employees_pkey PRIMARY KEY (employee_id);


--
-- TOC entry 3220 (class 2606 OID 16718)
-- Name: stock stock_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock
    ADD CONSTRAINT stock_pkey PRIMARY KEY (stock_id);


--
-- TOC entry 3216 (class 2606 OID 16670)
-- Name: students students_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_pkey PRIMARY KEY (student_id);


--
-- TOC entry 3222 (class 2606 OID 16850)
-- Name: transactions transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT transactions_pkey PRIMARY KEY (transaction_id);


--
-- TOC entry 3223 (class 2606 OID 16893)
-- Name: departments dept_manager_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.departments
    ADD CONSTRAINT dept_manager_id_fk FOREIGN KEY (manager_id) REFERENCES public.employees(employee_id) ON DELETE SET NULL;


--
-- TOC entry 3224 (class 2606 OID 16868)
-- Name: employees emp_dept_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employees
    ADD CONSTRAINT emp_dept_id_fk FOREIGN KEY (department_id) REFERENCES public.departments(department_id);


--
-- TOC entry 3225 (class 2606 OID 16883)
-- Name: employees emp_manager_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employees
    ADD CONSTRAINT emp_manager_id_fk FOREIGN KEY (manager_id) REFERENCES public.employees(employee_id) ON DELETE SET NULL;


--
-- TOC entry 3229 (class 2606 OID 16851)
-- Name: transactions fk_employee_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT fk_employee_id FOREIGN KEY (employee_id) REFERENCES public.employees(employee_id);


--
-- TOC entry 3230 (class 2606 OID 16856)
-- Name: transactions fk_stock_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT fk_stock_id FOREIGN KEY (stock_id) REFERENCES public.stock(stock_id);


--
-- TOC entry 3231 (class 2606 OID 16863)
-- Name: transactions fk_student_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT fk_student_id FOREIGN KEY (student_id) REFERENCES public.students(student_id);


--
-- TOC entry 3226 (class 2606 OID 16873)
-- Name: students std_dept_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT std_dept_id_fk FOREIGN KEY (department_id) REFERENCES public.departments(department_id);


--
-- TOC entry 3227 (class 2606 OID 16888)
-- Name: students std_manager_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT std_manager_id_fk FOREIGN KEY (manager_id) REFERENCES public.employees(employee_id) ON DELETE SET NULL;


--
-- TOC entry 3228 (class 2606 OID 16719)
-- Name: stock stock_category_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock
    ADD CONSTRAINT stock_category_fkey FOREIGN KEY (category) REFERENCES public.category(category_id);


-- Completed on 2024-07-03 09:13:13

--
-- PostgreSQL database dump complete
--

