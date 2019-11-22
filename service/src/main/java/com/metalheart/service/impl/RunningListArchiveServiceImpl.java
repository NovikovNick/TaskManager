package com.metalheart.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metalheart.model.jpa.RunningListArchive;
import com.metalheart.model.jpa.RunningListArchivePK;
import com.metalheart.model.rest.response.RunningListViewModel;
import com.metalheart.repository.jpa.RunningListArchiveRepository;
import com.metalheart.service.DateService;
import com.metalheart.service.RunningListArchiveService;
import com.metalheart.service.RunningListService;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RunningListArchiveServiceImpl implements RunningListArchiveService {

    @Autowired
    private RunningListArchiveRepository runningListArchiveRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RunningListService runningListService;

    @Autowired
    private DateService dateService;

    @Override
    public RunningListViewModel getPrev(Integer year, Integer week) {

        RunningListArchivePK weekId = RunningListArchivePK.builder().year(year).week(week).build();
        RunningListArchivePK prevWeekId = dateService.getPreviousWeekId(weekId);

        return getArchive(prevWeekId);
    }


    @Override
    public RunningListViewModel getNext(Integer year, Integer week) {

        RunningListArchivePK weekId = RunningListArchivePK.builder().year(year).week(week).build();
        RunningListArchivePK nextWeekId = dateService.getNextWeekId(weekId);

        if (dateService.getCurrentWeekId().equals(nextWeekId)) {
            return runningListService.getRunningList();
        }

        return getArchive(nextWeekId);
    }

    @Override
    public void archive() {

        RunningListArchivePK weekId = dateService.getCurrentWeekId();
        if (runningListArchiveRepository.existsById(weekId)) {
            throw new RuntimeException("archive already exist! weekId = " + weekId);
        }

        RunningListViewModel runningList = runningListService.getRunningList();

        try {
            RunningListArchive archive = runningListArchiveRepository.save(RunningListArchive.builder()
                .id(weekId)
                .archive(objectMapper.writeValueAsString(runningList))
                .build());
            log.info("Archive has been saved {}", archive);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasPreviousArchive(RunningListArchivePK weekId) {
        return isArchiveExist(dateService.getPreviousWeekId(weekId));
    }


    private boolean hasNextArchive(RunningListArchivePK weekId) {
        RunningListArchivePK nextWeekId = dateService.getNextWeekId(weekId);
        if (dateService.getCurrentWeekId().equals(nextWeekId)) {
            return true;
        }

        return isArchiveExist(nextWeekId);
    }

    private RunningListViewModel getArchive(RunningListArchivePK weekId) {
        RunningListViewModel runningListViewModel = getRunningListViewModel(weekId);
        runningListViewModel.setEditable(false);
        runningListViewModel.setHasPrevious(hasPreviousArchive(weekId));
        runningListViewModel.setHasNext(hasNextArchive(weekId));
        return runningListViewModel;
    }

    private boolean isArchiveExist(RunningListArchivePK weekId) {
        return runningListArchiveRepository.existsById(weekId);
    }

    private RunningListViewModel getRunningListViewModel(RunningListArchivePK weekId) {
        RunningListArchive archive = runningListArchiveRepository.getOne(weekId);

        try {
            return objectMapper.readValue(archive.getArchive(), RunningListViewModel.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
