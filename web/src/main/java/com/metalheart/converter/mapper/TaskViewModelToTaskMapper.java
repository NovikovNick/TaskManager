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

    TaskViewModel map(Task src);

    Task map(UpdateTaskRequest src);

    @Mapping(source = "taskId", target = "id")
    Task map(CreateTaskRequest src);

    @Mapping(source = "title", target = "text")
    TagViewModel map(Tag src);

    @Mapping(source = "text", target = "title")
    Tag map(TagViewModel src);

    @Mapping(source = "weekId.year", target = "year")
    @Mapping(source = "weekId.week", target = "week")
    RunningListViewModel map(RunningList src);

    CalendarViewModel map(Calendar src);

    default String map(WeekWorkLog src) {
        if (Objects.nonNull(src)) {
            return ObjectUtils.defaultIfNull(src.getStatus(), TaskStatus.NONE).toString();
        }
        return null;
    }

    default List<String> map(List<WeekWorkLog> src) {
        if (Objects.nonNull(src)) {
            return src.stream().map(this::map).collect(Collectors.toList());
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
