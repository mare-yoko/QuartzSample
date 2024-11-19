package com.example.demo.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@AllArgsConstructor
public class SampleScheduleJob implements Job {

    private final ObjectMapper objectMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap parameter = jobExecutionContext.getJobDetail().getJobDataMap();

        ScheduleInfo info = null;
        try {
            info = objectMapper.readValue((String)parameter.get("parameter"), ScheduleInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("execute Schedule sample");
        System.out.println(info.getTitle());
    }
}
