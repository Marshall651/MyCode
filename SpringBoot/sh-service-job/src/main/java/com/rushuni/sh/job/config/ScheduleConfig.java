package com.rushuni.sh.job.config;


import com.rushuni.sh.job.service.ClearSetamealPicJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Marshall
 * @date 2022/8/2
 */
@Configuration
public class ScheduleConfig {
    public static class JobDemoSchedule {

        @Bean
        public JobDetail clearSetmealPicJob() {
            // 返回 Job
            return JobBuilder.newJob(ClearSetamealPicJob.class)
                    .withIdentity("clearSetmealPicJob")
                    .storeDurably(true) // 保留JobDetail
                    .build();
        }

        @Bean
        public Trigger clearSetmealPicJobTrigger() {
            // 创建简单调度计划
            SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(10) // 频率。
                    .repeatForever(); // 次数。
            // 创建Cron调度计划
//            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ? *");

            // 返回 Trigger 构造器
            return TriggerBuilder.newTrigger()
                    .forJob(clearSetmealPicJob())
                    .withSchedule(simpleScheduleBuilder)
                    .withIdentity("clearSetmealPicJobTrigger")
                    .build();
        }
    }
}
