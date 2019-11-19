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
