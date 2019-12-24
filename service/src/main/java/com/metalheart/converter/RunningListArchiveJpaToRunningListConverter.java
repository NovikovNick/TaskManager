package com.metalheart.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metalheart.model.RunningList;
import com.metalheart.model.jpa.RunningListArchiveJpa;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RunningListArchiveJpaToRunningListConverter implements Converter<RunningListArchiveJpa, RunningList> {


    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public RunningList convert(RunningListArchiveJpa source) {
        try {
            return objectMapper.readValue(source.getArchive(), RunningList.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
