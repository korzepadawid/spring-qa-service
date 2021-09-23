package io.github.korzepadawid.springquoraclone.config;

import static java.util.Collections.singletonList;

import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Bean
  public Docket docket() {
    return new Docket(DocumentationType.SWAGGER_2)
        .ignoredParameterTypes(UsernamePasswordAuthenticationToken.class)
        .select()
        .paths(PathSelectors.ant("/api/v1/**"))
        .build()
        .apiInfo(createApiInfo())
        .securitySchemes(singletonList(createSchema()))
        .securityContexts(singletonList(createContext()));
  }

  private ApiInfo createApiInfo() {
    return new ApiInfo(
        "Q&A Service",
        "REST API for Q&A Service.",
        "1.0",
        "urn:tos",
        new Contact(
            "Dawid",
            "https://github.com/korzepadawid",
            "korzepadawid@yahoo.com"
        ),
        "MIT License",
        "https://choosealicense.com/licenses/mit/",
        Collections.emptyList()
    );
  }

  private SecurityContext createContext() {
    return SecurityContext.builder()
        .securityReferences(createRef())
        .forPaths(PathSelectors.regex("((?!/api/v1/auth/).)*$"))
        .build();
  }

  private List<SecurityReference> createRef() {
    AuthorizationScope authorizationScope = new AuthorizationScope(
        "global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return singletonList(new SecurityReference("apiKey", authorizationScopes));
  }

  private SecurityScheme createSchema() {
    return new ApiKey("apiKey", "Authorization", "header");
  }
}
