package com.example.uptime.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest(properties = {
        "spring.task.scheduling.enabled=false"
})
@AutoConfigureMockMvc
class TargetControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("uptime")
            .withUsername("uptime")
            .withPassword("uptime");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
        r.add("spring.flyway.enabled", () -> true);
    }

    @Autowired
    MockMvc mvc;

    @Test
    void createAndListTargets() throws Exception {
        mvc.perform(post("/api/targets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"name":"Httpbin","url":"https://httpbin.org","enabled":true,"checkEverySec":30}
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        mvc.perform(get("/api/targets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void duplicateUrlShouldReturn409() throws Exception {
        var body = """
            {"name":"A","url":"https://example.com","enabled":true,"checkEverySec":30}
        """;

        mvc.perform(post("/api/targets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        mvc.perform(post("/api/targets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void notFoundShouldReturn404() throws Exception {
        mvc.perform(get("/api/targets/999999"))
                .andExpect(status().isNotFound());
    }
}
