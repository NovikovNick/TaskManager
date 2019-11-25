package com.metalheart.repository.jooq.impl;

import com.metalheart.model.jooq.Sequences;
import com.metalheart.model.jooq.Tables;
import com.metalheart.model.jooq.tables.records.TaskRecord;
import com.metalheart.repository.jooq.TaskJooqRepository;
import java.util.Objects;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskJooqRepositoryImpl implements TaskJooqRepository {

    @Autowired
    private DSLContext dsl;

    @Override
    public void saveAndGenerateIdIfNotPresent(TaskRecord record) {

        if (Objects.isNull(record.getId())) {
            record.setId(dsl.nextval(Sequences.TASK_ID_SEQ).intValue());
        }

        dsl.insertInto(Tables.TASK).set(record).execute();
    }
}
