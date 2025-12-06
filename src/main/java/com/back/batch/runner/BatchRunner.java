package com.back.batch.runner;

import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// 자동 실행 비활성화 - 필요시 주석 해제
// @Component
public class BatchRunner implements CommandLineRunner {

    private final JobOperator jobOperator;
    private final org.springframework.batch.core.job.Job productJob;

    public BatchRunner(JobOperator jobOperator, org.springframework.batch.core.job.Job productJob) {
        this.jobOperator = jobOperator;
        this.productJob = productJob;
    }

    @Override
    public void run(String... args) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        System.out.println("=== Starting Batch Job ===");
        jobOperator.start(productJob, jobParameters);
        System.out.println("=== Batch Job Completed ===");
    }
}
