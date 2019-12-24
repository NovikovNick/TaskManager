package com.metalheart.converter.mapper;

import com.metalheart.model.Calendar;
import com.metalheart.model.RunningList;
import com.metalheart.model.Tag;
import com.metalheart.model.Task;
import com.metalheart.model.TaskStatus;
import com.metalheart.model.WeekWorkLog;
import com.metalheart.model.request.CreateTaskRequest;
import com.metalheart.model.request.UpdateTaskRequest;
import com.metalheart.model.response.CalendarViewModel;
import com.metalheart.model.response.RunningListViewModel;
import com.metalheart.model.response.TagViewModel;
import com.metalheart.model.response.TaskViewModel;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskViewModelToTaskMapper {

    TaskViewModelToTaskMapper INSTANCE = Mappers.getMapper(TaskViewModelToTaskMapper.class);

    TaskViewModel map(Task task);

    Task map(UpdateTaskRequest request);

    @Mapping(source = "taskId", target = "id")
    Task map(CreateTaskRequest task);

    @Mapping(source = "title", target = "text")
    TagViewModel map(Tag task);

    @Mapping(source = "weekId.year", target = "year")
    @Mapping(source = "weekId.week", target = "week")
    RunningListViewModel map(RunningList source);

    CalendarViewModel map(Calendar source);

    default String map(WeekWorkLog workLog) {
        if (Objects.nonNull(workLog)) {
            return ObjectUtils.defaultIfNull(workLog.getStatus(), TaskStatus.NONE).toString();
        }
        return null;
    }

    default List<String> map(List<WeekWorkLog> workLog) {
        if (Objects.nonNull(workLog)) {
            return workLog.stream().map(this::map).collect(Collectors.toList());
        }
        return null;
    }

    default Integer map(String src) {
        return StringUtils.isNumeric(src) ? Integer.valueOf(src) : 0;
    }

    default String map(Integer src) {
        return Objects.isNull(src) ? "" : src.toString();
    }
}
