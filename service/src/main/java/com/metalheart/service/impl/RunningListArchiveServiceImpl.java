package com.metalheart.service.impl;

import com.metalheart.model.RunningList;
import com.metalheart.model.WeekId;
import com.metalheart.model.jpa.RunningListArchiveJpa;
import com.metalheart.model.jpa.RunningListArchiveJpaPK;
import com.metalheart.repository.jpa.RunningListArchiveJpaRepository;
import com.metalheart.service.DateService;
import com.metalheart.service.RunningListArchiveService;
import com.metalheart.service.RunningListService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.metalheart.config.ServiceConfiguration.APP_CONVERSION_SERVICE;

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
    @Qualifier(APP_CONVERSION_SERVICE)
    private ConversionService conversionService;

    @Transactional
    @Override
    public Optional<RunningList> getPrev(Integer userId, WeekId weekId){

        WeekId prevWeekId = dateService.getPreviousWeekId(weekId);

        return getArchive(userId, prevWeekId);
    }

    @Override
    public Optional<RunningList> getNext(Integer userId, WeekId weekId) {

        WeekId nextWeekId = dateService.getNextWeekId(weekId);

        if (dateService.getCurrentWeekId().equals(nextWeekId)) {
            return Optional.of(runningListService.getRunningList(userId));
        }

        return getArchive(userId, nextWeekId);
    }


    @Override
    public boolean hasPreviousArchive(Integer userId, WeekId weekId) {
        return isArchiveExist(userId, dateService.getPreviousWeekId(weekId));
    }


    private boolean hasNextArchive(Integer userId, WeekId weekId) {
        WeekId nextWeekId = dateService.getNextWeekId(weekId);
        if (dateService.getCurrentWeekId().equals(nextWeekId)) {
            return true;
        }

        return isArchiveExist(userId, nextWeekId);
    }

    @Transactional
    @Override
    public Optional<RunningList> getArchive(Integer userId, WeekId weekId) {

        if (!isArchiveExist(userId, weekId)) {
            return Optional.empty();
        }

        RunningList runningList = getRunningList(userId, weekId);
        runningList.setEditable(false);
        runningList.setHasPrevious(hasPreviousArchive(userId, weekId));
        runningList.setHasNext(hasNextArchive(userId, weekId));
        return Optional.of(runningList);
    }

    @Override
    public boolean isArchiveExist(Integer userId, WeekId weekId) {
        var pk = conversionService.convert(weekId, RunningListArchiveJpaPK.class);
        pk.setUserId(userId);
        return runningListArchiveRepository.existsById(pk);
    }

    @Override
    public RunningList save(Integer userId, RunningList runningList) {
        RunningListArchiveJpa entity = conversionService.convert(runningList, RunningListArchiveJpa.class);
        entity.getId().setUserId(userId);
        runningListArchiveRepository.save(entity);
        return runningList;
    }

    @Override
    public void delete(Integer userId, WeekId weekId) {
        RunningListArchiveJpaPK pk = conversionService.convert(weekId, RunningListArchiveJpaPK.class);
        pk.setUserId(userId);
        runningListArchiveRepository.deleteById(pk);
    }

    private RunningList getRunningList(Integer userId, WeekId weekId) {
        var pk = conversionService.convert(weekId, RunningListArchiveJpaPK.class);
        pk.setUserId(userId);
        RunningListArchiveJpa archive = runningListArchiveRepository.getOne(pk);
        return conversionService.convert(archive, RunningList.class);
    }
}
