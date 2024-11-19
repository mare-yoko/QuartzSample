package com.example.demo.controller;

import com.example.demo.schedule.SampleScheduleJob;
import com.example.demo.schedule.ScheduleInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.quartz.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RestController
@AllArgsConstructor
public class DemoController {

    private final Scheduler scheduler;

    private final ObjectMapper objectMapper;

    @GetMapping("/test")
    public String get() {
        // パラメータ作成
        ScheduleInfo info = new ScheduleInfo();
        info.setId(100);
        info.setTitle("TestTitle");

        // ジョブ生成
        JobDetail job = JobBuilder.newJob(SampleScheduleJob.class)
                .withIdentity("sample_job").build();
        try {
            // パラメータセット
            job.getJobDataMap().put("parameter", this.objectMapper.writeValueAsString(info));


            // トリガー設定（10秒後に発火）
            Date date = Date.from(LocalDateTime.now().plusSeconds(10)
                    .atZone(ZoneId.systemDefault()).toInstant());
            Trigger trigger = TriggerBuilder.newTrigger()
                    .startAt(date)
                    .build();

            // ジョブ登録
            this.scheduler.scheduleJob(job, trigger);

        } catch (SchedulerException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return "ok";
    }
}
