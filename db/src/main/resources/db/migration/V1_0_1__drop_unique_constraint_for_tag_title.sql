ALTER TABLE tag DROP CONSTRAINT tag_title_key;
ALTER TABLE tag ADD CONSTRAINT tag_user_id_title_key UNIQUE (user_id, title);