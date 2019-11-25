-- ----------------------------------------------------------------------------
--
-- ----------------------------------------------------------------------------
/*CREATE SEQUENCE public.task_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 4
  CACHE 1;*/

ALTER TABLE task ALTER COLUMN id SET DATA TYPE integer;

-- id integer NOT NULL DEFAULT nextval('group_group_id_seq'::regclass),