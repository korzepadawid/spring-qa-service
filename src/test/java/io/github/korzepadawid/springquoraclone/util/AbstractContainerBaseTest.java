package io.github.korzepadawid.springquoraclone.util;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class AbstractContainerBaseTest {

  private static final PostgreSQLContainer container = new PostgreSQLContainer<>(
      "postgres:latest")
      .withReuse(true);

  @DynamicPropertySource
  public static void overrideProps(DynamicPropertyRegistry dynamicPropertyRegistry){
    dynamicPropertyRegistry.add("spring.datasource.url", container::getJdbcUrl);
    dynamicPropertyRegistry.add("spring.datasource.username", container::getUsername);
    dynamicPropertyRegistry.add("spring.datasource.password", container::getPassword);
  }

  @BeforeAll
  public static void setUp() {
    container.start();
  }
}
