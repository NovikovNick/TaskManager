package com.metalheart.repository.jooq;

import com.metalheart.model.jooq.tables.records.TaskRecord;

public interface TaskJooqRepository {
    void saveAndGenerateIdIfNotPresent(TaskRecord record);
}
