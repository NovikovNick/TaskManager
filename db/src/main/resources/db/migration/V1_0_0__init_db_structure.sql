-- ----------------------------------------------------------------------------
-- Table: task
-- ----------------------------------------------------------------------------
CREATE TABLE task
(
  id                serial                      PRIMARY KEY,
  title             varchar(255)                not null UNIQUE,
  description       text                        ,
  priority          integer                     ,
  created_at        timestamp with time zone    not null,
  modified_at       timestamp with time zone
) WITH (OIDS = FALSE);

ALTER TABLE task OWNER TO metalheart;

-- ----------------------------------------------------------------------------
-- Type: Task statuses
-- ----------------------------------------------------------------------------
CREATE TYPE task_status_enum AS ENUM ('NONE', 'TO_DO', 'IN_PROGRESS', 'CANCELED', 'DONE');
-- ----------------------------------------------------------------------------
-- Table: week_work_log
-- ----------------------------------------------------------------------------
CREATE TABLE week_work_log
(

  task_id    integer,
  day_index  integer,
  status     task_status_enum not null,
  primary key (task_id, day_index)

) WITH (OIDS = FALSE);
