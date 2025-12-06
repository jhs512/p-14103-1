package com.back.batch.job;

import com.back.batch.dto.ProductDto;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
public class ProductBatchJob {

    @Bean
    public org.springframework.batch.core.job.Job productJob(
            JobRepository jobRepository, 
            org.springframework.batch.core.step.Step productStep) {
        return new JobBuilder("productJob", jobRepository)
                .start(productStep)
                .build();
    }

    @Bean
    public org.springframework.batch.core.step.Step productStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<ProductDto> productReader,
            ItemProcessor<ProductDto, ProductDto> productProcessor,
            ItemWriter<ProductDto> productWriter) {
        return new StepBuilder("productStep", jobRepository)
                .<ProductDto, ProductDto>chunk(10)
                .transactionManager(transactionManager)
                .reader(productReader)
                .processor(productProcessor)
                .writer(productWriter)
                .build();
    }

    @Bean
    public ItemReader<ProductDto> productReader() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("products.json");
        
        List<ProductDto> products = objectMapper.readValue(
                resource.getInputStream(),
                new TypeReference<List<ProductDto>>() {}
        );
        
        return new ListItemReader<>(products);
    }

    @Bean
    public ItemProcessor<ProductDto, ProductDto> productProcessor() {
        return item -> {
            System.out.println("Processing: " + item);
            // 간단한 처리: 가격에 10% 할인 적용
            item.setPrice((int) (item.getPrice() * 0.9));
            return item;
        };
    }

    @Bean
    public ItemWriter<ProductDto> productWriter() {
        return chunk -> {
            System.out.println("=== Writing Products ===");
            for (ProductDto item : chunk.getItems()) {
                System.out.println("Saved: " + item);
            }
            System.out.println("========================");
        };
    }
}
