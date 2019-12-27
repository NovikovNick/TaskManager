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
