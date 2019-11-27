package com.metalheart.converter.mapper;

import com.metalheart.model.jooq.tables.records.TaskRecord;
import com.metalheart.model.jpa.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class TaskToTaskRecordMapper extends BaseMapper {

    public static final TaskToTaskRecordMapper INSTANCE = Mappers.getMapper(TaskToTaskRecordMapper.class);

    public abstract TaskRecord convert(Task source);

    public abstract Task convert(TaskRecord source);
}