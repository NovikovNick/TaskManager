package com.metalheart.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metalheart.model.rest.response.RunningListViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RunningListViewModelToStringConverter implements Converter<RunningListViewModel, String> {


    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public String convert(RunningListViewModel source) {

        try {
            return objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
