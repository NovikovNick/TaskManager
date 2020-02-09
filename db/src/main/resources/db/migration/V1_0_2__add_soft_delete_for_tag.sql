ALTER TABLE tag DROP CONSTRAINT tag_user_id_title_key;
ALTER TABLE tag ADD COLUMN deleted boolean default false;
