-- ----------------------------------------------------------------------------
-- Table: tag
-- ----------------------------------------------------------------------------
CREATE TABLE tag
(
  id                serial                      PRIMARY KEY,
  title             varchar(30)                 not null UNIQUE,
  created_at        timestamp with time zone    not null
) WITH (OIDS = FALSE);

ALTER TABLE tag OWNER TO metalheart;

CREATE index tag_title_index on tag (title);

CREATE TABLE tag_task
(
  tag_id            integer               not null,
  task_id           integer               not null,

  primary key (tag_id, task_id),
  constraint fk_tag  foreign key (tag_id) references tag,
  constraint fk_task foreign key (task_id) references task

) WITH (OIDS = FALSE);
