package com.back.global.initData;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Slf4j
public class BaseInitData {
    private final BaseInitData self;

    public BaseInitData(@Lazy BaseInitData self) {
        this.self = self;
    }

    @Bean
    ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            self.work1();
            self.work2();
            self.work3();
        };
    }

    @Transactional
    public void work1() {
        log.debug("작업 1");
    }

    @Transactional
    public void work2() {
        log.debug("작업 2");
    }

    @Transactional
    public void work3() {
        log.debug("작업 3");
    }
}
