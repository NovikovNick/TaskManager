package com.metalheart.converter;

import com.metalheart.converter.mapper.TaskViewModelToTaskMapper;
import com.metalheart.model.RunningList;
import com.metalheart.model.response.RunningListViewModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RunningListToRunningListViewModelConverter implements Converter<RunningList, RunningListViewModel> {

    @Override
    public RunningListViewModel convert(RunningList source) {
        return TaskViewModelToTaskMapper.INSTANCE.map(source);
    }
}
