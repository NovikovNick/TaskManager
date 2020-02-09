ALTER TABLE week_work_log DROP CONSTRAINT fk_week_work_log_task_id;
ALTER TABLE week_work_log
    ADD CONSTRAINT fk_week_work_log_task_id
    FOREIGN KEY (task_id) REFERENCES task
    ON DELETE CASCADE;

ALTER TABLE running_list_archive DROP CONSTRAINT fk_running_list_archive_user_id;
ALTER TABLE running_list_archive
    ADD CONSTRAINT fk_running_list_archive_user_id
    FOREIGN KEY (user_id) REFERENCES "user"
    ON DELETE CASCADE;

ALTER TABLE task DROP CONSTRAINT fk_user_id;
ALTER TABLE task
    ADD CONSTRAINT fk_user_id
    FOREIGN KEY (user_id) REFERENCES "user"
    ON DELETE CASCADE;

ALTER TABLE tag DROP CONSTRAINT fk_user_id;
ALTER TABLE tag
    ADD CONSTRAINT fk_user_id
    FOREIGN KEY (user_id) REFERENCES "user"
    ON DELETE CASCADE;

ALTER TABLE tag_task DROP CONSTRAINT fk_tag_task_tag;
ALTER TABLE tag_task DROP CONSTRAINT fk_tag_task_task;
ALTER TABLE tag_task
    ADD CONSTRAINT fk_tag_task_tag
    FOREIGN KEY (tag_id) REFERENCES tag
    ON DELETE CASCADE;
ALTER TABLE tag_task
    ADD CONSTRAINT fk_tag_task_task
    FOREIGN KEY (task_id) REFERENCES task
    ON DELETE CASCADE;


