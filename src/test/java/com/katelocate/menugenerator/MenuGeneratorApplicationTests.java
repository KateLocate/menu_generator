package com.katelocate.menugenerator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuGeneratorApplicationTests {

    @Autowired
    private MenuGeneratorApplication app;

    @Test
    void contextLoads() throws Exception {
        assertThat(app).isNotNull();
    }

}

