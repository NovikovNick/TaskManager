package com.metalheart.service.impl;

import com.metalheart.exception.NoSuchRunningListArchiveException;
import com.metalheart.model.RunningList;
import com.metalheart.model.WeekId;
import com.metalheart.model.jpa.RunningListArchiveJpa;
import com.metalheart.model.jpa.RunningListArchiveJpaPK;
import com.metalheart.repository.jpa.RunningListArchiveJpaRepository;
import com.metalheart.service.DateService;
import com.metalheart.service.RunningListArchiveService;
import com.metalheart.service.RunningListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class RunningListArchiveServiceImpl implements RunningListArchiveService {

    @Autowired
    private RunningListArchiveJpaRepository runningListArchiveRepository;

    @Autowired
    private RunningListService runningListService;

    @Autowired
    private DateService dateService;

    @Autowired
    private ConversionService conversionService;

    @Transactional
    @Override
    public RunningList getPrev(WeekId weekId) throws NoSuchRunningListArchiveException {

        WeekId prevWeekId = dateService.getPreviousWeekId(weekId);

        return getArchive(prevWeekId);
    }

    @Override
    public RunningList getNext(WeekId weekId) throws NoSuchRunningListArchiveException {

        WeekId nextWeekId = dateService.getNextWeekId(weekId);

        if (dateService.getCurrentWeekId().equals(nextWeekId)) {
            return runningListService.getRunningList();
        }

        return getArchive(nextWeekId);
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

    private RunningList getArchive(WeekId weekId) throws NoSuchRunningListArchiveException {

        if (!isArchiveExist(weekId)) {
            throw new NoSuchRunningListArchiveException(weekId);
        }

        RunningList runningList = getRunningList(weekId);
        runningList.setEditable(false);
        runningList.setHasPrevious(hasPreviousArchive(weekId));
        runningList.setHasNext(hasNextArchive(weekId));
        return runningList;
    }

    @Override
    public boolean isArchiveExist(WeekId weekId) {
        var pk = conversionService.convert(weekId, RunningListArchiveJpaPK.class);
        return runningListArchiveRepository.existsById(pk);
    }

    @Override
    public RunningListArchiveJpa save(RunningListArchiveJpa archiveToSave) {
        return runningListArchiveRepository.save(archiveToSave);
    }

    @Override
    public void delete(RunningListArchiveJpa archive) {
        runningListArchiveRepository.delete(archive);
    }

    private RunningList getRunningList(WeekId weekId) {
        var pk = conversionService.convert(weekId, RunningListArchiveJpaPK.class);
        RunningListArchiveJpa archive = runningListArchiveRepository.getOne(pk);
        return conversionService.convert(archive, RunningList.class);
    }
}
