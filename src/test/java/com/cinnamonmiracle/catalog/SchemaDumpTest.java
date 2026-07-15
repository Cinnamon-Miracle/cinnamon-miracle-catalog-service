package com.cinnamonmiracle.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Boots the context so Hibernate emits the create DDL (target/schema-catalog.sql),
 * confirming the generated column names. Also serves as a context-load smoke test.
 */
@SpringBootTest
@ActiveProfiles("test")
class SchemaDumpTest {

    @Test
    void contextLoads() {
    }
}
