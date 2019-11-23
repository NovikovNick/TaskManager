package com.metalheart.service.impl;

import com.metalheart.exception.NoSuchRunningListArchiveException;
import com.metalheart.exception.RunningListArchiveAlreadyExistException;
import com.metalheart.model.WeekId;
import com.metalheart.model.jpa.RunningListArchive;
import com.metalheart.model.jpa.RunningListArchivePK;
import com.metalheart.model.rest.response.RunningListViewModel;
import com.metalheart.repository.jpa.RunningListArchiveRepository;
import com.metalheart.service.DateService;
import com.metalheart.service.RunningListArchiveService;
import com.metalheart.service.RunningListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RunningListArchiveServiceImpl implements RunningListArchiveService {

    @Autowired
    private RunningListArchiveRepository runningListArchiveRepository;

    @Autowired
    private RunningListService runningListService;

    @Autowired
    private DateService dateService;

    @Autowired
    private ConversionService conversionService;

    @Override
    public RunningListViewModel getPrev(WeekId weekId) throws NoSuchRunningListArchiveException {

        WeekId prevWeekId = dateService.getPreviousWeekId(weekId);

        return getArchive(prevWeekId);
    }

    @Override
    public RunningListViewModel getNext(WeekId weekId) throws NoSuchRunningListArchiveException {

        WeekId nextWeekId = dateService.getNextWeekId(weekId);

        if (dateService.getCurrentWeekId().equals(nextWeekId)) {
            return runningListService.getRunningList();
        }

        return getArchive(nextWeekId);
    }

    @Override
    public void archive() throws RunningListArchiveAlreadyExistException {

        WeekId weekId = dateService.getCurrentWeekId();
        if (isArchiveExist(weekId)) {
            throw new RunningListArchiveAlreadyExistException(weekId);
        }

        RunningListViewModel runningList = runningListService.getRunningList();

        RunningListArchive archive = runningListArchiveRepository.save(RunningListArchive.builder()
            .id(conversionService.convert(weekId, RunningListArchivePK.class))
            .archive(conversionService.convert(runningList, String.class))
            .build());

        log.info("Archive has been saved {}", archive);
    }

    @Override
    public boolean hasPreviousArchive(WeekId weekId) {
        return isArchiveExist(dateService.getPreviousWeekId(weekId));
    }


    private boolean hasNextArchive(WeekId weekId) {
        WeekId nextWeekId = dateService.getNextWeekId(weekId);
        if (dateService.getCurrentWeekId().equals(nextWeekId)) {
            return true;
        }

        return isArchiveExist(nextWeekId);
    }

    private RunningListViewModel getArchive(WeekId weekId) throws NoSuchRunningListArchiveException {

        if (!isArchiveExist(weekId)) {
            throw new NoSuchRunningListArchiveException(weekId);
        }

        RunningListViewModel runningListViewModel = getRunningListViewModel(weekId);
        runningListViewModel.setEditable(false);
        runningListViewModel.setHasPrevious(hasPreviousArchive(weekId));
        runningListViewModel.setHasNext(hasNextArchive(weekId));
        return runningListViewModel;
    }

    private boolean isArchiveExist(WeekId weekId) {
        var pk = conversionService.convert(weekId, RunningListArchivePK.class);
        return runningListArchiveRepository.existsById(pk);
    }

    private RunningListViewModel getRunningListViewModel(WeekId weekId) {
        var pk = conversionService.convert(weekId, RunningListArchivePK.class);
        RunningListArchive archive = runningListArchiveRepository.getOne(pk);
        return conversionService.convert(archive, RunningListViewModel.class);
    }
}
