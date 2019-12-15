package com.metalheart.converter.mapper;

import com.metalheart.model.service.TaskModel;
import com.metalheart.model.jooq.tables.records.TaskRecord;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class TaskModelToTaskRecordMapper extends BaseMapper {

    public static final TaskModelToTaskRecordMapper INSTANCE = Mappers.getMapper(TaskModelToTaskRecordMapper.class);


    public abstract TaskRecord convert(TaskModel source);

    public abstract TaskModel convert(TaskRecord source);
}
