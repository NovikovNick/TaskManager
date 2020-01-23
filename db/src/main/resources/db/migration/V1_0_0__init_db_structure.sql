-- ----------------------------------------------------------------------------
-- Table: user
-- ----------------------------------------------------------------------------
CREATE TABLE "user"
(
  id                serial                      PRIMARY KEY,
  email             varchar(255)                not null UNIQUE,
  username          varchar(255)                not null UNIQUE,
  password          varchar(255)                not null,
  last_login_at     timestamp with time zone    ,
  created_at        timestamp with time zone    not null,
  modified_at       timestamp with time zone
) WITH (OIDS = FALSE);
ALTER TABLE "user" OWNER TO metalheart;


-- ----------------------------------------------------------------------------
-- Table: task
-- ----------------------------------------------------------------------------
CREATE TABLE task
(
  id                serial                      PRIMARY KEY,
  user_id           integer                     not null ,
  title             varchar(255)                not null UNIQUE,
  description       text                        ,
  priority          integer                     ,
  deleted           boolean                     default false,
  created_at        timestamp with time zone    not null,
  modified_at       timestamp with time zone,

  constraint fk_user_id  foreign key (user_id) references "user"

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

  primary key (task_id, day_index),
  constraint fk_week_work_log_task_id  foreign key (task_id) references task

) WITH (OIDS = FALSE);
ALTER TABLE week_work_log OWNER TO metalheart;


-- ----------------------------------------------------------------------------
-- Table: running_list_archive
-- ----------------------------------------------------------------------------
CREATE TABLE running_list_archive
(
  year              integer                     ,
  week              integer                     ,
  archive           text                        not null,

  primary key (year, week)

) WITH (OIDS = FALSE);
ALTER TABLE running_list_archive OWNER TO metalheart;


-- ----------------------------------------------------------------------------
-- Table: tag
-- ----------------------------------------------------------------------------
CREATE TABLE tag
(
  id                serial                      PRIMARY KEY,
  user_id           integer                     not null ,
  title             varchar(30)                 not null UNIQUE,
  created_at        timestamp with time zone    not null,

  constraint fk_user_id  foreign key (user_id) references "user"

) WITH (OIDS = FALSE);
ALTER TABLE tag OWNER TO metalheart;

CREATE index tag_title_index on tag (title);

CREATE TABLE tag_task
(
  tag_id            integer               not null,
  task_id           integer               not null,

  primary key (tag_id, task_id),
  constraint fk_tag_task_tag  foreign key (tag_id) references tag,
  constraint fk_tag_task_task foreign key (task_id) references task

) WITH (OIDS = FALSE);
ALTER TABLE tag_task OWNER TO metalheart;
